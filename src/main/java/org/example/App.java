package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    private Connection con;

    public static void main(String[] args) throws InterruptedException {
        App app = new App();

        try {
            app.connect();
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
}