package es.demo.routeplanner.api;

import es.demo.routeplanner.dto.Travel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RoutePlannerApi {

    @RequestMapping(value = "/interconnections",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<List<Travel>> travelsGet(
              @RequestParam("departure") final String departure
            , @RequestParam("arrival") final String arrival
            , @RequestParam("departureDateTime") final String departureDateTime
            , @RequestParam("arrivalDateTime") final String arrivalDateTime
            , final HttpServletRequest httpServletRequest
    );
}
