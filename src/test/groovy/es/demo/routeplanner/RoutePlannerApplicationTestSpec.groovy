package es.demo.routeplanner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.retry.annotation.EnableRetry
import spock.lang.Specification


class RoutePlannerApplicationTestSpec extends Specification {
    def "test annotation @SpringBootApplication is present"() {
        expect: "Annotation @SpringBootApplication is present"
        RoutePlannerApplication.class.isAnnotationPresent(SpringBootApplication.class)
    }

    def "test annotation @EnableRetry is present"() {
        expect: "Annotation @EnableRetry is present"
        RoutePlannerApplication.class.isAnnotationPresent(EnableRetry.class)
    }
}