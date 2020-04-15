package be.syntra.devshop.DevshopFront.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UpdateProductCache {
    @Builder.Default
    Boolean cacheNeedsUpdate = true;
}
