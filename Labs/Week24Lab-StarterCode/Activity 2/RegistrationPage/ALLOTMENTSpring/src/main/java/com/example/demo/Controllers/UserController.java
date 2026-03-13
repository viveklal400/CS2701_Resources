package com.example.demo.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Services.UserService;
import com.example.demo.DTO.UserPostDTO;
import com.example.demo.Models.User;
import com.example.demo.Models.UserType;

@RestController

@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    @Autowired
    UserService userService;

    // Get All Users
    @GetMapping("/user")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/user")
    public User registerUser(@RequestBody User user) {
        long start = System.currentTimeMillis();

        User saved = userRepository.save(user);

        long end = System.currentTimeMillis();
        System.out.println("Processing time: " + (end - start) + " ms");

        return saved;
    }

    /Post a User
            stMapping("/user")
                    ublic ResponseEntity<Optional
    	if (newUserDTO.getName()==null || 
    	

             return new ResponseEntity<>(Optional.ofNullable(null), HttpStatus.BAD_REQUEST);
        }
    	
    	User newUser = new User(newUserDTO.getName(), newUserDTO.getEmail(),
    	

     
    }

    
    //Get User by ID
    @

         return userService.findByID(Id);
    }
    
    
    /
 
       userService.deleteUser(Id);
        return "User Deleted"; 
    }
    
    //Get User by Email
    @GetMapping("/user/findByEmail")
    public Optional<User> getUserByEmail(@RequestParam String email) {
    	return Optional.ofNullable(userService.findByEmail(email));
    }
	}

 