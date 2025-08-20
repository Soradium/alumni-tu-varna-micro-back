package org.acme.util.mappers;

import java.sql.Timestamp;
import java.time.Instant;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
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
