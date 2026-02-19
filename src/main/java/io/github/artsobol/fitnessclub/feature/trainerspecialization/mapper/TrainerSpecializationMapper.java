package io.github.artsobol.fitnessclub.feature.trainerspecialization.mapper;

import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.request.TrainerSpecializationCreateRequest;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.response.TrainerSpecializationResponse;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.request.TrainerSpecializationUpdateRequest;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.entity.TrainerSpecialization;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TrainerSpecializationMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    TrainerSpecializationResponse toResponse(TrainerSpecialization entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title")
    TrainerSpecialization toEntity(TrainerSpecializationCreateRequest request);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget TrainerSpecialization entity, TrainerSpecializationUpdateRequest request);
}
