package es.demo.routeplanner.model

import spock.lang.Specification


class InfoFlagTypeTestSpec extends Specification {

    def "test generateValue"() {
        given: "InfoFlagType.FROM, InfoFlagType.TO"

        when: "call generateValue([InfoFlagType.FROM, InfoFlagType.TO])"
        def result = InfoFlagType.generateValue([InfoFlagType.FROM, InfoFlagType.TO])

        then: "result of call is 3"
        result == 3
    }

    def "test containsBothFromAndTo"() {
        given: "Values [1, 2, 3, 4] to test"

        expect: "Value 3 is associated with a connection that has links to arrival, departure"
        test == InfoFlagType.containsBothFromAndTo((int)value)

        where:
        value   ||  test
            1   ||  false
            2   ||  false
            3   ||  true
            4   ||  false
    }

    def "test fromAndToAreEqual"() {
        given: "Values [1, 2, 3, 4] to test"

        expect: "Value 4 from tested values is associated with a direct flight between arrival, departure"
        test == InfoFlagType.fromAndToAreEqual((int)value)

        where:
        value   ||  test
        1       ||  false
        2       ||  false
        3       ||  false
        4       ||  true
    }

}