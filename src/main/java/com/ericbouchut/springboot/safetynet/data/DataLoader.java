package com.ericbouchut.springboot.safetynet.data;

import com.ericbouchut.springboot.safetynet.exception.JsonConfigurationLoadException;
import com.ericbouchut.springboot.safetynet.model.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Load the application JSON input data
 * and creates an instance of <code>{@link Data}</code>.
 *
 * TODO: Choose a more appropriate package for this class
 */
@Component
public class DataLoader {
    /**
     * The object used to read the JSON file and create a Java object
     * (deserialize <code>Data</code>).
     */
    private final ObjectMapper objectMapper;

    /**
     * The file where to read JSON data from.
     */
    private final Resource dataFile;

    /**
     * Parse the JSON input file to create an instance of  <code>{@link Data}</code>.
     * @param objectMapper the object in charge of deserializing the JSON data into a <code>{@link Data}</code>
     * @param dataFile the location of the JSON data input (in <code>Resource</code> notation where to read the JSON data from)
     */
    public DataLoader(
            ObjectMapper objectMapper,
            @Value("${app.data.file}")
            final Resource dataFile
    ) {
        this.objectMapper = objectMapper;
        this.dataFile     = dataFile;
    }

    /**
     * Load the Safety Net JSON application data file to deserialize the <code>Data</code>.
     *
     * @return <code>Data</code> a data object that contains all the entities
     * @throws JsonConfigurationLoadException when an error occurs while loading the JSON file
     */
    public Data load() {
        try {
            return objectMapper.readValue(
                    dataFile.getInputStream(),
                    Data.class
            );
        } catch (IOException e) {
            throw new JsonConfigurationLoadException("Cannot load the JSON configuration file: " + dataFile, e);
        }
    }
}
