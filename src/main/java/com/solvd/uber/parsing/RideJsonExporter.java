package com.solvd.uber.parsing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.solvd.uber.models.Ride;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RideJsonExporter {

    private static final Logger LOGGER = LogManager.getLogger("RidesLogger");
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static void toJson(List<Ride> rides, String fileName) {
        try {
            mapper.writeValue(new File(fileName), rides);
            LOGGER.info("{} is saved", fileName);
        } catch (IOException e) {
            LOGGER.error("Error saving JSON file {}", fileName, e);
            throw new RuntimeException(e);
        }
    }

    public static List<Ride> fromJson(File file) {
        try {
            return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Ride.class));
        } catch (IOException e) {
            LOGGER.error("Error reading JSON file {}", file.getAbsolutePath(), e);
            throw new RuntimeException(e);
        }
    }
}
