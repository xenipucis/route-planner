package es.demo.routeplanner.logic;

import es.demo.routeplanner.configuration.RoutePlannerPropertiesConfiguration;
import es.demo.routeplanner.dto.*;
import es.demo.routeplanner.model.InfoFlagType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class AlgorithmComponent {

    private final ClientRestServices clientRestServices;
    private final AlgorithmHelper algorithmHelper;
    private final RoutePlannerPropertiesConfiguration routePlannerPropertiesConfiguration;

    private final static Logger LOGGER = LoggerFactory.getLogger(AlgorithmComponent.class);

    @Autowired
    public AlgorithmComponent(final ClientRestServices clientRestServices
                            , final AlgorithmHelper algorithmHelper
                            , final RoutePlannerPropertiesConfiguration routePlannerPropertiesConfiguration) {
        this.clientRestServices = clientRestServices;
        this.algorithmHelper = algorithmHelper;
        this.routePlannerPropertiesConfiguration = routePlannerPropertiesConfiguration;
    }

    public Map<String, Integer> computeInfoPerAirportIataCodesOfConnectionNode(final String airportFrom, final String airportTo) {
        LOGGER.debug("airportFrom = {}, airportTo = {}", airportFrom, airportTo);
        final Predicate<Route> noCountryAirportPredicate = route -> route != null && route.getConnectingAirport() == null;
        final Predicate<Route> airportFromEqualityPredicate = route -> route != null && airportFrom.equals(route.getAirportFrom());
        final Predicate<Route> airportToEqualityPredicate = route -> route != null && airportTo.equals(route.getAirportTo());
        final Set<Route> possibleRoutes = clientRestServices.callRoutesService()
                .stream()
                .filter(noCountryAirportPredicate
                        .and(
                                airportFromEqualityPredicate
                                        .or(airportToEqualityPredicate)
                        )
                )
                .collect(Collectors.toSet());
        LOGGER.debug("Initial possible routes size: {}", possibleRoutes.size());

        final Map<String, Integer> infoPerAirportCodesOfConnectionNode = new HashMap<>();

        for (Route route : possibleRoutes) {
            if (airportFrom.equals(route.getAirportFrom()) && airportTo.equals(route.getAirportTo())) {
                infoPerAirportCodesOfConnectionNode.put(airportFrom, InfoFlagType.FROM_EQUAL_TO.getValue());
            } else if (airportFrom.equals(route.getAirportFrom())) {
                final String airportToCode = route.getAirportTo();
                final Integer routesInfo = infoPerAirportCodesOfConnectionNode.get(airportToCode);
                final Integer valueToUpdate = InfoFlagType.TO.getValue();
                infoPerAirportCodesOfConnectionNode.put(airportToCode, routesInfo == null ? valueToUpdate : routesInfo + valueToUpdate);
            } else if (airportTo.equals(route.getAirportTo())) {
                final String airportFromCode = route.getAirportFrom();
                final Integer routesInfo = infoPerAirportCodesOfConnectionNode.get(airportFromCode);
                final Integer valueToUpdate = InfoFlagType.FROM.getValue();
                infoPerAirportCodesOfConnectionNode.put(airportFromCode, routesInfo == null ? valueToUpdate : routesInfo + valueToUpdate);
            }
        }

        LOGGER.debug("Possible routes size after first filter (keys in map): {}", infoPerAirportCodesOfConnectionNode.keySet().size());
        final int valueForRoutesWithFromTo = InfoFlagType.generateValue(Arrays.asList(InfoFlagType.FROM, InfoFlagType.TO));
        final List<String> connectionNodes =
                infoPerAirportCodesOfConnectionNode
                .keySet()
                .stream()
                .filter(connection -> infoPerAirportCodesOfConnectionNode.get(connection) == valueForRoutesWithFromTo)
                .collect(Collectors.toList());
        LOGGER.debug("Final Possible routes size after second filter (keys in map with value = {}): {}"
                , valueForRoutesWithFromTo
                , connectionNodes.size());
        LOGGER.debug("Found those connection nodes : {}", connectionNodes);

        return infoPerAirportCodesOfConnectionNode;
    }

    public List<Travel> computeTravels(final String departure
            , final String arrival
            , final LocalDateTime departureTime
            , final LocalDateTime arrivalTime
            , final Integer minimumDifferenceInHours
            , final Integer limitPerPage
            , final Map<String, Integer> infoPerAirportCodesOfConnectionNode) {

        LOGGER.debug("departure = {}, arrival = {}, departureTime = {}, arrivalTime = {}, minimumDifferenceInHours = {}, limitPerPage = {} "
                , departure
                , arrival
                , departureTime
                , arrivalTime
                , minimumDifferenceInHours
                , limitPerPage);

        final List<Travel> result = new ArrayList<>();

        computeTravelsWithDirectFlights(
                result
                , limitPerPage
                , departure
                , arrival
                , departureTime
                , arrivalTime
                , infoPerAirportCodesOfConnectionNode);

        computeTravelsWithOneStop(
                result
                , limitPerPage
                , departure
                , arrival
                , departureTime
                , arrivalTime
                , minimumDifferenceInHours
                , infoPerAirportCodesOfConnectionNode);

        LOGGER.debug("Original Travels size = {}, limitPerPage = {}", result.size(), limitPerPage);
        return result.subList(0, limitPerPage);
    }

    private void computeTravelsWithDirectFlights(
            final List<Travel> travels
            , final int limitPerPage
            , final String departure
            , final String arrival
            , final LocalDateTime departureTime
            , final LocalDateTime arrivalTime
            , final Map<String, Integer> infoPerAirportCodesOfConnectionNode
    ) {
        LOGGER.debug("travels.size = {}, departure = {}, arrival = {}, departureTime = {}, arrivalTime = {}, limitPerPage = {}"
                , travels.size()
                , departure
                , arrival
                , departureTime
                , arrivalTime
                , limitPerPage);

        boolean directFlightsExist = infoPerAirportCodesOfConnectionNode.values().stream().anyMatch(InfoFlagType::fromAndToAreEqual);
        LOGGER.debug("Direct Flights between departure = {} and arrival = {} exist: {}", departure, arrival, directFlightsExist);

        if (directFlightsExist) {
            final Set<Travel> singleTravels = computeSingleTravels(
                    departure
                    , arrival
                    , departureTime
                    , arrivalTime
            );
            LOGGER.debug("Found matching travels: {}", singleTravels);
            travels.addAll(new ArrayList<>(singleTravels).subList(0, singleTravels.size() < limitPerPage ? singleTravels.size() : limitPerPage));
        }
    }

    private void computeTravelsWithOneStop(
            final List<Travel> travels
            , final int limitPerPage
            , final String departure
            , final String arrival
            , final LocalDateTime departureTime
            , final LocalDateTime arrivalTime
            , final Integer minimumDifferenceInHours
            , final Map<String, Integer> infoPerAirportCodesOfConnectionNode) {
        LOGGER.debug("limitPerPage = {}, departure = {}, arrival = {}, departureTime = {}, arrivalTime = {}, minimumDifferenceInHours = {}"
            , limitPerPage
            , departure
            , arrival
            , departureTime
            , arrivalTime
            , minimumDifferenceInHours);
        int currentNumberOfMatchedTravels = 0;
        for (String connectionAirportCode : infoPerAirportCodesOfConnectionNode.keySet()) {
            if (currentNumberOfMatchedTravels >= limitPerPage) {
                break;
            }

            final Integer routesInfo = infoPerAirportCodesOfConnectionNode.get(connectionAirportCode);
            if (routesInfo != null) {
                if (InfoFlagType.containsBothFromAndTo(routesInfo)) {
                    LOGGER.debug("Processing connection = {}.", connectionAirportCode);
                    final Set<Travel> firstSegmentTravelsSet = computeSingleTravels(
                            departure
                            , connectionAirportCode
                            , departureTime
                            , arrivalTime
                    );

                    final Map<Travel, List<Travel>> twoTravelMap =
                            computeTwoTravelMapWithFirstTravel(firstSegmentTravelsSet);

                    Set<Travel> secondSegmentTravels = new HashSet<>();

                    if (currentNumberOfMatchedTravels < limitPerPage) {
                        secondSegmentTravels = computeSingleTravels(
                                connectionAirportCode
                                , arrival
                                , departureTime
                                , arrivalTime
                        );
                    }

                    if (!secondSegmentTravels.isEmpty()) {
                        for (Travel travel : firstSegmentTravelsSet) {
                            if (currentNumberOfMatchedTravels >= limitPerPage) {
                                LOGGER.debug("Pagination limit reached: limitPerPage = {}, but currentNumberOfMatchedTravels = {}"
                                        , limitPerPage
                                        , currentNumberOfMatchedTravels);
                                break;
                            }

                            final String arrivalLocalDateTimeAtConnectionString = travel.getLegs().get(0).getArrivalDateTime();
                            final LocalDateTime arrivalLocalDateTimeAtConnection = LocalDateTime.parse(arrivalLocalDateTimeAtConnectionString);
                            LOGGER.debug("arrivalLocalDateTimeAtConnection = {}", arrivalLocalDateTimeAtConnection);

                            final LocalDateTime arrivalLocalDateTimeAtConnectionPlusMinimumHours = arrivalLocalDateTimeAtConnection.plusHours(minimumDifferenceInHours);
                            LOGGER.debug("arrivalLocalDateTimeAtConnectionPlusMinimumHours = {}", arrivalLocalDateTimeAtConnectionPlusMinimumHours);

                            final Set<Travel> secondSegmentTravelsFiltered =
                                    secondSegmentTravels
                                            .stream()
                                            .filter(trv -> {
                                                final Leg leg = trv.getLegs().get(0);
                                                return LocalDateTime.parse(leg.getDepartureDateTime()).isAfter(arrivalLocalDateTimeAtConnectionPlusMinimumHours);
                                            })
                                            .collect(Collectors.toSet());

                            final List<Travel> trvls = twoTravelMap.get(travel);
                            trvls.addAll(secondSegmentTravelsFiltered);
                            currentNumberOfMatchedTravels += secondSegmentTravelsFiltered.size();
                        }
                    }

                    for (Travel firstTravel : twoTravelMap.keySet()) {
                        final List<Travel> secondTravels = twoTravelMap.get(firstTravel);
                        for (Travel secondTravel : secondTravels) {
                            travels.add(firstTravel.createTwoSegmentTravel(firstTravel, secondTravel));

                            if (travels.size() > limitPerPage) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private Set<Travel> computeSingleTravels(
            final String departure
            , final String arrival
            , final LocalDateTime departureTime
            , final LocalDateTime arrivalTime
    ) {
        LOGGER.debug("departure = {}, arrival = {}, departureTime = {}, arrivalTime = {}", departure, arrival, departureTime, arrivalTime);

        final Set<Travel> result = new HashSet<>();

        final LocalDateTime departureTimeUtc = routePlannerPropertiesConfiguration.computeUtcLocalTime(departure, departureTime);
        final LocalDateTime arrivalTimeUtc = routePlannerPropertiesConfiguration.computeUtcLocalTime(arrival, arrivalTime);

        final int departureYearAfterUtcTimeAlign = departureTimeUtc.getYear();
        final int arrivalYearAfterUtcTimeAlign = arrivalTimeUtc.getYear();
        final int departureMonthOfYearAfterUtcTimeAlign = departureTimeUtc.getMonthValue();
        final int arrivalMonthOfYearAfterUtcTimeAlign = arrivalTimeUtc.getMonthValue();
        final int departureDayOfMonthAfterUtcTimeAlign = departureTimeUtc.getDayOfMonth();
        final int arrivalDayOfMonthAfterUtcTimeAlign = arrivalTimeUtc.getDayOfMonth();

        LOGGER.debug("departure = {}, departureTime = {}, departureTimeUtc = {}, departureYearAfterUtcTimeAlign = {}, " +
                "departureMonthOfYearAfterUtcTimeAlign = {}, departureDayOfMonthAfterUtcTimeAlign = {}"
                , departure
                , departureTime
                , departureTimeUtc
                , departureYearAfterUtcTimeAlign
                , departureMonthOfYearAfterUtcTimeAlign
                , departureDayOfMonthAfterUtcTimeAlign);

        LOGGER.debug("arrival = {}, arrivalTime = {}, arrivalTimeUtc = {}, arrivalYearAfterUtcTimeAlign = {}, " +
                        "arrivalMonthOfYearAfterUtcTimeAlign = {}, arrivalDayOfMonthAfterUtcTimeAlign = {}"
                , arrival
                , arrivalTime
                , arrivalTimeUtc
                , arrivalYearAfterUtcTimeAlign
                , arrivalMonthOfYearAfterUtcTimeAlign
                , arrivalDayOfMonthAfterUtcTimeAlign);

        for (int currentYear = departureYearAfterUtcTimeAlign; currentYear <= arrivalYearAfterUtcTimeAlign; currentYear++) {

            final int[] startEndMonthsRangeAsArray = algorithmHelper.resolveRangeInUnitOfTimeChild(
                    currentYear
                    , departureYearAfterUtcTimeAlign
                    , arrivalYearAfterUtcTimeAlign
                    , departureMonthOfYearAfterUtcTimeAlign
                    , arrivalMonthOfYearAfterUtcTimeAlign
                    , algorithmHelper.getFirstAndLastMonthsAsIntValues()).toArray();

            LOGGER.debug("ranges of months within current year = {}", startEndMonthsRangeAsArray);

            for (int currentMonth : startEndMonthsRangeAsArray) {
                final MonthlySchedule monthlySchedule = clientRestServices.callScheduleService(departure, arrival, String.valueOf(departureYearAfterUtcTimeAlign), String.valueOf(departureMonthOfYearAfterUtcTimeAlign));
                final Predicate<DailySchedule> monthConstraintsPredicate = day -> {
                    final int dayValue = day.getDay();
                    return (departureMonthOfYearAfterUtcTimeAlign == arrivalMonthOfYearAfterUtcTimeAlign && dayValue >= departureDayOfMonthAfterUtcTimeAlign && dayValue <= arrivalDayOfMonthAfterUtcTimeAlign)
                            || (currentMonth == departureMonthOfYearAfterUtcTimeAlign && dayValue >= departureDayOfMonthAfterUtcTimeAlign)
                            || (currentMonth == arrivalMonthOfYearAfterUtcTimeAlign && dayValue <= arrivalDayOfMonthAfterUtcTimeAlign);
                };

                final List<DailySchedule> originalDailySchedules = monthlySchedule.getDays();
                LOGGER.debug("Original dailySchedules size = {}", originalDailySchedules.size());
                final List<DailySchedule> dailySchedules = originalDailySchedules.stream().filter(monthConstraintsPredicate).collect(Collectors.toList());
                LOGGER.debug("Final dailySchedules size = {}", dailySchedules.size());


                for (DailySchedule dailySchedule : dailySchedules) {
                    final Set<Travel> travels = algorithmHelper.extractTravelsPerDailySchedule(
                            currentYear
                            , currentMonth
                            , dailySchedule
                            , departure
                            , arrival
                            , departureTime
                            , arrivalTime);

                    result.addAll(travels);
                }
            }
        }

        return result;
    }



    private Map<Travel, List<Travel>> computeTwoTravelMapWithFirstTravel(final Set<Travel> travels) {

        final Map<Travel, List<Travel>> result = new HashMap<>();
        travels.forEach(travel -> result.put(travel, new ArrayList<>()));

        return result;
    }
}
