package com.usermanagement.user.entity;

import com.usermanagement.common.RoleEnum;
import com.usermanagement.user.dto.UserDto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Document(collection = "user")
public class User {
    @Id
    @Field("User_ID")
    private String id;
    private String firstName;
    private String lastName;
    @Indexed(name="userEmail", unique=true)
    private String email;
    private String password;
    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = false;
    private String token;
    private Set<String> passwordHistory;
    private int invalidLoginAttempt = 0;
    private boolean isFirstLogin = true;
    private LocalDate lastPasswordResetDate = LocalDate.now();
    private LocalDateTime tokenGeneratedDate;
    private List<RoleEnum> role = new ArrayList<>(0);

    public User() {
    }

    public User(UserDto userDto) {
        this.firstName = userDto.getFirstName();
        this.lastName = userDto.getLastName();
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
        this.role = userDto.getRoles();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getPasswordHistory() {
        return passwordHistory;
    }

    public void setPasswordHistory(Set<String> passwordHistory) {
        this.passwordHistory = passwordHistory;
    }

    public int getInvalidLoginAttempt() {
        return invalidLoginAttempt;
    }

    public void setInvalidLoginAttempt(int invalidLoginAttempt) {
        this.invalidLoginAttempt = invalidLoginAttempt;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    public LocalDate getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(LocalDate lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public LocalDateTime getTokenGeneratedDate() {
        return tokenGeneratedDate;
    }

    public void setTokenGeneratedDate(LocalDateTime tokenGeneratedDate) {
        this.tokenGeneratedDate = tokenGeneratedDate;
    }

    public List<RoleEnum> getRole() {
        return role;
    }

    public void setRole(List<RoleEnum> role) {
        this.role = role;
    }
}
