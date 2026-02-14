# OGMA Landing Page

Веб-приложение для презентации продукта OGMA с интегрированным backend-сервисом для сбора лидов и аналитики. Проект реализует полный цикл обработки данных: от фронтенд-валидации до сохранения в реляционную СУБД с логированием операций.

## Техническая реализация

Проект построен на основе микро-архитектуры без использования тяжеловесных фреймворков (Spring/Jakarta EE), что обеспечивает максимальную производительность и минимальное потребление ресурсов.

### Backend (Java Core)
Разработан на **Java 23** с использованием нативного **HTTP Server** (com.sun.net.httpserver).

**Ключевые технологии и паттерны:**
- **Layered Architecture**: Строгое разделение на слои Controllers, Services, Repositories (DAO).
- **RESTful API**: Реализация JSON-based API для взаимодействия с клиентом.
- **JDBC (Java Database Connectivity)**: Прямое взаимодействие с базой данных через драйвер PostgreSQL (42.7.7) для полного контроля над SQL-запросами.
- **Google Gson 2.10.1**: Сериализация и десериализация объектов данных (DTO).
- **Apache Commons Validator 1.8.0**: Профессиональная валидация email-адресов и входных данных (RFC 5322).
- **SLF4J + Log4j2 (2.23.1)**: Асинхронное структурное логирование событий с разделением по уровням (INFO, ERROR, DEBUG).
- **Unit Testing**: Покрытие бизнес-логики тестами с использованием **JUnit 5 (Jupiter)** и **Mockito 4.2.0** для изоляции зависимостей.

### Frontend
Реализован на чистых веб-технологиях для обеспечения кроссбраузерности и быстрой загрузки.

- **HTML5 Semantic**: Семантическая верстка для SEO-оптимизации.
- **CSS3 (Modern Layouts)**: Использование Flexbox и Grid Layout, адаптивный дизайн (Responsive Web Design) под мобильные устройства.
- **Vanilla JavaScript (ES6+)**: Асинхронные запросы к API (Fetch API), манипуляция DOM без использования сторонних библиотек (jQuery/React/Vue).

### Хранение данных
- **PostgreSQL 15+**: Реляционная СУБД для надежного хранения данных пользователей.
- **DDL/DML**: Использование сырых SQL-запросов для создания схем и манипуляции данными.

## Установка и запуск

### Предварительная настройка
1. Создать базу данных PostgreSQL:
   ```sql
   CREATE DATABASE ogma_landing_page;
   ```
2. Настроить подключение в `backend/config.properties`:
   ```properties
   database.url=jdbc:postgresql://localhost:5432/ogma_landing_page
   database.username=postgres
   database.password=1234
   server.port=8080
   ```

### Сборка и выполнение
Проект использует **Maven** для управления зависимостями и жизненным циклом сборки.

**Backend:**
```bash
cd backend
mvn clean compile
mvn exec:java -Dexec.mainClass="ru.ogma.app.Main"
```

**Frontend:**
Открыть `frontend/index.html` в браузере или развернуть через Nginx/Apache/Python SimpleServer.

## API Specification

**POST /register**
Эндпоинт для регистрации лидов. Принимает JSON, валидирует данные, сохраняет в БД.

**Request Body:**
```json
{
  "name": "User Name",
  "email": "user@example.com"
}
```

**Responses:**
- `204 No Content` - Успешная регистрация
- `400 Bad Request` - Ошибка валидации JSON
- `422 Unprocessable Entity` - Невалидный email
- `500 Internal Server Error` - Ошибка сервера/БД
