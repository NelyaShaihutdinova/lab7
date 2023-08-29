package command;


import builders.ResponseShaper;
import exception.ValidException;
import server.SQLCollectionController;

import java.sql.SQLException;

public class RemoveGreaterCommand implements Command {
    private CollectionController cc;
    private String param;
    private SQLCollectionController sqlCollectionController;

    public RemoveGreaterCommand(CollectionController cc) {
        this.cc = cc;
    }

    public RemoveGreaterCommand(String param, CollectionController cc, SQLCollectionController sqlCollectionController) {
        this.cc = cc;
        this.param = param;
        this.sqlCollectionController=sqlCollectionController;
    }

    //Смотря, выполняется ли команда execute_script, выполняется метод из CollectionController
    public ResponseShaper execute(String ownerId) throws ValidException, SQLException {
        return cc.removeGreater(ownerId, param, sqlCollectionController);
    }
}
