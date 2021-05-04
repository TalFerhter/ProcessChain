import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class ProcessCSV implements BaseProcessCSV {

    LineIterator iterator;
    FileOutputStream fileOutputStream;
    File tempFile;

    public ProcessCSV(File file) {
        try {
            this.iterator = FileUtils.lineIterator(file, "UTF-8");
            this.tempFile = File.createTempFile("processCSV", ".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProcessCSV sum() {
        long colSum = 0;
        while (iterator.hasNext()){
            colSum += Long.valueOf(iterator.nextLine().split(",")[0]);
        }
        System.out.println("sum: " + colSum);
        try {
            FileUtils.writeStringToFile(this.tempFile, String.valueOf(colSum), "UTF-8");
        } catch (IOException e) {
            return null;
        }
        return new ProcessCSV(this.tempFile);
    }

    @Override
    public ProcessCSV avg() {
        long colSum = 0;
        long rowCounter = 0;
        while (iterator.hasNext()){
            colSum += Long.valueOf(iterator.nextLine().split(",")[0]);
            rowCounter++;
        }
        System.out.println("avg: " + colSum/rowCounter);
        try {
            FileUtils.writeStringToFile(this.tempFile, String.valueOf(colSum/rowCounter), "UTF-8");
        } catch (IOException e) {
            return null;
        }
        return new ProcessCSV(this.tempFile);
    }

    @Override
    public ProcessCSV min() {
        long min = Long.valueOf(iterator.nextLine().split(",")[0]);
        while (iterator.hasNext()){
            min = Math.min(min, Long.valueOf(iterator.nextLine().split(",")[0]));
        }
        try {
            FileUtils.writeStringToFile(this.tempFile, String.valueOf(min), "UTF-8");
        } catch (IOException e) {
            return null;
        }
        return new ProcessCSV(this.tempFile);
    }

    @Override
    public ProcessCSV max() {
        long max = Long.valueOf(iterator.nextLine().split(",")[0]);
        while (iterator.hasNext()){
            max = Math.max(max, Long.valueOf(iterator.nextLine().split(",")[0]));
        }
        try {
            FileUtils.writeStringToFile(this.tempFile, String.valueOf(max), "UTF-8");
        } catch (IOException e) {
            return null;
        }
        return this;
    }

    @Override
    public ProcessCSV pluck(int fieldIndex) {
        while (iterator.hasNext()){
            System.out.println(iterator.nextLine().split(",")[fieldIndex]);
            try {
                FileUtils.writeStringToFile(this.tempFile, iterator.nextLine().split(",")[fieldIndex], "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ProcessCSV(this.tempFile);
    }

    @Override
    public ProcessCSV filter(int fieldIndex, String value) {
        File filteredFile = new File("");
        while (iterator.hasNext()){
            try {
                String row[] = iterator.nextLine().split(",");
                String currValue = row[fieldIndex];
                if (currValue.equals(value)) {
                    FileUtils.writeStringToFile(filteredFile, currValue, "UTF-8");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public ProcessCSV ceil() {
        while (iterator.hasNext()){
            try {
                FileUtils.writeStringToFile(this.tempFile, String.valueOf(Math.ceil(Double.valueOf(iterator.nextLine().split(",")[0]))),
                                "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ProcessCSV(this.tempFile);
    }
}
