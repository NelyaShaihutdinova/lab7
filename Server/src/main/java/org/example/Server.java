package org.example;

import builders.CommandShaper;
import builders.ResponseShaper;
import command.CollectionController;
import command.Invoker;
import entities.HumanSimpleValidator;
import server.RequestWorker;
import server.ResponseSender;
import server.SQLCollectionController;
import server.SqlUserManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Server {
    private SqlUserManager sqlUserManager;
    private SQLCollectionController sqlCollectionController;
    private ResponseSender responseSender;
    private Connection connection;
    private CollectionController cc;
    private final ForkJoinPool readForkJoinPool;
    private final ExecutorService executorFixedTreadPool = Executors.newFixedThreadPool(3);
    private final ExecutorService responseFixedTreadPool = Executors.newFixedThreadPool(3);

    public Server(String[] args) {
        initialize(args);
        this.readForkJoinPool = new ForkJoinPool();
    }

    public void run() throws IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        DatagramSocket ds = new DatagramSocket(1050);
        while (true){
            readMassage(ds);
//            DatagramPacket pack = new DatagramPacket(new byte[10000000], 1000000);
//            ds.receive(pack);
//            RequestWorker requestWorker = new RequestWorker();
//            CommandShaper commandShaper = requestWorker.deserializeRequest(pack.getData());
//            Invoker invoker = new Invoker(commandShaper, cc, sqlCollectionController);
//            ResponseShaper responseShaper = invoker.readCommand(sqlUserManager);
//            responseSender.sendResponse(responseShaper);
        }
    }

    public void readMassage(DatagramSocket ds) {
            readForkJoinPool.submit(() -> {
                try {
                    DatagramPacket pack = new DatagramPacket(new byte[10000000], 1000000);
                    ds.receive(pack);
                    RequestWorker requestWorker = new RequestWorker();
                    CommandShaper commandShaper = requestWorker.deserializeRequest(pack.getData());
                    executeCommand(commandShaper);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            });
    }

    public void executeCommand(CommandShaper commandShaper) {
        executorFixedTreadPool.submit(() -> {
            Invoker invoker = new Invoker(commandShaper, cc, sqlCollectionController);
            try {
                ResponseShaper responseShaper = invoker.readCommand(sqlUserManager);
                sendMassage(responseShaper);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void sendMassage(ResponseShaper responseShaper) {
        responseFixedTreadPool.submit(() -> {
            try {
                responseSender.sendResponse(responseShaper);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void initialize(String[] args) {
        try {

            responseSender = new ResponseSender("localhost", checkPort(args[0]));
            connectionToDb(args[1], args[2]);
            sqlUserManager = new SqlUserManager(connection);
            cc = new CollectionController(new HumanSimpleValidator());
            sqlCollectionController = new SQLCollectionController(connection, cc);
            sqlCollectionController.initTableOrExecuteHumanBeings();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Введите порт, бд юзер, пароль");
            System.exit(1);
        } catch (SQLException e) {
            System.out.println("Ошибка загрузки данных с бд");
            System.exit(1);
        }

    }

    public int checkPort(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            System.out.println("Неверный порт");
            System.exit(1);
        }
        return 0;
    }

    public void connectionToDb(String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", user, password);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Проблемы с подключением к базе данных");
            System.exit(1);
        }
    }
}