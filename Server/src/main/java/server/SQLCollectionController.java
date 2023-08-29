package server;

import command.CollectionController;
import entities.Car;
import entities.HumanBeing;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class SQLCollectionController {
    private static final String CREATE_TABLE = "" +
            "CREATE TABLE IF NOT EXISTS Coordinates(" +
            "    coordinates_id BIGSERIAL PRIMARY KEY," +
            "    X_coordinates  REAL NOT NULL," +
            "    Y_coordinates  BIGINT NOT NULL " +
            ");" +
            "CREATE TABLE IF NOT EXISTS Car(" +
            "    car_id          BIGSERIAL PRIMARY KEY," +
            "    cool            BOOLEAN NOT NULL" +
            ");" +
            "CREATE TABLE IF NOT EXISTS HumanBeings(" +
            "    id            BIGSERIAL PRIMARY KEY," +
            "    name          VARCHAR(127) NOT NULL," +
            "    coordinates   BIGINT NOT NULL REFERENCES Coordinates(coordinates_id) ON DELETE CASCADE," +
            "    creationDate TIMESTAMP NOT NULL," +
            "    impactSpeed   BIGINT NOT NULL," +
            "    realHero      BOOLEAN NOT NULL," +
            "    hasToothpick  BOOLEAN NOT NULL," +
            "    weaponType    TEXT NOT NULL," +
            "    mood          TEXT NOT NULL," +
            "    soundtrackName  VARCHAR(127) NOT NULL," +
            "    car           BIGINT REFERENCES Car(car_id) ON DELETE SET NULL," +
            "    ownerId      INTEGER NOT NULL REFERENCES users(id) on DELETE CASCADE" +
            ");";
    private final Connection conn;
    private CollectionController cc;

    public SQLCollectionController(Connection conn, CollectionController cc) {
        this.conn = conn;
        this.cc = cc;
    }

    public void initTableOrExecuteHumanBeings() throws SQLException {
        try (Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            s.execute(CREATE_TABLE);
            ResultSet res = s.executeQuery("SELECT *" +
                    "FROM HumanBeings" +
                    "         LEFT JOIN Coordinates ON HumanBeings.coordinates = Coordinates.coordinates_id" +
                    "         LEFT JOIN Car ON HumanBeings.car = Car.car_id;");
            while (res.next()) {
                HumanBeing humanBeing = ReadDBAndSaveInCollection(res);
                cc.addBD(humanBeing);
            }
            conn.setAutoCommit(false);
        }
    }

    public HumanBeing ReadDBAndSaveInCollection(ResultSet res) throws SQLException {
        HumanBeing humanBeing = new HumanBeing();
        humanBeing.setId(res.getInt("id"));
        humanBeing.setName(res.getString("name"));
        humanBeing.getCoordinates().setX(res.getDouble("x_coordinates"));
        humanBeing.getCoordinates().setY(res.getInt("y_coordinates"));
        humanBeing.setCreationDate(ZonedDateTime.ofInstant(res.getTimestamp("creationDate").toLocalDateTime().toInstant(ZoneOffset.UTC), ZoneId.of("UTC")));
        humanBeing.setHasToothpick(res.getBoolean("hasToothpick"));
        humanBeing.setRealHero(res.getBoolean("realHero"));
        humanBeing.setSoundtrackName(res.getString("soundtrackName"));
        humanBeing.setImpactSpeed(res.getDouble("impactSpeed"));
        humanBeing.setMood(res.getInt("mood"));
        humanBeing.setWeaponType(res.getInt("weaponType"));
        humanBeing.getCar().setCool(res.getBoolean("cool"));
        humanBeing.setOwnerId(res.getString("ownerId"));
        return humanBeing;
    }

    public Long addInDB(HumanBeing humanBeing, Long userId) throws Exception {
        System.out.println(humanBeing);
        String CAR = "INSERT INTO Car (cool)" +
                "VALUES (?) RETURNING car_id;";
        String COORDINATES = "INSERT INTO Coordinates (x_coordinates, y_coordinates)" +
                "VALUES (?, ?) RETURNING coordinates_id;";
        String HUMANBEING = "INSERT INTO HumanBeings (name, coordinates,  creationDate, impactSpeed, realHero, hasToothpick, weaponType, mood, soundtrackName, car, ownerId)" +
                "VALUES (?, ?, ?, ?, ?,?,?,?,?,?,?) RETURNING id;";
        Long coordinatesID;
        try (PreparedStatement preparedStatement = conn.prepareStatement(COORDINATES)) {
            preparedStatement.setDouble(1, humanBeing.getCoordinates().getX());
            preparedStatement.setLong(2, humanBeing.getCoordinates().getY());
            ResultSet coordinate = preparedStatement.executeQuery();
            coordinate.next();
            coordinatesID = coordinate.getLong(1);
        }
        Long carID = null;
        if (humanBeing.getCar() != null) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(CAR)) {
                preparedStatement.setBoolean(1, humanBeing.getCar().isCool());
                ResultSet car = preparedStatement.executeQuery();
                car.next();
                carID = car.getLong(1);
            }
        }
        String mood = null;
        if (humanBeing.getMood() != null) {
            mood = humanBeing.getMood().toString();
        }
        String weaponType = null;
        if (humanBeing.getWeaponType() != null) {
            weaponType = humanBeing.getWeaponType().toString();
        }
        long humanBeingID;
        try (PreparedStatement preparedStatement = conn.prepareStatement(HUMANBEING)) {
            preparedStatement.setString(1, humanBeing.getName());
            preparedStatement.setLong(2, coordinatesID);
            preparedStatement.setTimestamp(3, Timestamp.from(humanBeing.getCreationDate().toInstant()));
            preparedStatement.setDouble(4, humanBeing.getImpactSpeed());
            preparedStatement.setBoolean(5, humanBeing.getRealHero());
            preparedStatement.setBoolean(6, humanBeing.getHasToothpick());
            preparedStatement.setString(7, weaponType);
            preparedStatement.setString(8, mood);
            preparedStatement.setString(9, humanBeing.getSoundtrackName());
            if (carID == null) {
                preparedStatement.setNull(10, Types.BIGINT);
            } else {
                preparedStatement.setLong(10, carID);
            }
            preparedStatement.setLong(11, userId);
            ResultSet generatedKeys = preparedStatement.executeQuery();
            generatedKeys.next();
            humanBeingID = generatedKeys.getLong(1);
        }
        conn.commit();
        return humanBeingID;
    }


    public void clear(Long userId) throws SQLException {
        String CAR = "DELETE FROM car WHERE car_id IN (SELECT humanBeings.car FROM humanBeings WHERE ownerId=?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(CAR)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
        }
        String CLEAR_COORDINATES = "DELETE FROM coordinates WHERE coordinates_id IN (SELECT humanBeings.coordinates FROM humanBeings WHERE ownerId=?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(CLEAR_COORDINATES)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
        }
        String CLEAR = "DELETE FROM humanBeings WHERE ownerId=?;";
        try (PreparedStatement preparedStatement = conn.prepareStatement(CLEAR)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
        }
        conn.commit();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void removeByID(Long id, Long userId) throws SQLException {
        String CAR = "DELETE FROM car WHERE car_id IN (SELECT humanBeings.car FROM humanBeings WHERE id=? and ownerId=?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(CAR)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        }
        String CLEAR_COORDINATES = "DELETE FROM coordinates WHERE coordinates_id IN (SELECT humanBeings.coordinates FROM humanBeings WHERE id=? and ownerId=?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(CLEAR_COORDINATES)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        }
        String CLEAR = "DELETE FROM humanbeings WHERE id=? and ownerId=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(CLEAR)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        }
        conn.commit();
    }
    public void removeGreater(Double impactSpeed, Long userId) throws SQLException {
        String CAR = "DELETE FROM car WHERE car_id IN (SELECT humanBeings.car FROM humanBeings WHERE humanbeings.impactspeed>? and ownerId=?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(CAR)) {
            preparedStatement.setDouble(1, impactSpeed);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        }
        String CLEAR_COORDINATES = "DELETE FROM coordinates WHERE coordinates_id IN (SELECT humanBeings.coordinates FROM humanBeings WHERE humanbeings.impactspeed>? and ownerId=?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(CLEAR_COORDINATES)) {
            preparedStatement.setDouble(1, impactSpeed);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        }
        String CLEAR = "DELETE FROM humanBeings WHERE humanbeings.impactspeed>? and ownerId=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(CLEAR)) {
            preparedStatement.setDouble(1, impactSpeed);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        }
        conn.commit();
    }

    public void removeLower(Double impactSpeed, Long userId) throws SQLException {
        String CAR = "DELETE FROM car WHERE car_id IN (SELECT humanBeings.car FROM humanBeings WHERE humanbeings.impactspeed<? and ownerId=?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(CAR)) {
            preparedStatement.setDouble(1, impactSpeed);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        }
        String CLEAR_COORDINATES = "DELETE FROM coordinates WHERE coordinates_id IN (SELECT humanBeings.coordinates FROM humanBeings WHERE humanbeings.impactspeed<? and ownerId=?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(CLEAR_COORDINATES)) {
            preparedStatement.setDouble(1, impactSpeed);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        }
        String CLEAR = "DELETE FROM humanBeings WHERE humanbeings.impactspeed<? and ownerId=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(CLEAR)) {
            preparedStatement.setDouble(1, impactSpeed);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        }
        conn.commit();
    }

    public void update(HumanBeing humanBeing, Long userId) throws SQLException {
        String HUMANBEING = "UPDATE HumanBeings SET name = ?,coordinates=?, creationDate = ?, impactSpeed = ?, realHero = ?, hasToothpick = ?, weaponType = ?, mood = ?, soundtrackName = ?, car = ?, ownerId = ? WHERE id = ?;";
        String COORDINATES = "UPDATE Coordinates SET x_coordinates = ?, y_coordinates = ? WHERE coordinates_id = (SELECT humanBeings.coordinates FROM humanBeings where id = ?);";
        String GETID = "SELECT coordinates, car FROM humanBeings WHERE id = ?";
        Integer mood = null;
        Integer weaponType = null;
        Long coordinates = null;
        Long car = null;
        var humanBeingID = humanBeing.getId();
        if (humanBeing.getMood() != null) {
            mood = humanBeing.getMood();
        }
        if (humanBeing.getWeaponType() != null) {
            weaponType = humanBeing.getWeaponType();
        }
        try (PreparedStatement preparedStatement = conn.prepareStatement(GETID)) {
            preparedStatement.setLong(1, humanBeingID);
            var result = preparedStatement.executeQuery();
            result.next();
            coordinates = result.getLong("coordinates");
            car = result.getLong("car");
        }
        try (PreparedStatement preparedStatement = conn.prepareStatement(COORDINATES)) {
            preparedStatement.setDouble(1, humanBeing.getCoordinates().getX());
            preparedStatement.setLong(2, humanBeing.getCoordinates().getY());
            preparedStatement.setLong(3, humanBeingID);
        }

        car = updateCar(humanBeing.getCar(), car);

        try (PreparedStatement preparedStatement = conn.prepareStatement(HUMANBEING)) {
            preparedStatement.setString(1, humanBeing.getName());
            preparedStatement.setLong(2, coordinates);
            preparedStatement.setTimestamp(3, Timestamp.from(humanBeing.getCreationDate().toInstant()));
            preparedStatement.setDouble(4, humanBeing.getImpactSpeed());
            preparedStatement.setBoolean(5, humanBeing.getRealHero());
            preparedStatement.setBoolean(6, humanBeing.getHasToothpick());
            preparedStatement.setString(7, String.valueOf(weaponType));
            preparedStatement.setString(8, String.valueOf(mood));
            preparedStatement.setString(9, humanBeing.getSoundtrackName());
            if (car != null) {
                preparedStatement.setLong(10, car);
            } else {
                preparedStatement.setNull(10, Types.BIGINT);
            }
            preparedStatement.setLong(11, userId);
            preparedStatement.setLong(12, humanBeingID);
            preparedStatement.executeUpdate();
        }
        conn.commit();
    }

