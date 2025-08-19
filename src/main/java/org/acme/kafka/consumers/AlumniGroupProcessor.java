package org.acme.kafka.consumers;

import java.util.List;
import java.util.concurrent.CompletionStage;

import org.acme.avro.ambiguous.AlumniGroupDtoSimplified;
import org.acme.avro.back.AlumniGroupBackDto;
import org.acme.service.group_service.AlumniGroupService;
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
public class AlumniGroupProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AlumniGroupProcessor.class);

    @Inject
    private AlumniGroupService alumniGroupService;

    @Inject
    @Channel("alumni-group-get-list")
    Emitter<Message<List<AlumniGroupBackDto>>> listGroupEmitter;
    
    @Inject
    @Channel("alumni-group-get-single")
    Emitter<Message<AlumniGroupBackDto>> singleGroupEmitter;

    @Inject
    @Channel("alumni-group-delete-out")
    Emitter<Message<String>> deleteEmitter;

    @Inject
    @Channel("alumni-group-update-out")
    Emitter<Message<AlumniGroupBackDto>> updateEmitter;

    @Inject
    @Channel("alumni-group-create-out")
    Emitter<Message<AlumniGroupBackDto>> createEmitter;

    @Incoming("alumni-group-get-in-id")
    public CompletionStage<Void> consumeGetById(Message<AlumniGroupDtoSimplified> msg) throws Exception {
        AlumniGroupDtoSimplified group = msg.getPayload();

        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        
        Metadata metadata = correlationId != null 
                ? Metadata.of(new CorrelationIdMetadata(correlationId))
                : Metadata.empty();

        try {
                Message<AlumniGroupBackDto> replyMsg = Message.of(
                        alumniGroupService.getAlumniGroupDtoById(group.getId()),
                        metadata
                );
                return singleGroupEmitter.send(replyMsg);
        } catch (Exception e) {
                logger.error("Exception occured during creation of reply message", e);
                throw e;
        }
    }

    @Incoming("alumni-group-create-in")
    public CompletionStage<Void> consumeCreate(Message<AlumniGroupDtoSimplified> msg) throws Exception {
        AlumniGroupDtoSimplified dto = msg.getPayload();
        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();

        try {
            AlumniGroupBackDto created = alumniGroupService.getAlumniGroupDtoById(
                    alumniGroupService.createAlumniGroup(dto).getId()
            );
            return createEmitter.send(Message.of(created, metadata));
        } catch (Exception e) {
            logger.error("Exception occurred during create", e);
            throw e;
        }
    }

    @Incoming("alumni-group-update-in")
    public CompletionStage<Void> consumeUpdate(Message<AlumniGroupDtoSimplified> msg) throws Exception {
        AlumniGroupDtoSimplified dto = msg.getPayload();
        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();

        try {
            AlumniGroupBackDto updated = alumniGroupService.getAlumniGroupDtoById(
                    alumniGroupService.updateAlumniGroup(dto).getId()
            );
            return updateEmitter.send(Message.of(updated, metadata));
        } catch (Exception e) {
            logger.error("Exception occurred during update", e);
            throw e;
        }
    }

    @Incoming("alumni-group-delete-in")
    public CompletionStage<Void> consumeDelete(Message<AlumniGroupDtoSimplified> msg) throws Exception {
        AlumniGroupDtoSimplified dto = msg.getPayload();
        String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null);
        Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();

        try {
            alumniGroupService.deleteAlumniGroup(dto);
            return deleteEmitter.send(Message.of("Deleted group with ID: " + dto.getId(), metadata));
        } catch (Exception e) {
            logger.error("Exception occurred during delete", e);
            throw e;
        }
    }

	@Incoming("alumni-group-get-by-faculty-in")
	public CompletionStage<Void> consumeGetAllByFaculty(Message<String> msg) throws Exception {
	String faculty = msg.getPayload();
	String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
			.map(CorrelationIdMetadata::getCorrelationId)
			.orElse(null);
	Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();

	try {
			List<AlumniGroupBackDto> groups = alumniGroupService.getAllAlumniGroupsDtoByFaculty(faculty);
			return listGroupEmitter.send(Message.of(groups, metadata));
	} catch (Exception e) {
			logger.error("Exception occurred during getAllByFaculty", e);
			throw e;
		}
	}

	@Incoming("alumni-group-get-by-speciality-in")
	public CompletionStage<Void> consumeGetAllBySpeciality(Message<String> msg) throws Exception {
		String speciality = msg.getPayload();
		String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
				.map(CorrelationIdMetadata::getCorrelationId)
				.orElse(null);
		Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();

		try {
			List<AlumniGroupBackDto> groups = alumniGroupService.getAllAlumniGroupsDtoBySpeciality(speciality);
			return listGroupEmitter.send(Message.of(groups, metadata));
		} catch (Exception e) {
			logger.error("Exception occurred during getAllBySpeciality", e);
			throw e;
		}
	}

	@Incoming("alumni-group-get-by-graduation-year-in")
	public CompletionStage<Void> consumeGetAllByGraduationYear(Message<Integer> msg) throws Exception {
		int graduationYear = msg.getPayload();
		String correlationId = msg.getMetadata(CorrelationIdMetadata.class)
				.map(CorrelationIdMetadata::getCorrelationId)
				.orElse(null);
		Metadata metadata = correlationId != null ? Metadata.of(new CorrelationIdMetadata(correlationId)) : Metadata.empty();

		try {
			List<AlumniGroupBackDto> groups = alumniGroupService.getAllAlumniGroupsDtoByGraduationYear(graduationYear);
			return listGroupEmitter.send(Message.of(groups, metadata));
		} catch (Exception e) {
			logger.error("Exception occurred during getAllByGraduationYear", e);
			throw e;
		}
	}







//     @Inject
//     @Channel("alumni-group-data-out")
//     Emitter<Message<AlumniGroupBackDto>> replyEmitter; //chanel for responses
//     //TODO create functions for all functions; create topics for each function in service, i/o topics

// @Incoming("alumni-group-dto-in")

//     @Incoming("alumni-group-")//chanel for requests
//     public CompletionStage<Void> consume(Message<AlumniGroupDtoSimplified> alumni) { 
//         AlumniGroupDtoSimplified request = alumni.getPayload(); // get data from message
		
//         //implement business logic

//         try {
//                 AlumniGroupBackDto alumniGroupDto = alumniGroupService.getAlumniGroupDtoById(request.getId()); //just stub

//                 String correlationId = alumni.getMetadata(CorrelationIdMetadata.class)
//                         .map(CorrelationIdMetadata::getCorrelationId)
//                         .orElse(null); //extract metadata from message

//                 Metadata metadata = correlationId != null
//                         ? Metadata.of(new CorrelationIdMetadata(correlationId))
//                         : Metadata.empty(); //save this id if we have it
                
                
//                 Message<AlumniGroupBackDto> replyMessage = Message.of(alumniGroupDto)
//                         .withMetadata(metadata); //configure message
//                 return replyEmitter.send(replyMessage);//response
//         } catch(Exception e) {
//                 logger.error("Error occured during consume method in AlumniGroupConsumer! ", e);
//         } 
//     }

    
}
