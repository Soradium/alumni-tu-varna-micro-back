package org.acme.kafka.consumers;

import org.acme.avro.front.AlumniFrontDto;
import org.acme.service.AlumniService;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AlumniConsumer {

    @Inject
    private AlumniService alumniService;
    
    private static final Logger logger = LoggerFactory.getLogger(AlumniConsumer.class);

    @Incoming("alumni-save")
    public void saveAlumni(AlumniFrontDto alumni) {
        try {
        alumniService.saveAlumni(alumni);
        } catch (Exception e) {
            logger.error("Unable to save alumni" + e);
        }
    }

    @Incoming("alumni-update")
    public void updateAlumni(AlumniFrontDto alumni) {
        try {
            alumniService.updateAlumni(alumni);
        } catch (Exception e) {
            logger.error("Unable to update alumni" + e);
        }
    }

    @Incoming("alumni-delete")
    public void deleteAlumni(AlumniFrontDto alumni) {
        try {
            alumniService.deleteAlumni(alumni.getFacultyNumber());
        } catch (Exception e) {
            logger.error("Unable to delete alumni" + e);
        }
    }

    // @Outgoing("alumni-get")
    // public AlumniDto getAlumni(Integer facultyNumber) throws Exception{
    //     try {
    //         return alumniService.getAlumniDtoByFacultyNumber(facultyNumber);
    //     } catch (Exception e) {
    //         logger.error("Unable to get alumni" + e);
    //         return null;
    //     }
    // }
}
