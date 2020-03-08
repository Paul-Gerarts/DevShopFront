package be.syntra.devshop.DevshopFront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DevshopFrontApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevshopFrontApplication.class, args);
	}

}
