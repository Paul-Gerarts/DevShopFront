package be.syntra.devshop.DevshopFront.models.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchModelDto {
    // todo: expand model with DEV-015
    private String basicSearchTerm;
}
