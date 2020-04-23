package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.dtos.ErrorDto;
import be.syntra.devshop.DevshopFront.testutils.JsonUtils;
import be.syntra.devshop.DevshopFront.testutils.TestWebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RestClientTest(ErrorServiceImpl.class)
@ExtendWith(MockitoExtension.class)
@Import({TestWebConfig.class, JsonUtils.class})
public class ErrorServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ErrorServiceImpl errorService;

    MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void canDetermineError() {
        // given
        ErrorDto dummyErrorDto = ErrorDto.builder()
                .errorCode("500")
                .message("Something went wrong...")
                .build();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(errorService.determineError(request)).thenReturn(dummyErrorDto);

        // when
        final ErrorDto resultErrorDto = errorService.determineError(request);

        // then
        assertThat(resultErrorDto.getMessage()).isEqualTo(dummyErrorDto.getMessage());
    }
}
