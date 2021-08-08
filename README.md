# RestfulMovies
VEACT Coding Exercise - Software Developer (Backend), by Guido Marchini.

This is a RESTful microservice with CRUD implementation of movies.

## Getting up the server
> mvn install
> mvn spring-boot:run

## Services
This project has a swagger documentation. please check `/swagger-ui.html` to see it.

## About the project
This project was made in Kotlin using spring-boot. It has several layers:
* Model
  * The project Model. Just the Movie :)

* Application
  * Here you can find the Controller and application layer logic.
  
* Service
  * We don't have business logic here, but it would be here. The only thing that it does in this project is to communicate with the persistence layer.
  
* Persistence
  * Repository and entities. It's on spring JPA. This is the only place that knows about database domain.
  
## Docker
There's a docker file in order to create a docker image. IE:
>docker build -t movies:v1
>docker run --name movies -p 8080:8080 movies:v1

## Dev notes
Here are some notes about the problem statement:

`For data persistence we use the data store which is most suitable for the job`

I don't understand what kind of "data store" do you mean. The project is split into different layers so the only thing that would be needed to use a specific database would be to change the persistence layer.

`The service will be running in production. Make sure it is prepared for it and we know what it is doing`

I don't quite understand this one either, but my guess:
* There are some logs in the controller. As there's not a heavy domain logic, they're quite a few :(
* The only configuration that would change for prod is the database config. As it's a sensitive data, more development would be needed for this one.

Thanks in advance, and I hope you like the solution!