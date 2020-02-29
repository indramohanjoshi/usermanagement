package com.usermanagement.config;

import com.usermanagement.common.RoleEnum;
import com.usermanagement.user.entity.User;
import com.usermanagement.user.repository.IUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private IUserRepository IUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public InitialDataLoader(IUserRepository IUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.IUserRepository = IUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (null == IUserRepository.findByEmail("admin@gmail.com")) {
            User user = new User();
            user.setFirstName("Indramohan");
            user.setLastName("Joshi");
            user.setPassword(bCryptPasswordEncoder.encode("admin"));
            user.setEmail("admin@gmail.com");
            user.setRole(Collections.singletonList(RoleEnum.ADMIN));
            user.setEnabled(true);
            IUserRepository.save(user);
        }
    }
}