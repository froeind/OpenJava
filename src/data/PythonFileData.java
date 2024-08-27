package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PythonFileData {
    //Float[] data;
    public List<Float[]> datalist = new ArrayList<>();
    public void initialLoadFromFile() {

        this.datalist.clear();

        try (FileReader fileReader = new FileReader("src//data//java_winddata.csv");
             BufferedReader bufferedReader = new BufferedReader(fileReader);
             Scanner scanner = new Scanner(bufferedReader)) {
            boolean fromSecondLine = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (fromSecondLine) {
                    String[] tokens = line.split(";"); // Split the line into tokens
                    Float[] data = new Float[6];
                    int i = 0;
                    for (String token : tokens) {
                        data[i++] = Float.valueOf(token);
                        //System.out.print(token + " ");
                    }
                    //System.out.println();
                    this.datalist.add(data);
                }
                fromSecondLine = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}