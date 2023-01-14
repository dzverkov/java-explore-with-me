package ru.practicum.server.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.server.exception.ConflictException;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.users.model.User;
import ru.practicum.server.users.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getUsers(List<Long> ids, Integer from, Integer size) {
        Integer page = from / size;
        Pageable pageable = PageRequest.of(page, size);

        return userRepository.findAllByIdIn(ids, pageable).getContent();
    }

    @Override
    public User addUser(User newUser) {

        Optional<User> user = userRepository.findUserByEmail(newUser.getEmail());

        if (user.isPresent()) {
            throw new ConflictException("Такой пользователь уже существует, email: " + newUser.getEmail());
        }
        return userRepository.save(newUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
    }
}
