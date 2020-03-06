package be.syntra.devshop.DevshopFront.service;

import be.syntra.devshop.DevshopFront.model.SaveStatus;
import be.syntra.devshop.DevshopFront.model.dto.ProductDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public ProductDto createEmptyProduct() {
        return new ProductDto();
    }

    @Override
    public SaveStatus addProduct(ProductDto productDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ProductDto> request = new HttpEntity<>(productDto, httpHeaders);
        ProductDto productDtoResultFromBackEnd = restTemplate.postForObject("http://localhost:8080/products", request, ProductDto.class);
        if (productDto.equals(productDtoResultFromBackEnd)) {
            return SaveStatus.SAVED;
        }
        return SaveStatus.ERROR;
    }
}
