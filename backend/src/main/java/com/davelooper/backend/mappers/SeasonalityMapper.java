package com.davelooper.backend.mappers;

import com.davelooper.backend.dtos.SeasonalityResponseDTO;
import com.davelooper.backend.entities.Seasonality;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SeasonalityMapper {

  SeasonalityResponseDTO toResponse(Seasonality seasonality);
}
