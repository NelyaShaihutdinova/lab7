package entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "coordinates")
public class Coordinates {
    @JacksonXmlProperty
    private Double x; //Поле не может быть null
    @JacksonXmlProperty
    private Integer y; //Максимальное значение поля: 945, Поле не может быть null

    public Coordinates() {

    }

    public Coordinates(Double x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    //формарование координат из x и y
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getX()).append(" ").append(getY());
        return stringBuilder.toString();
    }
}
