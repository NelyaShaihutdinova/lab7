package command;


import builders.ResponseShaper;
import entities.HumanBeing;
import exception.ValidException;
import server.SQLCollectionController;

import java.sql.SQLException;

public class UpdateIdCommand implements Command {
    private CollectionController cc;
    private String param;
    private String personData;
    private SQLCollectionController sqlCollectionController;


    public UpdateIdCommand(String param, CollectionController cc, SQLCollectionController sqlCollectionController) {
        this.param = param;
        this.cc = cc;
        this.sqlCollectionController=sqlCollectionController;
    }

    public UpdateIdCommand(String param, CollectionController cc, String personData, SQLCollectionController sqlCollectionController ) {
        this.param = param;
        this.cc = cc;
        this.personData = personData;
        this.sqlCollectionController=sqlCollectionController;
    }

    //Смотря, выполняется ли команда execute_script, выполняется метод из CollectionController
    public ResponseShaper execute(String ownerId) throws ValidException, SQLException {
        return cc.updateIdScript(ownerId, personData, param, sqlCollectionController);
    }
}
