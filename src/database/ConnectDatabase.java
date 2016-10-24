package database;


import java.sql.*;
import java.util.ArrayList;

public class ConnectDatabase {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    // Esta na minha maquina! Ter atencao para depois nao haver conflitos
    static final String DB_URL = "jdbc:mysql://localhost:3306/sd_2016_17?user=root?autoReconnect=true&useSSL=false";

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
            connection.setAutoCommit(false);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {

            e.printStackTrace();
        }

        if (connection != null){
            System.out.println("Success");
        }else{
            System.out.println("Falha a conectar a base de dados");
        }
    }

    public void rollback(){
        try{
            connection.rollback();
        }catch(SQLException se){
            System.err.println("Rollback failed!");
        }
    }

    public void commit(){
        try{
            connection.commit();
        }catch(SQLException se){
            System.err.println("Commit failed!");
        }
    }

}
