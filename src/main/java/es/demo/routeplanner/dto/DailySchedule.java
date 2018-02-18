package es.demo.routeplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class DailySchedule implements Serializable {

    private static final long serialVersionUID = 5746013279163402607L;

    @JsonProperty("day")
    private Integer day;

    @JsonProperty("flights")
    private List<Flight> flights;

    public DailySchedule() {
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(final Integer day) {
        this.day = day;
    }

    public DailySchedule day(final Integer day) {
        this.day = day;
        return this;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(final List<Flight> flights) {
        this.flights = flights;
    }

    public DailySchedule flights(final List<Flight> flights) {
        this.flights = flights;
        return this;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DailySchedule dailySchedule = (DailySchedule) o;
        return Objects.equals(this.day, dailySchedule.day) &&
                Objects.equals(this.flights, dailySchedule.flights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, flights);
    }

    @Override
    public String toString() {
        return "DailySchedule {\n" +
                "    day: " + toIndentedString(day) + "\n" +
                "    flights: " + toIndentedString(flights) + "\n" +
                "}";
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
