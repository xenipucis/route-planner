package es.demo.routeplanner.logic;

import es.demo.routeplanner.dto.DailySchedule;
import es.demo.routeplanner.dto.Flight;
import es.demo.routeplanner.dto.Leg;
import es.demo.routeplanner.dto.Travel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class AlgorithmHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(AlgorithmHelper.class);

    public IntStream resolveRangeInUnitOfTimeChild(final int currentInUnitOfTimeParent
            , final int departureInUnitOfTimeParent
            , final int arrivalInUnitOfTimeParent
            , final int departureInUnitOfTimeChild
            , final int arrivalInUnitOfTimeChild
            , final Integer[] startEnd) {
        if (departureInUnitOfTimeParent == arrivalInUnitOfTimeParent) {
            return IntStream.rangeClosed(departureInUnitOfTimeChild, arrivalInUnitOfTimeChild);
        } else if (currentInUnitOfTimeParent == departureInUnitOfTimeParent) {
            return IntStream.rangeClosed(departureInUnitOfTimeChild, startEnd[1]);
        } else if (currentInUnitOfTimeParent == arrivalInUnitOfTimeParent) {
            return IntStream.rangeClosed(startEnd[0], arrivalInUnitOfTimeChild);
        } else {
            return IntStream.rangeClosed(startEnd[0], startEnd[1]);
        }
    }

    public Integer[] getFirstAndLastMonthsAsIntValues() {
        return Arrays.asList(YearMonth.of(0, Month.JANUARY).getMonthValue(), YearMonth.of(0, Month.DECEMBER).getMonthValue()).toArray(new Integer[0]);
    }

    public Set<Travel> extractTravelsPerDailySchedule(final int currentYear
            , final int currentMonth
            , final DailySchedule dailySchedule
            , final String departure
            , final String arrival
            , final LocalDateTime departureTime
            , final LocalDateTime arrivalTime) {
        final int currentDay = dailySchedule.getDay();

        LOGGER.debug("currentYear = {}, currentMonth = {}, departure = {}, arrival = {}, departureTime = {}, arrivalTime = {}"
            , currentYear
            , currentMonth
            , departure
            , arrival
            , departureTime
            , arrivalTime);

        final Predicate<Flight> allMatchingPredicate =
                flight -> {
                    final LocalDate currentLocalDate = LocalDate.of(currentYear, currentMonth, currentDay);
                    final LocalDateTime flightDepartureLocalDateTime = LocalDateTime.of(currentLocalDate, LocalTime.parse(flight.getDepartureTime()));
                    final LocalDateTime flightArrivalLocalDateTime = LocalDateTime.of(currentLocalDate, LocalTime.parse(flight.getArrivalTime()));
                    return ((flightDepartureLocalDateTime.isAfter(departureTime) || flightDepartureLocalDateTime.isEqual(departureTime))
                            && (flightArrivalLocalDateTime.isBefore(arrivalTime) || flightArrivalLocalDateTime.isEqual(arrivalTime)));

                };
        final Function<Flight, Travel> flightToTravelConverter =
                flight -> {
                    final LocalDate currentLocalDate = LocalDate.of(currentYear, currentMonth, currentDay);
                    final LocalDateTime flightDepartureLocalDateTime = LocalDateTime.of(currentLocalDate, LocalTime.parse(flight.getDepartureTime()));
                    final LocalDateTime flightArrivalLocalDateTime = LocalDateTime.of(currentLocalDate, LocalTime.parse(flight.getArrivalTime()));
                    return new Travel()
                            .legs(
                                    Collections.singletonList(
                                            new Leg()
                                                    .departureAirport(departure)
                                                    .departureDateTime(flightDepartureLocalDateTime.toString())
                                                    .arrivalAirport(arrival)
                                                    .arrivalDateTime(flightArrivalLocalDateTime.toString())));
                };
        return dailySchedule.getFlights().stream().filter(allMatchingPredicate).map(flightToTravelConverter).collect(Collectors.toSet());
    }
}
