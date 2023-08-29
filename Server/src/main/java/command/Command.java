package command;

import builders.ResponseShaper;
import exception.ExecuteScriptException;
import exception.FileException;
import exception.ValidException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

//Интерфейс для команд
public interface Command {
    ResponseShaper execute(String ownerId) throws Exception;

}
