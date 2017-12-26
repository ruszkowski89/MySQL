package com.ruszkowski.mysql.datebase;

import jdk.nashorn.internal.objects.annotations.Where;

import java.sql.*;
import java.util.Scanner;


public class CallableStatementExample {
  public static void main(String args[]) {

    String dbUrl = "jdbc:mysql://localhost:3306/demo"
        + "?useSSL=false&autoReconnect=true&useLegacyDatetimeCode=false&serverTimezone=CET";
    String user = "root";
    String password = "babajaga4";

    String selectAllQuery = "SELECT * FROM employees";
    Connection connection;
    Statement statement;

    try {
      connection =
          DriverManager.getConnection(dbUrl, user, password);
      connection.setAutoCommit(false);

      // to show salaries before update
      statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(selectAllQuery);
      System.out.println("Salaries before update: ");
      showSalaries(resultSet);

      // to create transaction that raises engineers salaries by 10k
      CallableStatement callableStatement =
          connection.prepareCall("{call raise_engineers_salaries(?)}");
      callableStatement.setInt(1, 10000);
      callableStatement.execute();

      // to show salaries after update and ask user if he wants to save result
      System.out.println("\nSalaries after update: ");
      resultSet = callableStatement.executeQuery(selectAllQuery);
      showSalaries(resultSet);

      boolean askUser = askUserIfHeWantsToSave();
      if(askUser){
        connection.commit();
      }
      else
        connection.rollback();



    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static boolean askUserIfHeWantsToSave() {
    System.out.println("Are you sure you want to save new salaries? Type y/n and press enter.");
    Scanner scanner = new Scanner(System.in);
    String choice = scanner.next();

    if (choice.equals("y")) {
      System.out.println("Y chosen");
      return true;
    }
    return false;
  }

  public static void showSalaries(ResultSet resultSet) throws SQLException{
    while (resultSet.next()) {
      System.out.println(
          resultSet.getString("last_name") + " "
              + resultSet.getInt("salary"));
    }
  }
}