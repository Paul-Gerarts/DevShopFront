package be.syntra.devshop.DevshopFront.client;

import be.syntra.devshop.DevshopFront.model.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "productClient", url = "${baseUrl}/products)")
public interface ProductClient {

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    void addProduct(ProductDto productDto);
}
