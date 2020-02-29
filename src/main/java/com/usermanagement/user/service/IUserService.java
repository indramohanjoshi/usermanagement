package com.usermanagement.user.service;

import com.usermanagement.user.dto.LoginRequest;
import com.usermanagement.user.dto.UserDto;
import com.usermanagement.user.entity.User;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface IUserService {
    UserDto getUser(@NotNull String email) throws Exception;

    List<UserDto> getAllUsers();

    UserDto createUser(UserDto userDto);

    boolean handleLogin(LoginRequest loginRequest) throws Exception;

    UserDto deleteUser(String email) throws Exception;

    UserDto updateUser(@NotNull User user) throws Exception;

    String signUp(UserDto userDto) throws Exception;

    String activateAccount(UserDto userDto) throws Exception;
}
