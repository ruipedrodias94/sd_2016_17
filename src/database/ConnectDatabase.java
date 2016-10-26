package database;


import resources.GetPropertiesValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class ConnectDatabase {

    GetPropertiesValues gpv = new GetPropertiesValues();
    Properties prop = gpv.getProperties();

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    // Esta na minha maquina! Ter atencao para depois nao haver conflitos
    final String DB_URL = prop.getProperty("stringJDBC");

    //  Database credentials

     String USER = prop.getProperty("dbUser");
     String PASS = prop.getProperty("dbPass");

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

            // Handle errors from JDBC
            //System.out.println("Nao encontrou a JDBC");
            e.printStackTrace();
        }

        if (connection != null){
            System.out.println("Connection to the database successfull");
        }else{
            //System.out.println("Falha a conectar a base de dados");
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
