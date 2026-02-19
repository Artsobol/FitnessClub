package io.github.artsobol.fitnessclub.feature.workout.mapper;

import io.github.artsobol.fitnessclub.feature.trainer.mapper.TrainerMapper;
import io.github.artsobol.fitnessclub.feature.workout.dto.request.WorkoutRequest;
import io.github.artsobol.fitnessclub.feature.workout.dto.response.WorkoutResponse;
import io.github.artsobol.fitnessclub.feature.workout.dto.request.WorkoutUpdateRequest;
import io.github.artsobol.fitnessclub.feature.workout.entity.Workout;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = TrainerMapper.class)
public interface WorkoutMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "startsAt", source = "startsAt")
    @Mapping(target = "endsAt", source = "endsAt")
    @Mapping(target = "capacity", source = "capacity")
    Workout toEntity(WorkoutRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "startsAt", source = "startsAt")
    @Mapping(target = "endsAt", source = "endsAt")
    @Mapping(target = "capacity", source = "capacity")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "trainer", source = "trainerProfile")
    WorkoutResponse toResponse(Workout entity);

    @BeanMapping(
            ignoreByDefault = true,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    void update(
            WorkoutUpdateRequest request,
            @MappingTarget Workout workout
    );

}
