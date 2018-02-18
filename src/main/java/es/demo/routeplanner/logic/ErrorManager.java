package es.demo.routeplanner.logic;

import es.demo.routeplanner.model.ErrorObject;
import es.demo.routeplanner.model.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ErrorManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorManager.class);

    public static final String QUALIFIER_NAME = "errorYml";
    private static final String ERRORS_CUSTOM = "errors.custom.";
    private static final String ERRORS_GENERIC = "errors.generic.";
    private static final String EMPTY_STRING = "";

    private static final String DOT = "\\.";
    private static final String COLON = ":";

    private YamlPropertiesFactoryBean yamlObject;
    private Properties properties = new Properties();

    private Map<String, ErrorObject> errorsMap = new HashMap<>();

    @Autowired
    public ErrorManager(@Qualifier(QUALIFIER_NAME) YamlPropertiesFactoryBean yamlObject) {
        this.yamlObject = yamlObject;
    }

    @PostConstruct
    public void initErrorManager() {
        properties = yamlObject.getObject();
        errorsMap = buildErrorsMap();
    }

    public ErrorObject getError(ErrorType errorType) {
        return errorsMap.get(errorType.name()).timestamp(System.currentTimeMillis());
    }

    private Map<String, ErrorObject> buildErrorsMap() {
        final Map<String, ErrorObject> map = new HashMap<>();

        for (Object keyObj : properties.keySet()) {
            final String key = (String) keyObj;

            if (key.startsWith(ERRORS_CUSTOM) || key.startsWith(ERRORS_GENERIC)) {
                String name = extractSubStringBetweenTwoSubStrings(key, ERRORS_CUSTOM, DOT);
                String typeOfProperty = extractSubStringBetweenTwoSubStrings(key,
                        ERRORS_CUSTOM + name + DOT, EMPTY_STRING);
                if (key.startsWith(ERRORS_GENERIC)) {
                    name = extractSubStringBetweenTwoSubStrings(key, ERRORS_GENERIC, DOT);
                    typeOfProperty = extractSubStringBetweenTwoSubStrings(key,
                            ERRORS_GENERIC + name + DOT, EMPTY_STRING);
                }
                final String valueOfProperty = properties.get(key).toString();

                if (map.get(name) == null) {
                    ErrorObject errorObject = new ErrorObject().errorName(name);

                    map.put(name, errorObject);
                }

                switch (typeOfProperty) {
                    case "status":
                        int status = -1;
                        try {
                            status = Integer.parseInt(valueOfProperty);
                        } catch (NumberFormatException nfe) {
                            LOGGER.error("Invalid http status in error configuration. Value: " + valueOfProperty, nfe);
                        }
                        map.get(name).setStatus(status);
                        break;
                    case "internalCode":
                        int internalCode = -1;
                        try {
                            internalCode = Integer.parseInt(valueOfProperty);
                        } catch (NumberFormatException nfe) {
                            LOGGER.error("Invalid http status in error configuration. Value: " + valueOfProperty, nfe);
                        }
                        map.get(name).setInternalCode(internalCode);
                        break;
                    case "shortMessage":
                        map.get(name).setShortMessage(valueOfProperty);
                        break;
                    case "detailedMessage":
                        map.get(name).setDetailedMessage(valueOfProperty);
                        break;
                }

            }
        }
        return map;
    }

    private String extractSubStringBetweenTwoSubStrings(final String baseString, final String str1, final String str2) {
        if (str2.isEmpty()) {
            return extractSubStringBetweenTwoSubStrings(baseString + COLON, str1,
                    COLON);
        }
        Pattern pattern = Pattern.compile(str1 + "(.*?)" + str2);
        Matcher matcher = pattern.matcher(baseString);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
