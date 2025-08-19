package org.acme.kafka.consumers;


import java.util.List;
import java.util.concurrent.CompletionStage;

import org.acme.avro.back.AlumniGroupMembershipDto;
import org.acme.avro.front.AlumniGroupMembershipFrontDto;
import org.acme.service.group_service.AlumniGroupMembershipService;
import org.acme.util.CorrelationIdMetadata;
import org.acme.util.mappers.AlumniGroupCommonMapper;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AlumniGroupMembershipProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AlumniGroupMembershipProcessor.class);

    @Inject
    AlumniGroupMembershipService alumniGroupMembershipService;

    @Inject
    AlumniGroupCommonMapper alumniGroupMappingService;

    @Inject @Channel("alumni-group-membership-get-single") 
    Emitter<Message<AlumniGroupMembershipDto>> singleEmitter;

    @Inject @Channel("alumni-group-membership-get-list") 
    Emitter<Message<List<AlumniGroupMembershipDto>>> listEmitter;

    @Inject @Channel("alumni-group-membership-create-out") 
    Emitter<Message<AlumniGroupMembershipDto>> createEmitter;

    @Inject @Channel("alumni-group-membership-update-out") 
    Emitter<Message<AlumniGroupMembershipDto>> updateEmitter;

    @Inject @Channel("alumni-group-membership-delete-out") 
    Emitter<Message<String>> deleteEmitter;

    @Incoming("alumni-group-membership-get-in-id")
    public CompletionStage<Void> consumeGetById(Message<Integer> msg) throws Exception {
        Integer id = msg.getPayload();
        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();

        try {
            AlumniGroupMembershipDto dto = alumniGroupMappingService.toBackAlumniGroupMembershipDto(
                    alumniGroupMembershipService.getAlumniGroupsMembershipById(id)
            );
            return singleEmitter.send(Message.of(dto, metadata));
        } catch (Exception e) {
            logger.error("Error fetching AlumniGroupMembership by ID", e);
            throw e;
        }
    }

    @Incoming("alumni-group-membership-get-all")
    public CompletionStage<Void> consumeGetAll(Message<Void> msg) throws Exception {
        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();

        try {
            List<AlumniGroupMembershipDto> list = alumniGroupMembershipService.getAllAlumniGroupMembershipsDto();
            return listEmitter.send(Message.of(list, metadata));
        } catch (Exception e) {
            logger.error("Error fetching all AlumniGroupMemberships", e);
            throw e;
        }
    }

    @Incoming("alumni-group-membership-get-by-group")
    public CompletionStage<Void> consumeGetByGroup(Message<Integer> msg) throws Exception {
        int groupId = msg.getPayload();
        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();

        try {
            List<AlumniGroupMembershipDto> list = alumniGroupMembershipService.getAllAlumniGroupMembershipsByGroup(groupId);
            return listEmitter.send(Message.of(list, metadata));
        } catch (Exception e) {
            logger.error("Error fetching AlumniGroupMemberships by group", e);
            throw e;
        }
    }

    @Incoming("alumni-group-membership-get-by-faculty-number")
    public CompletionStage<Void> consumeGetByFacultyNumber(Message<Integer> msg) throws Exception {
        int facultyNumber = msg.getPayload();
        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();

        try {
            List<AlumniGroupMembershipDto> list = alumniGroupMembershipService.getAllAlumniGroupMembershipsByFacultyNumber(facultyNumber);
            return listEmitter.send(Message.of(list, metadata));
        } catch (Exception e) {
            logger.error("Error fetching AlumniGroupMemberships by faculty number", e);
            throw e;
        }
    }

    @Incoming("alumni-group-membership-create-in")
    public CompletionStage<Void> consumeCreate(Message<AlumniGroupMembershipFrontDto> msg) throws Exception {
        AlumniGroupMembershipFrontDto dto = msg.getPayload();
        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();

        try {
            AlumniGroupMembershipDto createdDto = alumniGroupMappingService.toBackAlumniGroupMembershipDto(
                    alumniGroupMembershipService.createAlumniGroupsMembership(dto)
            );
            return createEmitter.send(Message.of(createdDto, metadata));
        } catch (Exception e) {
            logger.error("Error creating AlumniGroupMembership", e);
            throw e;
        }
    }

    @Incoming("alumni-group-membership-update-in")
    public CompletionStage<Void> consumeUpdate(Message<AlumniGroupMembershipFrontDto> msg) throws Exception {
        AlumniGroupMembershipFrontDto dto = msg.getPayload();
        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();
        try {
            AlumniGroupMembershipDto updatedDto = alumniGroupMappingService.toBackAlumniGroupMembershipDto(
                    alumniGroupMembershipService.updateAlumniGroupsMembership(dto)
            );
            return updateEmitter.send(Message.of(updatedDto, metadata));
        } catch (Exception e) {
            logger.error("Error updating AlumniGroupMembership", e);
            throw e;
        }
    }

    @Incoming("alumni-group-membership-delete-in")
    public CompletionStage<Void> consumeDelete(Message<Integer> msg) throws Exception {
        Integer id = msg.getPayload();
        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();

        try {
            alumniGroupMembershipService.deleteAlumniGroupsMembership(id);
            return deleteEmitter.send(Message.of("Deleted ID: " + id, metadata));
        } catch (Exception e) {
            logger.error("Error deleting AlumniGroupMembership", e);
            throw e;
        }
    }
}
