package model;
import lombok.*;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class Instrument {
    private int id;
    private String name, type;
    private int price, stock;
}