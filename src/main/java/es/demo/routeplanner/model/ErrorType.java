package es.demo.routeplanner.model;

import java.util.Arrays;
import java.util.Optional;

public enum ErrorType {

    // Generic Errors
    BAD_REQUEST_ERROR
    , RESOURCE_NOT_FOUND_ERROR
    , INTERNAL_SERVER_ERROR
    , NOT_SUPPORTED_ERROR

    // Custom Errors
    , DEPARTURE_TIME_INVALID_FORMAT
    , ARRIVAL_TIME_INVALID_FORMAT
    , DEPARTURE_TIME_AFTER_ARRIVAL_TIME
    , DEPARTURE_NOT_IATA_CODE
    , ARRIVAL_NOT_IATA_CODE
    ;



    public static Optional<ErrorType> getErrorTypeByName(final String name) {
        return Arrays.stream(ErrorType.values()).filter(errorType -> name.contains(errorType.name())).findFirst();
    }

}