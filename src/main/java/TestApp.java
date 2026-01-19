import logic.Auth;
import model.User;

public class TestApp {
    public static void main(String[] args) {
        System.out.println("TEST AUTH");
        Auth auth = new Auth();
        
        // Register
        User u = auth.register("testuser", "pass1");
        if (u != null) System.out.println("Registered: " + u.getId());
        else System.out.println("Register failed");
        
        // Login
        User u2 = auth.login("testuser", "pass1");
        if (u2 != null) System.out.println("Logged in: " + u2.getUsername());
        else System.out.println("Login failed");
    }
}