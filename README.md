# User Task Scheduler App (Microservices Architecture)

This project simulates a **user task scheduler application** that enables clients to customize how and when they receive notifications. Users can configure the number of reminders they want before an event, providing a flexible and personalized experience.

## Architecture

The application is built using a _**Microservices Architecture**_ and every one uses _**Hexagonal Architecture**_, ensuring scalability, modularity, and ease of maintenance. Each microservice is designed to handle a specific responsibility, promoting a clean and efficient structure.

### Key Features
- **Customizable Notifications**: Users can define notification preferences for timing and frequency.
- **Scalable Design**: A microservices-based approach allows seamless scalability as the application grows.
- **Database Optimization**:
    - **MySQL** is used to manage user data.
    - **MongoDB** is utilized to store and retrieve task-related information.
- **Efficient Communication**: Microservices communicate using **Kafka**, a robust message broker, ensuring reliable and efficient data exchange.
- **Docker Compose**: Used to run all databases and the kafka broker in different containers and is an easy way 

## Microservices responsibilities
- **ms-gateway**: Serves as the entry point to the application. It receives all incoming requests and routes them to the appropriate microservice for handling.
- **ms-auth**: Manages user login, registration, and account-related operations. It also handles the authentication and authorization of every request to ensure security.
- **ms-userevents**: Processes and stores user-defined events that need to be triggered in the future. It sends these events to the **ms-scheduler** via **Kafka** for scheduling.
- **ms-scheduler**: Listens for Kafka messages containing events to be scheduled. When it's time to notify a user about an event, it sends a Kafka message to the **ms-notifier** for further processing.
- **ms-notifier**: Sends notifications to users based on their preferred notification settings upon receiving Kafka messages from **ms-scheduler**.
- **ms-eureka**: Acts as the service registry, enabling dynamic discovery and registration of microservices to streamline communication and coordination across the system.
- **ms-config**: Centralizes the configuration management for all microservices. It downloads configuration data stored in the [CONFIG-REPO](https://github.com/EderV/spring-ms-app-config).  