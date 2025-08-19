package org.acme.service.group_service;

import java.util.List;

import org.acme.avro.ambiguous.AlumniGroupDtoSimplified;
import org.acme.avro.back.AlumniGroupBackDto;
import org.acme.avro.back.AlumniGroupMembershipDto;
import org.acme.avro.front.AlumniGroupMembershipFrontDto;
import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;

public interface AlumniGroupCommonMapperService {
    AlumniGroupMembershipDto toBackDto(AlumniGroupsMembership entity);

    List<AlumniGroupsMembership> toEntityList(List<AlumniGroupMembershipDto> membershipDtoList);

    List<AlumniGroupsMembership> toEntityFrontList(List<AlumniGroupMembershipFrontDto> membershipDtoList);

    AlumniGroupsMembership toEntity(AlumniGroupMembershipDto dto);

    AlumniGroupsMembership toEntityFront(AlumniGroupMembershipFrontDto dto);

    List<AlumniGroupMembershipDto> toBackDtos(List<AlumniGroupsMembership> memberships);

    List<Integer> extractMembershipIds(List<AlumniGroupsMembership> memberships);

    List<AlumniGroupsMembership> toBasicEntity(List<Integer> membershipIds);

    AlumniGroupBackDto toAlumniGroupDto(AlumniGroup entity);

    AlumniGroup toEntity(AlumniGroupBackDto dto);

    List<AlumniGroupBackDto> toDtoList(List<AlumniGroup> list);

    AlumniGroup toEntitySimplified(AlumniGroupDtoSimplified dto);
}
