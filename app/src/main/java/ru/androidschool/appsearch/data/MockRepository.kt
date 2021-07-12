package ru.androidschool.appsearch.data

object MockRepository {

    fun getPeople(): List<Person> {
        return listOf(
            Person(
                1,
                "Том Круз",
                "https://vokrug.tv/pic/news/f/b/9/2/fb927aca1ca4a42d9bd46ce4e3330bdd.jpg"
            ),
            Person(
                2,
                "Хью Джекман",
                "https://uznayvse.ru/images/content/2017/3/uzn_14894077421.jpg"
            ),
            Person(
                3,
                "Том Харди",
                "https://www.film.ru/sites/default/files/people/1456277-909723.jpg"
            ),
            Person(
                4,
                "Тоби Магуайр",
                "https://thumbs.dfs.ivi.ru/storage2/contents/1/5/5cba64768368fb4692290d77b3b09b.jpg"
            ),
            Person(
                5,
                "Анджелина Джоли",
                "https://bjdclub.ru/extimages/a9/c4/a9c41e4e6b22208440d9ea71b3fd9276.jpg"
            ),
            Person(
                6,
                "Тони Роббинс",
                "https://a.radikal.ru/a12/1909/7b/bafe102e1548.jpg"
            ),
            Person(
                7,
                "Рик",
                "https://sun9-75.userapi.com/s/v1/if1/i-AnpSkWwsRQpxzHu0Ap4QaUAGRIOHA2s4BClpDI5Mqq_sjXwBbXzPTINZ7GISYBcAGGlR4z.jpg?size=200x0&quality=96&crop=789,17,619,619&ava=1"
            ),
            Person(
                8,
                "Морти",
                "https://static.wikia.nocookie.net/rickandmorty/images/0/02/%D0%97%D0%BB%D0%BE%D0%B9_%D0%9C%D0%BE%D1%80%D1%82%D0%B8_001.jpg/revision/latest/scale-to-width-down/290?cb=20190714125714&path-prefix=ru"
            ),
            Person(
                9,
                "Босс-молокосос",
                "https://www.film.ru/sites/default/files/styles/thumb_1024x450/public/trailers_frame/mv5bmtk2.jpg"
            )
        )
    }
}