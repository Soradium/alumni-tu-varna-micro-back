package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.sql.Timestamp;
import java.time.Instant;

@Mapper(componentModel = "cdi")
@ApplicationScoped
public abstract class DateMappingUtils {

    @Named(value = "mapTimestampToInstant")
    public static Instant mapTimestampToInstant(Timestamp ts) {
        return ts != null ? ts.toInstant() : null;
    }

    @Named("mapInstantToTimestamp")
    public static Timestamp mapInstantToTimestamp(Instant instant) {
        return instant != null ? Timestamp.from(instant) : null;
    }


}
