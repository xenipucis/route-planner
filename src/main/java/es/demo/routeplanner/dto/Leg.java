package es.demo.routeplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class Leg implements Serializable {

    private static final long serialVersionUID = -9113075995959744747L;

    @JsonProperty("departureAirport")
    private String departureAirport;

    @JsonProperty("arrivalAirport")
    private String arrivalAirport;

    @JsonProperty("departureDateTime")
    private String departureDateTime;

    @JsonProperty("arrivalDateTime")
    private String arrivalDateTime;

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(final String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Leg departureAirport(final String departureAirport) {
        this.departureAirport = departureAirport;
        return this;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(final String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public Leg arrivalAirport(final String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
        return this;
    }

    public String getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(final String departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public Leg departureDateTime(final String departureDateTime) {
        this.departureDateTime = departureDateTime;
        return this;
    }

    public String getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(final String arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public Leg arrivalDateTime(final String arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
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
        final Leg leg = (Leg) o;
        return Objects.equals(this.departureAirport, leg.departureAirport) &&
                Objects.equals(this.arrivalAirport, leg.arrivalAirport) &&
                Objects.equals(this.departureDateTime, leg.departureDateTime) &&
                Objects.equals(this.arrivalDateTime, leg.arrivalDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departureAirport, arrivalAirport, departureDateTime, arrivalDateTime);
    }

    @Override
    public String toString() {
        return "Leg {\n" +
                "    departureAirport: " + toIndentedString(departureAirport) + "\n" +
                "    arrivalAirport: " + toIndentedString(arrivalAirport) + "\n" +
                "    departureDateTime: " + toIndentedString(departureDateTime) + "\n" +
                "    arrivalDateTime: " + toIndentedString(arrivalDateTime) + "\n" +
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
