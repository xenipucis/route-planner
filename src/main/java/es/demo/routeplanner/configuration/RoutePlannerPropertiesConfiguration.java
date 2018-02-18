package es.demo.routeplanner.configuration;

import es.demo.routeplanner.logic.ErrorManager;
import es.demo.routeplanner.model.CustomException;
import es.demo.routeplanner.model.ErrorType;
import org.apache.logging.slf4j.Log4jLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@Configuration
@ConfigurationProperties(prefix = "routePlanner")
public class RoutePlannerPropertiesConfiguration {

    private static final String X_LIMIT_RECORDS_PER_PAGE = "X-LIMIT-RECORDS-PER-PAGE";
    private static final String X_MIN_DIFF_IN_HOURS_ARRIVAL_NEXT_DEPARTURE = "X-MIN-DIFF-IN-HOURS-ARRIVAL-NEXT-DEPARTURE";

    private static final String FILE_NAME = "iata.tzmap";
    private static final String TAB = "\t";

    private static final Logger LOGGER = LoggerFactory.getLogger(RoutePlannerPropertiesConfiguration.class);

    private static Map<String, TimeZone> timezonePerAirportMap = new HashMap<>();

    private Integer limitRecordsPerPage;

    private Integer minimumDifferenceInHoursBetweenArrivalAndNextDeparture;

    private final ErrorManager errorManager;

    @Autowired
    public RoutePlannerPropertiesConfiguration(final ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    @PostConstruct
    public void initRoutePlannerPropertiesConfiguration() {
        LOGGER.debug("Reading timezones per IATA airport codes from resource file = {}", FILE_NAME);
        try (BufferedReader br = new BufferedReader(new FileReader(new ClassPathResource(FILE_NAME).getFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(TAB);
                timezonePerAirportMap.put(tokens[0], TimeZone.getTimeZone(tokens[1]));
            }
        } catch(IOException iOException) {
            LOGGER.error("Reading timezones per IATA airport codes from resource file = {}; EXCEPTION = {}"
                    , FILE_NAME
                    , iOException.getMessage()
                    , iOException);
        }
        LOGGER.debug("Successfully reading timezones per IATA airport codes from resource file = {}", FILE_NAME);
    }

    public Map<String, TimeZone> getTimezonePerAirportMapUnmodifiable() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(timezonePerAirportMap));
    }

    public Set<String> getAllAirportIATACodes() {
        return Collections.unmodifiableSet(new HashSet<>(timezonePerAirportMap.keySet()) );
    }



    public Integer getLimitRecordsPerPage() {
        return limitRecordsPerPage;
    }

    public void setLimitRecordsPerPage(Integer limitRecordsPerPage) {
        this.limitRecordsPerPage = limitRecordsPerPage;
    }

    public Integer getMinimumDifferenceInHoursBetweenArrivalAndNextDeparture() {
        return minimumDifferenceInHoursBetweenArrivalAndNextDeparture;
    }

    public void setMinimumDifferenceInHoursBetweenArrivalAndNextDeparture(Integer minimumDifferenceInHoursBetweenArrivalAndNextDeparture) {
        this.minimumDifferenceInHoursBetweenArrivalAndNextDeparture = minimumDifferenceInHoursBetweenArrivalAndNextDeparture;
    }

    public Integer getComputedMinimumDifferenceInHours(final HttpServletRequest httpServletRequest) {
        Integer minimumDifferenceInHours = getMinimumDifferenceInHoursBetweenArrivalAndNextDeparture();
        final String minimumDifferenceInHoursValueHeader = httpServletRequest.getHeader(X_MIN_DIFF_IN_HOURS_ARRIVAL_NEXT_DEPARTURE);
        if (!StringUtils.isEmpty(minimumDifferenceInHoursValueHeader)) {
            try {
                minimumDifferenceInHours = Integer.parseInt(minimumDifferenceInHoursValueHeader);
            } catch (NumberFormatException nfe) {
                LOGGER.error("Atempt to parse minimumDifferenceInHoursValueHeader = {} as Integer failed. " +
                                "minimumDifferenceInHours will e used with its old value: minimumDifferenceInHours = {}."
                        , minimumDifferenceInHoursValueHeader
                        , minimumDifferenceInHours);
            }
        }
        return minimumDifferenceInHours;
    }

    public Integer getComputedLimitRecordsPerPage(final HttpServletRequest httpServletRequest) {
        Integer limitRecordsPerPage = getLimitRecordsPerPage();
        final String limitRecordsPerPageValueHeader = httpServletRequest.getHeader(X_LIMIT_RECORDS_PER_PAGE);
        if (!StringUtils.isEmpty(limitRecordsPerPageValueHeader)) {
            try {
                limitRecordsPerPage = Integer.parseInt(limitRecordsPerPageValueHeader);
            } catch (NumberFormatException nfe) {
                LOGGER.error("Atempt to parse limitRecordsPerPageValueHeader = {} as Integer failed. " +
                                "limitRecordsPerPage will e used with its old value: limitRecordsPerPage = {}."
                        , limitRecordsPerPageValueHeader
                        , limitRecordsPerPage);
            }
        }
        return limitRecordsPerPage;
    }

    public LocalDateTime computeUtcLocalTime(final String iataCode, final LocalDateTime localDateTime) {
        final Map<String, TimeZone> timezonePerAirportUnmodifiableMap = getTimezonePerAirportMapUnmodifiable();
        final TimeZone timezone = timezonePerAirportUnmodifiableMap.get(iataCode);

        return localDateTime.atZone(ZoneId.of(timezone.getID())).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }
}
