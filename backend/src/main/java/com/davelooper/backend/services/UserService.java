package com.davelooper.backend.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.davelooper.backend.dtos.LoginRequestDTO;
import com.davelooper.backend.dtos.LoginResponseDTO;
import com.davelooper.backend.dtos.RegisterRequestDTO;
import com.davelooper.backend.dtos.RegisterResponseDTO;
import com.davelooper.backend.entities.User;
import com.davelooper.backend.mappers.LoginMapper;
import com.davelooper.backend.mappers.RegisterMapper;
import com.davelooper.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final RegisterMapper registerMapper;
  private final LoginMapper loginMapper;
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


  public LoginResponseDTO login(LoginRequestDTO request) {
    User user = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new IllegalArgumentException("Email ou mot de passe incorrect."));

    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new IllegalArgumentException("Email ou mot de passe incorrect.");
    }

    return loginMapper.toResponse(user, "token");
  }

}
