package ru.practicum.main.service.controller.adm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.NewUserRequest;
import ru.practicum.main.service.service.AdminService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<Object> findUsers(@RequestParam(required = false) long[] ids,
                                            @RequestParam(required = false, defaultValue = "0") int from,
                                            @RequestParam(required = false, defaultValue = "10") int to) {
        if (from < 0) {
            throw new IllegalArgumentException("Parameter 'from' can't be negative");
        }
        if (to <= 0) {
            throw new IllegalArgumentException("Parameter 'to' can't be negative or zero");
        }
        if (ids == null) {
            return adminService.findAllUsers(from, to);
        }
        return adminService.findUsers(ids);
    }

    @PostMapping
    public ResponseEntity<Object> createNewUser(@Valid @RequestBody NewUserRequest userRequest) {
        return adminService.createNewUser(userRequest);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable long userId) {
        return adminService.deleteUser(userId);
    }
}