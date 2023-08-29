package entities;


import exception.ValidException;

public interface Validator<T> {
    boolean checkElement(T object) throws ValidException;
}
