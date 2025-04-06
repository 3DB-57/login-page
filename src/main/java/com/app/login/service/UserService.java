package com.app.login.service;

import com.app.login.entity.User;
import com.app.login.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private int checkAndSetAge (User user){
        int age = Period.between(user.getBirth(), LocalDate.now()).getYears();
        if (age < 16){
            throw new IllegalStateException("Incorrect date of birth or you age less then 16");
        }
        return age;
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return userRepository.save(user);
    }

    public void delete (String userName){
        Optional<User> optionalUser = userRepository.findByUserName(userName);
        if(optionalUser.isEmpty()){
            throw new IllegalStateException("User " + userName + " does not exist.");
        }
        userRepository.deleteById(optionalUser.get().getId());
    }


}
