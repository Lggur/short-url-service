# Short URL Service

Консольное приложение для создания и управления короткими ссылками с поддержкой мультипользовательского режима, автоматического истечения срока действия и сохранения и загрузки данных в JSON.

---
## Функционал

- Создание коротких ссылок с настраиваемым лимитом переходов
- Мультипользовательский режим — каждый пользователь управляет своими ссылками
- Автоматическое истечение ссылок через настраиваемый период времени
- Редактирование и удаление ссылок владельцем
- Статистика переходов для каждого пользователя
- Сохранение и загрузка данных в JSON
- Валидация — проверка дубликатов, лимитов переходов и срока действия
- Планировщик — автоматическая очистка истекших ссылок

---
## Требования

- **Java 17** или выше
- **Gradle 7.0+** (включен через Gradle Wrapper)

---
## Установка

### Клонирование репозитория

```bash
git clone https://github.com/Lggur/short-url-service.git
cd short-url-service
```

### Сборка проекта

```bash
# Windows
.\gradlew.bat build

# Linux/MacOS
./gradlew build
```adlew build
```

### Запуск приложения

```bash
# Windows
.\gradlew.bat run

# Linux/MacOS
./gradlew run
```

Или запуск скомпилированного JAR:

```bash
java -jar build/libs/short-url-service-1.0-SNAPSHOT.jar
```

## Использование

### Основные команды меню

После запуска приложения вы увидите консольное меню с доступными действиями:

```
=== Меню ===
Пользователь не выбран

1. Выбрать пользователя
2. Создать короткую ссылку
3. Перейти по короткой ссылке
4. Сохранить данные
5. Загрузить данные
6. Выход
```

Аналогичное меню, если вы авторизованы:

```
=== Меню ===
Текущий пользователь: 392192b4-4620-4c6c-8820-e6130cf1fd76

1. Выбрать пользователя
2. Создать короткую ссылку
3. Перейти по короткой ссылке
4. Мои ссылки
5. Моя статистика
6. Редактировать ссылку
7. Удалить ссылку
8. Сохранить данные
9. Загрузить данные
10. Выйти из аккаунта
11. Выход
```

### Примеры использования

#### 1. Выбор пользователя

```
Выберите действие: 1
Введите UUID пользователя: 
```

#### 2. Создание короткой ссылки

```
Выберите действие: 2
Введите длинный URL: https://example.com/very/long/url
Введите лимит переходов: 5
✔ | Короткая ссылка: clck.ru/ZMXK3M
```

Если команда была вызвана без авторизации, будет создан новый пользователь с уникальным UUID, который сразу авторизуется

#### 3. Переход по короткой ссылке

```
Выберите действие: 3
Введите код короткой ссылки: ZMXK3M
✔ | Переход выполнен
```

#### 4. Просмотр своих ссылок

```
Выберите действие: 4

=== Ваши ссылки ===
- ZMXK3M -> https://example.com/very/long/url (клики: 1/5, истекает: 13.01.2026 05:48:48)
```

#### 5. Просмотр статистики

```
Выберите действие: 5

