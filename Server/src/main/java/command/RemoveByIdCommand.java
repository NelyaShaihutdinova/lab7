package command;


import builders.ResponseShaper;
import exception.ValidException;
import server.SQLCollectionController;

import java.sql.SQLException;

public class RemoveByIdCommand implements Command {
    private CollectionController cc;
    private String param;
    private SQLCollectionController sqlCollectionController;

    public RemoveByIdCommand(String param, CollectionController cc, SQLCollectionController sqlCollectionController) {
        this.param = param;
        this.cc = cc;
        this.sqlCollectionController=sqlCollectionController;
    }

    //выполняется метод из CollectionController
    public ResponseShaper execute(String ownerId) throws ValidException, SQLException {
        return cc.removeId(param, sqlCollectionController, ownerId);
    }
}
