package es.demo.routeplanner.api;

import es.demo.routeplanner.logic.ErrorManager;
import es.demo.routeplanner.model.CustomException;
import es.demo.routeplanner.model.ErrorObject;
import es.demo.routeplanner.model.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RestController
public class ExceptionMapper {

    @Value("${spring.application.name}")
    private String appName;

    private ErrorManager errorManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionMapper.class);

    @Autowired
    public ExceptionMapper(final ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorObject> handleBaseException(CustomException e)
    {
        LOGGER.error(e.getMessage());
        final ErrorObject errorObject = e.getErrorObject();
        errorObject.setAppName(appName);

        return new ResponseEntity<>(errorObject, HttpStatus.valueOf(errorObject.getStatus()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public @ResponseBody
    ErrorObject handleException(Exception e){
        LOGGER.error(e.getMessage());
        final ErrorObject errorObject = errorManager.getError(ErrorType.INTERNAL_SERVER_ERROR);
        errorObject.setAppName(appName);
        return errorObject;
    }

}

