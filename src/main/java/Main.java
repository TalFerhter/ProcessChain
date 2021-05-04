import java.io.File;

public class Main {
    public static void main(String[] args) {
        String fileName = "addresses.csv";
        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        ProcessCSV processCSV = new ProcessCSV(file);
        processCSV.pluck(0).max();
    }
}
