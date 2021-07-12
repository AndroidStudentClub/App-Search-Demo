# Демо-приложение для знакомства с App Search Library
Это проект, выполненный для демонстрации работы [AppSearch](https://developer.android.com/guide/topics/search/appsearch#kotlin)

Чтобы быть в курсе новых туториалов и статей подписывайся на telegram-канал [@android_school_ru](https://t.me/android_school_ru)
Пройти туториал можно на сайте [AndroidSchool.ru](AndroidSchool.ru)

## Приложение позволяет:

- Просматривать список сериалов, актёров
- Сохраняет их в локальный кэш
- Осуществляет поиск через AppSearch
- Выводит разные результаты в единый список

![Пример список сериалов](app/src/main/res/drawable/tvfeed.jpeg)
![Пример список актёров](app/src/main/res/drawable/actors.jpeg)
![Пример результата поиска](app/src/main/res/drawable/results.jpeg)

## В проекте используются следующие библиотеки и фрэймворки 📚:
- Groupie для построения сложных списков на базе RecyclerView. [Подробнее](https://github.com/lisawray/groupie)
- Android Navigation для навигации между экранами. [Подробнее](https://developer.android.com/guide/navigation/navigation-getting-started)
- AppSearch для локального мультипоиска среди всех документов. [Подробнее](https://developer.android.com/guide/topics/search/appsearch#kotlin)
- RxJava для асинхронной работы. [Подробнее](https://github.com/ReactiveX/RxJava)
- THE MOVIE DATABASE API для получения списка сериалов [THE MOVIE DATABASE API](https://www.themoviedb.org/documentation/api)

## Важно
Для получения списка сериалов используется [THE MOVIE DATABASE API](https://www.themoviedb.org/documentation/api), нужно подставить ключ в файл
MovieApiInterface.kt в поле  API_KEY

