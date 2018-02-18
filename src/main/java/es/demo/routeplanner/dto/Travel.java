package es.demo.routeplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Travel implements Serializable {

    private static final long serialVersionUID = -5548778241223170917L;

    @JsonProperty("stop")
    private Integer stops = 0;

    @JsonProperty("legs")
    private List<Leg> legs = new ArrayList<>();

    public Integer getStops() {
        return stops;
    }

    public void setStops(final Integer stops) {
        this.stops = stops;
    }

    public Travel stops(final Integer stops) {
        this.stops = stops;
        return this;
    }

    public List<Leg> getLegs() {
        return legs;
    }


    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public Travel legs(final List<Leg> legs) {
        this.legs = legs;
        return this;
    }

    public Travel createTwoSegmentTravel(final Travel firstSegmentTravel, final Travel secondSegmentTravel) {
        return new Travel()
                    .stops(1)
                    .legs(Arrays.asList(firstSegmentTravel.getLegs().get(0)
                                        , secondSegmentTravel.getLegs().get(0)));
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Travel travel = (Travel) o;
        return Objects.equals(this.stops, travel.stops) &&
                Objects.equals(this.legs, travel.legs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stops, legs);
    }

    @Override
    public String toString() {
        return "Travel {\n" +
                "    stops: " + toIndentedString(stops) + "\n" +
                "    legs: " + toIndentedString(legs) + "\n" +
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
