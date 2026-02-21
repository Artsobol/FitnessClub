package io.github.artsobol.fitnessclub.feature.trainer.mapper;

import io.github.artsobol.fitnessclub.feature.trainer.dto.TrainerResponse;
import io.github.artsobol.fitnessclub.feature.trainer.entity.TrainerProfile;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.mapper.TrainerSpecializationMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TrainerSpecializationMapper.class)
public interface TrainerMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "specialization", source = "specialization")
    TrainerResponse toResponse(TrainerProfile entity);
}
