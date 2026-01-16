package model;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class Order {
    private int id, userId, price;
    private String instrumentName, cardLast4;
    private LocalDateTime createdAt;
}