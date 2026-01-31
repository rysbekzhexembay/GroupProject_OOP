package logic;

import db.DB;
import model.Instrument;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.User;
import model.Order;

public class Shop {

    public List<Instrument> getCatalog(String type) {
        List<Instrument> list = new ArrayList<>();
        String sql = "SELECT instruments.id, instruments.name, instruments.price, instruments.stock, instrument_types.name as type_name " +
                     "FROM instruments JOIN instrument_types ON instruments.type_id = instrument_types.id";
        if (type != null) sql += " WHERE instrument_types.name = ?";
        
        try {
            Connection conn = DB.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            if (type != null) stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Instrument(rs.getInt("id"), rs.getString("name"), rs.getString("type_name"), rs.getInt("price"), rs.getInt("stock")));
            }
        } catch (SQLException e) {
            System.out.println("Error loading catalog: " + e.getMessage());
        }
        return list;
    }

    public boolean buy(User user, int instrumentId, String card, String date, String cvc) {
        if (card.length() != 16) {
            System.out.println("Error: Card number must be exactly 16 digits.");
            return false;
        }
        if (date.length() != 5 || !date.contains("/")) {
            System.out.println("Error: Date must be in format mm/yy (e.g. 12/25).");
            return false;
        }
        try {
            String[] parts = date.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]);
            if (month < 1 || month > 12 || year < 0 || year > 99) {
                System.out.println("Error: Invalid date values (Month: 1-12, Year: 00-99).");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error: Date must be numeric and in mm/yy format.");
            return false;
        }
        if (cvc.length() != 3) {
            System.out.println("Error: CVC must be exactly 3 digits.");
            return false;
        }

        try {
            Connection conn = DB.connect();
            PreparedStatement check = conn.prepareStatement("SELECT price, stock FROM instruments WHERE id = ?");
            check.setInt(1, instrumentId);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                int stock = rs.getInt("stock");
                int price = rs.getInt("price");

                if (stock > 0) {
                    PreparedStatement up = conn.prepareStatement("UPDATE instruments SET stock = stock - 1 WHERE id = ?");
                    up.setInt(1, instrumentId);
                    up.executeUpdate();

                    String ordSql = "INSERT INTO orders (user_id, instrument_id, price, card_last4) VALUES (?, ?, ?, ?)";
                    PreparedStatement ord = conn.prepareStatement(ordSql);
                    ord.setInt(1, user.getId());
                    ord.setInt(2, instrumentId);
                    ord.setInt(3, price);
                    
                    String last4 = card.substring(12);
                    ord.setString(4, last4);
                    
                    ord.executeUpdate();
                    return true;
                } else {
                    System.out.println("Error: Item is out of stock.");
                }
            } else {
                System.out.println("Error: Instrument ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Purchase failed: " + e.getMessage());
        }
        return false;
    }

    public List<Order> getMyOrders(User user) {
        List<Order> list = new ArrayList<>();
        try {
            Connection conn = DB.connect();
            String sql = "SELECT orders.id, orders.price, orders.card_last4, orders.created_at, instruments.name as instr_name " + 
                         "FROM orders JOIN instruments ON orders.instrument_id = instruments.id " + 
                         "WHERE orders.user_id = ?";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                list.add(new Order(
                    rs.getInt("id"),
                    user.getId(),
                    rs.getInt("price"),
                    rs.getString("instr_name"),
                    rs.getString("card_last4"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching orders: " + e.getMessage());
        }
        return list;
    }

    public boolean addCategory(String name) {
        try {
            Connection conn = DB.connect();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO instrument_types (name) VALUES (?)");
            stmt.setString(1, name);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding category: " + e.getMessage());
            return false;
        }
    }

    public boolean addInstrument(String name, String type, int price, int stock) {
        try {
            Connection conn = DB.connect();
            PreparedStatement typeStmt = conn.prepareStatement("SELECT id FROM instrument_types WHERE name = ?");
            typeStmt.setString(1, type);
            ResultSet rs = typeStmt.executeQuery();
            int typeId;
            if (rs.next()) {
                typeId = rs.getInt(1);
            } else {
                System.out.println("Error: Type '" + type + "' not found. Please create the category first.");
                return false;
            }

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO instruments (name, type_id, price, stock) VALUES (?, ?, ?, ?)");
            stmt.setString(1, name);
            stmt.setInt(2, typeId);
            stmt.setInt(3, price);
            stmt.setInt(4, stock);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding instrument: " + e.getMessage());
            return false;
        }
    }
}