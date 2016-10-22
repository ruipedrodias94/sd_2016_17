package database;


import java.sql.*;
import java.util.ArrayList;

public class ConnectDatabase {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    // Esta na minha maquina! Ter atencao para depois nao haver conflitos
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/?user=root";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";

    public Connection connection = null;
    public Statement statement = null;
    public ResultSet resultSet = null;

    public ConnectDatabase(){

        try{
            // Register JDBC
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            statement = connection.createStatement();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {

            // Handle errors from JDBC
            System.out.println("Nao encontrou a JDBC");
            e.printStackTrace();
        }

        if (connection != null){
            System.out.println("Success");
        }else{
            System.out.println("Falha a conectar a base de dados");
        }
    }

    //método apenas para teste
    public synchronized ArrayList<String> test() throws SQLException {
        ArrayList <String> results = new ArrayList<>();

        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM sd_2016_17.Utilizador;");

        while(resultSet.next())
        {
            results.add(resultSet.getString(2));
        }

        return results;
    }
}
