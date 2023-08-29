package entities;


import exception.ValidException;

import java.util.Objects;

public class HumanSimpleValidator implements Validator<HumanBeing> {
    //проверка human being на валидность
    @Override
    public boolean checkElement(HumanBeing object) throws ValidException {
        boolean check = !Objects.isNull(object) && !Objects.isNull(object.getRealHero()) && !Objects.isNull(object.getHasToothpick()) && !Objects.isNull(object.getImpactSpeed()) &&
                !Objects.isNull(object.getSoundtrackName()) && object.getImpactSpeed() > -992 && !Objects.isNull(object.getWeaponType()) && !Objects.isNull(object.getMood());
        if (!check) {
            throw new ValidException("Element isn't valid");
        }
        return true;
    }

    //проверка car на валидность
    private boolean checkCar(Car car) {
        return !Objects.isNull(car);
    }

    //проверка coordinates на валидность
    private boolean checkCoordinates(Coordinates coordinates) {
        return !Objects.isNull(coordinates) && !Objects.isNull(coordinates.getX()) && !Objects.isNull(coordinates.getY()) && coordinates.getY() >= 945;
    }

}
