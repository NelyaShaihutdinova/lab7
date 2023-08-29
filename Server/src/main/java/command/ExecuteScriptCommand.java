//package command;
//
//
//import builders.ResponseShaper;
//import exception.ExecuteScriptException;
//import exception.ValidException;
//
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//
//public class ExecuteScriptCommand extends Throwable implements Command {
//    private CollectionController cc;
//    private String param;
//    private String ownerId;
//
//    public ExecuteScriptCommand(String ownerId, String param, CollectionController cc) {
//        this.param = param;
//        this.ownerId = ownerId;
//        this.cc = cc;
//    }
//
//    //выполняется метод из CollectionController
//    public ResponseShaper execute() throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, ValidException, ExecuteScriptException {
//        return cc.executeScript(param);
//    }
//}
