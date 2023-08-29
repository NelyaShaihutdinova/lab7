package command;


import builders.ResponseShaper;
import entities.Car;
import entities.Coordinates;
import entities.HumanBeing;
import entities.Validator;
import exception.ExecuteScriptException;
import exception.ValidException;
import server.SQLCollectionController;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class CollectionController {
    private final Validator<HumanBeing> humanValidator;
    private final RecursionChecker recursionChecker = new RecursionChecker();
    private HashSet<HumanBeing> collection;
    private ZonedDateTime creationDate;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public CollectionController(Validator<HumanBeing> validator) {
        this.collection = new HashSet<>();
        this.creationDate = ZonedDateTime.now();
        this.humanValidator = validator;
    }

    //считываем человека, проверяем на валидность и добавляем в коллекцию
    public ResponseShaper addNewHuman(String ownerId, String param, Long humanId) throws ValidException {
        lock.writeLock().lock();
        HumanBeing newHumanBeing = personBuild(ownerId, param);
        humanValidator.checkElement(newHumanBeing);
        newHumanBeing.setId(Integer.parseInt(String.valueOf(humanId)));
        collection.add(newHumanBeing);
        sort();
        ResponseShaper responseShaper = new ResponseShaper("add completed");
        lock.writeLock().unlock();
        return responseShaper;
    }

    public void addBD(HumanBeing humanBeing) {
        collection.add(humanBeing);
        sort();
    }

    //сортировка коллекции по скорости
    private void sort() {
        collection = collection.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public HashSet<HumanBeing> getCollection() {
        return collection;
    }

    //вывод всех элементов коллекции
    public ResponseShaper show() {
        ResponseShaper responseShaper = new ResponseShaper(collection.toString());
        return responseShaper;
    }


    //очистка коллекции
    public ResponseShaper clear(String ownerId) {
        lock.writeLock().lock();
        collection.removeIf(humanbeing -> humanbeing.getOwnerId().equals(ownerId));
        lock.writeLock().unlock();
        ResponseShaper responseShaper = new ResponseShaper("clear completed");
        return responseShaper;
    }

    //вывод информации о коллекции
    public ResponseShaper info() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss");
        String resultInfo = "Тип: HashSet" + " Дата инициализации: " + creationDate.format(formatter) + " Количество элементов: " + collection.size();
        ResponseShaper responseShaper = new ResponseShaper(resultInfo);
        return responseShaper;
    }

    //замена элемента с id равным введённому для execute_script
    public ResponseShaper updateIdScript(String ownerId, String personData, String param, SQLCollectionController sqlCollectionController) throws ValidException, SQLException {
        HumanBeing newHumanBeing = personBuild(ownerId, personData);
        humanValidator.checkElement(newHumanBeing);
        newHumanBeing.setId(Integer.parseInt(param));
        lock.writeLock().lock();
        if (collection.removeIf(i -> i.getId() == newHumanBeing.getId() && i.getOwnerId().equals(ownerId))) {
            collection.add(newHumanBeing);
            sqlCollectionController.update(newHumanBeing, Long.parseLong(ownerId));
        }
        lock.writeLock().unlock();
        sort();
        ResponseShaper responseShaper = new ResponseShaper("update completed");
        return responseShaper;
    }

    //удаление элемента с id равным введённому
    public ResponseShaper removeId(String param, SQLCollectionController sqlCollectionController, String ownerId) throws ValidException, SQLException {
        if (param.matches("^[0-9]+$")) {
            lock.writeLock().lock();
            if (collection.removeIf(humanBeing -> humanBeing.getId() == Integer.parseInt(param) && humanBeing.getOwnerId().equals(ownerId))) {
                sqlCollectionController.removeByID(Long.parseLong(param), Long.parseLong(ownerId));
            }
            ;
            lock.writeLock().unlock();
            ResponseShaper responseShaper = new ResponseShaper("remove_by_id completed");
            return responseShaper;
        } else {
            ResponseShaper responseShaper = new ResponseShaper(String.valueOf(new ValidException("Uncorrected id")));
            return responseShaper;
        }
    }


    //создаём человека и если он является наименьшим, то добавляем в коллекцию для execute_script
    public ResponseShaper addIfMin(String ownerId, String param, SQLCollectionController sqlCollectionController) throws Exception {
        lock.writeLock().lock();
        HumanBeing newHumanBeing = personBuild(ownerId, param);
        humanValidator.checkElement(newHumanBeing);
        System.out.println(Collections.max(collection).getImpactSpeed());
        if (collection.size() == 0) {
            sort();
        } else if (collection.size() == 1) {
            for (HumanBeing humanBeing : collection) {
                if (humanBeing.getImpactSpeed() > newHumanBeing.getImpactSpeed()) {
                    addNewHuman(ownerId, param, sqlCollectionController.addInDB(personBuild(ownerId, param), Long.parseLong(ownerId)));
                    sort();
                }
            }
        }
        else if (Collections.max(collection).getImpactSpeed() > newHumanBeing.getImpactSpeed()) {
                addNewHuman(ownerId, param, sqlCollectionController.addInDB(personBuild(ownerId, param), Long.parseLong(ownerId)));
                sort();
        }
        lock.writeLock().unlock();
        ResponseShaper responseShaper = new ResponseShaper("add_if_min completed");
        return responseShaper;
    }


    public ResponseShaper removeGreater(String ownerId, String param, SQLCollectionController sqlCollectionController) throws ValidException, SQLException {
        HumanBeing newHumanBeing = personBuild(ownerId, param);
        humanValidator.checkElement(newHumanBeing);
        lock.writeLock().lock();
        if (collection.removeIf(humanBeing -> humanBeing.getImpactSpeed() > newHumanBeing.getImpactSpeed() && humanBeing.getOwnerId().equals(ownerId))) {
            sqlCollectionController.removeGreater(newHumanBeing.getImpactSpeed(), Long.parseLong(ownerId));
        }
        ;
        lock.writeLock().unlock();
        ResponseShaper responseShaper = new ResponseShaper("remove_greater completed");
        return responseShaper;
    }

    public ResponseShaper removeLower(String ownerId, String param, SQLCollectionController sqlCollectionController) throws ValidException, SQLException {
        HumanBeing newHumanBeing = personBuild(ownerId, param);
        humanValidator.checkElement(newHumanBeing);
        lock.writeLock().lock();
        if (collection.removeIf(humanBeing -> humanBeing.getImpactSpeed() < newHumanBeing.getImpactSpeed() && humanBeing.getOwnerId().equals(ownerId))) {
            sqlCollectionController.removeLower(newHumanBeing.getImpactSpeed(), Long.parseLong(ownerId));
        }
        ;
        lock.writeLock().unlock();
        ResponseShaper responseShaper = new ResponseShaper("remove_lower completed");
        return responseShaper;
    }

    public ResponseShaper countGreater(String param) throws ValidException {
        if (param.matches("^[-+]?[0-9]*lod") && Double.parseDouble(param) > -992) {
            int counter = 0;
            for (HumanBeing humanBeing : collection) {
                if (humanBeing.getImpactSpeed() > Integer.parseInt(String.valueOf(param))) {
                    counter += 1;
                }
            }
            ResponseShaper responseShaper = new ResponseShaper(String.valueOf(counter));
            return responseShaper;
        } else {
            ResponseShaper responseShaper = new ResponseShaper(String.valueOf(new ValidException("Uncorrected ImpactSpeed")));
            return responseShaper;
        }
    }

    public ResponseShaper filterContains(String param) throws ValidException {
        String result = new String();
        if (param != null) {
            for (HumanBeing humanBeing : collection) {
                if (humanBeing.getSoundtrackName().contains(param)) {
                    result = result + humanBeing;
                }
            }
            ResponseShaper responseShaper = new ResponseShaper(result);
            return responseShaper;
        } else {
            ResponseShaper responseShaper = new ResponseShaper(String.valueOf(new ValidException("Uncorrected SoundTrackName")));
            return responseShaper;
        }
    }

    public ResponseShaper filterGreater(String param) throws ValidException {
        String result = new String();
        if (param.matches("^[-+]?[0-9]*\\.?[0-9]+$") && Double.parseDouble(param) > -992) {
            for (HumanBeing humanBeing : collection) {
                if (humanBeing.getImpactSpeed() > Double.parseDouble(param)) {
                    result = result + humanBeing;
                }
            }
            ResponseShaper responseShaper = new ResponseShaper(result);
            return responseShaper;
        } else {
            ResponseShaper responseShaper = new ResponseShaper(String.valueOf(new ValidException("Uncorrected ImpactSpeed")));
            return responseShaper;
        }
    }

//    public ResponseShaper executeScript(String param) throws ValidException, ExecuteScriptException {
//        recursionChecker.addFile(param);
//        Invoker invoker = new Invoker(this);
//        invoker.readCommandsScript(param);
//        ResponseShaper responseShaper = new ResponseShaper("execute_script completed");
//        return responseShaper;
//    }

    public HumanBeing personBuild(String ownerId, String param) throws ValidException {
        String[] data = param.split(" ");
        if ((data[0] != null) && (data[1].matches("^[-+]?[0-9]*\\.?[0-9]+$")) &&
                (data[2].matches("^[-+]?[0-9]+$") && Double.parseDouble(data[2]) < 945) &&
                (data[5].matches("^[-+]?[0-9]*\\.?[0-9]+$") && Double.parseDouble(data[5]) > -992) &&
                (data[6] != null) && (data[7].matches("^[1-3]$")) && (data[8].matches("^[1-4]$"))) {
            String newName = data[0];
            Double newX = Double.valueOf(data[1]);
            Integer newY = Integer.valueOf(data[2]);
            Boolean newRealHero = Boolean.valueOf(data[3]);
            Boolean newHasToothpick = Boolean.valueOf(data[4]);
            Double newImpactSpeed = Double.valueOf(data[5]);
            String newSoundtrackName = data[6];
            Integer newWeaponType = Integer.valueOf(data[7]);
            Integer newMood = Integer.valueOf(data[8]);
            Boolean newCool = Boolean.valueOf(data[9]);
            Random random = new Random(new Date().getTime());
            int newId = random.nextInt(10000000);
            Coordinates newCoordinates = new Coordinates(newX, newY);
            Car newCar = new Car(newCool);
            Integer weaponType = newWeaponType;
            Integer mood = newMood;
            ZonedDateTime newCreationDate = ZonedDateTime.now();
            return new HumanBeing(ownerId, newId, newName, newCoordinates, newCreationDate, newRealHero, newHasToothpick, newImpactSpeed, newSoundtrackName, weaponType, mood, newCar);
        } else {
            ResponseShaper responseShaper = new ResponseShaper(String.valueOf(new ValidException("Uncorrected person data")));
            return null;
        }
    }

    private static class RecursionChecker {
        private final HashSet<String> history;
        private int size;

        private RecursionChecker() {
            history = new HashSet<>();
        }

        private void addFile(String file) throws ExecuteScriptException {
            history.add(file);
            size += 1;
            checkRecursion();
        }

        private void checkRecursion() throws ExecuteScriptException {
            if (history.size() != size) {
                ResponseShaper responseShaper = new ResponseShaper(String.valueOf(new ExecuteScriptException("RECURSION:(")));
            }
        }

        private HashSet<String> getHistory() {
            return history;
        }

        private void setSize() {
            this.size = 0;
        }
    }
}
