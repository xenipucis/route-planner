package es.demo.routeplanner.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.demo.routeplanner.dto.MonthlySchedule;
import es.demo.routeplanner.dto.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.List;

@Component
public class ClientRestServices {

    public final static String ROUTES_URI = "https://api.ryanair.com/core/3/routes";
    private final static String MONTHLY_SCHEDULE_URI = "https://api.ryanair.com/timetable/3/schedules/{0}/{1}/years/{2}/months/{3}";

    private RestTemplate restTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger("ClientRestServices.class");

    @Autowired
    public ClientRestServices(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retryable(
            value = { HttpClientErrorException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 5000))
    public List<Route> callRoutesService() {
        LOGGER.debug("Start call to {}.", ROUTES_URI);
        final ResponseEntity<List> response = restTemplate.getForEntity(ROUTES_URI, List.class);

        LOGGER.debug("Call to {} was completed successfully.", ROUTES_URI);
        return new ObjectMapper().convertValue(response
                .getBody(), new TypeReference<List<Route>>() {
        });

    }

    @Retryable(
            value = { HttpClientErrorException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 5000))
    public MonthlySchedule callScheduleService(final String departure, final String arrival, final String year, final String month) {
        final String monthlyScheduleComputedUri = MessageFormat.format(MONTHLY_SCHEDULE_URI, departure, arrival, year, month);
        LOGGER.debug("Start call to {}.", monthlyScheduleComputedUri);
        final ResponseEntity<MonthlySchedule> response = restTemplate.getForEntity(monthlyScheduleComputedUri, MonthlySchedule.class);
        LOGGER.debug("Call to {} was completed successfully.", monthlyScheduleComputedUri);
        return response.getBody();
    }
}
