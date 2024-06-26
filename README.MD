# Приложение для демонстрации работы с API на Spring Boot, Hibernate, CriteriaAPI, Testcontainers, Docker, Swagger.
###
Приложение представляет собой простой API для управления продуктами, категориями и ревью на продукты.
Ревью добавляет или убавляет средний рейтинг продукта при добавлении, удалении, а также изменении.
Также реализована фильтрация продуктов по цене, рейтингу, категории и описанию с помощью Criteria API.

 Подробную документацию и схемы API можно найти в [Swagger](http://localhost:8080/swagger-ui/index.html).

## Updated 
- Добавлены интеграционные тесты контроллеров с использованием Testcontainers.

При запуске тестов создается один контейнер с PostgreSQL и удаляется после завершения тестов, а также создается один контекст.
Это реализовано с помощью EnablePostgresTestContainerContextCustomizerFactory который находится в [конфиг папке](https://github.com/wiezq/java-consumer-producer-resttemplate/tree/task5_testcontainers/supplier/src/test/java/com/example/supplier/config)


Для запуска тестов выполните следующую команду в папке supplier

Linux и MacOS:
```bash
./gradlew test
```

Windows:
```bash
gradlew.bat test
```
Или же вручную запустите тесты в Intellij IDEA.



## Запуск приложения
### С помощью docker-compose
Для запуска приложения с помощью docker-compose выполните следующую команду в корневом каталоге проекта:
```bash
docker compose up
```


### Использование
API доступен по адресу http://localhost:8080. Вы можете использовать инструменты, такие как Postman или curl, для взаимодействия с конечными точками API.

##### Например, чтобы получить продукт по ID:

GET http://localhost:8080/api/products/1

Чтобы создать новый обзор для продукта:

POST http://localhost:8080/api/reviews/products/1
Content-Type: application/json

```json
{
"rating": 4,
"comment": "Отличный продукт!",
"productId": 1
}
```

### Реализация Criteria API для фильтрации продуктов.
Например, чтобы получить все продукты с ценой от 1000 до 2000 и рейтингом от 4 до 5:

Нужно выполнить запрос:
```http request
GET http://localhost:8080/api/products/filter?page=0&size=10&sort=desc&sortBy=price&maxPrice=500000&categoryId=1
```

Все параметры являются необязательными.
#### Список параметров
- minPrice: Минимальная цена продукта.
- maxPrice: Максимальная цена продукта.
- minRating: Минимальный рейтинг продукта.
- maxRating: Максимальный рейтинг продукта.
- page: Номер страницы.
- size: Количество элементов на странице.
- sort: Поле для сортировки. Например, sort=price. Возможные значения: price, avarageRating.
- sortBy: Направление сортировки. Возможные значения: asc, desc.
- categoryId: ID категории продукта.
- description: Поиск по описанию продукта.

Обратитесь к документации Swagger для получения более подробной информации о параметрах и ответах каждой конечной точки. 🚀

# Consumer 
Для демонстрации работы с API был создан простой клиент, который позволяет взаимодействовать с API с помощью RestTemplate.

## Запуск приложения
Приложение запустится автоматически при запуске и выведет в логи результаты вызовов API.
```bash
docker compose up
```
