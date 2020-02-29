package com.usermanagement.user.controller;

import com.usermanagement.user.dto.LoginRequest;
import com.usermanagement.user.dto.UserDto;
import com.usermanagement.user.service.IUserService;
import com.usermanagement.validator.UserFormValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping("/api")
public class LoginController {

    private IUserService IUserService;
    private UserFormValidator userFormValidator;

    @Autowired
    public LoginController(IUserService IUserService, UserFormValidator userFormValidator) {
        this.IUserService = IUserService;
        this.userFormValidator = userFormValidator;
    }

    @InitBinder({"loginRequest", "userDto"})
    protected void initLoginBinder(WebDataBinder binder) {
        binder.setValidator(userFormValidator);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON)
    public boolean login(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        return IUserService.handleLogin(loginRequest);
    }

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON)
    public String signUp(@Valid @RequestBody UserDto userDto) throws Exception {
        return IUserService.signUp(userDto);
    }

    @PostMapping(value = "/activateaccount", produces = MediaType.APPLICATION_JSON)
    public String activateAccount(@Valid @RequestBody UserDto userDto) throws Exception {
        return IUserService.activateAccount(userDto);
    }

    @GetMapping(value = "/login")
    public String loginPage() throws Exception {
        return "Please login using /api/login";
    }

}
