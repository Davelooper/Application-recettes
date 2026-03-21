package com.davelooper.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import com.davelooper.backend.dtos.LoginRequestDTO;
import com.davelooper.backend.dtos.LoginResponseDTO;
import com.davelooper.backend.entities.User;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LoginMapper {

  LoginResponseDTO toResponse(User user, String token);

  @Mappings({@Mapping(target = "id", ignore = true),
      @Mapping(target = "passwordHash", ignore = true),
      @Mapping(target = "username", ignore = true), @Mapping(target = "role", ignore = true),
      @Mapping(target = "createdAt", ignore = true),})
  User toEntity(LoginRequestDTO request);
}
