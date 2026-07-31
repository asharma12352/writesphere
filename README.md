# Writesphere

Writesphere is a web application built with Spring Boot that allows users to register, log in, and publish their own blogs and articles. Users can create, edit, delete, and search posts, and view their activity through a personal profile page.

## Features

- User registration and login (session-based authentication)
- Create, edit, and delete posts (Blogs or Articles)
- View all posts on the home feed
- View a single post in detail
- Search posts by keyword (title or content)
- User profile page with post statistics (total posts, blogs, articles)
- Logout functionality

## Tech Stack

- **Backend:** Java, Spring Boot, Spring MVC
- **Templating:** Thymeleaf
- **Database:** MySQL
- **ORM:** Spring Data JPA / Hibernate
- **Build Tool:** Maven
- **Server:** Embedded Apache Tomcat

## Project Structure

```
src/main/java/com/example/writesphere
 ├── controller/     # Handles HTTP requests (Auth, Post)
 ├── model/          # Entity classes (User, Post)
 ├── repository/     # Spring Data JPA repositories
 └── service/        # Business logic (UserService, PostService)

src/main/resources
 ├── templates/       # Thymeleaf HTML views
 ├── static/          # CSS/JS/static assets
 └── application.properties.example   # Template for DB config
```

## Getting Started

### Prerequisites

- JDK 17+ (or the version specified in `pom.xml`)
- Maven (or use the included `mvnw` wrapper)
- MySQL Server running locally

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR-USERNAME/writesphere.git
   cd writesphere
   ```

2. **Create the MySQL database**
   ```sql
   CREATE DATABASE writesphere_db;
   ```

3. **Configure your database credentials**

   Copy the example properties file and fill in your own credentials:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```

   Then edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/writesphere_db
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   spring.jpa.hibernate.ddl-auto=update
   ```

   > **Note:** `application.properties` is excluded via `.gitignore` and will never be pushed to GitHub, so your real credentials stay private.

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```
   or run `WritesphereApplication.java` directly from your IDE.

5. **Open in browser**
   ```
   http://localhost:8080/login
   ```

## Routes

| Method | Path              | Description                  |
|--------|-------------------|-------------------------------|
| GET    | `/login`          | Show login page               |
| POST   | `/login`          | Handle login                  |
| GET    | `/register`       | Show registration page        |
| POST   | `/register`       | Handle registration           |
| GET    | `/logout`         | Log out current user          |
| GET    | `/home`           | Home feed (all posts)         |
| GET    | `/post/create`    | Show create-post page         |
| POST   | `/post/create`    | Handle post creation          |
| GET    | `/post/{id}`      | View a single post            |
| GET    | `/post/edit/{id}` | Show edit-post page           |
| POST   | `/post/edit/{id}` | Handle post edit              |
| GET    | `/post/delete/{id}`| Delete a post                |
| GET    | `/profile`        | User profile & stats          |
| GET    | `/search`         | Search posts by keyword       |

## License

This project is open source and available for personal or educational use.
