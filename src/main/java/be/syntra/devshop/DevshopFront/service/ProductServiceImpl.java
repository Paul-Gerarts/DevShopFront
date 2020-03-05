package be.syntra.devshop.DevshopFront.service;

import be.syntra.devshop.DevshopFront.model.dto.ProductDto;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public ProductDto createEmptyProduct() {
        return new ProductDto();
    }

    @Override
    public void addProduct(ProductDto productDto) {

    }
}
