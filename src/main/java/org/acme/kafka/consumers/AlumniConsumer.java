package org.acme.kafka.consumers;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.avro.AlumniDto;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class AlumniConsumer {

    @Incoming("alumni-data")
    public void consume(AlumniDto alumni) {
        System.out.println(alumni);
    }
}
