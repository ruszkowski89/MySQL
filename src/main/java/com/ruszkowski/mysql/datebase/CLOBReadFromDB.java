package com.ruszkowski.mysql.datebase;

import java.io.*;
import java.sql.*;

public class CLOBReadFromDB {
    public static void main (String args[]) throws SQLException, IOException {
        String dbUrl = "jdbc:mysql://localhost:3306/demo"
                        + "?useSSL=false&autoReconnect=true"
                        + "&useLegacyDatetimeCode=false&serverTimezone=CET";
        String userName = "kekoludek";
        String password = "mama44";

        Connection connection;
        Statement statement = null;
        String sql = null;
        ResultSet resultSet;

        try {
            connection = DriverManager.getConnection(dbUrl, userName, password);
            statement = connection.createStatement();

            sql = "SELECT CLOB from employees "
                + "WHERE email='john.doe@foo.com'";
            resultSet = statement.executeQuery(sql);

            // output file handle
            File file = new File("resume-from-DB.txt");
            FileOutputStream output = new FileOutputStream(file);

            // read CLOB and store it in output file
            while (resultSet.next()){
                Reader input = resultSet.getCharacterStream("CLOB"); // read CLOB

                int character;
                while ((character = input.read()) > 0){
                    System.out.println(character);
                    output.write(character);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null){
                statement.close();
            }
        }

    }
}
