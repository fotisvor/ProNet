package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.Advertisment;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.AdvertismentRepository;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.bezkoder.springjwt.repository.UserRepository;


import javax.validation.Valid;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final AdvertismentRepository advertismentRepository;

    public TestController(UserRepository userRepository, RoleRepository roleRepository, AdvertismentRepository advertismentRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.advertismentRepository= advertismentRepository;
    }


    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public Set<?> userAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the principal is an instance of UserDetailsImpl


        // Cast the principal to UserDetailsImpl

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // Retrieve the full user details from the database using the user ID
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User not found." + authentication.getPrincipal()));;
        return user.getPosts();
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @PostMapping("/createPost")
    public ResponseEntity<?> createPost(@Valid @RequestBody Advertisment advertisment) {
        // Retrieve the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the principal is an instance of UserDetailsImpl
        if (authentication == null ) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: Unauthorized"));
        }

        // Cast the principal to UserDetailsImpl


        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // Retrieve the full user details from the database using the user ID
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User not found." + authentication.getPrincipal()));

        // Create a new Advertisment object using the data from the request body
        Advertisment newAdvertisment = new Advertisment(advertisment.getContent(), user.getUsername());

        // Save the Advertisment to the database
        advertismentRepository.save(newAdvertisment);

        // Associate the new Advertisment with the user
        user.getPosts().add(newAdvertisment);

        // Save the updated user to the database
        userRepository.save(user);

        // Return a response indicating success
        return ResponseEntity.ok(new MessageResponse("Post created successfully!"));
    }
}
