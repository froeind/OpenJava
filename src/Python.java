
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;

class PythonData {
    Float[] data;
    List<Float[]> datalist = new ArrayList<>();
    public void initialLoadFromFile() {

        this.datalist.clear();

        try (FileReader fileReader = new FileReader("src//java_winddata.csv");
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
public class Python {
    public static void main(String[] args) {

        PythonData pythonData = new PythonData();
        pythonData.initialLoadFromFile();
        for (Float[] data : pythonData.datalist) {
            System.out.println(Arrays.toString(data));
        }
        System.out.println(pythonData.datalist.size());

    }
}

/*
import java.sql.*;

public class SQLiteExample {

    public static void main(String[] args) {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish connection to SQLite database
            Connection connection = DriverManager.getConnection("jdbc:sqlite:mydb.db");

            // Create a statement to execute SQL query
            Statement statement = connection.createStatement();

            // Execute query to retrieve data from a table
            ResultSet resultSet = statement.executeQuery("SELECT * FROM mytable");

            // Process result set
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");

                System.out.println("ID: " + id + ", Name: " + name + ", Price: " + price);
            }

            // Close result set and connection
            resultSet.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
 */