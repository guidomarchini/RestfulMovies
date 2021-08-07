package veact.gmarchini.movies.application.validation

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import veact.gmarchini.movies.application.ValidationError
import veact.gmarchini.movies.model.Movie
import veact.gmarchini.movies.service.MovieService

internal class MovieValidatorTest {
    private lateinit var movieValidator: MovieValidator
    private lateinit var movieService: MovieService
    private val fightClub = Movie(
        name = "Fight club",
        year = 1999,
        id = 1
    )

    @BeforeEach
    fun setUp() {
        movieService = mock()
        movieValidator = MovieValidator(movieService)
    }

    @Test
    fun `validate a creation - pass`() {
        // arrange
        whenever(movieService.getByName(fightClub.name)).thenReturn(null)

        // act
        movieValidator.validateForCreate(fightClub)

        // assert - no error thrown
    }

    @Test
    fun `validate a creation - fail`() {
        // arrange
        val movie: Movie = Movie(
            name = fightClub.name,
            year = 1900,
            id = 1
        )
        whenever(movieService.getByName(fightClub.name)).thenReturn(movie)

        // act - assert
        assertThrows<ValidationError> {
            movieValidator.validateForCreate(fightClub)
        }
    }

    @Test
    fun `update a movie - pass`() {
        // arrange
        val movie: Movie = Movie(
            name = fightClub.name + " II",
            year = 1900,
            id = fightClub.id
        )
        whenever(movieService.getByName(movie.name)).thenReturn(fightClub)

        // act
        movieValidator.validateForUpdate(movie)

        // assert - no error
    }

    @Test
    fun `update a movie - fail - no id`() {
        // arrange
        val movie: Movie = Movie(
            name = fightClub.name + " II",
            year = 1900,
            id = null
        )

        // act - assert
        assertThrows<ValidationError> {
            movieValidator.validateForUpdate(movie)
        }
    }

    @Test
    fun `update a movie - fail - existing movie with same namae`() {
        // arrange
        val movie: Movie = Movie(
            name = fightClub.name + " II",
            year = 1900,
            id = fightClub.id!! + 1
        )
        whenever(movieService.getByName(movie.name)).thenReturn(fightClub)

        // act - assert
        assertThrows<ValidationError> {
            movieValidator.validateForUpdate(movie)
        }
    }
}