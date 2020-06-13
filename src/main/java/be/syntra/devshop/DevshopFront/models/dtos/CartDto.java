package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartDto {
    private String user;
    private LocalDateTime cartCreationDateTime = LocalDateTime.now();
    private boolean finalizedCart;
    private boolean paidCart;
    private List<CartProductDto> cartProductDtoList = new ArrayList<>();
}
