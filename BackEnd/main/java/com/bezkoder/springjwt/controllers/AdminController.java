package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.UserRepository;
import com.thoughtworks.xstream.XStream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/admin")
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserDetails(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users/export/json")
    public ResponseEntity<List<User>> exportUsersToJson() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/export/xml")
    public ResponseEntity<String> exportUsersToXml() {
        List<User> users = userRepository.findAll();
        XStream xstream = new XStream();
        xstream.alias("user", User.class);
        String xml = xstream.toXML(users);
        return ResponseEntity.ok(xml);
    }

    @PostMapping("/users/export/json")
    public ResponseEntity<?> exportSelectedUsersToJson(@RequestBody List<Long> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        if (users.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("No users found for the given IDs."));
        }
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users/export/xml")
    public ResponseEntity<?> exportSelectedUsersToXml(@RequestBody List<Long> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        if (users.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("No users found for the given IDs."));
        }
        XStream xstream = new XStream();
        xstream.alias("user", User.class);
        String xml = xstream.toXML(users);
        return ResponseEntity.ok(xml);
    }

}
