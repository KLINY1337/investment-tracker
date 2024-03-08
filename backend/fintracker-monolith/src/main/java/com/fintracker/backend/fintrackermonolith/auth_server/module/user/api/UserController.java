package com.fintracker.backend.fintrackermonolith.auth_server.module.user.api;

import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.request.CreateUserRequest;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.request.GetUsersByIdsRequest;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.request.UpdateUserByIdRequest;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response.CreateUserResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response.DeleteUsersByIdsResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response.GetUsersResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response.UpdateUserByIdResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.model.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
    public ResponseEntity<GetUsersResponse> getUsersByIds(@Valid @RequestParam List<Long> idList) {
        GetUsersResponse response = userService.getUsersByIds(idList);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    ResponseEntity<UpdateUserByIdResponse> updateUserById(@RequestParam("id") Long id, UpdateUserByIdRequest request) {
        UpdateUserByIdResponse response = userService.updateUserById(id, request.username(), request.email(), request.password());

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    ResponseEntity<DeleteUsersByIdsResponse> deleteUsersByIds(@RequestParam List<Long> idList) {
        DeleteUsersByIdsResponse response = userService.deleteUsersByIds(idList);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
