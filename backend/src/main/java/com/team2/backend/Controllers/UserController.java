package com.team2.backend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team2.backend.DTO.User.ChangeDisplayNameDTO;
import com.team2.backend.DTO.User.ChangePasswordDTO;
import com.team2.backend.DTO.User.ChangeUsernameDTO;
import com.team2.backend.DTO.User.UserLoginDTO;
import com.team2.backend.DTO.User.UserResponseDTO;
import com.team2.backend.DTO.User.UserSignUpDTO;
import com.team2.backend.Enums.UserRole;
import com.team2.backend.Models.User;
import com.team2.backend.Service.UserService;

import jakarta.validation.Valid;

/**
 * REST controller for managing user-related operations.
 * Handles HTTP requests for user registration and delegates
 * the business logic to the UserService layer.
 */
@RestController
@RequestMapping("/user")
public class UserController {

        // Injects the UserService instance for handling user-related business logic.
        @Autowired
        private UserService userService;

        /**
         * Endpoint to register a new user.
         * 
         * @param userRequestDTO The user details sent in the request body as a JSON
         *                       object.
         * @return A ResponseEntity containing either the newly created user object
         *         with a 200 OK status, or an error message with a 400 Bad Request
         *         status.
         */
        @PostMapping("/register")
        public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserSignUpDTO userSignUpDTO) {
                // Convert UserRequestDTO to User entity
                User user = new User();
                user.setUsername(userSignUpDTO.getUsername());
                user.setPassword(userSignUpDTO.getPassword());
                user.setDisplayName(userSignUpDTO.getDisplayName());
                user.setUserRole(UserRole.CONTRIBUTOR);

                // Call the UserService to register the user
                String token = userService.createUser(user);

                // Create response DTO
                UserResponseDTO responseDTO = new UserResponseDTO("Register successful.", token);

                ResponseCookie cookie = ResponseCookie.from("token", token)
                                // Frontend uses jwt token for userInfo so it needs to be accessible by
                                // javascript
                                .httpOnly(false)
                                .secure(false) // Only send over HTTPS
                                .path("/")
                                .maxAge(24 * 60 * 60) // 1 day
                                .sameSite("Strict") // Protect against CSRF
                                .build();

                return ResponseEntity.status(HttpStatus.OK).header("Set-Cookie", cookie.toString()).body(responseDTO);

        }

        /**
         * Endpoint to log in a user.
         *
         * @param loginRequestDTO The login credentials provided in the request body.
         * @return A ResponseEntity containing a success message or error message.
         */
        @PostMapping("/login")
        public ResponseEntity<UserResponseDTO> loginUser(@Valid @RequestBody UserLoginDTO loginRequestDTO) {

                // Call the UserService to authenticate the user
                String token = userService.authenticateUser(loginRequestDTO.getUsername(),
                                loginRequestDTO.getPassword());

                // Create response DTO
                UserResponseDTO responseDTO = new UserResponseDTO("Login successful.", token);

                ResponseCookie cookie = ResponseCookie.from("token", token)
                                // Frontend uses jwt token for userInfo so it needs to be accessible by
                                // javascript
                                .httpOnly(false)
                                .secure(false) // Only send over HTTPS
                                .path("/")
                                .maxAge(24 * 60 * 60) // 1 day
                                .sameSite("Strict") // Protect against CSRF
                                .build();

                return ResponseEntity.status(HttpStatus.OK).header("Set-Cookie", cookie.toString()).body(responseDTO);

        }

        @PutMapping("/username")
        public ResponseEntity<String> changeUsername(@RequestParam String username,
                        @Valid @RequestBody ChangeUsernameDTO changeUsernameDTO) {
                String token = userService.changeUsername(username, changeUsernameDTO);

                ResponseCookie cookie = ResponseCookie.from("token", token)
                                // Frontend uses jwt token for userInfo so it needs to be accessible by
                                // javascript
                                .httpOnly(false)
                                .secure(false) // Only send over HTTPS
                                .path("/")
                                .maxAge(24 * 60 * 60) // 1 day
                                .sameSite("Strict") // Protect against CSRF
                                .build();

                return ResponseEntity.status(HttpStatus.OK).header("Set-Cookie", cookie.toString())
                                .body("Username changed successfully.");

        }

        @PutMapping("/password")
        public ResponseEntity<String> changePassword(@RequestParam String username,
                        @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
                String token = userService.changePassword(username, changePasswordDTO);

                ResponseCookie cookie = ResponseCookie.from("token", token)
                                // Frontend uses jwt token for userInfo so it needs to be accessible by
                                // javascript
                                .httpOnly(false)
                                .secure(false) // Only send over HTTPS
                                .path("/")
                                .maxAge(24 * 60 * 60) // 1 day
                                .sameSite("Strict") // Protect against CSRF
                                .build();
                return ResponseEntity.status(HttpStatus.OK).header("Set-Cookie", cookie.toString()).body("Password changed successfully.");

        }

        @PutMapping("/displayname")
        public ResponseEntity<String> changeDisplayName(@RequestParam String username,
                        @RequestBody ChangeDisplayNameDTO nameDTO) {
                String token = userService.changeDisplayName(username, nameDTO);
                ResponseCookie cookie = ResponseCookie.from("token", token)
                                // Frontend uses jwt token for userInfo so it needs to be accessible by
                                // javascript
                                .httpOnly(false)
                                .secure(false) // Only send over HTTPS
                                .path("/")
                                .maxAge(24 * 60 * 60) // 1 day
                                .sameSite("Strict") // Protect against CSRF
                                .build();
                return ResponseEntity.status(HttpStatus.OK).header("Set-Cookie", cookie.toString()).body("Display name changed successfully.");

        }

}
