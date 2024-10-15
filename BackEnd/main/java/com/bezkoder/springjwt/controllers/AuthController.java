package com.bezkoder.springjwt.controllers;
import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.payload.request.*;
import com.bezkoder.springjwt.payload.response.AdvertismentWithImage;
import com.bezkoder.springjwt.payload.response.JwtResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.*;
import com.bezkoder.springjwt.security.jwt.JwtUtils;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import com.bezkoder.springjwt.security.services.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final AdvertismentRepository advertismentRepository;

    private final SkillRepository skillRepository;

    private final RequestRepository requestRepository;

    private final JobRepository jobRepository;

    private final PasswordEncoder encoder;

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;
    private final CommentRepository commentRepository;

    @Autowired
    private  ConversationRepository conversationRepository;

    @Autowired
    private ChatRepository chatRepository;

    private final NotificationRepository notificationRepository;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getName(),
                userDetails.getSurname(),
                userDetails.getPhone(),
                userDetails.isEmailPublic(),
                userDetails.isPositionPublic(),
                userDetails.isCompanyPublic(),
                userDetails.isCVPublic(),
                userDetails.isPhonePublic(),
                userDetails.isNamePublic(),
                userDetails.isSurnamePublic(),

                roles));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public String getPublicContent() {
        // Generate random data (you can replace this with your actual logic)
        Random random = new Random();
        int randomNumber = random.nextInt(1000);
        return "Random data: " + randomNumber;
    }

    @GetMapping("/user")
    public String getUserContent(){
        Random random = new Random();
        int randomNumber = random.nextInt(1000);
        return "Random data: " + randomNumber;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Create new user's account
        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getName(),
                signUpRequest.getSurname(),
                signUpRequest.getPhone()
        );

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;

                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping(value = "/createPost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(@RequestParam("post") String post,
                                        @RequestParam("username") String username,
                                        @RequestParam(value = "file", required = false) MultipartFile file) {
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


        // Create a new Advertisment object using the post content and username
        Advertisment newAdvertisment = new Advertisment(post, username);


        // Save the Advertisment to the database
        advertismentRepository.save(newAdvertisment);


        user.getPosts().add(newAdvertisment);
        userRepository.save(user);
        // Handle the file upload (if present)
        String fileUrl = null;
        if (file != null && !file.isEmpty()) {
            try {
                // Define the path where the file will be saved
                String fileName = newAdvertisment.getId() + "_post_image." + getFileExtension(file.getOriginalFilename());
                Path path = Paths.get("src/main/resources/uploads/" + fileName);

                // Ensure the directory exists
                Files.createDirectories(path.getParent());

                // Save the file to the specified path
                Files.write(path, file.getBytes());

                // Set the file path
                fileUrl = "src/main/resources/uploads/" + fileName;

                System.out.println("File saved at: " + fileUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body(new MessageResponse("Error: Failed to upload image."));
            }
        }



        // Return a response indicating success
        return ResponseEntity.ok(new MessageResponse("Post created successfully!"));
    }

    @GetMapping("/allPosts")
    public List<Advertisment> getAllPosts(){
        return advertismentRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @GetMapping("/allImagePosts")
    public ResponseEntity<List<AdvertismentWithImage>> getAllImagePosts() throws IOException {
        List<Advertisment> posts = advertismentRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<AdvertismentWithImage> result = new ArrayList<>();

        for (Advertisment post : posts) {
            String fileName = post.getId() + "_post_image.jpeg";
            Path path = Paths.get("src/main/resources/uploads/" + fileName);
            byte[] imageBytes = null;

            if (Files.exists(path)) {
                imageBytes = Files.readAllBytes(path);
            }
            // Add post to result list, imageBytes will be null if there's no image
            result.add(new AdvertismentWithImage(post, imageBytes));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    @GetMapping("/getImage")
    public ResponseEntity<byte[]> getPostImage(@RequestParam String  id) {
        try {
            // Validate and convert id
            if (id == null || id.equals("undefined")) {
                return ResponseEntity.badRequest().body(null); // or you can send an appropriate error response
            }

            Integer postId;
            try {
                postId = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(null); // or an appropriate error response
            }
            Path path = Paths.get("src/main/resources/uploads/" + postId +"_post_image.jpeg");
            byte[] imageBytes = Files.readAllBytes(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust if your images are in a different format
            headers.setContentLength(imageBytes.length);

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/addLike")
    public ResponseEntity<?> addLike(@RequestParam Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Cast the principal to UserDetailsImpl

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        User currentUser = currentUserOpt.get();
        Optional<Advertisment> optadvertisment = advertismentRepository.findById(id);
        if (!optadvertisment.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        Advertisment advertisment = optadvertisment.get();
        advertisment.addLike(currentUser.getUsername());
        advertismentRepository.save(advertisment);

        Optional<User> UserOpt = userRepository.findByUsername(advertisment.getUsername());
        if (!UserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        User user = UserOpt.get();

        // Create a notification
        Notification notification = new Notification(
                currentUser.getUsername() + " liked your post",
                user
        );
        notificationRepository.save(notification);
        user.addNotification(notification);
        return ResponseEntity.ok(new MessageResponse("Like added!" + advertisment.getId()));
    }

    @PostMapping("/removeLike")
    public ResponseEntity<?> removeLike(@RequestParam Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Cast the principal to UserDetailsImpl

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        User currentUser = currentUserOpt.get();
        Optional<Advertisment> optadvertisment = advertismentRepository.findById(id);
        if (!optadvertisment.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        Advertisment advertisment = optadvertisment.get();
        advertisment.removeLike(currentUser.getUsername());
        return ResponseEntity.ok(new MessageResponse("Like removed!"));
    }

    @GetMapping("/likes")
    public Integer getLikes(@RequestParam Integer id){
        Optional<Advertisment> optadvertisment = advertismentRepository.findById(id);
        if (!optadvertisment.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        Advertisment advertisment = optadvertisment.get();
        return advertisment.getLikecounter();
    }

    @PostMapping("/addComment")
    public ResponseEntity<?> addComment(@RequestBody CommentRequest commentRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Cast the principal to UserDetailsImpl

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        User currentUser = currentUserOpt.get();
        Comment comment = new Comment(commentRequest.getContent(), currentUser.getUsername());
        commentRepository.save(comment);
        Optional<Advertisment> optionalAdvertisment = advertismentRepository.findById(commentRequest.getId());
        if (!optionalAdvertisment.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        Advertisment advertisment= optionalAdvertisment.get();
        advertisment.addComment(comment);
        advertismentRepository.save(advertisment);

        Optional<User> UserOpt = userRepository.findByUsername(advertisment.getUsername());
        if (!UserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        User user = UserOpt.get();

        // Create a notification
        Notification notification = new Notification(
                currentUser.getUsername() + " commented on your post",
                        user
        );
        notificationRepository.save(notification);
        user.addNotification(notification);
        return ResponseEntity.ok(new MessageResponse("Comment posted!"));


    }
    @GetMapping("/allUsers")
    public List<?> getAllUsers(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Cast the principal to UserDetailsImpl

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        User currentUser = currentUserOpt.get();
        List<User> allUsers = userRepository.findAll();

        // Remove the current user from the list
        allUsers.removeIf(user -> user.getUsername().equals(currentUser.getUsername() ) || currentUser.getFriends().contains(user));
        return allUsers;
    }

    @PostMapping("/sendFriendRequest")
    public ResponseEntity<?> sendFriendRequest(@Valid @RequestBody String username){
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
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        User currentUser = currentUserOpt.get();
        Request newRequest = new Request(currentUser.getUsername(),username);
        requestRepository.save(newRequest);

        Optional<User> UserOpt = userRepository.findByUsername(username);
        if (!UserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        User user = UserOpt.get();

        // Create a notification
        Notification notification = new Notification(
                currentUser.getUsername() + " sent you a friend request",
                user
        );
        notificationRepository.save(notification);
        user.addNotification(notification);
        return ResponseEntity.ok(new MessageResponse("Friend Request was sent!"));
    }

    @GetMapping("/Requests")
    public List<Request> getRequests(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        User currentUser = currentUserOpt.get();
        List requests = requestRepository.findByToUser(currentUser.getUsername());
        if(requests==null){
            throw new RuntimeException("Error: No requests found for " + currentUser.getUsername());
        }

        return requests;
    }

    @PostMapping("/acceptFriendRequest")
    public ResponseEntity<?> AcceptRequest(@Valid @RequestBody Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        User currentUser = currentUserOpt.get();
        Optional<Request> requestOpt = requestRepository.findById(id);

        if (!requestOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request not found."));
        }

        Request request = requestOpt.get();
        Optional<User> userOptional = userRepository.findByUsername(request.getFromUser());

        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request not found."));
        }
        User user1 = userOptional.get();

        Optional<User> userOptional2 = userRepository.findByUsername(request.getToUser());

        if (!userOptional2.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request not found."));
        }
        User user2 = userOptional2.get();

        user1.getFriends().add(user2);
        user2.getFriends().add(user1);



        // Create a notification
        Notification notification = new Notification(
                currentUser.getUsername() + " accepted your friend request!",
                user1
        );
        notificationRepository.save(notification);
        user1.addNotification(notification);

        requestRepository.delete(request);
        return ResponseEntity.ok(new MessageResponse("Friend Request was accepted"));

    }

    @PostMapping("/rejectFriendRequest")
    public ResponseEntity<?> RejectRequest(@Valid @RequestBody Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        User currentUser = currentUserOpt.get();
        Optional<Request> requestOpt = requestRepository.findById(id);

        if (!requestOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request not found."));
        }

        Request request = requestOpt.get();

        requestRepository.delete(request);
        return ResponseEntity.ok(new MessageResponse("Friend Request was accepted"));

    }

    @GetMapping("/friends")
    public List<User> getFriends(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }

        User currentUser = currentUserOpt.get();
        Set<User> friends = currentUser.getFriends();


        return new ArrayList<>(friends);
    }

    @PostMapping("/conversations")
    public ResponseEntity<?> createConversation(@RequestParam String friendUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }

        Optional<User> friendOpt = userRepository.findByUsername(friendUsername);
        if (!friendOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Friend not found."));
        }

        User currentUser = currentUserOpt.get();
        User friend = friendOpt.get();

        // Create a new conversation
        Conversation conversation = new Conversation(currentUser.getUsername() + " & " + friend.getUsername());
        conversationRepository.save(conversation);

        return ResponseEntity.ok(new MessageResponse("Conversation created successfully"));
    }

    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestBody ChatRequest chatRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }

        Optional<User> recipientOpt = userRepository.findByUsername(chatRequest.getToUser());
        if (!recipientOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Recipient not found."));
        }

        User currentUser = currentUserOpt.get();
        User recipient = recipientOpt.get();

        // Find or create a conversation between the two users
        Conversation conversation = findOrCreateConversation(currentUser, recipient);

        Chat chat = new Chat(currentUser.getUsername(), chatRequest.getMessage());
        conversation.addChat(chat);

        conversationRepository.save(conversation);



        // Create a notification
        Notification notification = new Notification(
                currentUser.getUsername() + " sent you a message",
                recipient
        );
        notificationRepository.save(notification);
        recipient.addNotification(notification);
        return ResponseEntity.ok(new MessageResponse("Message sent successfully"));
    }

    @GetMapping("/messages/{friend}")
    public ResponseEntity<?> getChatsByConversation(@PathVariable String friend) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }
        User currentUser = currentUserOpt.get();

        Optional<User> friendOpt = userRepository.findByUsername(friend);

        if (!friendOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }
        User Friend= friendOpt.get();


        Conversation conversation = findOrCreateConversation(currentUser,Friend);


        List<Chat> chats = conversation.getChats();
        return ResponseEntity.ok(chats);
    }


    @GetMapping("/notifications")
    public List<Notification> getNotifications(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");        }
        User currentUser = currentUserOpt.get();
        return currentUser.getNotifications();
    }

    @PostMapping("/notifications/markAsRead")
    public ResponseEntity<?> markAsRead(@Valid @RequestBody Long id) {

        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        if (!notificationOpt.isPresent()) {
            throw new RuntimeException("Error: Notification not found.");
        }

        Notification notification = notificationOpt.get();
        notification.setRead(true);
        notificationRepository.save(notification);
        return ResponseEntity.ok(new MessageResponse("Notification marked as read"));
    }

    @RequestMapping(value = "/updateUser",
            method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUser(@Valid @ModelAttribute UserUpdateRequest updateUserRequest) {
        System.out.println("Received update request: " + updateUserRequest);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: User not found."));
        }

        User currentUser = currentUserOpt.get();

        // Update username
        if (!updateUserRequest.getUsername().equals(currentUser.getUsername()) && userRepository.existsByUsername(updateUserRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        currentUser.setUsername(updateUserRequest.getUsername());

        // Update email
        currentUser.setEmail(updateUserRequest.getEmail());

        // Handle the file upload
        MultipartFile file = updateUserRequest.getFile();

        if (updateUserRequest.getFile() != null && !updateUserRequest.getFile().isEmpty()) {
            try {
                // Define the path where the file will be saved
                String fileName = updateUserRequest.getUsername() + "_profile_image." + getFileExtension(updateUserRequest.getFile().getOriginalFilename());
                Path path = Paths.get("src/main/resources/" + fileName);

                // Ensure the directory exists
                Files.createDirectories(path.getParent());

                // Save the file
                Files.write(path, updateUserRequest.getFile().getBytes());

                // Set the profile image URL for the user
                String fileUrl = "src/main/resources/" + fileName;

                System.out.println("File path: " + fileUrl );
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Failed to upload image");
            }
        }


        userRepository.save(currentUser);

        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }

    @RequestMapping(value = "/updateUser1", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUser1(@Valid @ModelAttribute UserUpdateRequest updateUserRequest) {
        System.out.println("Received update request: " + updateUserRequest);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: User not found."));
        }

        User currentUser = currentUserOpt.get();

        // Update username if provided
        if (updateUserRequest.getUsername() != null &&
                !updateUserRequest.getUsername().equals(currentUser.getUsername()) &&
                userRepository.existsByUsername(updateUserRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (updateUserRequest.getUsername() != null) {
            currentUser.setUsername(updateUserRequest.getUsername());
        }

        // Update email if provided
        if (updateUserRequest.getEmail() != null) {
            currentUser.setEmail(updateUserRequest.getEmail());
        }

        // Update position if provided
        if (updateUserRequest.getPosition() != null) {
            currentUser.setPosition(updateUserRequest.getPosition());
        }

        // Update company if provided
        if (updateUserRequest.getCompany() != null) {
            currentUser.setCompany(updateUserRequest.getCompany());
        }

        if (updateUserRequest.getName() != null) {
            currentUser.setName(updateUserRequest.getName());
        }

        if (updateUserRequest.getSurname() != null) {
            currentUser.setSurname(updateUserRequest.getSurname());
        }

        if (updateUserRequest.getPhone() != null) {
            currentUser.setPhone(updateUserRequest.getPhone());
        }

        // Update password if provided
        if (updateUserRequest.getPassword() != null) {
            // Ideally, you'd hash the password here before storing it
            currentUser.setPassword(encoder.encode(updateUserRequest.getPassword()));
        }

        // Handle profile image upload if provided
        MultipartFile profileImage = updateUserRequest.getFile();
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                // Define the path where the profile image will be saved
                String fileName = currentUser.getUsername() + "_profile_image." + getFileExtension(profileImage.getOriginalFilename());
                Path path = Paths.get("src/main/resources/images/" + fileName);

                // Ensure the directory exists
                Files.createDirectories(path.getParent());

                // Save the profile image file
                Files.write(path, profileImage.getBytes());

                // Set the profile image URL for the user
                String profileImageUrl = "src/main/resources/images/" + fileName;
                currentUser.setProfilePhotoUrl(profileImageUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Failed to upload profile image");
            }
        }

        // Handle the CV upload if provided
        MultipartFile cvFile = updateUserRequest.getCsvFile();
        if (cvFile != null && !cvFile.isEmpty()) {
            try {
                // Define the path where the CV will be saved
                String fileName = currentUser.getUsername() + "_cv." + getFileExtension(cvFile.getOriginalFilename());
                Path path = Paths.get("src/main/resources/cvs/" + fileName);

                // Ensure the directory exists
                Files.createDirectories(path.getParent());

                // Save the CV file
                Files.write(path, cvFile.getBytes());

                // Set the CV URL for the user
                String cvUrl = "src/main/resources/cvs/" + fileName;
                currentUser.setCvURL(cvUrl);
            } catch (IOException e) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON); // Set content type as JSON for error message
                String message = "No CV available for user " + currentUser.getUsername();
                return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.NO_CONTENT);

            }
        }
        //update user flags for private/public
        currentUser.setEmailPublic(updateUserRequest.getisEmailPublic());
        currentUser.setPositionPublic(updateUserRequest.getisPositionPublic());
        currentUser.setCompanyPublic(updateUserRequest.getisCompanyPublic());
        currentUser.setCVPublic(updateUserRequest.getisCvPublic());
        currentUser.setPhonePublic(updateUserRequest.getisPhonePublic());
        currentUser.setNamePublic(updateUserRequest.getisNamePublic());
        currentUser.setSurnamePublic(updateUserRequest.getisSurnamePublic());




        // Save the updated user
        userRepository.save(currentUser);

        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }




    // Helper method to get the file extension
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @GetMapping("/profileImage")
    public ResponseEntity<byte[]> getProfileImage() throws IOException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());
        if (!currentUserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");        }
        User currentUser = currentUserOpt.get();
        String fileName = currentUser.getUsername() + "_profile_image.jpeg";
        Path path = Paths.get("src/main/resources/" + fileName);
        byte[] imageBytes = Files.readAllBytes(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // or MediaType.IMAGE_PNG, depending on the file type
        headers.setContentLength(imageBytes.length);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }



    private Conversation findOrCreateConversation(User user1, User user2) {
        // Create a unique identifier for the conversation
        String conversationTitle = generateConversationTitle(user1.getUsername(), user2.getUsername());
        Optional<Conversation> conversationOpt = conversationRepository.findByTitle(conversationTitle);

        return conversationOpt.orElseGet(() -> {
            Conversation newConversation = new Conversation(conversationTitle);
            return conversationRepository.save(newConversation);
        });
    }

    private Optional<Conversation> findConversation(User user1, User user2) {
        // Find conversation using the unique identifier
        String conversationTitle = generateConversationTitle(user1.getUsername(), user2.getUsername());
        return conversationRepository.findByTitle(conversationTitle);
    }

    private String generateConversationTitle(String username1, String username2) {
        // Sort the usernames lexicographically to create a unique identifier
        if (username1.compareTo(username2) < 0) {
            return username1 + " & " + username2;
        } else {
            return username2 + " & " + username1;
        }
    }

    @PostMapping("/addSkills")
    public ResponseEntity<?> AddSkills(@Valid @RequestBody List<SkillRequest> skillRequests) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: User not found."));
        }

        User user = currentUserOpt.get();

        // Loop through the list of skill requests
        for (SkillRequest skillRequest : skillRequests) {
            Optional<Skill> existingSkillOpt = skillRepository.findByName(skillRequest.getName());
            Skill skill;

            if (existingSkillOpt.isPresent()) {
                skill = existingSkillOpt.get();
            } else {
                // Create a new skill if it doesn't exist
                skill = new Skill(skillRequest.getName());
                skillRepository.save(skill);
            }

            // Add the skill to the user if it's not already present
            if (!user.getSkills().contains(skill)) {
                user.getSkills().add(skill);
            }
        }

        // Save the updated user with the new skills
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Skills were added successfully!"));
    }
    @GetMapping("/recommended")
    public ResponseEntity<List<Job>> getRecommendedAds() {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User currentUser = currentUserOpt.get();
        Set<Skill> userSkills = currentUser.getSkills();
        List<Job> recommendedJobs = jobRepository.findJobsByUserSkills(userSkills);

        return new ResponseEntity<>(recommendedJobs, HttpStatus.OK);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username){
        System.out.println("Received request for user: " + username);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }
        User currentUser = currentUserOpt.get();

        Optional<User> newUser = userRepository.findByUsername(username);
        if (!newUser.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }
        User user = newUser.get();
        System.out.println(user.getPhone());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/originaluser")
    public User getOriginalUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());
        System.out.println("Request received for user: " + userDetails.getUsername());  // Log when the request is received

        if (!currentUserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }
        User currentUser = currentUserOpt.get();


        System.out.println(currentUser.getPhone());
        return currentUser;
    }



    @GetMapping("/cvFile")
    public ResponseEntity<byte[]> getCVfile() throws IOException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }
        User currentUser = currentUserOpt.get();
        String fileName = currentUser.getUsername() + "_cv.pdf";
        Path path = Paths.get("src/main/resources/cvs/" + fileName);
        if (!Files.exists(path)) {
            throw new RuntimeException("Error: CV file not found.");
        }
        byte[] pdfBytes = Files.readAllBytes(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF); // Set the content type to PDF
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/cvFile/{username}")
    public ResponseEntity<byte[]> getCVfile(@PathVariable String username) throws IOException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> currentUserOpt = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUserOpt.isPresent()) {
            throw new RuntimeException("Error: User not found.");
        }
        User currentUser = currentUserOpt.get();
        String fileName = username + "_cv.pdf";
        Path path = Paths.get("src/main/resources/cvs/" + fileName);
        if (!Files.exists(path)) {
            throw new RuntimeException("Error: CV file not found.");
        }
        byte[] pdfBytes = Files.readAllBytes(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF); // Set the content type to PDF
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/friendProfileImage/{username}")
    public ResponseEntity<byte[]> getFriendProfileImage(@PathVariable String username) throws IOException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String fileName = username + "_profile_image.jpeg";
        Path path = Paths.get("src/main/resources/" + fileName);
        byte[] imageBytes = Files.readAllBytes(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // or MediaType.IMAGE_PNG, depending on the file type
        headers.setContentLength(imageBytes.length);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }


}


