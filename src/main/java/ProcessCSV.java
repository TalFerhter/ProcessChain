import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class ProcessCSV {

    private LineIterator iterator;
    private File tempFile;
    private ArrayList funcList;

    public  ProcessCSV(File file) {
        try {
            this.iterator = FileUtils.lineIterator(file, "UTF-8");
            this.tempFile = File.createTempFile("processCSV", ".csv");
            this.funcList = new ArrayList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ProcessCSV sum() {
        AggregatedTransformer transformer = new AggregatedTransformer() {
            long sum;

            @Override
            public String[] get() {
                return new String[]{String.valueOf(sum)};
            }

            @Override
            public void accept(String[] row) {
                this.sum += Long.valueOf(row[0]);
            }
        };
        this.funcList.add(transformer);
        return this;
    }

    public ProcessCSV avg() {
        AggregatedTransformer transformer = new AggregatedTransformer() {
            long sum;
            long counter;

            @Override
            public String[] get() {
                return new String[]{String.valueOf(sum / counter)};
            }

            @Override
            public void accept(String[] row) {
                this.sum = Long.parseLong(row[0]);
            }
        };
        this.funcList.add(transformer);
        return this;
    }

    public ProcessCSV min() {
        AggregatedTransformer transformer = new AggregatedTransformer() {
            long min;

            @Override
            public String[] get() {
                return new String[]{String.valueOf(min)};
            }

            @Override
            public void accept(String[] row) {
                if (Long.parseLong(row[0]) < min) {
                    this.min = Long.parseLong(row[0]);
                }
            }
        };
        this.funcList.add(transformer);
        return this;
    }

    public ProcessCSV max() {
        AggregatedTransformer transformer = new AggregatedTransformer() {
            long max;

            @Override
            public String[] get() {
                return new String[]{String.valueOf(this.max)};
            }

            @Override
            public void accept(String[] row) {
                if (Long.valueOf(row[0]) > max) {
                    this.max = Long.valueOf(row[0]);
                }
            }
        };
        this.funcList.add(transformer);
        return this;
    }

    public ProcessCSV pluck(int fieldIndex) {
        Transformer transformer = new Transformer() {
            @Override
            public String[] apply(String[] row) {
                return new String[]{row[fieldIndex]};
            }
        };
        this.funcList.add(transformer);
        return this;
    }

    public ProcessCSV filter(int fieldIndex, String value) {
        Transformer transformer = new Transformer() {
            @Override
            public String[] apply(String[] row) {
                String currValue = row[fieldIndex];
                if (currValue.equals(value)) {
                    return row;
                }
                return null;
            }
        };
        this.funcList.add(transformer);
        return this;
    }

    public ProcessCSV ceil() {
        Transformer transformer = new Transformer() {
            @Override
            public String[] apply(String[] row) {
                String currValue = row[0];
                return new String[]{String.valueOf(Math.ceil(Double.parseDouble(currValue)))};
            }
        };
        this.funcList.add(transformer);
        return this;
    }

    public void run() {
        String sum = "";
        while (iterator.hasNext()) {
            String[] row = iterator.nextLine().split(",");
            for (Object f : this.funcList) {
                if (f instanceof AggregatedTransformer) {
                    AggregatedTransformer func = ((AggregatedTransformer) f);
                    func.accept(row);
                    sum = func.get()[0];
                } else if (f instanceof Transformer) {
                    row = ((Transformer) f).apply(row);
                }
            }
            try {
                FileUtils.writeLines(this.tempFile, Arrays.asList(row));
                System.out.println(row[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(sum);
    }
}
