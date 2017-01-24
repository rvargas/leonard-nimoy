package me.rafaelvargas.leonardnimoy.api

import grails.test.mixin.support.GrailsUnitTestMixin
import grails.test.mixin.TestMixin
import spock.lang.Specification
import spock.lang.Unroll

@TestMixin(GrailsUnitTestMixin)
class SearchCommandSpec extends Specification {

    def setup() {
        mockForConstraintsTests(SearchCommand)
    }

    @Unroll("Test '#reason' resulted #expectedValidation with #expectedErrorCount errors")
    Void "Validate User constraints but Unrolled"(){

        given: "A searchCommand"
            SearchCommand searchCommandInstance = new SearchCommand(title:title)

        when: "SearchCommand validation"
            Boolean validationResult = searchCommandInstance.validate()

        then: "Validation is expected"
            validationResult == expectedValidation
        and: "Error count is expected"
            searchCommandInstance.errors.errorCount == expectedErrorCount

        where:
            //title
            title           ||expectedValidation    |expectedErrorCount |reason
            null            ||false                 |1                  |"Title is null"
            ""              ||false                 |1                  |"Title is blank"
            "The Godfather" ||true                  |0                  |"Title is ok"
    }
}
