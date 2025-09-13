# OGMA Landing Page

**OGMA Landing Page** - это веб-приложение, состоящее из лендинговой страницы и backend API для регистрации пользователей. Проект представляет собой полнофункциональный сайт-визитку для продукта OGMA с возможностью сбора контактных данных посетителей.

## 🚀 Описание проекта

Этот проект представляет собой лендинговую страницу для продукта OGMA - ИИ-ассистента для анализа коммуникаций. Сайт демонстрирует преимущества продукта, сравнивает его с конкурентами и предоставляет форму регистрации для заинтересованных пользователей.

### Основные компоненты проекта:
- 🌐 **Лендинговая страница** - современный адаптивный дизайн с презентацией продукта
- 🔧 **Backend API** - REST API для обработки регистраций пользователей
- 💾 **База данных** - хранение данных зарегистрированных пользователей
- 📊 **Система логирования** - мониторинг работы приложения
- 🧪 **Тестовое покрытие** - unit-тесты для основных компонентов

## 🏗️ Архитектура проекта

Проект состоит из двух основных компонентов:

### Backend (Java)
- **Технологии**: Java 23, Maven, PostgreSQL
- **Архитектура**: Layered Architecture (Controller → Service → Repository)
- **HTTP сервер**: Встроенный Java HTTP Server
- **База данных**: PostgreSQL с JDBC
- **Логирование**: SLF4J + Log4j2

### Frontend (HTML/CSS)
- **Технологии**: HTML5, CSS3, Vanilla JavaScript
- **Дизайн**: Адаптивный дизайн с современным UI
- **Шрифты**: Google Fonts (Montserrat)

## 📁 Структура проекта

```
OGMA_Landing_Page/
├── backend/                    # Backend приложение
│   ├── src/main/java/ru/ogma/
│   │   ├── app/               # Главный класс приложения
│   │   ├── controllers/       # HTTP контроллеры
│   │   ├── services/          # Бизнес-логика
│   │   ├── repositories/      # Слой доступа к данным
│   │   ├── entities/          # Модели данных
│   │   ├── exceptions/        # Пользовательские исключения
│   │   ├── utils/             # Утилиты
│   │   └── console/           # Админ консоль
│   ├── src/main/resources/    # Конфигурационные файлы
│   ├── src/test/              # Тесты
│   └── config.properties      # Конфигурация БД и сервера
├── frontend/                   # Frontend приложение
│   ├── index.html             # Главная страница
│   ├── styles/                # CSS стили
│   └── img/                   # Изображения и иконки
├── docs/                      # Документация
└── logs/                      # Логи приложения
```

## 🛠️ Технологический стек

### Backend
- **Java 23** - основной язык программирования
- **Maven** - система сборки и управления зависимостями
- **PostgreSQL** - реляционная база данных
- **Gson** - JSON парсинг
- **Commons Validator** - валидация email
- **SLF4J + Log4j2** - логирование
- **JUnit 5 + Mockito** - тестирование

### Frontend
- **HTML5** - разметка
- **CSS3** - стилизация
- **Google Fonts** - веб-шрифты

## 🚀 Быстрый старт

### Предварительные требования
- Java 23 или выше
- Maven 3.6+
- PostgreSQL 12+
- Git

### Установка и запуск

1. **Клонирование репозитория**
   ```bash
   git clone <repository-url>
   cd OGMA_Landing_Page
   ```

2. **Настройка базы данных**
   ```sql
   CREATE DATABASE ogma_landing_page;
   CREATE USER postgres WITH PASSWORD '1234';
   GRANT ALL PRIVILEGES ON DATABASE ogma_landing_page TO postgres;
   ```

3. **Настройка конфигурации**
   
   Отредактируйте файл `backend/config.properties`:
   ```properties
   # Database Configuration
   database.url=jdbc:postgresql://localhost:5432/ogma_landing_page
   database.username=postgres
   database.password=1234
   port=5432

   # Server Configuration
   server.host=127.0.0.1
   server.port=8080
   ```

4. **Сборка и запуск backend**
   ```bash
   cd backend
   mvn clean compile
   mvn exec:java -Dexec.mainClass="ru.ogma.app.Main"
   ```

5. **Запуск frontend**
   
   Откройте файл `frontend/index.html` в браузере или используйте локальный сервер:
   ```bash
   cd frontend
   python -m http.server 8000
   # или
   npx serve .
   ```

## 📡 API Документация

### Регистрация пользователя

**POST** `/register`

Регистрирует нового пользователя в системе.

#### Запрос
```json
{
  "name": "Имя пользователя",
  "email": "user@email.com"
}
```

#### Параметры
- `name` (string, обязательный) - имя пользователя
- `email` (string, обязательный) - email адрес (валидируется по RFC 5322)

#### Ответы
- `204 No Content` - регистрация успешна
- `400 Bad Request` - некорректные или неполные данные
- `405 Method Not Allowed` - неподдерживаемый HTTP метод
- `422 Unprocessable Entity` - данные не проходят валидацию
- `500 Internal Server Error` - внутренняя ошибка сервера

#### Пример использования
```bash
curl.exe -X POST http://localhost:8080/register \
  -H "Content-Type: application/json" \
  -d '{"name": "Иван Иванов", "email": "ivan@example.com"}'
```

### Запуск тестов
```bash
cd backend
mvn test
```

### Покрытие тестами
Проект включает unit-тесты для основных компонентов:
- `PersonServiceTest` - тестирование бизнес-логики
- `PersonParseTest` - тестирование парсинга JSON
- `PersonValidatorTest` - тестирование валидации данных

## 📊 Мониторинг и логирование

### Логирование
Приложение использует SLF4J с Log4j2 для логирования:
- **Уровни логирования**: DEBUG, INFO, WARN, ERROR
- **Файлы логов**: `logs/application.log`, `logs/error.log`
- **Конфигурация**: `src/main/resources/log4j2.properties`

### Админ консоль
После запуска сервера доступна админ консоль с командами:
- `exit` или `quit` - корректное завершение работы сервера

## 🔧 Конфигурация

### Переменные окружения
Основные настройки находятся в `backend/config.properties`:

| Параметр | Описание | По умолчанию |
|----------|----------|--------------|
| `database.url` | URL подключения к БД | `jdbc:postgresql://localhost:5432/ogma_landing_page` |
| `database.username` | Имя пользователя БД | `postgres` |
| `database.password` | Пароль БД | `1234` |
| `server.host` | Хост сервера | `127.0.0.1` |
| `server.port` | Порт сервера | `8080` |

## 🚀 Развертывание

### Локальное развертывание
1. Следуйте инструкциям в разделе "Быстрый старт"
2. Убедитесь, что PostgreSQL запущен
3. Запустите backend и frontend
