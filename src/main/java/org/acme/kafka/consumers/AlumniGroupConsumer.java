package org.acme.kafka.consumers;

import java.util.concurrent.CompletionStage;

import org.acme.avro.ambiguous.AlumniGroupDtoSimplified;
import org.acme.avro.back.AlumniGroupBackDto;
import org.acme.service.AlumniGroupService;
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
public class AlumniGroupConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AlumniGroupConsumer.class);
    @Inject
    private AlumniGroupService alumniGroupService;

    @Inject
    @Channel("alumni-group-data-out")
    Emitter<Message<AlumniGroupBackDto>> replyEmitter; //chanel for responses

    @Incoming("alumni-group-data-in")//chanel for requests
    public CompletionStage<Void> consume(Message<AlumniGroupDtoSimplified> alumni) { 
        AlumniGroupDtoSimplified request = alumni.getPayload(); // get data from message

        //implement business logic

        try {
                AlumniGroupBackDto alumniGroupDto = alumniGroupService.getAlumniGroupDtoById(request.getId()); //just stub

                String correlationId = alumni.getMetadata(CorrelationIdMetadata.class)
                        .map(CorrelationIdMetadata::getCorrelationId)
                        .orElse(null); //extract metadata from message

                Metadata metadata = correlationId != null
                        ? Metadata.of(new CorrelationIdMetadata(correlationId))
                        : Metadata.empty(); //save this id if we have it
                
                
                Message<AlumniGroupBackDto> replyMessage = Message.of(alumniGroupDto)
                        .withMetadata(metadata); //configure message
                return replyEmitter.send(replyMessage);//response
        } catch(Exception e) {
                logger.error("Error occured during consume method in AlumniGroupConsumer! ", e);
				return null; // TODO fix later!!!
        } 
    }
}
