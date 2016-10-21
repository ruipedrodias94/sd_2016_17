package database;


import java.sql.*;

public class ConnectDatabase {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    // Esta na minha maquina! Ter atencao para depois nao haver conflitos
    static final String DB_URL = "jdbc:mysql://localhost:3306/?user=root?autoReconnect=true&useSSL=false";

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
            System.out.println("Sucess");
        }else{
            System.out.println("Falha a conectar a base de dados");
        }
    }

    //método apenas para teste
    public synchronized void test() throws SQLException {

        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT * from ");
    }
}
