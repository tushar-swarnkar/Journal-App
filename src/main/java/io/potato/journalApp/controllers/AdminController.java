package io.potato.journalApp.controllers;

import io.potato.journalApp.cache.AppCache;
import io.potato.journalApp.entity.User;
import io.potato.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppCache appCache;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        List<User> all = userService.findAll();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("crate-admin-user")
    public void createUser(@RequestBody  User user) {
        userService.saveAdmin(user);
    }

    @GetMapping("/clear-app-cache")
    public void appCache() {
        appCache.init();
    }

}
