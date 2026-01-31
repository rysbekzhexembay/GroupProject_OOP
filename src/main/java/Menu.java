import logic.Auth;
import logic.Shop;
import model.User;
import java.util.Scanner;

public class Menu {
    private final Scanner sc = new Scanner(System.in);
    private final Auth auth = new Auth();
    private final Shop shop = new Shop();
    private User user;

    public void start() {
        while (true) {
            if (user == null) {
                System.out.println("\nMUSIC STORE");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("0. Exit");
                System.out.print("> ");
                
                String cmd = sc.nextLine();
                switch (cmd) {
                    case "1" -> login();
                    case "2" -> register();
                    case "0" -> System.exit(0);
                    default -> System.out.println("Invalid command.");
                }
            } else {
                System.out.println("\nUSER: " + user.getUsername() + " [" + user.getRole() + "]");
                System.out.println("1. Catalog");
                System.out.println("2. Filter by Type");
                System.out.println("3. Buy Item");
                System.out.println("4. My Orders");
                boolean isAdmin = "admin".equalsIgnoreCase(user.getRole());
                if (isAdmin) {
                    System.out.println("5. Add Category (Admin)");
                    System.out.println("6. Add Instrument (Admin)");
                }
                System.out.println("0. Logout");
                System.out.print("> ");
                
                String cmd = sc.nextLine();
                switch (cmd) {
                    case "1" -> catalog(null);
                    case "2" -> filter();
                    case "3" -> buy();
                    case "4" -> orders();
                    case "5" -> { if (isAdmin) addCategory(); else System.out.println("Invalid command."); }
                    case "6" -> { if (isAdmin) addInstrument(); else System.out.println("Invalid command."); }
                    case "0" -> user = null;
                    default -> System.out.println("Invalid command.");
                }
            }
        }
    }

    private void login() {
        System.out.print("Username: "); 
        String l = sc.nextLine();
        System.out.print("Password: "); 
        String p = sc.nextLine();
        user = auth.login(l, p);
        if (user == null) System.out.println("Login failed.");
    }

    private void register() {
        System.out.print("Username: "); 
        String l = sc.nextLine();
        System.out.print("Password: "); 
        String p = sc.nextLine();
        user = auth.register(l, p);
        if (user == null) System.out.println("Registration failed.");
    }

    private void catalog(String type) {
        var list = shop.getCatalog(type);
        if (list.isEmpty()) System.out.println("Catalog is empty.");
        
        list.forEach(i -> System.out.println("#" + i.getId() + " " + i.getName() + 
                " (" + i.getType() + ") - $" + i.getPrice() + 
                " [Stock: " + i.getStock() + "]"));
    }

    private void filter() {
        System.out.print("Enter type (Guitars/Drums): ");
        catalog(sc.nextLine());
    }

    private void buy() {
        try {
            System.out.print("Instrument ID: ");
            int id = Integer.parseInt(sc.nextLine());
            
            System.out.print("Card Number (16 digits): ");
            String card = sc.nextLine();
            
            System.out.print("Exp Date (mm/yy): ");
            String date = sc.nextLine();
            
            System.out.print("CVC (3 digits): ");
            String cvc = sc.nextLine();
            
            if (shop.buy(user, id, card, date, cvc)) {
                System.out.println("Purchase successful!");
            } else {
                System.out.println("Purchase failed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter numbers for ID.");
        }
    }

    private void orders() {
        var list = shop.getMyOrders(user);
        if (list.isEmpty()) System.out.println("No orders found.");
        
        list.forEach(o -> System.out.println("Order #" + o.getId() + 
                " | Item: " + o.getInstrumentName() + 
                " | Price: $" + o.getPrice() + 
                " | Date: " + o.getCreatedAt()));
    }

    private void addCategory() {
        System.out.print("Category Name: ");
        String name = sc.nextLine();
        if (shop.addCategory(name)) System.out.println("Category added.");
    }

    private void addInstrument() {
        try {
            System.out.print("Name: "); String name = sc.nextLine();
            System.out.print("Type: "); String type = sc.nextLine();
            System.out.print("Price: "); int price = Integer.parseInt(sc.nextLine());
            System.out.print("Stock: "); int stock = Integer.parseInt(sc.nextLine());
            
            if (shop.addInstrument(name, type, price, stock)) System.out.println("Instrument added.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        }
    }
}