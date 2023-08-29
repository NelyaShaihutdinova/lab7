package command;


import builders.ResponseShaper;
import server.SQLCollectionController;

import java.sql.SQLException;

public class ClearCommand implements Command {

    private CollectionController cc;
    private SQLCollectionController sqlCollectionController;

    public ClearCommand(CollectionController cc, SQLCollectionController sqlCollectionController) {
        this.cc = cc;
        this.sqlCollectionController = sqlCollectionController;
    }

    //Смотря, выполняется ли команда execute_script, выполняется метод из CollectionController
    @Override
    public ResponseShaper execute(String ownerId) {
        try {
            sqlCollectionController.clear(Long.parseLong(ownerId));
        } catch (SQLException e) {
            return new ResponseShaper("ошибка при очистки коллекции");
        }
        return cc.clear(ownerId);
    }
}
