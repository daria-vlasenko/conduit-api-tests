# Conduit (RealWorld) API Test Framework

Фреймворк автоматизации API-тестирования публичного демо-сервиса
[RealWorld / Conduit](https://realworld-docs.netlify.app/docs/specs/backend-specs/endpoints).

## Стек

| Компонент | Назначение |
|---|---|
| Java 17 | язык |
| Maven | сборка и запуск |
| REST Assured 5 | HTTP-клиент и проверки |
| JUnit 5 | тестовый движок |
| Jackson | сериализация DTO |
| Datafaker | генерация тестовых данных |
| Allure | отчётность |

## Структура

```
src/main/java/com/conduit
├── api
│   ├── models   DTO запросов и ответов (чистые POJO с билдерами)
│   ├── specs    RequestSpecification / ResponseSpecification, Allure-фильтр, LogDetail.ALL
│   └── steps    API Steps: AuthSteps, ArticleSteps, CommentSteps (все методы под @Step)
├── config       чтение конфигурации
└── data         TestDataFactory на Datafaker

src/test/java/com/conduit/tests
├── BaseTest      регистрация пользователя и получение JWT в @BeforeEach
├── AuthTests     регистрация и логин
├── ArticleTests  создание, публичное чтение и удаление статей
└── CommentTests  комментарии к статье
```

Тесты не вызывают `RestAssured.given()` напрямую — только шаги из `api/steps`.

## Конфигурация

Значения по умолчанию лежат в `src/main/resources/application.properties`:

```
base.url=https://node-express-conduit.appspot.com
base.path=/api
default.password=Password123!
```

Приоритет источников: системное свойство → переменная окружения → `application.properties`.
Имя переменной окружения — ключ в верхнем регистре с `_` вместо `.` (`base.url` → `BASE_URL`).

```bash
mvn clean test -Dbase.url=https://my-conduit-instance.example.com
BASE_URL=https://my-conduit-instance.example.com mvn clean test
```

Локальные секреты храните в `.env` (в `.gitignore`, шаблон — `.env.example`):

```bash
set -a && source .env && set +a && mvn clean test
```

Токены и URL в тестах не хардкодятся: JWT выдаётся динамически через `POST /users/login`
и передаётся в заголовке `Authorization: Token {jwt}`.

## Запуск

```bash
mvn clean test                 # все тесты
mvn clean test -Dtest=AuthTests
mvn allure:report              # отчёт в target/site/allure-maven-plugin
mvn allure:serve               # отчёт в браузере
```

В отчёте видны тела запросов и ответов — за это отвечает `AllureRestAssured`,
подключённый в `Specs.request()`.

## Docker

```bash
docker build -t conduit-api-tests .
docker run --rm -e BASE_URL=https://node-express-conduit.appspot.com conduit-api-tests
```

## CI

`.github/workflows/ci.yml` на каждый push и pull request прогоняет `mvn clean test`,
генерирует Allure-отчёт и публикует его как артефакт (`allure-report`, `allure-results`,
`surefire-reports`).

## Покрытие

| Фича | Проверка |
|---|---|
| Регистрация | новый пользователь получает токен |
| Регистрация | занятый email отклоняется |
| Логин | валидные креды выдают JWT, `GET /user` подтверждает владельца |
| Логин | невалидный пароль и неизвестный email отклоняются |
| Статьи | авторизованный пользователь создаёт статью |
| Статьи | анонимный запрос создания отклоняется |
| Статьи | анонимный пользователь читает опубликованную статью |
| Статьи | автор удаляет свою статью |
| Статьи | чужую статью удалить нельзя, статья остаётся доступной |
| Комментарии | авторизованный пользователь комментирует статью |
| Комментарии | комментарии видны анонимному пользователю |
| Комментарии | анонимный комментарий отклоняется |

## Выбор бэкенда

Официальный `https://api.realworld.io/api` недоступен (Cloudflare 1016), поэтому по умолчанию
используется демо-инстанс `https://node-express-conduit.appspot.com`. Публичные инстансы
отличаются кодами ошибок (401/403/422), поэтому негативные проверки допускают семейство
статусов, а не один код.
