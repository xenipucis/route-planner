package es.demo.routeplanner.api.impl;

import es.demo.routeplanner.api.RoutePlannerApi;
import es.demo.routeplanner.dto.Travel;
import es.demo.routeplanner.logic.ErrorManager;
import es.demo.routeplanner.model.CustomException;
import es.demo.routeplanner.model.ErrorType;
import es.demo.routeplanner.service.RoutePlannerService;
import es.demo.routeplanner.configuration.RoutePlannerPropertiesConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class RoutePlannerController implements RoutePlannerApi {

    private final RoutePlannerService routePlannerService;

    private final RoutePlannerPropertiesConfiguration routePlannerPropertiesConfiguration;

    private final ErrorManager errorManager;

    private final static Logger LOGGER = LoggerFactory.getLogger(RoutePlannerController.class);

    @Autowired
    public RoutePlannerController( final RoutePlannerService routePlannerService
                                , final RoutePlannerPropertiesConfiguration routePlannerPropertiesConfiguration
                                , final ErrorManager errorManager) {

        this.routePlannerService = routePlannerService;
        this.routePlannerPropertiesConfiguration = routePlannerPropertiesConfiguration;
        this.errorManager = errorManager;
    }

    @Override
    public ResponseEntity<List<Travel>> travelsGet(@RequestParam("departure") final String departure
                                                 , @RequestParam("arrival") final String arrival
                                                 , @RequestParam("departureDateTime") final String departureDateTime
                                                 , @RequestParam("arrivalDateTime") final String arrivalDateTime
                                                 , final HttpServletRequest httpServletRequest) {

        LOGGER.info("Get Travels: departure = {}, arrival = {}, departureDateTime = {}, arrivalDateTime = {} ", departure, arrival, departureDateTime, arrivalDateTime);
        final Integer limitRecordsPerPage = routePlannerPropertiesConfiguration.getComputedLimitRecordsPerPage(httpServletRequest);
        final Integer minimumDifferenceInHours = routePlannerPropertiesConfiguration.getComputedMinimumDifferenceInHours(httpServletRequest);



        final LocalDateTime departureTime = routePlannerService.getLocalDateTimeFromString(departureDateTime, ErrorType.DEPARTURE_TIME_INVALID_FORMAT);
        final LocalDateTime arrivalTime = routePlannerService.getLocalDateTimeFromString(arrivalDateTime, ErrorType.ARRIVAL_TIME_INVALID_FORMAT);

        if (departureTime.isAfter(arrivalTime)) {
            throw new CustomException(errorManager.getError(ErrorType.DEPARTURE_TIME_AFTER_ARRIVAL_TIME));
        }

        final Set<String> allIataAirportsCodes = routePlannerPropertiesConfiguration.getAllAirportIATACodes();
        if (!allIataAirportsCodes.contains(departure)) {
            throw new CustomException(errorManager.getError(ErrorType.DEPARTURE_NOT_IATA_CODE));
        }

        if (!allIataAirportsCodes.contains(arrival)) {
            throw new CustomException(errorManager.getError(ErrorType.ARRIVAL_NOT_IATA_CODE));
        }

        final List<Travel> travels = routePlannerService.computeTravels(departure, arrival, departureTime, arrivalTime, minimumDifferenceInHours, limitRecordsPerPage);

        LOGGER.info("Received Travels: size is {}", travels.size());
        return new ResponseEntity<>(travels, HttpStatus.PARTIAL_CONTENT);
    }
}
