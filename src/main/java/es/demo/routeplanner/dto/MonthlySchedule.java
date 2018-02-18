package es.demo.routeplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class MonthlySchedule implements Serializable {

    private static final long serialVersionUID = 5228687154605427295L;

    @JsonProperty("month")
    private Integer month;

    @JsonProperty("days")
    private List<DailySchedule> days;

    public MonthlySchedule() {
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(final Integer month) {
        this.month = month;
    }

    public MonthlySchedule day(final Integer month) {
        this.month = month;
        return this;
    }

    public List<DailySchedule> getDays() {
        return days;
    }

    public void setDays(final List<DailySchedule> days) {
        this.days = days;
    }

    public MonthlySchedule days(final List<DailySchedule> days) {
        this.days = days;
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
        final MonthlySchedule monthlySchedule = (MonthlySchedule) o;
        return Objects.equals(this.month, monthlySchedule.month) &&
                Objects.equals(this.days, monthlySchedule.days);
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, days);
    }

    @Override
    public String toString() {
        return "MonthlySchedule {\n" +
                "    month: " + toIndentedString(month) + "\n" +
                "    days: " + toIndentedString(days) + "\n" +
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
