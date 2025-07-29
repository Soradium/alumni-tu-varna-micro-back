package org.acme.kafka.consumers;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entites.Alumni;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class AlumniConsumer {

    @Incoming("alumni-data")
    public void consume(Alumni alumni) {
        System.out.println(alumni);
    }
}
