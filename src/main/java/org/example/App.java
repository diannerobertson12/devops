package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws InterruptedException {
        int retries = 10;

        for (int attempt = 1; attempt <= retries; attempt++) {
            System.out.println("Connecting to database... attempt " + attempt);

            // Give MySQL time to start and load its data.
            Thread.sleep(30000);

            try (Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://db:3306/employees"
                            + "?allowPublicKeyRetrieval=true&useSSL=false",
                    "root",
                    "example")) {

                System.out.println("Successfully connected");
                return;

            } catch (SQLException e) {
                System.out.println("Failed to connect: " + e.getMessage());
            }
        }

        throw new IllegalStateException("Could not connect to MySQL after all attempts.");
    }
}