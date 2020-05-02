package be.syntra.devshop.DevshopFront.models.dtos;

import be.syntra.devshop.DevshopFront.models.Product;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartDto {
    private String user;
    private LocalDateTime cartCreationDateTime;
    private List<Product> products;
    private boolean activeCart;
    private boolean finalizedCart;
    private boolean paidCart;
}
