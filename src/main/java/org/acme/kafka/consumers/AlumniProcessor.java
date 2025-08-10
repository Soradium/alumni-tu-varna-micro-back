package org.acme.kafka.consumers;

import org.acme.avro.back.AlumniDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniDetails;
import org.acme.service.AlumniDetailsService;
import org.acme.service.AlumniService;
import org.acme.util.mappers.AlumniMapper;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AlumniProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AlumniProcessor.class);
    @Inject
    private AlumniService alumniService;
    @Inject
    private AlumniMapper alumniMapper;
    @Inject
    private AlumniDetailsService alumniDetailsService;

    @Incoming("alumni-save-request")
    @Outgoing("alumni-save-response")
    public AlumniDto saveAlumni(AlumniFrontDto alumniDto) throws Exception {
        Alumni alumni = null;
        AlumniDetails details = null;
        try {
            alumni = alumniService.saveAlumni(alumniDto);
            details = alumniDetailsService.getDetailsForAlumni(alumni);
        } catch (Exception e) {
            logger.error("Unable to save alumni" + e);
            throw e;
        }
        return alumniMapper.toDto(alumni, details);
    }

    @Incoming("alumni-update-request")
    @Outgoing("alumni-update-response")
    public AlumniDto updateAlumni(AlumniFrontDto alumniDto) throws Exception {
        Alumni alumni = null;
        AlumniDetails details = null;
        try {
            // maybe make db directly return some
            // json-like formatted doc and just return it?
            alumni = alumniService.updateAlumni(alumniDto);
            details = alumniDetailsService.getDetailsForAlumni(alumni);
            // a suggestion for front end - when update window
            // is entered - fill up all the form field with data that
            // object was populated with during get request before. 
        } catch (Exception e) {
            logger.error("Unable to update alumni" + e);
            throw e;
        }
        return alumniMapper.toDto(alumni, details);
    }

    @Incoming("alumni-delete-request")
    @Outgoing("alumni-delete-response")
    public int deleteAlumni(AlumniFrontDto alumni) throws Exception {
        int fn = alumni.getFacultyNumber();
        try {
            alumniService.deleteAlumni(fn);
        } catch (Exception e) {
            logger.error("Unable to delete alumni" + e);
            throw e;
        }
        return fn; 
    }

    @Incoming("alumni-get-request")
    @Outgoing("alumni-get-response")
    public AlumniDto getAlumni(Integer facultyNumber) throws Exception{
        try {
            return alumniService.getAlumniDtoByFacultyNumber(facultyNumber);
        } catch (Exception e) {
            logger.error("Unable to get alumni" + e);
            throw e;
        }
    }
}
