package veact.gmarchini.movies.application

import io.restassured.RestAssured
import io.restassured.RestAssured.*
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import veact.gmarchini.movies.model.Movie
import veact.gmarchini.movies.service.MovieService

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MoviesControllerTest (
    @Autowired val movieService: MovieService,
    @LocalServerPort val port: Int
) {

    lateinit var fightClub: Movie
    lateinit var illusionist: Movie

    @BeforeAll
    fun beforeAll() {
        RestAssured.port = port
        fightClub = movieService.create(Movie(
            name = "Fight club",
            year = 1999,
            id = null
        ))
        illusionist = movieService.create(Movie(
            name = "The Illusionist",
            year = 2006,
            id = null
        ))
    }

    @AfterAll
    fun afterAll() {
        movieService.delete(fightClub.id!!)
        movieService.delete(illusionist.id!!)
    }

    @Test
    fun `get movie`() {
        // arrange

        // act
        get("/movies/{id}", fightClub.id)
            // assert
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("name", Matchers.equalTo(fightClub.name))
            .body("year", Matchers.equalTo(fightClub.year))
            .body("id", Matchers.equalTo(fightClub.id))
    }

    @Test
    fun `get non existing movie`() {
        // arrange

        // act
        get("/movies/{id}", -1)
            // assert
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `get movies`() {
        // arrange

        // act
        get("/movies")
            // assert
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("size()", Matchers.equalTo(2))
            .body("find { it.name == '${fightClub.name}' }", Matchers.notNullValue())
            .body("find { it.name == '${illusionist.name}' }", Matchers.notNullValue())
    }

    @Test
    fun `get movies for a given year`() {
        // arrange

        // act
        get("/movies?year={year}", fightClub.year)
            // assert
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("size()", Matchers.equalTo(1))
            .body("find { it.name == '${fightClub.name}' }", Matchers.notNullValue())
    }

    @Test
    fun `create a movie`() {
        // arrange
        val newMovie = Movie(
            name = "The wolf of Wall Street",
            year = 2013,
            id = null
        )

        // act
        val newMovieId: Int = given()
            .body(newMovie)
            .contentType(ContentType.JSON)
            .post("/movies")
        // assert
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("name", Matchers.equalTo(newMovie.name))
            .body("year", Matchers.equalTo(newMovie.year))
            .extract()
            .path("id")

        // tear down
        movieService.delete(newMovieId)
    }

    @Test
    fun `create a movie with existing name`() {
        // arrange
        val newMovie = Movie(
            name = fightClub.name,
            year = 2013,
            id = null
        )

        // act
        given()
            .body(newMovie)
            .contentType(ContentType.JSON)
            .post("/movies")
            // assert
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `update a movie`() {
        // arrange
        val newMovie = Movie(
            name = "The wolf of Wall Street",
            year = 2012,
            id = null
        )

        val newMovieId: Int = given()
            .body(newMovie)
            .contentType(ContentType.JSON)
            .post("/movies")
            .then()
            .extract()
            .path("id")

        val updatedMovie = Movie(
            name = newMovie.name,
            year = 2013,
            id = newMovieId
        )

        // act
        given()
            .body(updatedMovie)
            .contentType(ContentType.JSON)
            .put("/movies")
            .then()
            // assert
            .statusCode(HttpStatus.OK.value())
            .body("name", Matchers.equalTo(updatedMovie.name))
            .body("year", Matchers.equalTo(updatedMovie.year))
            .body("id", Matchers.equalTo(updatedMovie.id))

        // teardown
        movieService.delete(newMovieId)
    }

    @Test
    fun `update a movie with no id`() {
        // arrange
        val newMovie = Movie(
            name = "The wolf of Wall Street",
            year = 2013,
            id = null
        )

        // act
        given()
            .body(newMovie)
            .contentType(ContentType.JSON)
            .put("/movies")
            .then()
            // assert
            .statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `update a movie with existing movie name`() {
        // arrange
        val newMovie = Movie(
            name = fightClub.name,
            year = 2013,
            id = illusionist.id
        )

        // act
        given()
            .body(newMovie)
            .contentType(ContentType.JSON)
            .put("/movies")
            .then()
            // assert
            .statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `delete a movie`() {
        // arrange
        val newMovie = Movie(
            name = "The wolf of Wall Street",
            year = 2012,
            id = null
        )

        val newMovieId: Int = given()
            .body(newMovie)
            .contentType(ContentType.JSON)
            .post("/movies")
            .then()
            .extract()
            .path("id")

        // act
        delete("/movies/{id}", newMovieId)
            .then()
            // assert
            .statusCode(HttpStatus.NO_CONTENT.value())

        Assertions.assertThat(movieService.get(newMovieId)).isNull()
    }
}