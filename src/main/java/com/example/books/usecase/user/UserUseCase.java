package com.example.books.usecase.user;

import com.example.books.domain.core.AdminUser;
import com.example.books.domain.core.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Use case boundary exposing user management workflows for adapters.
 */
public interface UserUseCase {
    com.example.books.infrastructure.database.jpa.entity.User registerUser(AdminUser user, String password);

    com.example.books.infrastructure.database.jpa.entity.User createUser(AdminUser user);

    Optional<com.example.books.infrastructure.database.jpa.entity.User> activateRegistration(String key);

    Optional<com.example.books.infrastructure.database.jpa.entity.User> requestPasswordReset(String mail);

    Optional<com.example.books.infrastructure.database.jpa.entity.User> completePasswordReset(String newPassword, String key);

    Optional<com.example.books.infrastructure.database.jpa.entity.User> getUserWithAuthorities();

    Optional<com.example.books.infrastructure.database.jpa.entity.User> getUserWithAuthoritiesByLogin(String login);

    Optional<com.example.books.infrastructure.database.jpa.entity.User> findOneByEmailIgnoreCase(String email);

    Optional<com.example.books.infrastructure.database.jpa.entity.User> findOneByLogin(String login);

    void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl);

    void changePassword(String currentPassword, String newPassword);

    Optional<AdminUser> updateUser(AdminUser user);

    Page<AdminUser> getAllManagedUsers(Pageable pageable);

    Page<User> getAllPublicUsers(Pageable pageable);

    void deleteUser(String login);

    List<String> getAuthorities();
}
