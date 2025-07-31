package org.acme.kafka.consumers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.avro.AlumniGroupDto;
import org.acme.util.CorrelationIdMetadata;
import org.eclipse.microprofile.reactive.messaging.*;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class AlumniGroupConsumer {

    @Inject
    @Channel("alumni-group-data-out")
    Emitter<Message<AlumniGroupDto>> replyEmitter; //chanel for responses

    @Incoming("alumni-group-data-in")//chanel for requests
    public CompletionStage<Void> consume(Message<AlumniGroupDto> alumni) {
        AlumniGroupDto request = alumni.getPayload(); // get data from message

        //implement business logic

        AlumniGroupDto alumniGroupDto = new AlumniGroupDto(); //just stub

        String correlationId = alumni.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null); //extract metadata from message

        Metadata metadata = correlationId != null
                ? Metadata.of(new CorrelationIdMetadata(correlationId))
                : Metadata.empty(); //save this id if we have it

        Message<AlumniGroupDto> replyMessage = Message.of(alumniGroupDto).
                withMetadata(metadata);//configure message
        return replyEmitter.send(replyMessage);//response
    }
}
