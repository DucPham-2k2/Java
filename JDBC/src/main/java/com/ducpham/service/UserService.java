package com.ducpham.service;

import com.ducpham.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    List<User> getAll();

    List<User> getAll(int pageNo, int pageSize);

    User getById(Long id);

    User create(User user) throws SQLException;

    User update(Long id, User user);

    String delete(Long id) throws SQLException;

    boolean existsUserByUsername(String username);

    boolean existsUserByUsernameAndNotId(String username, Long id);
}
