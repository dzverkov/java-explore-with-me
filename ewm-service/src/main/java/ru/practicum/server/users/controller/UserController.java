package ru.practicum.server.users.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.users.dto.NewUserRequestDto;
import ru.practicum.server.users.dto.UserDto;
import ru.practicum.server.users.model.User;
import ru.practicum.server.users.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    /////////////////////////////////////
    // Admin: Пользователи
    // API для работы с пользователями
    /////////////////////////////////////

    @GetMapping
    public ResponseEntity<Object> getUsers(
            @RequestParam List<Long> ids,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос GET на получение всех пользователей.");

        List<User> userList = userService.getUsers(ids, from, size);
        List<UserDto> userDtoList = userList.stream().map(mapper::toUserDto).collect(Collectors.toList());
        return ResponseEntity.ok(userDtoList);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(
            @RequestBody @Valid NewUserRequestDto newUserRequestDto
    ) {
        log.info("Получен запрос POST на добавление нового пользователя : {}.", newUserRequestDto);
        UserDto newUserDto = mapper.toUserDto(
                userService.addUser(
                        mapper.toUser(newUserRequestDto)
                )
        );
        return ResponseEntity.ok(newUserDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(
            @PathVariable @Positive Long userId
    ) {
        log.info("Получен запрос DELETE на удаление пользователя с Id:{}.", userId);
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
