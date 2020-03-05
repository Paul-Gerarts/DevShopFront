package be.syntra.devshop.DevshopFront.service;

import be.syntra.devshop.DevshopFront.client.ProductClient;
import be.syntra.devshop.DevshopFront.model.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductClient productClient;

    @Autowired
    public ProductServiceImpl(ProductClient productClient) {
        this.productClient = productClient;
    }

    @Override
    public ProductDto createEmptyProduct() {
        return new ProductDto();
    }

    @Override
    public void addProduct(ProductDto productDto) {
        productClient.addProduct(productDto);
    }
}
