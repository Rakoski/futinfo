package br.com.backend;

import br.com.backend.common.configs.ApplicationContextProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import({ApplicationContextProvider.class, ApplicationContextProviderTest.TestConfig.class})
class ApplicationContextProviderTest {

    @Test
    void shouldGetBeanByClass() {
        TestService service = ApplicationContextProvider.getBean(TestService.class);
        assertNotNull(service);
        assertEquals("test", service.getMessage());
    }

    @Test
    void shouldThrowExceptionForNonExistentBean() {
        assertThrows(BeansException.class, () -> {
            ApplicationContextProvider.getBean(NonExistentService.class);
        });
    }

    @Configuration
    static class TestConfig {
        @Bean
        TestService testService() {
            return new TestService();
        }
    }

    static class TestService {
        public String getMessage() {
            return "test";
        }
    }

    static class NonExistentService {
    }
}