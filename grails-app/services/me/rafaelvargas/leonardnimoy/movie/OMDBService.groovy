package me.rafaelvargas.leonardnimoy.movie

import grails.transaction.Transactional
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import me.rafaelvargas.leonardnimoy.api.SearchResult
import org.codehaus.groovy.grails.web.json.JSONElement

@Transactional
class OMDBService {

    private final URL = 'http://www.omdbapi.com/'

    SearchResult findMovieByTitle(String title) {
        Map params = [title:title]

        try {
            RestResponse restResponse = initRestBuilder().get(URL + '?s={title}', params)
            JSONElement jsonResponse = restResponse.json

            new SearchResult(status:jsonResponse["Response"] == 'True',
                             result:jsonResponse["Search"],
                             error:jsonResponse["Error"])
        }
        catch(Exception e) {
            new SearchResult(status:false, error:e.message)
        }
    }

    private RestBuilder initRestBuilder() {
        RestBuilder rest = new RestBuilder()
        rest.restTemplate.messageConverters.removeAll {
            it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter'
        }
        rest
    }
}
