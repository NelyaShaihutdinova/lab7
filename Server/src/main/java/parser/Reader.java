package parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import entities.HumanBeing;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Reader {
    private File file;

    public Reader(File file) {
        this.file = file;
    }

    public List<HumanBeing> getPersons() {
        return Arrays.asList(parsingPersonsFromXml(readFileToString(file)));
    }

    private String readFileToString(File file) {
        byte[] data;
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            data = inputStream.readAllBytes();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("exception");
            return "";
        }
        return new String(data);
    }

    private HumanBeing[] parsingPersonsFromXml(String data) {
        XmlMapper xmlmapper = new XmlMapper();
        try {
            xmlmapper.registerModule(new JavaTimeModule());
            HumanBeing[] humanBeing = xmlmapper.readValue(data, HumanBeing[].class);
            return humanBeing;
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            System.out.println("no");
            return new HumanBeing[0];
        }
    }
}