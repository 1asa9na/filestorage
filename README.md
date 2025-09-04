# File Storage REST API

## 📌 Описание
REST API для работы с файловым хранилищем. Реализованы CRUD-операции для сущностей:
- **User**
- **File**
- **Event**

Проект построен с использованием:
- Java
- Servlets (HTTP API)
- Hibernate (ORM)
- MySQL (БД)
- Maven (сборка проекта)
- Flyway (миграции базы данных)
- Swagger (документация)

---

## ⚙️ Архитектура
Проект следует **MVC-подходу**:

- **Model** → Hibernate-сущности (`User`, `File`, `Event`)
- **Controller** → Сервлеты (`UserServlet`, `FileServlet`, `EventServlet`)
- **Repository** → Классы для работы с БД через Hibernate
- **View** → JSON-ответы клиенту (через Postman/Swagger)

Поток запроса:
```
Postman → Servlet (Controller, DTO) → Repository (Hibernate) → MySQL
```

---

## 🚀 Запуск проекта

### 1. Поднять базу данных MySQL
```sql
CREATE DATABASE filestorage;
CREATE USER 'fs_user'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON file_storage.* TO 'fs_user'@'%';
FLUSH PRIVILEGES;
```

### 2. Настроить `src/main/resources/hibernate.cfg.xml`
```xml
<hibernate-configuration>
    <session-factory>
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/filestorage</property>
        <property name="hibernate.connection.username">fs_user</property>
        <property name="hibernate.connection.password">password</property>
        <property name="hibernate.dialect">org.hibernate.dialect.MySQL8Dialect</property>
        <property name="hibernate.hbm2ddl.auto">validate</property>
    </session-factory>
</hibernate-configuration>
```

### 3. Применить миграции Flyway
```bash
mvn flyway:migrate
```

### 4. Собрать и запустить проект
```bash
mvn clean package
```

Задеплоить полученный `.war` в Tomcat.

---

## 📖 Эндпоинты

### Users (`/users`)
- `GET /users` → список пользователей
- `GET /users?id={id}` → получить пользователя
- `POST /users` → создать пользователя
- `PUT /users` → обновить пользователя
- `DELETE /users/?id={id}` → удалить пользователя

### Files (`/files`)
- `GET /files` → список файлов
- `GET /files?id={id}` → получить файл
- `POST /files` → загрузить файл
- `PUT /files` → обновить файл
- `DELETE /files?id={id}` → удалить файл

### Events (`/events`)
- `GET /events` → список событий
- `GET /events?event_id={id}` → получить событие
- `GET /events?user_id={id}` → получить события пользователя
- `POST /events` → создать событие
- `PUT /events` → обновить событие
- `DELETE /events/id={id}` → удалить событие

---

## 📌 Swagger
После запуска доступна Swagger-документация:  
```
https://app.swaggerhub.com/apis/nope-89c/rest-api-basics-crud-test-variable/1.0.0#/
```

---

## 🛠 Инструменты разработки
- **Postman** для тестирования API: https://www.getpostman.com/
- **Swagger** для автоматической документации
- **Maven** для сборки
- **Flyway** для миграций
