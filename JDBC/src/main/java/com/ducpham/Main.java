package com.ducpham;

import com.ducpham.model.User;
import com.ducpham.service.IUserService;
import com.ducpham.service.UserService;

import java.sql.SQLException;

public class Main {
    static UserService userService;

    static {
        try {
            userService = new IUserService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(userService.create(new User("ducpham2k2", "30122002", "demo")));
        System.out.println(userService.getById(1L));
        System.out.println(userService.getAll());
        System.out.println(userService.getAll(1, 2));
        System.out.println(userService.update(1L, new User("ducpham2k2", "30122002", "demo update user")));
        System.out.println(userService.delete(1L));
    }
}