=== Статистика ===
Всего ссылок: 1
Активных ссылок: 1
Всего переходов: 1
```

#### 6. Редактирование ссылки

```
Выберите действие: 6
Введите код короткой ссылки для редактирования: ZMXK3M
Оставьте поле пустым, если не хотите менять это значение
Новый URL (enter для пропуска): https://google.com
Новый лимит переходов (enter для пропуска): 10
✔ | Ссылка успешно обновлена
```

#### 7. Удаление ссылки

```
Выберите действие: 7
Введите код короткой ссылки для удаления: ZNDJbU
Вы уверены? [y/N]: y
✔ | Ссылка успешно удалена
```

#### 8. Сохранение данных

```
Выберите действие: 8
✔ | Данные успешно сохранены в папку data/
Пользователей: 2
Ссылок: 3
Создано файлов со ссылками: 2
```

#### 9. Загрузка данных

```
Выберите действие: 9
✔ | Данные успешно загружены из папки data/
Загружено пользователей: 2
Загружено ссылок: 3
```

#### 10. Выход из аккаунта

```
Выберите действие: 10
✔ | Вы вышли из аккаунта 0df36e78-81ad-428f-8dbd-c86d920f502c
```

#### 11. Закрытие приложения

```
Выберите действие: 11
Сохранить данные перед выходом? [Y/n]:
Данные успешно сохранены.
До свидания!
```

### Файловая персистентность

Данные сохраняются в директории `data/`:
- `data/users.json` — информация о пользователях
- `data/links/links_<user-id>.json` — ссылки каждого пользователя

---
## Архитектура

Проект построен с использованием принципов Clean Architecture (Чистая архитектура).

```
src/main/java/lggur/shorturl/
├── core/                     # Ядро приложения
│   └── domain/                 # Доменная модель
│       ├── link/                   # Агрегат Short Link
│       │   ├── ShortLink.java              # Сущность короткой ссылки
│       │   ├── ShortLinkFactory.java       # Фабрика для создания ссылок
│       │   ├── ShortLinkRepository.java    # Интерфейс репозитория
│       │   ├── ShortCodeGenerator.java     # Интерфейс генератора кодов
│       │   └── LinkLifetimePolicy.java     # Интерфейс политики времени жизни
│       └── user/                   # Агрегат User
│           ├── User.java                # Сущность пользователя
│           └── UserRepository.java      # Интерфейс репозитория пользователей
│
├── application/              # Слой приложения
│   ├── usecase/                # Варианты использования (Use Cases)
│   │   ├── CreateShortLinkUseCase.java
│   │   ├── RedirectUseCase.java
│   │   ├── EditShortLinkUseCase.java
│   │   ├── DeleteShortLinkUseCase.java
│   │   ├── GetUserStatisticsUseCase.java
│   │   └── ExpireLinksUseCase.java
│   ├── dto/                    # Data Transfer Objects
│   │   ├── CreateShortLinkRequest.java
│   │   ├── CreateShortLinkResponse.java
│   │   ├── RedirectResponse.java
│   │   └── UserStatistics.java
│   └── ports/                  # Порты (интерфейсы)
│       ├── NotificationPort.java
│       ├── ShortLinkQueryPort.java
│       └── Clock.java
│
├── infra/                    # Инфраструктурный слой
│   ├── repository/           # Реализация репозиториев
│   │   ├── InMemoryShortLinkRepository.java
│   │   ├── InMemoryUserRepository.java
│   │   └── InMemoryShortLinkQueryAdapter.java
│   ├── service/              # Реализация сервисов
│   │   ├── SimpleShortCodeGenerator.java
│   │   ├── FixedLifetimePolicy.java
│   │   ├── SystemClock.java
│   │   └── ConsoleNotificationService.java
│   ├── persistence/          # Персистентность данных
│   │   ├── DataPersistenceService.java
│   │   ├── ShortLinkData.java
│   │   └── UserData.java
│   ├── scheduler/            # Планировщики
│   │   └── ExpiredLinksScheduler.java
│   └── config/               # Конфигурация
│       └── AppConfig.java
│
├── ui/                       # Пользовательский интерфейс
│   ├── ConsoleApp.java         # Главное приложение
│   ├── ConsoleContext.java     # Контекст консоли
│   ├── input/                  # Ввод данных
│   │   ├── ConsoleReader.java
│   │   └── Reader.java
│   ├── output/                 # Вывод данных
│   │   ├── ConsolePrinter.java
│   │   └── Printer.java
│   └── menu/                   # Меню и действия
│       ├── MenuAction.java
│       ├── ActionHandler.java
│       └── actions/                # Обработчики действий
│           ├── CreateLinkAction.java
│           ├── OpenLinkAction.java
│           ├── SwitchUserAction.java
│           ├── MyLinksAction.java
│           ├── MyStatsAction.java
│           ├── EditLinkAction.java
│           ├── DeleteLinkAction.java
│           ├── SaveDataAction.java
│           ├── LoadDataAction.java
│           ├── LogoutAction.java
│           └── ExitAction.java
│
└── Main.java                 # Точка входа, Dependency Injection
```

### Принципы архитектуры

#### 1. **Domain Layer (Ядро)**
- Содержит бизнес-логику и правила
- Независим от внешних фреймворков и инфраструктуры
- Агрегаты `ShortLink` и `User` инкапсулируют валидацию и бизнес-правила
- Интерфейсы репозиториев и сервисов определены в доменном слое

#### 2. **Application Layer (Приложение)**
- Координирует выполнение бизнес-процессов через Use Cases
- Use Cases оркеструют взаимодействие между доменными объектами
- Определяет порты (интерфейсы) для внешних зависимостей

#### 3. **Infrastructure Layer (Инфраструктура)**
- Реализует адаптеры для портов, определенных в application слое
- In-memory реализации репозиториев (`InMemoryShortLinkRepository`, `InMemoryUserRepository`)
- Планировщик `ExpiredLinksScheduler` автоматически очищает истекшие ссылки
- `DataPersistenceService` использует Jackson для сериализации в JSON

#### 4. **UI Layer (Пользовательский интерфейс)**
- Консольный интерфейс с меню и действиями
- `MenuAction` enum определяет доступные команды
- `ActionHandler` обрабатывает пользовательский ввод и вызывает Use Cases
- Условное отображение меню на основе статуса авторизации

---
## ⚙️ Конфигурация

Настройки приложения находятся в файле `src/main/resources/config.properties`:

```properties
# Время жизни ссылок по умолчанию (в часах)
link.default-lifetime-hours=24

# Минимальное количество переходов
link.min-clicks=1

# Максимальное количество переходов
link.max-clicks-limit=10

# Начальная задержка планировщика (в единицах времени)
scheduler.initial-delay=1

# Период запуска планировщика (в единицах времени)
scheduler.period=1

# Единицы времени: SECONDS, MINUTES, HOURS, DAYS
scheduler.time-unit=HOURS
```

---
## Тестирование

### Запуск всех тестов

```bash
# Windows
.\gradlew.bat test

# Linux/MacOS
./gradlew test
```

### Запуск тестов с отчетом

```bash
.\gradlew.bat test --info
```

Отчет о тестировании будет доступен в: `build/reports/tests/test/index.html`

### Запуск конкретного теста

```bash
# Windows
.\gradlew test --tests ShortLinkTest

# Linux/macOS
./gradlew test --tests ShortLinkTest
```

### Используемые технологии тестирования

- **JUnit 5** - фреймворк для тестирования
- **Mockito** - мокирование зависимостей

---

## Зависимости

Основные библиотеки:

```kotlin
dependencies {
    // JSON сериализация/десериализация
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
    
    // Тестирование
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
}
```