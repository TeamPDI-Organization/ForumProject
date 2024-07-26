# Forum Project

This is a Spring Boot-based forum project.

## Getting Started

### Prerequisites

- Java 11 or higher
- Gradle
- An IDE like IntelliJ IDEA

## API Endpoints

### User Endpoints

- **Create User**
    - **URL:** `/api/users`
    - **Method:** `POST`
    - **Request Body:**
        ```json
        {
            "username": "string",
            "password": "string",
            "email": "string"
        }
        ```
    - **Response:**
        ```json
        {
            "id": "long",
            "username": "string",
            "email": "string"
        }
        ```

- **Get User by ID**
    - **URL:** `/api/users/{id}`
    - **Method:** `GET`
    - **Response:**
        ```json
        {
            "id": "long",
            "username": "string",
            "email": "string"
        }
        ```

### Post Endpoints

- **Create Post**
    - **URL:** `/api/posts`
    - **Method:** `POST`
    - **Request Body:**
        ```json
        {
            "title": "string",
            "content": "string",
            "userId": "long"
        }
        ```
    - **Response:**
        ```json
        {
            "id": "long",
            "title": "string",
            "content": "string",
            "userId": "long"
        }
        ```

- **Get Post by ID**
    - **URL:** `/api/posts/{id}`
    - **Method:** `GET`
    - **Response:**
        ```json
        {
            "id": "long",
            "title": "string",
            "content": "string",
            "userId": "long"
        }
        ```

### Comment Endpoints

- **Create Comment**
    - **URL:** `/api/comments`
    - **Method:** `POST`
    - **Request Body:**
        ```json
        {
            "content": "string",
            "postId": "long",
            "userId": "long"
        }
        ```
    - **Response:**
        ```json
        {
            "id": "long",
            "content": "string",
            "postId": "long",
            "userId": "long"
        }
        ```

- **Get Comment by ID**
    - **URL:** `/api/comments/{id}`
    - **Method:** `GET`
    - **Response:**
        ```json
        {
            "id": "long",
            "content": "string",
            "postId": "long",
            "userId": "long"
        }
        ```

## Usage in Postman

1. **Create a User:**
    - **Method:** `POST`
    - **URL:** `http://localhost:8080/api/users`
    - **Body:**
        ```json
        {
            "username": "john_doe",
            "password": "password123",
            "email": "john@example.com"
        }
        ```

2. **Get User by ID:**
    - **Method:** `GET`
    - **URL:** `http://localhost:8080/api/users/{id}`

3. **Create a Post:**
    - **Method:** `POST`
    - **URL:** `http://localhost:8080/api/posts`
    - **Body:**
        ```json
        {
            "title": "My First Post",
            "content": "This is the content of my first post.",
            "userId": 1
        }
        ```

4. **Get Post by ID:**
    - **Method:** `GET`
    - **URL:** `http://localhost:8080/api/posts/{id}`

5. **Create a Comment:**
    - **Method:** `POST`
    - **URL:** `http://localhost:8080/api/comments`
    - **Body:**
        ```json
        {
            "content": "This is a comment.",
            "postId": 1,
            "userId": 1
        }
        ```

6. **Get Comment by ID:**
    - **Method:** `GET`
    - **URL:** `http://localhost:8080/api/comments/{id}`

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
