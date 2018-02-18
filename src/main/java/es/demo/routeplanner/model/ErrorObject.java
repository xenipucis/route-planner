package es.demo.routeplanner.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * ErrorObject
 */
public class ErrorObject implements Serializable {

    private static final long serialVersionUID = -5934257388636218469L;

    @JsonProperty("appName")
    private String appName;

    @JsonProperty("errorName")
    private String errorName;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("internalCode")
    private Integer internalCode;

    @JsonProperty("shortMessage")
    private String shortMessage;

    @JsonProperty("detailedMessage")
    private String detailedMessage;

    @JsonProperty("timestamp")
    private Long timestamp;

    public ErrorObject appName(String appName) {
        this.appName = appName;
        return this;
    }

    /**
     * Name of the application
     * @return appName
     **/
    @NotNull
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public ErrorObject timestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Error's timestamp
     * @return timestamp
     **/
    @NotNull
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ErrorObject errorName(String name) {
        this.errorName = name;
        return this;
    }

    /**
     * RoutePlanner internal error name
     * @return name
     **/
    @NotNull
    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String name) {
        this.errorName = name;
    }

    public ErrorObject status(Integer status) {
        this.status = status;
        return this;
    }

    /**
     * HTTP status code of the error response
     * @return status
     **/
    @NotNull
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ErrorObject internalCode(Integer internalCode) {
        this.internalCode = internalCode;
        return this;
    }

    /**
     * RoutePlanner internal error code
     * @return internalCode
     **/
    @NotNull
    public Integer getInternalCode() {
        return internalCode;
    }

    public void setInternalCode(Integer internalCode) {
        this.internalCode = internalCode;
    }

    public ErrorObject shortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
        return this;
    }

    /**
     * Short error message
     * @return shortMessage
     **/
    @NotNull
    public String getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
    }

    public ErrorObject detailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
        return this;
    }

    /**
     * Detailed error message
     * @return detailedMessage
     **/
    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorObject error = (ErrorObject) o;
        return Objects.equals(this.appName, error.appName) &&
                Objects.equals(this.timestamp, error.timestamp) &&
                Objects.equals(this.errorName, error.errorName) &&
                Objects.equals(this.status, error.status) &&
                Objects.equals(this.internalCode, error.internalCode) &&
                Objects.equals(this.shortMessage, error.shortMessage) &&
                Objects.equals(this.detailedMessage, error.detailedMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appName, timestamp, errorName, status, internalCode, shortMessage, detailedMessage);
    }

    @Override
    public String toString() {
        return "ErrorObject {\n" +
                "    appName: " + toIndentedString(appName) + "\n" +
                "    timestamp: " + toIndentedString(timestamp) + "\n" +
                "    errorName: " + toIndentedString(errorName) + "\n" +
                "    status: " + toIndentedString(status) + "\n" +
                "    internalCode: " + toIndentedString(internalCode) + "\n" +
                "    shortMessage: " + toIndentedString(shortMessage) + "\n" +
                "    detailedMessage: " + toIndentedString(detailedMessage) + "\n" +
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



