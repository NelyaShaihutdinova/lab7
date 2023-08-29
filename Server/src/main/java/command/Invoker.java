package command;

import builders.CommandShaper;
import builders.ResponseShaper;
import exception.ExecuteScriptException;
import exception.FileException;
import exception.ValidException;
import server.SQLCollectionController;
import server.SqlUserManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Invoker {
    private CollectionController cc;
    private SQLCollectionController sqlCollectionController;
    private Map<String, Command> commands = new HashMap<>();
    private CommandShaper commandShaper;


    private String param;
    private String personData;
    private String ownerId;
    private Connection connection;


    //конструктор класса с добавлением в HashMap commands команд и объектов соответствующего класса
    public Invoker(CommandShaper commandShaper, CollectionController cc, SQLCollectionController sqlCollectionController) {
        this.cc = cc;
        this.commandShaper = commandShaper;
        this.sqlCollectionController = sqlCollectionController;
        commands.put("add", new AddCommand(param, cc, sqlCollectionController));
        commands.put("add", new AddCommand(param, cc, sqlCollectionController, personData));
        commands.put("show", new ShowCommand(cc));
        commands.put("clear", new ClearCommand(cc, sqlCollectionController));
        commands.put("info", new InfoCommand(cc));
        commands.put("help", new HelpCommand(this));
        commands.put("add_if_min", new AddIfMinCommand(param, cc, sqlCollectionController));
        commands.put("remove_greater", new RemoveGreaterCommand(param, cc, sqlCollectionController));
        commands.put("remove_lower", new RemoveLowerCommand(param, cc, sqlCollectionController));
//        commands.put("save", new SaveCommand(ownerId, cc));
//        commands.put("update", new UpdateIdCommand(ownerId, param, cc));
        commands.put("update", new UpdateIdCommand(param, cc, personData,sqlCollectionController));
        commands.put("remove_by_id", new RemoveByIdCommand(param, cc, sqlCollectionController));
        commands.put("count_greater_than_impact_speed", new CountGreaterCommand(param, cc, sqlCollectionController));
        commands.put("filter_contains_soundtrack_name", new FilterContainsCommand(param, cc, sqlCollectionController));
        commands.put("filter_greater_than_impact_speed", new FilterGreaterCommand(param, cc, sqlCollectionController));
//        commands.put("execute_script", new ExecuteScriptCommand(ownerId, param, cc));
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    public String getAccount(SqlUserManager sqlUserManager) throws SQLException, NoSuchAlgorithmException {
        sqlUserManager.initUserTable();
        if (!(sqlUserManager.searchUser(commandShaper.getAuntification())) && commandShaper.getAuntification().getUserAction().equals("reg")) {
            sqlUserManager.registration(commandShaper.getAuntification());
            return String.valueOf(sqlUserManager.login(commandShaper.getAuntification()));
        } else if ((sqlUserManager.searchUser(commandShaper.getAuntification())) && commandShaper.getAuntification().getUserAction().equals("login")) {
            return String.valueOf(sqlUserManager.login(commandShaper.getAuntification()));
        } else if ((sqlUserManager.searchUser(commandShaper.getAuntification())) && commandShaper.getAuntification().getUserAction().equals("reg")){
            return String.valueOf(sqlUserManager.login(commandShaper.getAuntification()));
        }
        return null;
    }

    //построчное чтение команд с изменением конструктора класса команды через рефлексию
    public ResponseShaper readCommand(SqlUserManager sqlUserManager) throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        try {
            ownerId = getAccount(sqlUserManager);
            String commandName = commandShaper.getName();
            Command command = commands.get(commandName);
            String line = "no";
            String[] tokensCheck = commandShaper.getParam().split(" ");
            if (ownerId == null){
                ResponseShaper responseShaper = new ResponseShaper("проблемы с авторизацией");
                return responseShaper;
            }
            else if (Objects.isNull(command)) {
                ResponseShaper responseShaper = new ResponseShaper(String.valueOf(new ValidException("Команда не найдена")));
                return responseShaper;
            } else if (commandShaper.getParam().equals(line)) {
                return command.execute(ownerId);

            } else if (tokensCheck.length == 11) {
                String[] tokens = commandShaper.getParam().split(" ", 2);
                param = tokens[0];
                personData = tokens[1];
                Command updatedCommand = command.getClass().getConstructor(String.class, CollectionController.class, String.class, SQLCollectionController.class).newInstance(param, cc, personData, sqlCollectionController);
                commands.replace(String.valueOf(command), updatedCommand);
                return updatedCommand.execute(ownerId);
            } else {
                String param = commandShaper.getParam();
                Command updatedCommand = command.getClass().getConstructor(String.class, CollectionController.class, SQLCollectionController.class).newInstance(param, cc, sqlCollectionController);
                commands.replace(String.valueOf(command), updatedCommand);
                return updatedCommand.execute(ownerId);
            }

        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (FileException e) {
            throw new RuntimeException(e);
        } catch (ValidException e) {
            System.out.println(e.getMessage());
        } catch (ExecuteScriptException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    //построчное чтение команд с изменением конструктора класса команды через рефлексию для execute_script
    public ResponseShaper readCommandsScript(String newParam) throws ValidException {
        try {
            File script = new File(newParam);
            FileReader fr = new FileReader(script);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            if (line != null) {
                String[] tokensCheck = line.split(" ");
                if (tokensCheck.length == 12) {
                    String[] tokens = line.split(" ", 3);
                    Command command = commands.get(tokens[0]);
                    if (Objects.isNull(command)) {
                        ResponseShaper responseShaper = new ResponseShaper(String.valueOf(new ValidException("Комманда не найдена")));
                        return responseShaper;
                    }
                    param = tokens[1];
                    personData = tokens[2];
                    Command updatedCommand = command.getClass().getConstructor(String.class, CollectionController.class, String.class).newInstance(param, cc, personData);
                    commands.replace(tokens[0], updatedCommand);
                    return updatedCommand.execute(ownerId);
                } else if (tokensCheck.length == 11 || tokensCheck.length == 2) {
                    String[] tokens = line.split(" ", 2);
                    Command command = commands.get(tokens[0]);
                    if (Objects.isNull(command)) {
                        ResponseShaper responseShaper = new ResponseShaper(String.valueOf(new ValidException("Комманда не найдена")));
                        return responseShaper;
                    }
                    param = tokens[1];
                    Command updatedCommand = command.getClass().getConstructor(String.class, CollectionController.class).newInstance(param, cc);
                    commands.replace(tokens[0], updatedCommand);
                    return updatedCommand.execute(ownerId);
                } else {
                    String[] tokens = line.split(" ");
                    Command command = commands.get(tokens[0]);
                    if (Objects.isNull(command)) {
                        ResponseShaper responseShaper = new ResponseShaper(String.valueOf(new ValidException("Комманда не найдена")));
                        return responseShaper;
                    }
                    return command.execute(ownerId);
                }
            }
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    String[] newTokensCheck = line.split(" ");
                    if (newTokensCheck.length == 12) {
                        String[] newTokens = line.split(" ", 3);
                        Command command = commands.get(newTokens[0]);
                        if (Objects.isNull(command)) {
                            ResponseShaper responseShaper = new ResponseShaper(String.valueOf(new ValidException("Комманда не найдена")));
                            return responseShaper;
                        }
                        param = newTokens[1];
                        personData = newTokens[2];
                        Command updatedCommand = command.getClass().getConstructor(String.class, CollectionController.class, String.class).newInstance(param, cc, personData);
                        commands.replace(newTokens[0], updatedCommand);
                        return updatedCommand.execute(ownerId);
                    } else if (newTokensCheck.length == 2 || newTokensCheck.length == 11) {
                        String[] newTokens = line.split(" ", 2);
                        Command command = commands.get(newTokens[0]);
                        if (Objects.isNull(command)) {
                            ResponseShaper responseShaper = new ResponseShaper(String.valueOf(new ValidException("Комманда не найдена")));
                            return responseShaper;
                        }
                        param = newTokens[1];
                        Command updatedCommand = command.getClass().getConstructor(String.class, CollectionController.class).newInstance(param, cc);
                        commands.replace(newTokens[0], updatedCommand);
                        return updatedCommand.execute(ownerId);
                    } else {
                        String[] tokens = line.split(" ");
                        Command command = commands.get(tokens[0]);
                        if (Objects.isNull(command)) {
                            ResponseShaper responseShaper = new ResponseShaper(String.valueOf(new ValidException("Комманда не найдена")));
                            return responseShaper;
                        }
                        return command.execute(ownerId);
                    }
                }
            }
        } catch (IOException | FileException | ExecuteScriptException | InvocationTargetException |
                 InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
