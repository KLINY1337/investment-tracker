package com.fintracker.backend.fintrackermonolith.auth_server.module.user.api;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;
import com.fintracker.backend.fintrackermonolith.auth_server.db.repository.UserRepository;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.request.CreateUserRequest;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.request.GetUsersByIdsRequest;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.request.UpdateUserByIdRequest;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response.CreateUserResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response.DeleteUsersByIdsResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response.GetUsersResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response.UpdateUserByIdResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.model.service.UserService;
import com.fintracker.backend.fintrackermonolith.auth_server.util.AccessTokenUtils;
import com.fintracker.backend.fintrackermonolith.core.util.RequestParamUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://5.23.48.222:8080")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        CreateUserResponse response = userService.createUser(request.username(), request.email(), request.password());

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<GetUsersResponse> getUsersByIds(@Valid @RequestParam String idList) {
        GetUsersResponse response = userService.getUsersByIds(
                RequestParamUtils.parseParamAsList(idList, ",", Long::valueOf)
        );

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    ResponseEntity<UpdateUserByIdResponse> updateUserById(@RequestHeader("Authorization") String authHeader, @RequestBody UpdateUserByIdRequest request) {
        Optional<User> user = userRepository.findUserByUsernameOrEmail(AccessTokenUtils.getUsernameFromToken(authHeader.substring(7)));
        UpdateUserByIdResponse response = userService.updateUserById(user.get().getId(), request.username(), request.email(), request.password());

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    ResponseEntity<DeleteUsersByIdsResponse> deleteUsersByIds(@RequestParam String idList) {
        DeleteUsersByIdsResponse response = userService.deleteUsersByIds(
                RequestParamUtils.parseParamAsList(idList, ",", Long::valueOf)
        );

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/info")
    ResponseEntity<User> getUserByAccessToken(@RequestHeader("Authorization") String authHeader) {
        Optional<User> user = userRepository.findUserByUsernameOrEmail(AccessTokenUtils.getUsernameFromToken(authHeader.substring(7)));
        return ResponseEntity.ofNullable(user.orElse(null));
    }
}
