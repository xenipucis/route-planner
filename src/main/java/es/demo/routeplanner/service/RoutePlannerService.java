package es.demo.routeplanner.service;

import es.demo.routeplanner.dto.*;
import es.demo.routeplanner.logic.AlgorithmComponent;
import es.demo.routeplanner.logic.ClientRestServices;
import es.demo.routeplanner.logic.ErrorManager;
import es.demo.routeplanner.model.CustomException;
import es.demo.routeplanner.model.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class RoutePlannerService {

    private final AlgorithmComponent algorithmComponent;
    private final ErrorManager errorManager;

    private final static Logger LOGGER = LoggerFactory.getLogger(RoutePlannerService.class);

    public RoutePlannerService(final AlgorithmComponent algorithmComponent
                            , final ErrorManager errorManager) {
        this.algorithmComponent = algorithmComponent;
        this.errorManager = errorManager;
    }

    public LocalDateTime getLocalDateTimeFromString(final String ldtString, final ErrorType errorType) {
        final LocalDateTime ldt;

        try {
            ldt = LocalDateTime.parse(ldtString);
        } catch (DateTimeParseException dateTimeParseException) {
            LOGGER.error("Exception when parsing {}; exception message = {}", ldtString, dateTimeParseException.getMessage());
            throw new CustomException(errorManager.getError(errorType));
        }

        return ldt;
    }

    public List<Travel> computeTravels(final String departure
                            , final String arrival
                            , final LocalDateTime departureTime
                            , final LocalDateTime arrivalTime
                            , final Integer minimumDifferenceInHours
                            , final Integer limitPerPage) {
        LOGGER.info("Compute Travels: departure = {}, arrival = {}, departureTime = {}, arrivalTime = {}, minimumDifferenceInHours = {}, limitPerPage = {}"
            , departure, arrival, departureTime, arrivalTime, minimumDifferenceInHours, limitPerPage);

        LOGGER.debug("Building Map with all possible routes, according to: {} call.", ClientRestServices.ROUTES_URI);
        final Map<String, Integer> infoPerAirportIataCodesOfConnectionNode = algorithmComponent.computeInfoPerAirportIataCodesOfConnectionNode(departure, arrival);
        LOGGER.debug("Success in Building Map with all possible routes, according to: {} call. Map is {}", ClientRestServices.ROUTES_URI, infoPerAirportIataCodesOfConnectionNode);

        return algorithmComponent.computeTravels(
                departure,
                arrival,
                departureTime,
                arrivalTime,
                minimumDifferenceInHours,
                limitPerPage,
                infoPerAirportIataCodesOfConnectionNode);
    }
}
