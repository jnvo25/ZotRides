package com.example.zotrides;

import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class UpdateSecurePassword {

    /*
     * 
     * This program updates the existing zotrides customers table to change the
     * plain text passwords to encrypted passwords.
     * 
     * This program should only be run once **once**, because this program uses the
     * existing passwords as real passwords, then replace them. If you run it more
     * than once, it will treat the encrypted passwords as real passwords and
     * generate wrong values.
     * 
     */
    public static void main(String[] args) throws Exception {
        // Setup for connection
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/zotrides";
        Class.forName("com.mysql.jdbc.Driver").newInstance();

        // Establish connection
        try(Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);) {
            // get the ID and password for each customer
            PreparedStatement statement = connection.prepareStatement("SELECT id, password from Customers");
            ResultSet rs = statement.executeQuery();

            // we use the StrongPasswordEncryptor from jasypt library (Java Simplified Encryption)
            //  it internally use SHA-256 algorithm and 10,000 iterations to calculate the encrypted password
            PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

            // execute the update queries to update the passwords in Customers table
            String encryptQuery = "UPDATE Customers SET password=? WHERE id=?;";
            PreparedStatement statement2 = connection.prepareStatement(encryptQuery);
            System.out.println("encrypting Customer passwords (this might take a while)");
            int count = 0;
            while (rs.next()) {
                // get the ID and plain text password from current table
                String id = rs.getString("id");
                String password = rs.getString("password");

                // encrypt the password using StrongPasswordEncryptor
                String encryptedPassword = passwordEncryptor.encryptPassword(password);

                // generate the update query
                statement2.setString(1, encryptedPassword);
                statement2.setString(2, id);
                count += statement2.executeUpdate();
            }

            System.out.println("updating customer passwords completed, " + count + " rows affected");

            // clean up
            rs.close();
            statement.close();
            statement2.close();

            System.out.println("finished with Customers");

            // ----- Encrypt for Employees
            statement = connection.prepareStatement("SELECT email, password from Employees");
            rs = statement.executeQuery();
            encryptQuery = "UPDATE Employees SET password=? WHERE email=?;";
            statement2 = connection.prepareStatement(encryptQuery);
            System.out.println("encrypting Employee passwords (this might take a while)");

            count = 0;
            while (rs.next()) {
                // get the ID and plain text password from current table
                String email = rs.getString("email");
                String password = rs.getString("password");

                // encrypt the password using StrongPasswordEncryptor
                String encryptedPassword = passwordEncryptor.encryptPassword(password);

                // generate the update query
                statement2.setString(1, encryptedPassword);
                statement2.setString(2, email);
                count += statement2.executeUpdate();
            }

            System.out.println("updating employee passwords completed, " + count + " rows affected");

            // clean up
            rs.close();
            statement.close();
            statement2.close();
        } catch (Exception e) {
            // display error message
            System.out.println("Error: " + e.getMessage());
            return;
        }

    }

}
