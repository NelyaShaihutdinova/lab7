package client;

import builders.Auntification;

import java.net.InetSocketAddress;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.in;

public class ConsoleWorker {

    public String buildParam() {
        Scanner scanner = new Scanner(in);
        String newName = null;
        while (newName == null || newName == "") {
            System.out.println("print name (String):");
            try {
                newName = scanner.nextLine();
            } catch (InputMismatchException | NullPointerException e) {
                System.out.println("Ошибка! Введите имя ещё раз:)");
                scanner.nextLine();
            }
        }
        Double newX = null;
        while (newX == null) {
            System.out.println("print x (Double):");
            try {
                newX = scanner.nextDouble();
            } catch (InputMismatchException | NullPointerException e) {
                System.out.println("Ошибка! Введите x ещё раз:)");
                scanner.nextLine();
            }
        }
        Integer newY = null;
        while (newY == null || newY > 945) {
            System.out.println("print y (Integer):");
            try {
                newY = scanner.nextInt();
            } catch (InputMismatchException | NullPointerException e) {
                System.out.println("Ошибка! Введите x ещё раз:)");
                scanner.nextLine();
            }
        }
        Boolean newRealHero = null;
        while (newRealHero == null) {
            System.out.println("print realHero (Boolean):");
            try {
                newRealHero = scanner.nextBoolean();
            } catch (InputMismatchException | NullPointerException e) {
                System.out.println("Ошибка! Введите realHero ещё раз:)");
                scanner.nextLine();
            }
        }

        Boolean newHasToothpick = null;
        while (newHasToothpick == null) {
            System.out.println("print hasToothpick (Boolean):");
            try {
                newHasToothpick = scanner.nextBoolean();
            } catch (InputMismatchException | NullPointerException e) {
                System.out.println("Ошибка! Введите hasToothpick ещё раз:)");
                scanner.nextLine();
            }
        }

        Double newImpactSpeed = null;
        while (newImpactSpeed == null || newImpactSpeed <= -992) {
            System.out.println("print impactSpeed (Double):");
            try {
                newImpactSpeed = scanner.nextDouble();
            } catch (InputMismatchException | NullPointerException e) {
                System.out.println("Ошибка! Введите impactSpeed ещё раз:)");
                scanner.nextLine();
            }
        }

        scanner.nextLine();
        String newSoundtrackName = null;
        while (newSoundtrackName == null) {
            System.out.println("print soundTrackName (String):");
            try {
                newSoundtrackName = scanner.nextLine();
            } catch (InputMismatchException | NullPointerException e) {
                System.out.println("Ошибка! Введите имя саундтрека ещё раз:)");
                scanner.nextLine();
            }
        }
        Boolean newCool = null;
        while (newCool == null) {
            System.out.println("print cool (Boolean):");
            try {
                newCool = scanner.nextBoolean();
            } catch (InputMismatchException | NullPointerException e) {
                System.out.println("Ошибка! Введите cool ещё раз:)");
                scanner.nextLine();
            }
        }
        Integer newWeaponType = null;
        while (newWeaponType == null || newWeaponType < 1 || newWeaponType > 3) {
            System.out.println("print WeaponType (Integer [1-3]):");
            try {
                newWeaponType = scanner.nextInt();
            } catch (InputMismatchException | NullPointerException e) {
                System.out.println("Ошибка! Введите WeaponType ещё раз:)");
                scanner.nextLine();
            }
        }
        Integer newMood = null;
        while (newMood == null || newMood < 1 || newMood > 4) {
            System.out.println("print Mood (Integer [1-4]):");
            try {
                newMood = scanner.nextInt();
            } catch (InputMismatchException | NullPointerException e) {
                System.out.println("Ошибка! Введите Mood ещё раз:)");
                scanner.nextLine();
            }
        }
        String param = newName + ' ' + newX + ' ' + newY + ' ' + newRealHero + ' ' + newHasToothpick + ' ' + newImpactSpeed + ' ' + newSoundtrackName + ' ' + newWeaponType + ' ' + newMood + ' ' + newCool;
        return param;
    }

    public Auntification loginUser() {
        Scanner scanner = new Scanner(System.in);
        String userName = null;
        String password = null;
        String userAction = null;
        System.out.println("Если вы новый пользователь зарегистрируйтесь написав команду reg, если вы уже зарегистрированы войдите в аккаунт написав команду login");
        userAction = "";
        var checkCommand = true;
        while (checkCommand) {
            userAction = scanner.nextLine().trim();
            if (userAction.equals("reg")) checkCommand = false;
            if (userAction.equals("login")) checkCommand = false;
            if (checkCommand)
                System.out.println("Неверно введённая команда введите reg, если вы хотите зарегистрироваться или login, если вы уже зарегистрированы ");
        }
        while (userName == null) {
            try {
                System.out.println("Введите логин");
                userName = scanner.nextLine();
            } catch (InputMismatchException | NullPointerException e) {
                System.out.println("Ошибка! Введите логин ещё раз:)");
                scanner.nextLine();
            }
        }
        while (password == null) {
            try {
                System.out.println("Введите пароль");
                password = scanner.nextLine();
            } catch (InputMismatchException | NullPointerException e) {
                System.out.println("Ошибка! Введите пароль ещё раз:)");
                scanner.nextLine();
            }
        }

        Auntification auntification = new Auntification(userName, password, userAction);
        return auntification;
    }
}
