package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.init.SimpleCacheCustomize;
import be.syntra.devshop.DevshopFront.security.services.SecurityLoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SecurityLoginServiceTest {

    @Autowired
    private SecurityLoginService securityLoginService;

    @Autowired
    private SimpleCacheCustomize simpleCacheCustomize;

    @Test
    void canReturnAuthTokenTest() {
        // given

        // when
        String authTokenResult = securityLoginService.login();

        // then
        assertThat(authTokenResult).isNotBlank();
        assertThat(authTokenResult.length()).isGreaterThan(64);
    }

    @Test
    void assertThatCacheIsSetTest() {
        // given
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager("test");

        // when
        simpleCacheCustomize.customize(cacheManager);

        // then
        assertThat(cacheManager.getCacheNames()).isNotEmpty();
        assertThat(cacheManager.getCache("test")).isNotNull();

        // clean up results
        requireNonNull(cacheManager.getCache("test")).clear();
    }
}
