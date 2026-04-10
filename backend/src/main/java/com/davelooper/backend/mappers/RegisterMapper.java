package com.davelooper.backend.mappers;

import com.davelooper.backend.dtos.RegisterRequestDTO;
import com.davelooper.backend.dtos.RegisterResponseDTO;
import com.davelooper.backend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RegisterMapper {

  // "Prends l'objet User et crée un RegisterRequestDTO avec"
  // Comme les noms de champs sont identiques (email, username...),
  // MapStruct fait le lien tout seul.
  RegisterResponseDTO toResponse(User user);

  // "Prends le DTO et crée une entité User"
  @Mappings({
    @Mapping(target = "id", ignore = true), // On ignore l'ID car c'est la DB qui le génère
    @Mapping(target = "passwordHash", ignore = true), // On l'ignore car le DTO a "password" (clair)
    @Mapping(target = "role", ignore = true), // Sécurité : on ne laisse pas l'user choisir son rôle
    @Mapping(target = "createdAt", ignore = true) // Géré par Hibernate (@CreationTimestamp)
  })
  User toEntity(RegisterRequestDTO request);
}
