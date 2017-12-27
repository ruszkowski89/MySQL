package com.ruszkowski.mysql.datebase;

import java.io.*;
import java.sql.*;

public class BLOB {
    public static void main (String args[]) throws IOException {

        String dbUrl = "jdbc:mysql://localhost:3306/demo"
            + "?useSSL=false&autoReconnect=true"
            + "&useLegacyDatetimeCode=false&serverTimezone=CET"
            + "&nullNamePatternMatchesAll=true";
        String user = "root";
        String password = "babajaga4";
        Connection connection;

        try {
            connection = DriverManager.getConnection(dbUrl, user, password);
            addResumeFileToEmployee(connection, "david.williams@foo.com", null);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static File getResumeFileFromEmployee(
        Connection connection, String email) throws SQLException, IOException {

        String sql = "SELECT resume FROM employees WHERE email=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();

        // handle to the output file
        File file = new File("resume.pdf");
        FileOutputStream output = new FileOutputStream(file);

        while (resultSet.next()){
            // this is handle to the 'resume' column
            InputStream input = resultSet.getBinaryStream("resume");

            // this is standard java I/O they say...
            byte[] buffer = new byte[1024];
            while(input.read(buffer) > 0){
                output.write(buffer);
            }
        }
        return file;
    }

    public static void addResumeFileToEmployee(
        Connection connection, String email, File file) throws SQLException, FileNotFoundException {

        String sql = "UPDATE employees SET resume=?"
            + "WHERE email='david.williams@foo.com'";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        if (file==null) {
            file = new File("./src/main/resources/sample_resume.pdf");
            System.out.println("sample resume from directory loaded");
        }

        FileInputStream fileInputStream = new FileInputStream(file);
        preparedStatement.setBinaryStream(1, fileInputStream);
        preparedStatement.executeUpdate();
    }
}
