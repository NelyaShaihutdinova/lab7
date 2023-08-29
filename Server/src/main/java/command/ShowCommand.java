package command;

import builders.ResponseShaper;

public class ShowCommand implements Command {
    private CollectionController cc;

    public ShowCommand(CollectionController cc) {
        this.cc = cc;
    }

    //выполняется метод из CollectionController
    @Override
    public ResponseShaper execute(String ownerId) {

        return cc.show();
    }
}
