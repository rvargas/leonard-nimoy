package me.rafaelvargas.leonardnimoy.api

import grails.validation.Validateable

@Validateable
class SearchCommand {

    String title

    static constraints = {
        title   blank:false
    }
}
