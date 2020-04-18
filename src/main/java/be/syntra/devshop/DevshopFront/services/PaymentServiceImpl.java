package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.StatusNotification;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
//
//    @Value("${baseUrl}")
//    private String baseUrl;
//
//    @Value("${cartEndpoint}")
//    private String endpoint;
//
//    private String resourceUrl = null;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @PostConstruct
//    private void init() {
//        resourceUrl = baseUrl.concat(endpoint);
//    }

    /**
     * Here we send a rest call to the backend So the payment is persisted in the database.
     *
     * @return StatusNotification since we want to confirm the payment is persisted.
     */
    @Override
    public StatusNotification archivePayment() {
        return null;
    }
}
