package es.demo.routeplanner.model;

import es.demo.routeplanner.dto.Route;

import java.util.Objects;

public class RoutesInfo {

    private int value;

    private Route routeFrom;

    private Route routeTo;

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
    }

    public RoutesInfo value(final int value) {
        this.value = value;
        return this;
    }

    public Route getRouteFrom() {
        return routeFrom;
    }

    public void setRouteFrom(final Route routeFrom) {
        this.routeFrom = routeFrom;
    }

    public RoutesInfo routeFrom(final Route routeFrom) {
        this.routeFrom = routeFrom;
        return this;
    }

    public Route getRouteTo() {
        return routeTo;
    }

    public void setRouteTo(final Route routeTo) {
        this.routeTo = routeTo;
    }

    public RoutesInfo routeTo(final Route routeTo) {
        this.routeTo = routeTo;
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
        final RoutesInfo routesInfo = (RoutesInfo) o;
        return Objects.equals(this.value, routesInfo.value) &&
                Objects.equals(this.routeFrom, routesInfo.routeFrom) &&
                Objects.equals(this.routeTo, routesInfo.routeTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, routeFrom, routeTo);
    }

    @Override
    public String toString() {
        return "RoutesInfo {\n" +
                "    value: " + toIndentedString(value) + "\n" +
                "    routeFrom: " + toIndentedString(routeFrom) + "\n" +
                "    routeTo: " + toIndentedString(routeTo) + "\n" +
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
