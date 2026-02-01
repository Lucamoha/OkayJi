package com.okayji.identity.service.impl;

import com.okayji.enums.Gender;
import com.okayji.identity.dto.request.UserCreationRequest;
import com.okayji.identity.dto.response.UserResponse;
import com.okayji.identity.entity.Profile;
import com.okayji.identity.entity.Role;
import com.okayji.identity.entity.User;
import com.okayji.enums.UserRole;
import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.mapper.UserMapper;
import com.okayji.identity.repository.RoleRepository;
import com.okayji.identity.repository.UserRepository;
import com.okayji.identity.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@AllArgsConstructor
@Slf4j(topic = "USER-SERVICE")
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Override
    public UserResponse findById(String id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND)));
    }

    @Override
    @Transactional
    public UserResponse create(UserCreationRequest userCreationRequest) {
        log.info("User creation request: username={}", userCreationRequest.getUsername());

        User user = userMapper.toUser(userCreationRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(UserRole.USER).ifPresent(roles::add);
        user.setRoles(roles);

        Profile profile = Profile.builder()
                .user(user)
                .birthday(userCreationRequest.getBirthday())
                .fullName(userCreationRequest.getFullName())
                .gender(userCreationRequest.getGender())
                .build();
        user.setProfile(profile);

        user = userRepository.saveAndFlush(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public void delete(String userId) {

    }
}
