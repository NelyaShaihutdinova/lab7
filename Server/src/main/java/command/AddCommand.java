package command;


import builders.ResponseShaper;
import exception.ValidException;
import server.SQLCollectionController;

public class AddCommand implements Command {
    private CollectionController cc;
    private String param;
    private String personData;
    private SQLCollectionController sqlCollectionController;

    public AddCommand(String param, CollectionController cc, SQLCollectionController sqlCollectionController) {
        this.param = param;
        this.cc = cc;
        this.sqlCollectionController = sqlCollectionController;
    }

    public AddCommand(String param, CollectionController cc, SQLCollectionController sqlCollectionController, String personData) {
        this.cc = cc;
        this.param = param;
        this.sqlCollectionController = sqlCollectionController;
        this.personData = personData;
    }

    //Смотря, выполняется ли команда execute_script, выполняется метод из CollectionController
    @Override
    public ResponseShaper execute(String ownerId) throws Exception {
        return cc.addNewHuman(ownerId, param, sqlCollectionController.addInDB(cc.personBuild(ownerId, param), Long.parseLong(ownerId)));
    }
}
