package com.ericbouchut.springboot.safetynet.config;

import com.ericbouchut.springboot.safetynet.data.DataLoader;
import com.ericbouchut.springboot.safetynet.model.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;


@Configuration
public class SafetynetConfiguration {

    /**
     * ObjectMapper cannot deserialize dates by default.
     * When deserializing <code>LocalDate</code> from JSON
     * we get this error:
     * Java 8 date/time type `java.time.LocalDate` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling
     * <p>
     * This class workarounds this issue
     * by activating the <code>JavaTimeModule</code> module.
     *
     * @see <a href="https://stackoverflow.com/a/74188917">...</a>
     * Posted by Toni, modified by community. See post 'Timeline' for change history
     * Retrieved 2025-11-17, License - CC BY-SA 4.0
     */
    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    /**
     * Deserialize the <code>Data</code> object from a JSON file
     * containing the data of the Safety Net altering application.
     * That is read and parse the JSON file,to instantiate the corresponding "entities"
     * (<code>Data</code>, <code>Person</code>, <code>FireStation</code>, <code>MedicalResord</code>.
     * The path of the JSON file can be configured in <code>application.properties</code>
     * with the <code>app.data.file</code> property.
     * The default value is <code>classpath:data/data.json</code>
     * which means that by default the JSON file is <code>resources/data/data.json</code>.
     *
     * @param dataLoader The bean in charge of loading the Safety Net alerting application <code>Data</code> from a JSON file
     * @return the data loaded from the JSON file
     *
     * @see org.springframework.core.io.Resource
     */
    @Bean
    public Data data(DataLoader dataLoader) {
        return dataLoader.load();
    }

    /**
     * The only goal of this bean provider method is to
     * allow tests to use a different <code>Clock</code>
     * for instance to set a <b>fixed</b> current date and time.
     *
     * @return the default Clock for the current timezone.
     */
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}

