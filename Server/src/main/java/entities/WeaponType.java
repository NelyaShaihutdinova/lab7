package entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;
import java.util.Optional;

public enum WeaponType {
    @JacksonXmlProperty(localName = "1")
    W1(1),
    @JacksonXmlProperty(localName = "2")
    W2(2),
    @JacksonXmlProperty(localName = "3")
    W3(3);

    private final int order;

    WeaponType(int order) {
        this.order = order;
    }

    public static WeaponType fromInteger(int order) {
        Optional<WeaponType> result = Arrays.stream(WeaponType.values()).filter(o -> o.order == order).findFirst();
        return result.orElse(null);
    }
}
