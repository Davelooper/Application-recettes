package com.davelooper.backend.services;

import java.util.Objects;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.davelooper.backend.dtos.LoginRequestDTO;
import com.davelooper.backend.dtos.LoginResponseDTO;
import com.davelooper.backend.dtos.RegisterRequestDTO;
import com.davelooper.backend.dtos.RegisterResponseDTO;
import com.davelooper.backend.entities.User;
import com.davelooper.backend.exceptions.EmailAlreadyUsedException;
import com.davelooper.backend.exceptions.InvalidCredentialsException;
import com.davelooper.backend.exceptions.InvalidRegistrationException;
import com.davelooper.backend.mappers.LoginMapper;
import com.davelooper.backend.mappers.RegisterMapper;
import com.davelooper.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final LoginMapper loginMapper;
  private final AccessTokenService accessTokenService;
  private final RegisterMapper registerMapper;

  public LoginResponseDTO login(LoginRequestDTO request) {
    User user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(() -> new InvalidCredentialsException("Email ou mot de passe incorrect."));

    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new InvalidCredentialsException("Email ou mot de passe incorrect.");
    }

    return loginMapper.toResponse(user, accessTokenService.generateToken(user));
  }

  @Transactional
  public RegisterResponseDTO register(RegisterRequestDTO request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new EmailAlreadyUsedException();
    }

    if (!Objects.equals(request.password(), request.passwordConfirm())) {
      throw new InvalidRegistrationException("Les deux mots de passe doivent correspondre.");
    }

    String passwordHash = passwordEncoder.encode(request.password());

    User user = new User();
    user.setEmail(request.email());
    user.setUsername(request.username());
    user.setPasswordHash(passwordHash);
    User userSaved = userRepository.save(user);

    return registerMapper.toResponse(userSaved);
  }

  // public RefreshResponseDTO refresh(RefreshRequestDTO request) {

  // // 1. Lire le refresh token envoyé par le client
  // String providedRefreshToken = request.refreshToken();

  // // 2. Retrouver le refresh token en base
  // RefreshToken storedRefreshToken = refreshTokenService
  // .findByToken(providedRefreshToken)
  // .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

  // // 3. Vérifier que le refresh token n'est pas révoqué
  // if (storedRefreshToken.isRevoked()) {
  // throw new UnauthorizedException("Refresh token has been revoked");
  // }

  // // 4. Vérifier que le refresh token n'est pas expiré
  // if (refreshTokenService.isExpired(storedRefreshToken)) {
  // throw new UnauthorizedException("Refresh token has expired");
  // }

  // // 5. Récupérer l'utilisateur associé au refresh token
  // User user = storedRefreshToken.getUser();

  // // 6. Générer un nouvel access token
  // String newAccessToken = accessTokenService.generateToken(user);

  // // 7. Générer un nouveau refresh token
  // RefreshToken newRefreshToken = refreshTokenService.create(user);

  // // 8. Révoquer l'ancien refresh token
  // refreshTokenService.revoke(storedRefreshToken);

  // // 9. Sauvegarder le nouveau refresh token
  // refreshTokenService.save(newRefreshToken);

  // // 10. Retourner les nouveaux tokens au client
  // return new RefreshResponseDTO(
  // newAccessToken,
  // newRefreshToken.getToken()
  // );
  // }

  // public void logout(LogoutRequestDTO request) {

  //   // 1. Lire le refresh token envoyé par le client
  //   String providedRefreshToken = request.refreshToken();

  //   // 2. Rechercher le refresh token en base
  //   RefreshToken storedRefreshToken = refreshTokenService
  //       .findByToken(providedRefreshToken)
  //       .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

  //   // 3. Vérifier s'il est déjà révoqué
  //   if (storedRefreshToken.isRevoked()) {
  //     throw new UnauthorizedException("Refresh token has already been revoked");
  //   }

  //   // 4. Vérifier s'il est expiré
  //   if (refreshTokenService.isExpired(storedRefreshToken)) {
  //     throw new UnauthorizedException("Refresh token has expired");
  //   }

  //   // 5. Révoquer le refresh token
  //   refreshTokenService.revoke(storedRefreshToken);

  //   // 6. Fin : aucun retour nécessaire
  // }

}
