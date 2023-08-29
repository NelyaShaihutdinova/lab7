package entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;
import java.util.Optional;

public enum Mood {

    @JacksonXmlProperty(localName = "1")
    M1(1),
    @JacksonXmlProperty(localName = "2")
    M2(2),
    @JacksonXmlProperty(localName = "3")
    M3(3),
    @JacksonXmlProperty(localName = "4")
    M4(4);
    private final int order;

    Mood(int order) {
        this.order = order;
    }

    public static Mood fromInteger(int order) {
        Optional<Mood> result = Arrays.stream(Mood.values()).filter(o -> o.order == order).findFirst();
        return result.orElse(null);
    }
}
