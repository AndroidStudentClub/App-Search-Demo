package ru.androidschool.appsearch.data

object PersonMapper {

    fun toPersonDocument(person: Person): PersonDocument {
        return PersonDocument(
            namespace = "person",
            id = person.id.toString(),
            score = 9,
            name = person.name,
            personPoster = person.imageUrl
        )
    }

    fun fromPersonDocument(d: PersonDocument): Person {
        return Person(
            id = d.id.toInt(),
            name = d.name,
            imageUrl = d.personPoster?:""
        )
    }
}