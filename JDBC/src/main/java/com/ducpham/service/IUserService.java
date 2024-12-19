package com.ducpham.service;

import com.ducpham.config.JdbcConfig;
import com.ducpham.model.User;
import com.ducpham.exception.EnumException;
import com.ducpham.exception.ExceptionHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IUserService implements UserService {
    public Connection connection;

    public IUserService() throws SQLException {
        this.connection = JdbcConfig.getConnection();
        this.connection.setAutoCommit(false);
    }

    /**
     * This function is used to get all user
     * @return List
     *
     * @author DucPham at 19 / 12 / 2024
     */
    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<User>();
        String sql = "select * from tbl_user";
        try (Statement statement = this.connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                users.add(new User(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
            }
        } catch (SQLException e) {
            throw new ExceptionHandler(EnumException.SQL_EXCEPTION, e.getMessage());
        }
        return users;
    }

    /**
     * This function is used to get all user of page
     * @param pageNo the number of page
     * @param pageSize the quantity users of page
     * @return List
     *
     * @author DucPham at 19 / 12 / 2024
     */
    @Override
    public List<User> getAll(int pageNo, int pageSize) {
        List<User> users = new ArrayList<User>();
        String sql = "select * from tbl_user offset ? rows fetch next ? rows only";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, (pageNo - 1) * pageSize);
            preparedStatement.setInt(2, pageSize);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(new User(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
            }
        } catch (SQLException e) {
            throw new ExceptionHandler(EnumException.SQL_EXCEPTION, e.getMessage());
        }
        return users;
    }

    /**
     * This function is used to get user by id
     * @param id the id to get user
     * @return User
     * @exception ExceptionHandler occur when cannot get a user by id
     *
     * @author DucPham at 19 / 12 / 2024
     */
    @Override
    public User getById(Long id) {
        User user = null;
        String query = "select * from tbl_user where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = new User(resultSet.getLong("id"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("description"));
            }
        } catch (SQLException e) {
            throw new ExceptionHandler(EnumException.SQL_EXCEPTION, e.getMessage());
        }
        if (user != null) return user;
        throw new ExceptionHandler(EnumException.NOT_FOUND, " Cannot get User by id " + id);
    }

    /**
     * This function is user to create new user
     * @apiNote check duplicate username with other people
     * @param user the new user to create
     * @return User
     * @exception ExceptionHandler occur when cannot create a new user
     *
     * @author DucPham at 19 / 12 / 2024
     */
    @Override
    public User create(User user) throws SQLException {
        if (existsUserByUsername(user.getUsername()))
            throw new ExceptionHandler(EnumException.EXISTED_DATA, " User already exists with username " + user.getUsername());
        int numRow = -1;
        String query = "insert into tbl_user (username, password, description) values (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getDescription());
            numRow = preparedStatement.executeUpdate();
            if (numRow != 0) System.out.println("Create user with username " + user.getUsername() + " successfully");
            else System.out.println("Create user fail");
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new ExceptionHandler(EnumException.SQL_EXCEPTION, e.getMessage());
        }
        if (numRow != -1) return user;
        throw new ExceptionHandler(EnumException.CREATE_FAILED, " Cannot create user with username " + user.getUsername());
    }

    /**
     * This function is user to update user
     * @apiNote check duplicate info with other user
     * @param user the new user's info to update
     * @return User
     * @exception ExceptionHandler occur when cannot update user because duplicate info with other user or cannot update user
     *
     * @author DucPham at 19 / 12 / 2024
     */
    @Override
    public User update(Long id, User user) {
        if (existsUserByUsernameAndNotId(user.getUsername(), id))
            throw new ExceptionHandler(EnumException.EXISTED_DATA, " User already exists with username " + user.getUsername());
        User userUpdate = getById(id);
        String query = "update tbl_user set username = ? , password = ? , description = ? where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUsername().equals(userUpdate.getUsername()) ? userUpdate.getUsername() : user.getUsername());
            preparedStatement.setString(2, user.getPassword().equals(userUpdate.getPassword()) ? userUpdate.getPassword() : user.getPassword());
            preparedStatement.setString(3, user.getDescription().equals(userUpdate.getDescription()) ? userUpdate.getDescription() : user.getDescription());
            preparedStatement.setLong(4, id);

            int numRow = preparedStatement.executeUpdate();

            if (numRow != 0) System.out.println("Update user with username " + user.getUsername() + " successfully");

            else throw new ExceptionHandler(EnumException.UPDATE_FAILED, " Cannot update user with username " + user.getUsername());
        } catch (RuntimeException | SQLException e) {
            throw new ExceptionHandler(EnumException.SQL_EXCEPTION, e.getMessage());
        }
        return user;
    }

    /**
     * This function is used to delete user
     * @param id the id of user to delete
     * @return String
     * @exception ExceptionHandler occur when don't get user by id or cannot delete user
     *
     * @author  DucPham at 19 / 12 / 2024
     */
    @Override
    public String delete(Long id) throws SQLException {
        String query = "delete from tbl_user where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            int numRow = preparedStatement.executeUpdate();
            if (numRow == 0)
                throw new ExceptionHandler(EnumException.DELETE_FAILED, " Cannot delete user with username " + id);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new ExceptionHandler(EnumException.SQL_EXCEPTION, e.getMessage());
        }
        return "Delete user with id " + id + " successfully";
    }

    /**
     * This function is used to check exist other user with username
     * @param username the username of user
     * @return boolean
     *
     * @author  DucPham at 19 / 12 / 2024
     */
    @Override
    public boolean existsUserByUsername(String username) {
        String query = "select 1 from tbl_user where username = ?";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            throw new ExceptionHandler(EnumException.SQL_EXCEPTION, e.getMessage());
        }
    }

    /**
     * This function is used to check exist username with other people
     * @param username the username of people
     * @param id the id of user who needed to check
     * @return boolean
     *
     * @author  DucPham at 19 / 12 / 2024
     */
    @Override
    public boolean existsUserByUsernameAndNotId(String username, Long id) {
        String query = "select 1 from tbl_user where username = ? and id <> ?";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setLong(2, id);
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            throw new ExceptionHandler(EnumException.SQL_EXCEPTION, e.getMessage());
        }
    }

}
