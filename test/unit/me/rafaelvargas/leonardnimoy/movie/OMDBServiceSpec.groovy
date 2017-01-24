package me.rafaelvargas.leonardnimoy.movie

import me.rafaelvargas.leonardnimoy.api.SearchResult
import me.rafaelvargas.leonardnimoy.movie.OMDBService
import grails.plugins.rest.client.RestBuilder
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(OMDBService)
class OMDBServiceSpec extends Specification {

    void "Validate findMovieByTitle with results"() {
        given: "A movie title"
            String title = "The Godfather"

        when: "Calling findMovieByTitle method"
            SearchResult response = service.findMovieByTitle(title)
            println response

        then: "Result is ok"
            response.status
        and: "A list of movies"
            response.result.size() == 10
    }

    void "Validate findMovieByTitle without results"() {
        given: "A movie title"
            String title = "Vincos Assemble"

        when: "Calling findMovieByTitle method"
            SearchResult response = service.findMovieByTitle(title)
            println response

        then: "Result is false"
            !response.status
        and: "Error is expected"
            response.error == "Movie not found!"
    }

    void "Validate findMovieByTitle with a exception"() {
        given: "A movie title"
            String title = "The Godfather"
        and: "Mock initRestBuilder"
            def mockBuilder = GroovySpy(RestBuilder, global: true)
            new RestBuilder() >> {
                throw new RuntimeException('Example runtime Error')
            }

        when: "Calling findMovieByTitle method"
            SearchResult response = service.findMovieByTitle(title)

        then: "Exception was thrown"
            notThrown(RuntimeException)
        and: "Was handled"
            !response.status
            response.error == "Example runtime Error"
    }
}
