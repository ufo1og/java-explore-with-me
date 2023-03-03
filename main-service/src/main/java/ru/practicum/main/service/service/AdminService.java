package ru.practicum.main.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.main.service.dto.NewUserRequest;
import ru.practicum.main.service.dto.UserDto;
import ru.practicum.main.service.model.User;
import ru.practicum.main.service.repository.UserRepository;
import ru.practicum.main.service.utils.UserConverter;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    public ResponseEntity<Object> createNewUser(NewUserRequest userRequest) {
        User createdUser = UserConverter.toUser(userRequest);
        createdUser = userRepository.save(createdUser);
        UserDto userDto = UserConverter.toUserDto(createdUser);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> deleteUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(String.format("User with id = %s not found", userId));
        }
        userRepository.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<Object> findAllUsers(int from, int to) {
        List<User> foundUsers = userRepository.findAll(PageRequest.of(from, to)).toList();
        return convertUsersToResponseEntity(foundUsers);
    }

    public ResponseEntity<Object> findUsers(long[] ids) {
        List<User> foundUsers = userRepository.findByIdIn(ids);
        return convertUsersToResponseEntity(foundUsers);
    }

    private ResponseEntity<Object> convertUsersToResponseEntity(List<User> foundUsers) {
        if (foundUsers.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
        List<UserDto> userDtos = foundUsers.stream().map(UserConverter::toUserDto).collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }
}
