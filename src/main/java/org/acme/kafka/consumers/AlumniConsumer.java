package org.acme.kafka.consumers;

import org.acme.avro.back.AlumniDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.service.AlumniService;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AlumniConsumer {

    @Inject
    private AlumniService alumniService;

    @Incoming("alumni-save")
    public void saveAlumni(AlumniFrontDto alumni) {
        // alumniService.createAlumni(alumni);
    }

    @Incoming("alumni-update")
    public void updateAlumni(AlumniFrontDto alumni) {

    }

    @Incoming("alumni-delete")
    public void deleteAlumni(AlumniFrontDto alumni) {

    }

    @Outgoing("alumni-get")
    public AlumniDto getAlumni(Integer facultyNumber) throws Exception{
        return alumniService.convertAlumniToDto(alumniService.getAlumniByFacultyNumber(facultyNumber));
    }
}
