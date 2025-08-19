package org.acme.kafka.consumers;

import java.util.List;
import java.util.concurrent.CompletionStage;

import org.acme.avro.ambiguous.DegreeDto;
import org.acme.entites.Degree;
import org.acme.service.DegreeService;
import org.acme.util.CorrelationIdMetadata;
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
public class DegreeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DegreeProcessor.class);

    @Inject
    private DegreeService degreeService;

    @Inject @Channel("degree-get-list-out")
    Emitter<Message<List<DegreeDto>>> listEmitter;

    @Inject @Channel("degree-get-single-out")
    Emitter<Message<Degree>> singleEmitter;

    @Inject @Channel("degree-delete-out")
    Emitter<Message<String>> deleteEmitter;

    @Inject @Channel("degree-update-out")
    Emitter<Message<Degree>> updateEmitter;

    @Inject @Channel("degree-create-out")
    Emitter<Message<Degree>> createEmitter;

    @Incoming("degree-get-by-id-in")
    public CompletionStage<Void> consumeGetById(Message<Long> msg) throws Exception {
        Long id = msg.getPayload();
        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();
        try {
            Message<Degree> replyMsg = Message.of(degreeService.getDegreeById(id), metadata);
            return singleEmitter.send(replyMsg);
        } catch (Exception e) {
            logger.error("Exception occurred during fetching degree by ID", e);
            throw e;
        }
    }

    @Incoming("degree-get-by-name-in")
    public CompletionStage<Void> consumeGetByName(Message<String> msg) throws Exception {
        String name = msg.getPayload();
        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();
        try {
            Message<Degree> replyMsg = Message.of(degreeService.getDegreeByName(name), metadata);
            return singleEmitter.send(replyMsg);
        } catch (Exception e) {
            logger.error("Exception occurred during fetching degree by name", e);
            throw e;
        }
    }

    @Incoming("degree-get-list-in")
    public CompletionStage<Void> consumeGetAll(Message<Void> msg) throws Exception {
        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();
        try {
            Message<List<DegreeDto>> replyMsg = Message.of(degreeService.getAllDegrees(), metadata);
            return listEmitter.send(replyMsg);
        } catch (Exception e) {
            logger.error("Exception occurred during fetching all degrees", e);
            throw e;
        }
    }

    @Incoming("degree-create-in")
    public CompletionStage<Void> consumeCreate(Message<DegreeDto> msg) throws Exception {
        DegreeDto dto = msg.getPayload();
        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();
        try {
            Message<Degree> replyMsg = Message.of(degreeService.createDegree(dto), metadata);
            return createEmitter.send(replyMsg);
        } catch (Exception e) {
            logger.error("Exception occurred during creating degree", e);
            throw e;
        }
    }

    // Update Degree
    @Incoming("degree-update-in")
    public CompletionStage<Void> consumeUpdate(Message<DegreeDto> msg) throws Exception {
        DegreeDto dto = msg.getPayload();
        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();
        try {
            Message<Degree> replyMsg = Message.of(degreeService.updateDegree(dto), metadata);
            return updateEmitter.send(replyMsg);
        } catch (Exception e) {
            logger.error("Exception occurred during updating degree", e);
            throw e;
        }
    }

    @Incoming("degree-delete-in")
    public CompletionStage<Void> consumeDelete(Message<Long> msg) throws Exception {
        Long id = msg.getPayload();
        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();
        try {
            degreeService.deleteDegree(id);
            Message<String> replyMsg = Message.of("Degree deleted successfully", metadata);
            return deleteEmitter.send(replyMsg);
        } catch (Exception e) {
            logger.error("Exception occurred during deleting degree", e);
            throw e;
        }
    }
}
