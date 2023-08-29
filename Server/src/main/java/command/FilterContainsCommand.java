package command;


import builders.ResponseShaper;
import exception.ValidException;
import server.SQLCollectionController;

public class FilterContainsCommand implements Command {
    private CollectionController cc;
    private String param;
    private SQLCollectionController sqlCollectionController;

    public FilterContainsCommand(String param, CollectionController cc, SQLCollectionController sqlCollectionController) {
        this.param = param;
        this.cc = cc;
        this.sqlCollectionController=sqlCollectionController;
    }

    //выполняется метод из CollectionController
    public ResponseShaper execute(String ownerId) throws ValidException {
        return cc.filterContains(param);
    }
}
