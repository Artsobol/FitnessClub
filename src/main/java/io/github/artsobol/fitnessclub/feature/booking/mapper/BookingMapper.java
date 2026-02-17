package io.github.artsobol.fitnessclub.feature.booking.mapper;

import io.github.artsobol.fitnessclub.feature.booking.dto.BookingResponse;
import io.github.artsobol.fitnessclub.feature.booking.entity.Booking;
import io.github.artsobol.fitnessclub.feature.user.mapper.UserMapper;
import io.github.artsobol.fitnessclub.feature.workout.mapper.WorkoutMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {WorkoutMapper.class, UserMapper.class})
public interface BookingMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "workout", source = "workout")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "status", source = "status")
    BookingResponse toResponse(Booking entity);

}
