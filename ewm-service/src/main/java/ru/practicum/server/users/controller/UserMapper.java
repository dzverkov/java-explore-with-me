package ru.practicum.server.users.controller;

import org.mapstruct.Mapper;
import ru.practicum.server.users.dto.NewUserRequestDto;
import ru.practicum.server.users.dto.UserDto;
import ru.practicum.server.users.model.User;

@Mapper
public interface UserMapper {

    User toUser(NewUserRequestDto userDto);

    UserDto toUserDto(User user);
}
