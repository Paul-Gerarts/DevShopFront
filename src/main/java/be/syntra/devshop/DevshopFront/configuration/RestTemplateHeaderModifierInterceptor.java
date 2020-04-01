package be.syntra.devshop.DevshopFront.configuration;

import be.syntra.devshop.DevshopFront.security.services.SecurityLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestTemplateHeaderModifierInterceptor implements ClientHttpRequestInterceptor {

    @Value("${loginEndpoint}")
    private String login;

    @Autowired
    private SecurityLoginService securityLoginService;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (!request.getURI().getPath().equals(login)) {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + securityLoginService.login());
        }
        return execution.execute(request, body);
    }
}
