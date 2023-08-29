//package command;
//
//
//import builders.ResponseShaper;
//import exception.FileException;
//
//import java.io.IOException;
//
//public class SaveCommand implements Command {
//    private CollectionController cc;
//    private String ownerId;
//
//    public SaveCommand(String ownerId, CollectionController cc) {
//        this.ownerId = ownerId;
//        this.cc = cc;
//    }
//
//    //выполняется метод из CollectionController
//    @Override
//    public ResponseShaper execute() throws IOException, FileException {
//        return cc.save();
//    }
//}
