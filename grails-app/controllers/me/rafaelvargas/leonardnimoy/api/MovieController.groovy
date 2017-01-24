package me.rafaelvargas.leonardnimoy.api

import grails.converters.JSON
import me.rafaelvargas.leonardnimoy.movie.OMDBService

class MovieController {

    def OMDBService

    static allowedMethods = [search:'GET']

    def search(SearchCommand searchCommandInstance) {

        if(!searchCommandInstance.validate()) {
            response.status = 400
            Map errors = [errors:(searchCommandInstance.errors.allErrors).collectEntries({[(it.field):it.code]})]
            render(errors as JSON)
            return
        }

        SearchResult searchResult = OMDBService.findMovieByTitle(searchCommandInstance.title)

        if(!searchResult.status) {
            response.status = 666
            render([error:searchResult.error] as JSON)
            return
        }

        render(searchResult.result as JSON)
    }
}
