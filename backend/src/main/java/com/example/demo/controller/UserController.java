package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.User;
import com.example.demo.UserService;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/addUserForm")
    public String showAddUserForm() {
        return "add-user";
    }

    @PostMapping("/addUser")
    public String addUser(@RequestParam String name, @RequestParam String email, Model model) {
        User newUser = new User(name, email, null);
        userService.addUser(newUser);

        String message = "User " + name + " with email " + email + " added successfully!";
        model.addAttribute("message", message);
        return "add-user";
    }

    @ResponseBody
    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/deleteUser/{id}") // New endpoint for deleting a user
    @ResponseBody // Typically, you might return a status or a message
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "User with ID " + id + " deleted successfully!";
    }

    @GetMapping("/editUser")
    @ResponseBody
    public User editUser(@RequestBody User user) {
        return userService.editUser(user);
    }
}
