package me.rafaelvargas.leonardnimoy.profile

class User {

    String username

    String firstName

    String lastName

    static constraints = {
        username    blank: false, maxSize:100, email:true, unique:true
        firstName   blank: false, maxSize:100
        lastName    blank: false, maxSize:100
    }

    String fullName() {
        "$firstName $lastName"
    }

    static List findAll() {
        String query = "from User"
        User.executeQuery(query)
    }
}
