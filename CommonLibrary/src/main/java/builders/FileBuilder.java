package builders;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileBuilder {
    private File file;

    private static boolean checkFileExists(File file) throws FileNotFoundException {
        if (!(file.exists() && file.isFile())) {
            throw new FileNotFoundException(String.format("Файл не найден"));
        }
        return true;
    }

    public static boolean checkOpportunityForWrite(File file) throws IOException {
        return checkFileExists(file) && checkFileToWrite(file);
    }

    private static boolean checkFileToWrite(File file) throws IOException {
        if (!file.canWrite()) {
            throw new IOException(String.format("Не хватает прав на запись в файл"));
        }
        return true;
    }

    public boolean checkOpportunityForRead(File file) throws IOException {
        return checkFileExists(file) && checkFileToRead(file);
    }

    private boolean checkFileToRead(File file) throws IOException {
        if (!file.canRead()) {
            throw new IOException(String.format("Не хватает прав на чтение файла"));
        }
        return true;
    }
//
//    public File getEnv() throws FileException, IOException {
//        String path = System.getenv("FILELAB");
//        File file;
//        if (!Objects.isNull(path)) {
//            file = new File(path);
//            checkOpportunityForRead(file);
//        } else {
//            throw new FileException("Перменная окружения не найдена");
//        }
//        return file;
//    }
}