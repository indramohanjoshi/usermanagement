package com.usermanagement.user.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.usermanagement.common.RoleEnum;
import com.usermanagement.user.dto.LoginRequest;
import com.usermanagement.user.dto.UserDto;
import com.usermanagement.user.entity.User;
import com.usermanagement.user.repository.IUserRepository;
import com.usermanagement.user.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final int MAX_PASSWORD_AGE = 60;
    private static final int MAX_TOKEN_AGE = 24;
    private IUserRepository IUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final int MAX_ALLOWED_INVALID_ATTEMPT = 3;

    @Autowired
    public UserServiceImpl(IUserRepository IUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.IUserRepository = IUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public boolean handleLogin(LoginRequest loginRequest) throws Exception {
        LOGGER.debug(String.format("getUser with email: %s", loginRequest.getUsername()));
        User user = IUserRepository.findByEmail(StringUtils.trimWhitespace(loginRequest.getUsername()));
        if (null == user) {
            throw new Exception("Invalid login Attempt . Please provide valid credentials to prevent your login from being disabled.");
        } else if (!user.isEnabled()) {
            throw new Exception("Your account is not enabled yet. Please contact to admin.");
        } else if (!user.isAccountNonLocked()) {
            throw new Exception("Your account is locked. Please contact to admin.");
        }
        credentialExpireAction(user);
        if (!user.isCredentialsNonExpired()) {
            throw new Exception("Your password is expired. To generate new password, use \"Generate Password\" option.");
        } else if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return invalidCredentialAction(user);
        }
        return grantAuthority(loginRequest, user.getRole());
    }

    private void credentialExpireAction(User user) {
        if (ChronoUnit.DAYS.between(LocalDate.now(), user.getLastPasswordResetDate()) > MAX_PASSWORD_AGE) {
            user.setCredentialsNonExpired(false);
            IUserRepository.save(user);
        }
    }

    private boolean invalidCredentialAction(User user) throws Exception {
        int invalidAttempt = user.getInvalidLoginAttempt() + 1;
        user.setInvalidLoginAttempt(invalidAttempt);
        if (MAX_ALLOWED_INVALID_ATTEMPT == invalidAttempt) {
            user.setAccountNonLocked(false);
        }
        IUserRepository.save(user);
        throw new Exception("You have made " + invalidAttempt + " unsuccessful attempt(s)." +
                "The maximum retry attempts allowed for login are " + MAX_ALLOWED_INVALID_ATTEMPT +
                " Password is case-sensitive.To generate new password, use Generate Password option.");
    }

    private boolean grantAuthority(LoginRequest loginRequest, List<RoleEnum> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(getAuthorities(roles));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(), authorities);

        SecurityContextHolder.getContext().setAuthentication(newAuth);
        return true;
    }

    private static List<GrantedAuthority> getAuthorities(List<RoleEnum> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleEnum role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return authorities;
    }

    @Override
    public UserDto getUser(@NotNull String email) throws Exception {
        LOGGER.debug(String.format("getUser with email: %s", email));
        User user = IUserRepository.findByEmail(email);
        if (null == user) {
            throw new Exception("No user found with email: " + email);
        }
        return getUserDtoFromUser(user);
    }

    @Override
    public UserDto deleteUser(@NotNull String email) throws Exception {
        LOGGER.debug(String.format("deleteUser with email: %s", email));
        User user = IUserRepository.findByEmail(email);
        if (null == user) {
            throw new Exception("No user found with email: " + email);
        }
        user.setEnabled(false);
        IUserRepository.save(user);
        return getUserDtoFromUser(user);
    }

    private UserDto getUserDtoFromUser(User user) {
        return new UserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        LOGGER.debug("getAllUsers");
        List<User> users = IUserRepository.findAll();
        return !CollectionUtils.isEmpty(users) ? getUserDtoListFromUserList(users) : null;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        
        LOGGER.debug(String.format("createUser with email: %s", userDto.getEmail()));
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        User newUser = new User(userDto);
        User savedUser = IUserRepository.save(newUser);
       return getUserDtoFromUser(savedUser);
    }

    @Override
    public UserDto updateUser(@NotNull User user) throws Exception {
        LOGGER.debug(String.format("updateUser with email: %s", user.getEmail()));
        User fromStore = IUserRepository.findByEmail(user.getEmail());
        if (null == fromStore) {
            throw new Exception("No user found with email: " + user.getEmail());
        }
        user.setId(fromStore.getId());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return getUserDtoFromUser(IUserRepository.save(user));
    }

    @Override
    public String signUp(UserDto userDto) throws Exception {
        User fromStore = IUserRepository.findByEmail(userDto.getEmail());
        if (null != fromStore) {
            throw new Exception("Account already exist with email : " + userDto.getEmail());
        }
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        User newUser = new User(userDto);
        newUser.setFirstLogin(true);
        String token = UUID.randomUUID().toString();
        newUser.setToken(token);
        newUser.setTokenGeneratedDate(LocalDateTime.now());
        //send email
        IUserRepository.save(newUser);
        return "Thank You! The activation link has been sent to your email address." +
                " Please check your email to activate your account " + "token: " + token;
    }

    @Override
    public String activateAccount(UserDto userDto) throws Exception {
        User fromStore = IUserRepository.findByEmail(userDto.getEmail());
        if (null == fromStore) {
            throw new Exception("Account does not exist with email : " + userDto.getEmail());
        }
        validateUserToken(fromStore, userDto);
        fromStore.setEnabled(true);
        IUserRepository.save(fromStore);
        return "Thank You ! your account is activated successfully.";
    }

    private void validateUserToken(User fromStore, UserDto userDto) throws Exception {
        if (null == userDto.getToken() || !userDto.getToken().equals(fromStore.getToken())) {
            throw new Exception("Invalid Token in the request");
        }
        if (ChronoUnit.HOURS.between(LocalDateTime.now(), fromStore.getTokenGeneratedDate()) > MAX_TOKEN_AGE) {
            throw new Exception("Token expired. use resend activation link option.");
        }
    }

    private List<UserDto> getUserDtoListFromUserList(List<User> users) {
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(user -> userDtos.add(getUserDtoFromUser(user)));
        return userDtos;
    }

}
