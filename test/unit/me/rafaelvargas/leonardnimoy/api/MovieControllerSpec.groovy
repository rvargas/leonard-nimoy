package me.rafaelvargas.leonardnimoy.api

import me.rafaelvargas.leonardnimoy.movie.OMDBService
import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import groovy.json.JsonSlurper
import spock.lang.Specification
import spock.lang.Unroll

@Mock([OMDBService])
@TestFor(MovieController)
class MovieControllerSpec extends Specification {

    @Unroll("[search] Method #requestMethod have response code #responseStatus")
    void "Search doesn't work with other request methods than GET"(){
        setup:"Set Request method"
            request.method = requestMethod

        when:"Calling search action"
            controller.search()

        then:"Validate status"
            response.status == responseStatus

        where:"Only GET method is allowed"
            requestMethod   |responseStatus
            'HEAD'          |405
            'TRACE'         |405
            'CONNECT'       |405
            'POST'          |405
            'PUT'           |405
    }

    void "[search] An invalid searchCommand returns a 400 error code"(){
        given: "A null searchCommand"
            SearchCommand searchCommandInstance = new SearchCommand()

        when: "Calling search action"
            controller.search(searchCommandInstance)
        and: "Parse response"
            Map responseJSON = new JsonSlurper().parseText(response.text)

        then: "Error code is 400"
            response.status == 400
        and: "Response contains errors key"
            responseJSON.containsKey('errors')
        and: "Errors is a map"
            responseJSON.errors instanceof Map
        and: "Errors is not empty"
            responseJSON.errors.size() > 0
    }

    void "[search] A search with no results returns a 666 code"(){
        given: "A searchCommand"
            SearchCommand searchCommandInstance = new SearchCommand(title:'Vi Vi Vinco')

        when: "Calling search action"
            controller.search(searchCommandInstance)
        and: "Parse response"
            Map responseJSON = new JsonSlurper().parseText(response.text)

        then: "Error code is 666"
            response.status == 666
        and: "Response contains error key"
            responseJSON.containsKey('error')
        and: "Error is 'Movie not found!'"
            responseJSON.error == 'Movie not found!'
    }

    void "[search] A search with results"(){
        given: "A searchCommand"
            SearchCommand searchCommandInstance = new SearchCommand(title:'Batman')

        when: "Calling search action"
            controller.search(searchCommandInstance)
        and: "Parse response"
            List responseJSON = new JsonSlurper().parseText(response.text)

        then: "Code is 200"
            response.status == 200
        and: "Result list is not empty"
            responseJSON.size() > 0
    }
}
