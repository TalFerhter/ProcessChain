import java.io.File;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws URISyntaxException {
        String fileName = "addresses.csv";
        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).toURI());
        ProcessCSV processCSV = new ProcessCSV(file);
        processCSV.pluck(5).sum().run();
    }
}
