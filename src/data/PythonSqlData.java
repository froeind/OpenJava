package data;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PythonSqlData {

    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private boolean created = false;

    public PythonSqlData() {
        try {
            // Load SQLite JDBC driver
            //Class.forName("org.sqlite.JDBC");
            // Establish connection to SQLite database
            this.connection = DriverManager.getConnection("jdbc:sqlite:src//data//java_confanddata.sqlite3");
            // Create a statement to execute SQL query
            this.statement = this.connection.createStatement();
            if ( ! this.created) {
                String sql = """
                CREATE TABLE IF NOT EXISTS configuration (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                key TEXT NOT NULL UNIQUE,
                ivalue INTEGER,
                rvalue REAL,
                tvalue TEXT)
                """;
                this.statement.execute(sql);
                sql = """
                CREATE TABLE IF NOT EXISTS dataki1 (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                key TEXT NOT NULL UNIQUE,
                rvalue1 REAL,
                rvalue2 REAL,
                rvalue3 REAL,
                rvalue4 REAL,
                rvalue5 REAL,
                rvalue6 REAL,
                bvalue1 INTEGER,
                bvalue2 INTEGER)
                """;
                this.statement.execute(sql);
            }
            this.created = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeDb() {
        if (this.created) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void showData(String table) {
        try {
            this.resultSet = this.statement.executeQuery("SELECT * FROM " + table);
            if ( table.equals("configuration") ) {
                while (this.resultSet.next()) {
                    Integer id = this.resultSet.getInt("id");
                    String key = this.resultSet.getString("key");
                    Integer ivalue = this.resultSet.getInt("ivalue");
                    Float rvalue = this.resultSet.getFloat("rvalue");
                    String tvalue = this.resultSet.getString("tvalue");
                    System.out.println("id: " + id + ", key: " + key + ", ivalue: " + ivalue + ", rvalue: " + rvalue + ", tvalue: " + tvalue);
                }
            } else {
                while (this.resultSet.next()) {
                    Integer id = this.resultSet.getInt("id");
                    String key = this.resultSet.getString("key");
                    Float rvalue1 = this.resultSet.getFloat("rvalue1");
                    Float rvalue2 = this.resultSet.getFloat("rvalue2");
                    Float rvalue3 = this.resultSet.getFloat("rvalue3");
                    Float rvalue4 = this.resultSet.getFloat("rvalue4");
                    Float rvalue5 = this.resultSet.getFloat("rvalue5");
                    Float rvalue6 = this.resultSet.getFloat("rvalue6");
                    Integer bvalue1 = this.resultSet.getInt("bvalue1");
                    Integer bvalue2 = this.resultSet.getInt("bvalue2");
                    System.out.println("id: " + id + ", key: " + key + ", rvalue1: " + rvalue1 + ", rvalue2: " + rvalue2 + ", rvalue3: " + rvalue3 + ", rvalue4: " + rvalue4 + ", rvalue5: " + rvalue5 + ", rvalue6: " + rvalue6 + ", bvalue1: " + bvalue1 + ", bvalue2: " + bvalue2);
                }
            }
            // Close result set
            this.resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


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

/*


    def droptables(self, tables):
        for table in tables:
            try: res = this.connection.execute("DROP TABLE " + table)
            except: pass

    def setvalues(self, table: str, keyandvalues: list, field: str = "") -> int:

        """
        Args:
            table: selbsterklärend
            keyandvalues: list of tuples
                [(key1, ivalue1, rvalue1, tvalue1), (key2, ivalue2, rvalue2, tvalue2), ...]
                [(key1, rvalue11, rvalue21, rvalue31, rvalue41, rvalue51, rvalue61, bvalue11, bvalue21), ...]
            field: wenn Feldname übergeben, dann nur für diesen Update
        Returns:
            int: Anzahl nicht gesetzter Datensätze
        Description:
            zuerst wird versucht einen Tupel als neuen Datensatz zu setzen
            ist der Key aber schon vorhanden, werden dessen Values aktualisiert
        """

        tableavailable = True
        if table == "configuration":
            fields = "(key, ivalue, rvalue, tvalue)"
            values = "(?, ?, ?, ?)"
        elif table == "dataki1":
            fields = "(key, rvalue1, rvalue2, rvalue3, rvalue4, rvalue5, rvalue6, bvalue1, bvalue2)"
            values = "(?, ?, ?, ?, ?, ?, ?, ?, ?)"
        else:
            tableavailable = False
            return len(keyandvalues)

        if tableavailable:
            notset = 0
            for row in keyandvalues:
                if field == "":
                    try:
                        #print(f"INSERT INTO {table}{fields} VALUES{values}")
                        this.connection.execute(f"INSERT INTO {table}{fields} VALUES{values}", row)
                        # executemany erst mal gestrichen, weil bei Nicht-Eindeutigkeit crasht das Ganze
                        this.connection.commit()
                    except:
                        if table == "configuration": setvalues = f"ivalue={row[1]}, rvalue={row[2]}, tvalue='{row[3]}'"
                        elif table == "dataki1": setvalues = f"rvalue1={row[1]}, rvalue2={row[2]}, rvalue3={row[3]}, rvalue4={row[4]}, rvalue5={row[5]}, rvalue6={row[6]}, bvalue1={row[7]}, bvalue2={row[8]}"
                        else: break
                        try:
                            #print(f"UPDATE {table} SET {setvalues} WHERE key='{row[0]}'")
                            this.connection.execute(f"UPDATE {table} SET {setvalues} WHERE key='{row[0]}'")
                            this.connection.commit()
                        except: notset += 1
                else:
                    try:
                        this.connection.execute(f"INSERT INTO {table}(key,{field}) VALUES(?, ?)", row)
                        this.connection.commit()
                    except:
                        if field[:6] == "tvalue": textmarker = "'"
                        else: textmarker = ""
                        try:
                            this.connection.execute(f"UPDATE {table} SET {field}={textmarker}{row[1]}{textmarker} WHERE key='{row[0]}'")
                            this.connection.commit()
                        except: notset += 1
            return notset

    def getvalues(self, table: str, keys: tuple = "", field: str = ""):
        """
        Args:
            table: selbsterklärend
            keys: leer (gesamte Tabelle ausgeben) oder Tupel of strings (gezielte Ausgabe)
                (key1, key2, ...)
            field:
                "" es wird alles gefundene zurückgegeben (mit Keys)
                    [(key1, ivalue1, rvalue1, tvalue1), (key2, ivalue2, rvalue2, tvalue2), ...]
                "field" es werden die Einzelwerte zurückgegeben
                    [ivalue1, ivalue2, ivalue3, tvalue4, ...]
        """
        if keys == "":
            if table == "configuration": fields = "key, ivalue, rvalue, tvalue"
            elif table == "dataki1": fields = "key, rvalue1, rvalue2, rvalue3, rvalue4, rvalue5, rvalue6, bvalue1, bvalue2"
            else: fields = "*"
            res = this.connection.execute(f"SELECT {fields} FROM {table}")
            rows = res.fetchall()
            return rows
        else:
            operation = lambda keys: "=" if isinstance(keys, str) else "in"
            keyvalue = lambda keys: "'" + keys + "'" if isinstance(keys, str) else keys
            if field == "":
                #print(f"SELECT * FROM {table}} WHERE key {operation(keys)} {keyvalue(keys)}")
                res = this.connection.execute(f"SELECT * FROM {table} WHERE key {operation(keys)} {keyvalue(keys)}")
                rows = res.fetchall()
                # das gebe ich so aus wie es ist
                return rows
            else:
                #print(f"SELECT {field} FROM {table} WHERE key {operation(keys)} {keyvalue(keys)}")
                res = this.connection.execute(f"SELECT {field} FROM {table} WHERE key {operation(keys)} {keyvalue(keys)}")
                rows = res.fetchall()
                # das wandle ich in eine aufgeräumte Liste um
                if rows != []: rows = [x[0] for x in rows]
                return rows

    def getcount(self, table: str, keys: tuple = "", like: bool = False) -> int:
        """
        Args:
            table: selbsterklärend
            keys: leer (Gesamtanzahl ermitteln) oder Tupel of strings (gezielt zählen)
                (key1, key2, ...)
            like: Gleichheitssuche (mit = oder IN)
                  oder Like (Wildcards % oder _ müssen in keys gesetzt sein)
                    %: Stellt eine beliebige Anzahl von Zeichen dar.
                    _: Stellt ein einzelnes Zeichen dar.
        Returns:
            int: Anzahl gefundener Datensätze
        """
        if like:
            query = ""
            for key in keys: query += f"(key LIKE '{key}') OR "
            query = query[:len(query)-4]
            #print(f"SELECT COUNT(*) FROM {table}} WHERE {query}")
            res = this.connection.execute(f"SELECT COUNT(*) FROM {table} WHERE {query}")
        elif keys == "":
            #print(f"SELECT COUNT(*) FROM {table}")
            res = this.connection.execute(f"SELECT COUNT(*) FROM {table}")
        else:
            operation = lambda keys: "=" if isinstance(keys, str) else "in"
            keyvalue = lambda keys: "'" + keys + "'" if isinstance(keys, str) else keys
            #print(f"SELECT COUNT(*) FROM {table} WHERE key {operation(keys)} {keyvalue(keys)}")
            res = this.connection.execute(f"SELECT COUNT(*) FROM {table} WHERE key {operation(keys)} {keyvalue(keys)}")
        rows = res.fetchone()
        # das gebe ich so aus wie es ist
        return rows[0]


 */