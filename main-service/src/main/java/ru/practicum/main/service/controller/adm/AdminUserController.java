package ru.practicum.main.service.controller.adm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.NewUserRequest;
import ru.practicum.main.service.service.adm.AdminUserService;

import static ru.practicum.main.service.utils.PageRequestGetter.getPageRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<Object> findUsers(@RequestParam(required = false) long[] ids,
                                            @RequestParam(required = false, defaultValue = "0") int from,
                                            @RequestParam(required = false, defaultValue = "10") int size) {
        if (ids == null) {
            return adminUserService.findAllUsers(getPageRequest(from, size));
        }
        return adminUserService.findUsers(ids);
    }

    @PostMapping
    public ResponseEntity<Object> createNewUser(@Valid @RequestBody NewUserRequest userRequest) {
        return adminUserService.createNewUser(userRequest);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable long userId) {
        return adminUserService.deleteUser(userId);
    }
}
