package com.fintracker.backend.fintrackermonolith.auth_server.module.user.api;

import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.request.CreateUserRequest;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.request.GetUsersByIdsRequest;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.request.UpdateUserByIdRequest;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response.CreateUserResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response.DeleteUsersByIdsResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response.GetUsersResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response.UpdateUserByIdResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.model.service.UserService;
import com.fintracker.backend.fintrackermonolith.core.util.RequestParamUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
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
    ResponseEntity<UpdateUserByIdResponse> updateUserById(@RequestParam Long id, @RequestBody UpdateUserByIdRequest request) {
        UpdateUserByIdResponse response = userService.updateUserById(id, request.username(), request.email(), request.password());

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
}
