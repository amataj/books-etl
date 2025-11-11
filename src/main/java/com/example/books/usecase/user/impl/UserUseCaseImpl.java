package com.example.books.usecase.user.impl;

import com.example.books.domain.core.AdminUser;
import com.example.books.domain.core.User;
import com.example.books.domain.service.UserService;
import com.example.books.usecase.user.UserUseCase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserUseCaseImpl implements UserUseCase {

    private final UserService userService;

    public UserUseCaseImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public com.example.books.infrastructure.database.jpa.entity.User registerUser(AdminUser user, String password) {
        return userService.registerUser(user, password);
    }

    @Override
    public com.example.books.infrastructure.database.jpa.entity.User createUser(AdminUser user) {
        return userService.createUser(user);
    }

    @Override
    public Optional<com.example.books.infrastructure.database.jpa.entity.User> activateRegistration(String key) {
        return userService.activateRegistration(key);
    }

    @Override
    public Optional<com.example.books.infrastructure.database.jpa.entity.User> requestPasswordReset(String mail) {
        return userService.requestPasswordReset(mail);
    }

    @Override
    public Optional<com.example.books.infrastructure.database.jpa.entity.User> completePasswordReset(String newPassword, String key) {
        return userService.completePasswordReset(newPassword, key);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<com.example.books.infrastructure.database.jpa.entity.User> getUserWithAuthorities() {
        return userService.getUserWithAuthorities();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<com.example.books.infrastructure.database.jpa.entity.User> getUserWithAuthoritiesByLogin(String login) {
        return userService.getUserWithAuthoritiesByLogin(login);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<com.example.books.infrastructure.database.jpa.entity.User> findOneByEmailIgnoreCase(String email) {
        return userService.findOneByEmailIgnoreCase(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<com.example.books.infrastructure.database.jpa.entity.User> findOneByLogin(String login) {
        return userService.findOneByLogin(login);
    }

    @Override
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        userService.updateUser(firstName, lastName, email, langKey, imageUrl);
    }

    @Override
    public void changePassword(String currentPassword, String newPassword) {
        userService.changePassword(currentPassword, newPassword);
    }

    @Override
    public Optional<AdminUser> updateUser(AdminUser user) {
        return userService.updateUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminUser> getAllManagedUsers(Pageable pageable) {
        return userService.getAllManagedUsers(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getAllPublicUsers(Pageable pageable) {
        return userService.getAllPublicUsers(pageable);
    }

    @Override
    public void deleteUser(String login) {
        userService.deleteUser(login);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }
}
