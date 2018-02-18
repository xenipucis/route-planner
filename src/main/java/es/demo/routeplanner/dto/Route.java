package es.demo.routeplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class Route implements Serializable {

    private static final long serialVersionUID = 2742010078663407918L;

    @JsonProperty("airportFrom")
    private String airportFrom;

    @JsonProperty("airportTo")
    private String airportTo;

    @JsonProperty("connectingAirport")
    private String connectingAirport;

    @JsonProperty("newRoute")
    private Boolean newRoute;

    @JsonProperty("seasonalRoute")
    private Boolean seasonalRoute;

    @JsonProperty("group")
    private String group;

    public Route() {}

    public String getAirportFrom() {
        return airportFrom;
    }

    public void setAirportFrom(final String airportFrom) {
        this.airportFrom = airportFrom;
    }

    public Route airportFrom(final String airportFrom) {
        this.airportFrom = airportFrom;
        return this;
    }

    public String getAirportTo() {
        return airportTo;
    }

    public void setAirportTo(final String airportTo) {
        this.airportTo = airportTo;
    }

    public Route airportTo(final String airportTo) {
        this.airportTo = airportTo;
        return this;
    }

    public String getConnectingAirport() {
        return connectingAirport;
    }

    public void setConnectingAirport(String connectingAirport) {
        this.connectingAirport = connectingAirport;
    }

    public Boolean getNewRoute() {
        return newRoute;
    }

    public void isNewRoute(final Boolean newRoute) {
        this.newRoute = newRoute;
    }

    public Route newRoute(final Boolean newRoute) {
        this.newRoute = newRoute;
        return this;
    }

    public Boolean isSeasonalRoute() {
        return seasonalRoute;
    }

    public void setSeasonalRoute(final Boolean seasonalRoute) {
        this.seasonalRoute = seasonalRoute;
    }

    public Route seasonalRoute(final Boolean seasonalRoute) {
        this.seasonalRoute = seasonalRoute;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(final String group) {
        this.group = group;
    }

    public Route group(final String group) {
        this.group = group;
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
        final Route route = (Route) o;
        return Objects.equals(this.airportFrom, route.airportFrom) &&
                Objects.equals(this.airportTo, route.airportTo) &&
                Objects.equals(this.connectingAirport, route.connectingAirport) &&
                Objects.equals(this.newRoute, route.newRoute) &&
                Objects.equals(this.seasonalRoute, route.seasonalRoute) &&
                Objects.equals(this.group, route.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airportFrom, airportTo, connectingAirport, newRoute, seasonalRoute, group);
    }

    @Override
    public String toString() {
        return "Route {\n" +
                "    airportFrom: " + toIndentedString(airportFrom) + "\n" +
                "    airportTo: " + toIndentedString(airportTo) + "\n" +
                "    connectingAirport: " + toIndentedString(connectingAirport) + "\n" +
                "    newRoute: " + toIndentedString(newRoute) + "\n" +
                "    seasonalRoute: " + toIndentedString(seasonalRoute) + "\n" +
                "    group: " + toIndentedString(group) + "\n" +
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
