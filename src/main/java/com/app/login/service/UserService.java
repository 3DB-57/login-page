package com.app.login.service;

import com.app.login.entity.User;
import com.app.login.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private int checkAndSetAge (User user){
        int age = Period.between(user.getBirth(), LocalDate.now()).getYears();
        if (age < 16){
            throw new IllegalStateException("Incorrect date of birth or you age less then 16");
        }
        return age;
    }

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User create (User user) {
        Optional<User> optionalUser = userRepository.findByUserName(user.getUserName());
        Optional<User> emailCheck = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()){
            throw new IllegalStateException("User with this username already exists");
        }
        if (emailCheck.isPresent()){
            throw new IllegalStateException("User with this email already exists");
        }
        user.setAge(checkAndSetAge(user));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void delete (String userName){
        Optional<User> optionalUser = userRepository.findByUserName(userName);
        if (optionalUser.isEmpty()){
            throw new IllegalStateException("User " + userName + " does not exist.");
        }
        userRepository.deleteById(optionalUser.get().getId());
    }

    public User authenticationCheck (String userName, String pass){
        Optional<User> optionalUser = userRepository.findByUserName(userName);
        if (optionalUser.isEmpty()){
            throw new IllegalStateException("User " + userName + " does not exist.");
        }
        else {
            if (!optionalUser.get().getPassword().equals(pass)){
                throw new IllegalStateException("Incorrect password");
            }
        }
        return optionalUser.orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

}
