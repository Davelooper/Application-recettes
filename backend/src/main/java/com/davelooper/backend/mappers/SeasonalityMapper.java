package com.davelooper.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.davelooper.backend.dtos.SeasonalityResponseDTO;
import com.davelooper.backend.entities.Seasonality;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SeasonalityMapper {

  SeasonalityResponseDTO toResponse(Seasonality seasonality);
}
