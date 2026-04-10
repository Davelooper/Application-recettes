package com.davelooper.backend.services;

import com.davelooper.backend.dtos.RegisterRequestDTO;
import com.davelooper.backend.dtos.RegisterResponseDTO;
import com.davelooper.backend.entities.User;
import com.davelooper.backend.mappers.RegisterMapper;
import com.davelooper.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final RegisterMapper registerMapper;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public RegisterResponseDTO register(RegisterRequestDTO request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("Cet email est déjà utilisé.");
    }

    User user = registerMapper.toEntity(request);

    String encodedPassword = passwordEncoder.encode(request.password());
    user.setPasswordHash(encodedPassword);

    User savedUser = userRepository.save(user);

    return registerMapper.toResponse(savedUser);
  }
}
