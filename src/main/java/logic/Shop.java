package logic;

import db.DB;
import model.Instrument;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Shop {

    public List<Instrument> getCatalog(String type) {
        List<Instrument> list = new ArrayList<>();
        
        String sql = "SELECT instruments.id, instruments.name, instruments.price, instruments.stock, instrument_types.name as type_name " +
                     "FROM instruments JOIN instrument_types ON instruments.type_id = instrument_types.id";
        
        if (type != null) {
            sql += " WHERE instrument_types.name = ?";
        }
        
        try {
            Connection conn = DB.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            if (type != null) {
                stmt.setString(1, type);
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Instrument(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("type_name"),
                    rs.getInt("price"),
                    rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error loading catalog: " + e.getMessage());
        }
        return list;
    }
}