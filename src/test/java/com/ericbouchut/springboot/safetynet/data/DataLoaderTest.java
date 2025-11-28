package com.ericbouchut.springboot.safetynet.data;

import com.ericbouchut.springboot.safetynet.exception.JsonConfigurationLoadException;
import com.ericbouchut.springboot.safetynet.model.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
public class DataLoaderTest {

    // See JavaSpringBootSafetynetConfiguration#objectMapper()
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void load_shouldParseJsonCorrectly() {
        Resource dataFileResource  = new ClassPathResource("data/data.json");

        DataLoader dataLoader = new DataLoader(
            objectMapper,
            dataFileResource
        );
        Data data = dataLoader.load();

        assertNotNull(data);

        assertNotNull(data.getPersons());
        assertEquals(23, data.getPersons().size());
        // "firestations" contains 13 entries
        // including this duplicate:
        //      {
        //            "address": "748 Townings Dr",
        //            "station": "3"
        //        }
        assertNotNull(data.getFireStations());
        assertEquals(12, data.getFireStations().size());

        assertNotNull(data.getMedicalRecords());
        assertEquals(23, data.getMedicalRecords().size());
    }

    @Test
    public void load_nonExistentJsonFile() {
        Resource nonExistentDataFileResource  = new ClassPathResource("non_existent.json");

        DataLoader dataLoader = new DataLoader(
                objectMapper,
                nonExistentDataFileResource
        );

        assertThrows(JsonConfigurationLoadException.class, dataLoader::load);
    }


}