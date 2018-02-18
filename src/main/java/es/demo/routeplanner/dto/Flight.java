package es.demo.routeplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class Flight implements Serializable {

    private static final long serialVersionUID = 5746013279163402607L;

    @JsonProperty("number")
    private String number;

    @JsonProperty("departureTime")
    private String departureTime;

    @JsonProperty("arrivalTime")
    private String arrivalTime;


    public Flight() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public Flight number(final String number) {
        this.number = number;
        return this;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(final String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Flight arrivalTime(final String arrivalTime) {
        this.arrivalTime = arrivalTime;
        return this;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(final String departureTime) {
        this.departureTime = departureTime;
    }

    public Flight departureTime(final String departureTime) {
        this.departureTime = departureTime;
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
        final Flight flight = (Flight) o;
        return Objects.equals(this.number, flight.number) &&
                Objects.equals(this.arrivalTime, flight.arrivalTime) &&
                Objects.equals(this.departureTime, flight.departureTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, arrivalTime, departureTime);
    }

    @Override
    public String toString() {
        return"Flight {\n" +
                "    number: " + toIndentedString(number) + "\n" +
                "    arrivalTime: " + toIndentedString(arrivalTime) + "\n" +
                "    departureTime: " + toIndentedString(departureTime) + "\n" +
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