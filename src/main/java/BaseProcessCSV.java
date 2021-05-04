import java.io.File;

public interface BaseProcessCSV {
    public BaseProcessCSV sum();
    public BaseProcessCSV avg();
    public BaseProcessCSV min();
    public BaseProcessCSV max();
    public BaseProcessCSV pluck(int fieldIndex);
    public BaseProcessCSV filter(int fieldIndex, String value);
    public BaseProcessCSV ceil();

}