//    private Long updateCar(Car car, Long carID) throws SQLException {
//        if (car != null && carID != null) {
//            updateNotNullCar(car, carID);
//            return carID;
//        } else if (car == null && carID != null) {
//            deleteCar(carID);
//            return null;
//        } else if (car != null && carID == null) {
//            return insertCar(car);
//        } else {
//            return null;
//        }
//    }

    private Long updateCar(Car car, Long carID) throws SQLException {
        String CAR = "UPDATE Car SET cool = ? WHERE car_id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(CAR)) {
            preparedStatement.setBoolean(1, car.isCool());
            preparedStatement.setLong(2, carID);
            preparedStatement.executeUpdate();
        }
        return carID;
    }

//    private void deleteCar(Long carID) throws SQLException {
//        String CAR = "DELETE FROM Car WHERE car_id = ?";
//        try (PreparedStatement preparedStatement = conn.prepareStatement(CAR)) {
//            preparedStatement.setLong(1, carID);
//            preparedStatement.executeUpdate();
//        }
//    }
//
//    private Long insertCar(Car car) throws SQLException {
//        String CAR = "INSERT INTO Car (cool)" +
//                "VALUES (?) RETURNING car_id;";
//        try (PreparedStatement preparedStatement = conn.prepareStatement(CAR)) {
//            preparedStatement.setBoolean(1, car.isCool());
//            var res = preparedStatement.executeQuery();
//            res.next();
//            return res.getLong(1);
//        }
//    }
}
