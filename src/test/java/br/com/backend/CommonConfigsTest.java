package br.com.backend;

import br.com.backend.common.configs.CommonConfigs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(CommonConfigs.class)
class CommonConfigsTest {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ObjectWriter objectWriter;

    @Test
    void modelMapperShouldBeConfiguredCorrectly() {
        assertNotNull(modelMapper);

        var config = modelMapper.getConfiguration();
        assertTrue(config.isFieldMatchingEnabled());
        assertEquals(org.modelmapper.config.Configuration.AccessLevel.PRIVATE,
                config.getFieldAccessLevel());
        assertEquals(MatchingStrategies.STRICT, config.getMatchingStrategy());

        TestSourceClass source = new TestSourceClass("test", 123);
        TestDestClass dest = modelMapper.map(source, TestDestClass.class);
        assertEquals(source.name(), dest.name);
        assertEquals(source.value(), dest.value);
    }

    @Test
    void objectMapperShouldHandleDatesCorrectly() {
        assertNotNull(objectMapper);

        assertFalse(objectMapper
                .getSerializationConfig()
                .hasSerializationFeatures(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS.getMask()));

        LocalDateTime now = LocalDateTime.now();
        TestDateClass dateClass = new TestDateClass(now);

        try {
            String json = objectMapper.writeValueAsString(dateClass);
            TestDateClass deserialized = objectMapper.readValue(json, TestDateClass.class);
            assertEquals(dateClass.getDateTime(), deserialized.getDateTime());
        } catch (Exception e) {
            fail("Date serialization/deserialization failed", e);
        }
    }

    @Test
    void objectMapperShouldHandleEmptyBeans() {
        assertFalse(objectMapper
                .getSerializationConfig()
                .hasSerializationFeatures(SerializationFeature.FAIL_ON_EMPTY_BEANS.getMask()));
    }

    @Test
    void objectWriterShouldBePrettyPrinted() {
        assertNotNull(objectWriter);

        TestSourceClass test = new TestSourceClass("test", 123);
        try {
            String json = objectWriter.writeValueAsString(test);
            assertTrue(json.contains("\n"));
        } catch (Exception e) {
            fail("Pretty printing failed", e);
        }
    }

    private record TestSourceClass(String name, int value) {}

    @Setter
    @Getter
    private static class TestDestClass {
        private String name;
        private int value;
    }

    @Setter
    @Getter
    private static class TestDateClass {
        public TestDateClass() {
        }

        private LocalDateTime dateTime;

        public TestDateClass(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }
    }
}