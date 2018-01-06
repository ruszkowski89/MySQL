package com.ruszkowski.mysql.datebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;

public class CLOBWriteToDB {
    public static void main (String args[]) throws SQLException, FileNotFoundException {

        String dbUrl = "jdbc:mysql://localhost:3306/demo"
                        + "?useSSL=false&autoReconnect=true"
                        + "&useLegacyDatetimeCode=false&serverTimezone=CET";
        String user = "root";
        String password = "babajaga4";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(dbUrl, user, password);
            preparedStatement = connection.prepareStatement(
                                "UPDATE employees set CLOB=? " +
                                    "WHERE email='john.doe@foo.com'");

            // handle for local file
            File file = new File("src/sample_resume.txt");
            FileReader input = new FileReader(file);

            preparedStatement.setCharacterStream(1, input);
            // update DB
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }
}

