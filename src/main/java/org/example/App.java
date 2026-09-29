package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class App {
    private Connection con;

    public static void main(String[] args) throws InterruptedException {
        App app = new App();

        try {
            app.connect();
            Employee employee = app.getEmployee(10001);

            if (employee != null) {
                System.out.println("Employee number: " + employee.emp_no);
                System.out.println("Name: " + employee.first_name
                        + " " + employee.last_name);
            } else {
                throw new IllegalStateException("Employee 10001 was not found.");
            }
        } finally {
            app.disconnect();
        }
    }

    public void connect() throws InterruptedException {
        int retries = 10;

        for (int attempt = 1; attempt <= retries; attempt++) {
            System.out.println("Connecting to database... attempt " + attempt);
            Thread.sleep(30000);

            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/employees"
                                + "?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "example");

                System.out.println("Successfully connected");
                return;
            } catch (SQLException e) {
                System.out.println("Failed to connect: " + e.getMessage());
            }
        }

        throw new IllegalStateException(
                "Could not connect to MySQL after all attempts.");
    }

    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                con = null;
                System.out.println("Disconnected from database");
            } catch (SQLException e) {
                throw new IllegalStateException(
                        "Error closing database connection", e);
            }
        }
    }

public Employee getEmployee(int id) {
    String sql = "SELECT emp_no, first_name, last_name "
            + "FROM employees WHERE emp_no = ?";

    try (PreparedStatement statement = con.prepareStatement(sql)) {
        statement.setInt(1, id);

        try (ResultSet results = statement.executeQuery()) {
            if (results.next()) {
                Employee employee = new Employee();
                employee.emp_no = results.getInt("emp_no");
                employee.first_name = results.getString("first_name");
                employee.last_name = results.getString("last_name");
                return employee;
            }

            return null;
        }
    } catch (SQLException e) {
        throw new IllegalStateException("Failed to get employee details", e);
    }
}
}
