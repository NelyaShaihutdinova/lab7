package parser;

import builders.FileBuilder;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exception.FileException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Writer<T> {
    private final XmlMapper xmlMapper = new XmlMapper();

    public String parsingPersonsToXml(List<T> collection) {
        try {
            String data;
            xmlMapper.registerModule(new JavaTimeModule());
            data = xmlMapper.writeValueAsString(collection);
            return data;
        } catch (IOException e) {
            System.out.println("exception");
            return "";
        }
    }

    public void writeCollectionToFile(String data, File file) throws IOException, FileException {
        if (FileBuilder.checkOpportunityForWrite(file)) {
            try {
                BufferedWriter writter = new BufferedWriter(new FileWriter(file));
                writter.write(data);
                writter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new FileException("Беда с правами на файл");
        }

    }
}



