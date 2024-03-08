package com.fintracker.backend.fintrackermonolith.auth_server.module.user.model.service;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;
import com.fintracker.backend.fintrackermonolith.auth_server.db.repository.UserRepository;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response.CreateUserResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response.DeleteUsersByIdsResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response.GetUsersResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response.UpdateUserByIdResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.model.exception.UserAlreadyExistsException;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.model.exception.UserIdsNotFoundException;
import com.fintracker.backend.fintrackermonolith.auth_server.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public CreateUserResponse createUser(String username, String email, String password) {
        if (userRepository.existsByUsernameOrEmail(username, email)) {
            throw new UserAlreadyExistsException("Specified username or email is already occupied");
        }
        User user = User.builder()
                .username(username)
                .email(email)
                .password(PasswordUtils.encodePassword(password))
                .build();

        return new CreateUserResponse(
                true,
                "User created successfully",
                userRepository.save(user)
        );
    }

    public GetUsersResponse getUsersByIds(List<Long> ids) {
        Map<Boolean, List<Long>> processableAndUnprocessableIds = ids.stream()
                .collect(Collectors.partitioningBy(userRepository::existsById));

        if (!processableAndUnprocessableIds.get(false).isEmpty()) {
            throw new UserIdsNotFoundException("Specified idList contains values, that don't exist in database", processableAndUnprocessableIds.get(false));
        }

        return new GetUsersResponse(
                true,
                "Specified users retrieved from database",
                userRepository.findAllById(processableAndUnprocessableIds.get(true))
        );
    }

    public UpdateUserByIdResponse updateUserById(Long id, String username, String email, String password) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new UserIdsNotFoundException("Specified user does not exist in database", List.of(id)));

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(PasswordUtils.encodePassword(password));

        return new UpdateUserByIdResponse(
                true,
                "User successfully updated",
                user
        );
    }

    public DeleteUsersByIdsResponse deleteUsersByIds(List<Long> ids) {
        Map<Boolean, List<Long>> processableAndUnprocessableIds = ids.stream()
                .collect(Collectors.partitioningBy(userRepository::existsById));

        if (!processableAndUnprocessableIds.get(false).isEmpty()) {
            throw new UserIdsNotFoundException("Specified idList contains values, that don't exist in database", processableAndUnprocessableIds.get(false));
        }

        userRepository.deleteAllById(processableAndUnprocessableIds.get(true));
        return new DeleteUsersByIdsResponse(
                true,
                "Specified users deleted from database",
                processableAndUnprocessableIds.get(true).size()
        );
    }

    public User getUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findUserByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Specified user does not exist"));
    }
}
