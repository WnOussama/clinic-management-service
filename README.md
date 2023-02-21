# Clinic Management Service
This project was generated with help of Spring initilizr https://start.spring.io/.

# Getting Started

### How to build
**`mvn clean install`**

### How to start

#### Local environment (with Postgres)
**Pre-requisite:** You should have docker desktop in your local windows machine. You can refer to [documentation](https://www.docker.com/products/docker-desktop/)

In order to run Postgres in docker container with your local environment, run command:
**`docker-compose up -d`** which will use the configuration in **docker-compose.yaml** file.

In order to start the application, run:
**`mvn spring-boot:run -Dspring-boot.run.profiles=local`**

To access swagger ui in local environment, go to:
**`http://localhost:8085/clinic-management-service/swagger-ui/index.html`**

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.2/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#web)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#web.security)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* [Java Mail Sender](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#io.email)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

