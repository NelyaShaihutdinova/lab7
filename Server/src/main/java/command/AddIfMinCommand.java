package command;


import builders.ResponseShaper;
import exception.ValidException;
import server.SQLCollectionController;

public class AddIfMinCommand implements Command {
    private CollectionController cc;
    private String param;
    private SQLCollectionController sqlCollectionController;

    public AddIfMinCommand(CollectionController cc) {
        this.cc = cc;
    }

    public AddIfMinCommand(String param, CollectionController cc, SQLCollectionController sqlCollectionController) {
        this.cc = cc;
        this.param = param;
        this.sqlCollectionController=sqlCollectionController;
    }

    //Смотря, выполняется ли команда execute_script, выполняется метод из CollectionController
    public ResponseShaper execute(String ownerId) throws Exception {
        return cc.addIfMin(ownerId, param, sqlCollectionController);
    }
}
