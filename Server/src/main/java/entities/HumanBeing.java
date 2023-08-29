package entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.time.ZonedDateTime;
import java.util.Objects;


public class HumanBeing implements Comparable<HumanBeing> {
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    @JacksonXmlProperty
    private Coordinates coordinates; //Поле не может быть null
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z")
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    private Boolean realHero; //Поле не может быть null
    private Boolean hasToothpick; //Поле не может быть null
    private Double impactSpeed; //Значение поля должно быть больше -992, Поле может быть null
    private String soundtrackName; //Поле не может быть null
    private Integer weaponType; //Поле не может быть null
    private Integer mood; //Поле не может быть null
    @JacksonXmlProperty
    private Car car; //Поле может быть null
    @JacksonXmlProperty(localName = "ownerId", isAttribute = true)
    private String ownerId;

    public HumanBeing() {
        coordinates = new Coordinates();
        car = new Car();
    }


    public HumanBeing(int newId, String newName, Coordinates newCoordinates, ZonedDateTime newCreationDate, Boolean newRealHero, Boolean newHasToothpick, Integer newImpactSpeed, String newSoundtrackName, WeaponType weaponType, Mood mood, Car newCar) {
    }

    public HumanBeing(String ownerId, int id, String name, Coordinates coordinates, ZonedDateTime creationDate, Boolean realHero, Boolean hasToothpick, Double impactSpeed, String soundtrackName, Integer weaponType, Integer mood, Car car) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.realHero = realHero;
        this.hasToothpick = hasToothpick;
        this.impactSpeed = impactSpeed;
        this.soundtrackName = soundtrackName;
        this.weaponType = weaponType;
        this.mood = mood;
        this.car = car;
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getRealHero() {
        return realHero;
    }

    public void setRealHero(Boolean realHero) {
        this.realHero = realHero;
    }

    public Boolean getHasToothpick() {
        return hasToothpick;
    }

    public void setHasToothpick(Boolean hasToothpick) {
        this.hasToothpick = hasToothpick;
    }

    public Double getImpactSpeed() {
        return impactSpeed;
    }

    public void setImpactSpeed(Double impactSpeed) {
        this.impactSpeed = impactSpeed;
    }

    public String getSoundtrackName() {
        return soundtrackName;
    }

    public void setSoundtrackName(String soundtrackName) {
        this.soundtrackName = soundtrackName;
    }

    public Integer getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(Integer weaponType) {
        this.weaponType = weaponType;
    }

    public Integer getMood() {
        return mood;
    }

    public void setMood(Integer mood) {
        this.mood = mood;
    }

    public Car getCar() {
        return car;
    }


    public void setCar(Car car) {
        this.car = car;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ownerId: " + ownerId + " id: " + id + " name: " + name + " coordinates: " + coordinates + " creationDate: " + creationDate + " realHero: " + realHero + " hasToothpicks: " + hasToothpick + " impactSpeed: " + impactSpeed + " soundtrackName: " + soundtrackName + " weaponType: " + weaponType + " moon: " + mood + " car: " + car + "\n";
    }


    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public int compareTo(HumanBeing o) {
        return Double.compare(o.impactSpeed, this.getImpactSpeed());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HumanBeing that = (HumanBeing) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(creationDate, that.creationDate) && Objects.equals(coordinates, that.coordinates) &&
                Objects.equals(realHero, that.realHero) && Objects.equals(hasToothpick, that.hasToothpick) &&
                Objects.equals(impactSpeed, that.impactSpeed) && Objects.equals(soundtrackName, that.soundtrackName) &&
                weaponType == that.weaponType && mood == that.mood && Objects.equals(car, that.car);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, realHero, hasToothpick, impactSpeed, soundtrackName, weaponType, mood, car);
    }
}
