package com.bezkoder.springjwt.controllers;
import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.payload.request.ChatRequest;
import com.bezkoder.springjwt.payload.request.JobRequest;
import com.bezkoder.springjwt.payload.request.LoginRequest;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.payload.response.JwtResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.*;
import com.bezkoder.springjwt.security.jwt.JwtUtils;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import com.bezkoder.springjwt.security.services.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/jobs")
public class JobController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final JobRepository jobRepository;

    private final SkillRepository skillRepository;

    private final NotificationRepository notificationRepository;

    private final PasswordEncoder encoder;

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    @GetMapping
    public List<Job> getJobs(){
        return jobRepository.findAll();
    }

    @PostMapping("/createJob")
    public ResponseEntity<?> createJob(@RequestBody JobRequest jobRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }

        User currentUser = currentUserOpt.get();
        Set<Skill> skills = new HashSet<>();

        for (String skillName : jobRequest.getSkills()) {
            Skill skill = skillRepository.findByName(skillName)
                    .orElseGet(() -> skillRepository.save(new Skill(skillName)));
            skills.add(skill);
        }

        Job job = new Job(currentUser.getUsername(), jobRequest.getTitle(), jobRequest.getDescription(), skills);
        jobRepository.save(job);

        return ResponseEntity.ok(new MessageResponse("Job posted successfully: " + jobRequest.getTitle()));
    }


    @PostMapping("/apply")
    public ResponseEntity<?> apply(@RequestBody Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }
        User currentUser = currentUserOpt.get();
        Optional<Job> optjob = jobRepository.findById(id);
        if (!optjob.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }
        Job job = optjob.get();
        job.addApplicant(currentUser);

        Optional<User> UserOpt = userRepository.findByUsername(job.getEmployer());
        if (!UserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        User user = UserOpt.get();

        // Create a notification
        Notification notification = new Notification(
                currentUser.getUsername() + " applied for your job posting",
                user
        );
        notificationRepository.save(notification);
        user.addNotification(notification);
        return ResponseEntity.ok(new MessageResponse("Applicant added successfully "));
    }

    @GetMapping("/user_jobs")
    public List<Job> getUserJobs(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");        }
        User currentUser = currentUserOpt.get();
        return jobRepository.findJobsByEmployer(currentUser.getUsername());

    }



}
