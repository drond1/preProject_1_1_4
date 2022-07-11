package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final Connection CONNECTION = Util.getConnection();
    private static final String createTable = "CREATE TABLE IF NOT EXISTS UserTable (id int NOT NULL AUTO_INCREMENT PRIMARY KEY, name varchar(30) NOT NULL, lastName varchar(30) NOT NULL, age int NOT NULL)";
    private static final String getUsersFromTable = "SELECT * FROM UserTable";
    private static final String saveUser = "INSERT INTO UserTable (name, lastName, age) VALUES (?,?,?)";
    private static final String deleteUser = "DELETE FROM UserTable WHERE id=?";
    private static final String dropTable = "DROP TABLE IF EXIST UserTable";
    private static final String cleanTable = "TRUNCATE TABLE UserTable";

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Statement createUsersTableStatement = CONNECTION.createStatement()) {
            createUsersTableStatement.executeUpdate(createTable);
            CONNECTION.commit();
        } catch (SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
//            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try (Statement statement = CONNECTION.createStatement()) {
            statement.executeUpdate(dropTable);
        } catch (SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
//            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(saveUser)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            CONNECTION.commit();

        } catch (SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
//            throw new RuntimeException(e);
        }
        System.out.println("User с именем " + name + " добавлен в базу данных");
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(deleteUser)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            CONNECTION.commit();
        } catch (SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
//            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Statement statement = CONNECTION.createStatement()) {
            ResultSet resultSet = statement.executeQuery(getUsersFromTable);

            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        resultSet.getByte("age"));

                users.add(user);
                resultSet.close();
            }
            CONNECTION.commit();

        } catch (SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
//            throw new RuntimeException(e);
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = CONNECTION.createStatement()) {
            statement.executeUpdate(cleanTable);
            CONNECTION.commit();
        } catch (SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
//            throw new RuntimeException(e);
        }
    }
}
