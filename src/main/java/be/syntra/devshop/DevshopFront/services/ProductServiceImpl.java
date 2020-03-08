package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.SaveStatus;
import be.syntra.devshop.DevshopFront.models.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductServiceImpl implements ProductService {
    @Value("${baseUrl}")
    String baseUrl;
    @Value("${productsEndpoint}")
    String endpoint;

    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ProductDto createEmptyProduct() {
        return new ProductDto();
    }

    @Override
    public SaveStatus addProduct(ProductDto productDto) {
        final String url = baseUrl.concat(endpoint);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ProductDto> request = new HttpEntity<>(productDto, httpHeaders);
        try {
            ResponseEntity<ProductDto> productDtoResponseEntity = restTemplate.postForEntity(url, request, ProductDto.class);
            if (HttpStatus.CREATED.equals(productDtoResponseEntity.getStatusCode())) {
                logger.info("addProduct() -> saved > " + productDto.toString());
                return SaveStatus.SAVED;
            }
        } catch (Exception e) {
            //System.out.println(e.getCause());
            logger.error("addProduct() -> " + e.getCause().toString());
            //System.out.println(e.getLocalizedMessage());
            logger.error("addProduct() -> " + e.getLocalizedMessage());
        }
        return SaveStatus.ERROR;
    }
}
