package me.rafaelvargas.leonardnimoy.profile

import grails.test.mixin.TestFor
import spock.lang.Unroll
import grails.test.spock.IntegrationSpec

class UserIntegrationSpec extends IntegrationSpec {

    def setup() {
        new User(username:'rafael@gmail.com',
                 firstName:'Rafael',
                 lastName:'Vargas').save(flush:true)

        assert User.count() == 1
    }

    def cleanup() {
        User.executeUpdate('delete from User')
        assert User.count() == 0
    }

    @Unroll("Test '#reason' resulted #expectedValidation with #expectedErrorCount errors")
    Void "Validate User constraints but Unrolled"(){

        given: "A user"
            User userInstance = new User(username:username,
                                         firstName:firstName,
                                         lastName:lastName)

        when: "User validation"
            Boolean validationResult = userInstance.validate()

        then: "Validation is expected"
            validationResult == expectedValidation
        and: "Error count is expected"
            userInstance.errors.errorCount == expectedErrorCount

        where:
            username                |firstName  |lastName       |expectedValidation |expectedErrorCount |reason
            "rv@vincoorbis.com"     |"Rafael"   |"Vargas"       |true               |0                  |"All fields are valid :-)"
            null                    |null       |null           |false              |3                  |"All fields are invalid :-("

            // Username
            null                    |"Rafael"   |"Vargas"       |false              |1                  |"Username is null"
            ""                      |"Rafael"   |"Vargas"       |false              |1                  |"Username is blank"
            "rv"                    |"Rafael"   |"Vargas"       |false              |1                  |"Username is not an email"
            "${'e'*91}@gmail.com"   |"Rafael"   |"Vargas"       |false              |1                  |"Username is longer than maxSize (100)"
            "rafael@gmail.com"      |"Rafael"   |"Vargas"       |false              |1                  |"Username is not unique"

            //firstName
            "rv@vincoorbis.com"     |null       |"Vargas"       |false              |1                  |"FirstName is null"
            "rv@vincoorbis.com"     |""         |"Vargas"       |false              |1                  |"FirstName is blank"
            "rv@vincoorbis.com"     |"R"*200    |"Vargas"       |false              |1                  |"FirstName is longer thanh maxSize (100)"

            //lastName
            "rv@vincoorbis.com"     |"Rafael"   |null           |false              |1                  |"LastName is null"
            "rv@vincoorbis.com"     |"Rafael"   |""             |false              |1                  |"LastName is blank"
            "rv@vincoorbis.com"     |"Rafael"   |"V"*200        |false              |1                  |"LastName is longer thanh maxSize (100)"
    }

    void "Validate fullName method"(){
        given: "A user"
            User userInstance = new User(username:"rv@vincoorbis.com",
                                         firstName:"Rafael",
                                         lastName:"Vargas")

        when: "Calling fullName method"
            String fullName = userInstance.fullName()

       then: "fullName is correct"
            fullName == "Rafael Vargas"
            fullName != "RafaelVargas"
    }

    void "validate findAll method"(){
        when: "Calling findAll method"
            List userList = User.findAll()

        then: "Validate userList result"
            userList.size() == 1
            userList.first().username == "rafael@gmail.com"
    }
}
