package model;
import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class User {
    private int id;
    private String username, passwordHash;
}