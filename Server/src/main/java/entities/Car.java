package entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "car")
public class Car {
    @JacksonXmlProperty
    private boolean cool;

    public Car() {
    }

    public boolean isCool() {
        return cool;
    }

    public void setCool(boolean cool) {
        this.cool = cool;
    }

    public Car(boolean cool) {
        this.cool = cool;
    }

    @Override
    public String toString() {
        return String.valueOf(cool);
    }
}
