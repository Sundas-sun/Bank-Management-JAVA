package services;

import dao.UserDAO;
import models.User;
import utils.PasswordUtils;

import java.sql.SQLException;

public class AuthenticationService {
    private UserDAO userDAO = new UserDAO();

    public User login(String username, String password) throws SQLException {
        String hash = PasswordUtils.hashPassword(password);
        return userDAO.authenticate(username, hash);
    }

    public void registerUser(String username, String password, String role) throws SQLException {
        String hash = PasswordUtils.hashPassword(password);
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(hash);
        user.setRole(role);
        userDAO.createUser(user);
    }
}