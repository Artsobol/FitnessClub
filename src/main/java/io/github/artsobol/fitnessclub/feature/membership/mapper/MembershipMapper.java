package io.github.artsobol.fitnessclub.feature.membership.mapper;

import io.github.artsobol.fitnessclub.feature.membership.dto.MembershipCreateRequest;
import io.github.artsobol.fitnessclub.feature.membership.dto.MembershipResponse;
import io.github.artsobol.fitnessclub.feature.membership.dto.MembershipUpdateRequest;
import io.github.artsobol.fitnessclub.feature.membership.entity.Membership;
import io.github.artsobol.fitnessclub.feature.user.mapper.UserMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface MembershipMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "startsAt", source = "startsAt")
    @Mapping(target = "endsAt", source = "endsAt")
    Membership toEntity(MembershipCreateRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "startsAt", source = "startsAt")
    @Mapping(target = "endsAt", source = "endsAt")
    MembershipResponse toResponse(Membership entity);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "startsAt", source = "startsAt")
    @Mapping(target = "endsAt", source = "endsAt")
    void update(@MappingTarget Membership entity, MembershipUpdateRequest request);
}
