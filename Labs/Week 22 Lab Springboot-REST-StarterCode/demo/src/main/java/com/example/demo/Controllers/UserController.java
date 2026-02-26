package com.example.demo.Controllers;

import com.example.demo.DTO.UserPostDTO;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Models.User;
import com.example.demo.Models.UserType;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // ============================================================================
    // GET ALL USERS
    // ============================================================================
    // GET http://localhost:8080/user
    // Returns: 200 OK with list of all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);  // 200 OK
    }

    // ============================================================================
    // GET USER BY ID
    // ============================================================================
    // GET http://localhost:8080/user/{id}
    // Example: http://localhost:8080/user/1
    // Returns: 200 OK if found, 404 Not Found if not found, 400 Bad Request if invalid id
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable(value = "id") Long userId) {
        // Validate input
        if (userId <= 0) {
            return ResponseEntity.badRequest().body("Invalid user ID");  // 400 Bad Request
        }

        try {
            User user = userService.findByID(userId);
            return ResponseEntity.ok(user);  // 200 OK
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with id: " + userId);  // 404 Not Found
        }
    }

    // ============================================================================
    // GET USER BY EMAIL (Query String)
    // ============================================================================
    // GET http://localhost:8080/user/findByEmail?email=bob@sample.com
    // Returns: 200 OK if found, 404 Not Found if not found, 400 Bad Request if email missing
    @GetMapping("/findByEmail")
    public ResponseEntity<?> getUserByEmail(@RequestParam(value = "email") String email) {
        // Validate input
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email parameter is required");  // 400 Bad Request
        }

        try {
            User user = userService.findByEmail(email);
            return ResponseEntity.ok(user);  // 200 OK
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with email: " + email);  // 404 Not Found
        }
    }

    // ============================================================================
    // CREATE NEW USER (POST)
    // ============================================================================
    // POST http://localhost:8080/user
    // Request Body: JSON matching UserPostDTO
    // Example:
    // {
    //   "name": "John Doe",
    //   "email": "john@example.com",
    //   "password": "password123",
    //   "buyer": true,
    //   "seller": false
    // }
    // Returns: 201 Created with created user, 400 Bad Request if invalid input
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserPostDTO newUserDTO) {
        // Validate input
        if (newUserDTO == null) {
            return ResponseEntity.badRequest().body("Request body cannot be empty");  // 400 Bad Request
        }

        if (newUserDTO.getName() == null || newUserDTO.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Name is required");  // 400 Bad Request
        }

        if (newUserDTO.getEmail() == null || newUserDTO.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");  // 400 Bad Request
        }

        if (newUserDTO.getPassword() == null || newUserDTO.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");  // 400 Bad Request
        }

        if (newUserDTO.getUserType() == UserType.NONE) {
            return ResponseEntity.badRequest().body("User must be a Buyer, Seller, or Both");  // 400 Bad Request
        }

        // Convert DTO to Model
        User newUser = new User(
                newUserDTO.getName(),
                newUserDTO.getEmail(),
                newUserDTO.getPassword(),
                newUserDTO.getUserType()
        );

        // Call service to save user
        User createdUser = userService.addUser(newUser);

        // Return created user with 201 Created status
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);  // 201 Created
    }

    // ============================================================================
    // DELETE USER BY ID
    // ============================================================================
    // DELETE http://localhost:8080/user/{id}
    // Example: http://localhost:8080/user/1
    // Returns: 200 OK if deleted, 404 Not Found if not found, 400 Bad Request if invalid id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long userId) {
        // Validate input
        if (userId <= 0) {
            return ResponseEntity.badRequest().body("Invalid user ID");  // 400 Bad Request
        }

        try {
            // First check if user exists
            User user = userService.findByID(userId);
            
            // If found, delete it
            userService.deleteUser(userId);
            
            return ResponseEntity.ok("User deleted successfully");  // 200 OK
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with id: " + userId);  // 404 Not Found
        }
    }

    // ============================================================================
    // BONUS: UPDATE USER (PUT) - Optional challenge
    // ============================================================================
    // PUT http://localhost:8080/user/{id}
    // Request Body: JSON matching UserPostDTO
    // Returns: 200 OK if updated, 404 Not Found if not found, 400 Bad Request if invalid
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(value = "id") Long userId, 
                                       @RequestBody UserPostDTO updateDTO) {
        // Validate input
        if (userId <= 0) {
            return ResponseEntity.badRequest().body("Invalid user ID");  // 400 Bad Request
        }

        if (updateDTO == null) {
            return ResponseEntity.badRequest().body("Request body cannot be empty");  // 400 Bad Request
        }

        try {
            // Find existing user
            User existingUser = userService.findByID(userId);

            // Update fields if provided
            if (updateDTO.getName() != null && !updateDTO.getName().trim().isEmpty()) {
                existingUser.setName(updateDTO.getName());
            }
            if (updateDTO.getEmail() != null && !updateDTO.getEmail().trim().isEmpty()) {
                existingUser.setEmail(updateDTO.getEmail());
            }
            if (updateDTO.getPassword() != null && !updateDTO.getPassword().trim().isEmpty()) {
                existingUser.setPassword(updateDTO.getPassword());
            }
            if (updateDTO.getUserType() != UserType.NONE) {
                existingUser.setUserType(updateDTO.getUserType());
            }

            // Save updated user
            User updatedUser = userService.addUser(existingUser);

            return ResponseEntity.ok(updatedUser);  // 200 OK
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with id: " + userId);  // 404 Not Found
        }
    }
}