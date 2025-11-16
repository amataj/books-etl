package com.example.books.etl.domain.user;

import java.io.Serializable;

/**
 * A DTO representing a password change required data - current and new password.
 */
public class PasswordChange implements Serializable {

    private static final long serialVersionUID = 1L;

    private String currentPassword;
    private String newPassword;

    public PasswordChange() {
        // Empty constructor needed for Jackson.
    }

    public PasswordChange(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
