package command;

import builders.ResponseShaper;

public class HelpCommand implements Command {
    private Invoker invoker;

    public HelpCommand(Invoker invoker) {
        this.invoker = invoker;
    }

    //вывод всех элементов commands и их описания
    @Override
    public ResponseShaper execute(String ownerId) {
        String resultHelp = "help - вывести справку по доступным командам\nadd - добавить новый элемент в коллекцию\n" +
                "add_if_min - добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" +
                "clear - очистить коллекцию\ncount_greater_than_impact_speed impactSpeed - вывести количество элементов, значение поля impactSpeed которых больше заданного\n" +
                "execute_script file_name - считать и выполнить команды из указанного файла\n" +
                "filter_contains_soundtrack_name soundtrackName - вывести элементы, значение поля soundtrackName которых содержит заданную подстроку\n" +
                "filter_greater_than_impact_speed impactSpeed - вывести элементы, значение поля impactSpeed которых больше заданного\n" +
                "info - вывести информацию о коллекции\nremove_by_id id - удалить элемент из коллекции по его id\nremove_greater - удалить из коллекции все элементы, превышающие заданный\n" +
                "remove_lower - удалить из коллекции все элементы, меньшие, чем заданный\nshow - вывести все элементы коллекции\n" +
                "update id - обновить значение элемента коллекции, id которого равен заданному";
        ResponseShaper responseShaper = new ResponseShaper(resultHelp);
        return responseShaper;
    }
}
