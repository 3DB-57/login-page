// Для теста CRUD операций
package com.app.login.controller;

import com.app.login.entity.User;
import com.app.login.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping (path = "/api/main")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll(){
        return userService.findAll();
    }

    @PostMapping
    public User create (@RequestBody User user){
        return userService.create(user);
    }

    @DeleteMapping (path = "{userName}")
    public void delete(@PathVariable String userName){
        userService.delete(userName);
    }
}
