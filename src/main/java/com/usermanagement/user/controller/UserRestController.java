package com.usermanagement.user.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usermanagement.user.dto.UserDto;
import com.usermanagement.user.entity.User;
import com.usermanagement.user.service.IUserService;
import com.usermanagement.validator.UserFormValidator;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private IUserService IUserService;
    private UserFormValidator userFormValidator;

    @Autowired
    public UserRestController(IUserService IUserService, UserFormValidator userFormValidator) {
        this.IUserService = IUserService;
        this.userFormValidator = userFormValidator;
    }

    @InitBinder("userDto")
    protected void initUserBinder(WebDataBinder binder) {
        binder.addValidators(userFormValidator);
    }

    @GetMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getUser(@PathVariable("email") String email) throws Exception {
        return IUserService.getUser(email);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> getAllUsers() {
        return IUserService.getAllUsers();
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        return IUserService.createUser(userDto);
    }

    @PutMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto updateUser(@NotNull @RequestBody User user) throws Exception {
        return IUserService.updateUser(user);
    }

    @DeleteMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto deleteUser(@PathVariable("email") String email) throws Exception {
        return IUserService.deleteUser(email);
    }

}
