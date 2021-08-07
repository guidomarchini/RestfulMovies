package veact.gmarchini.movies.service

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import veact.gmarchini.movies.model.Movie
import veact.gmarchini.movies.persistence.MovieRepository
import veact.gmarchini.movies.persistence.entity.MovieEntity
import veact.gmarchini.movies.service.mapper.MovieMapper
import java.util.*

internal class MovieServiceTest {
    lateinit var movieService: MovieService
    lateinit var repository: MovieRepository
    lateinit var mapper: MovieMapper

    val modelObject: Movie = Movie(
        name = "Fight club",
        year = 1999,
        id = 1
    )
    val entityObject: MovieEntity = MovieEntity(
        name = "Fight club",
        year = 1999,
        id = 1
    )

    @BeforeEach
    fun setUp() {
        repository = mock()
        mapper = mock()
        movieService = MovieService(
            repository = repository,
            mapper = mapper
        )

        whenever(mapper.toEntity(modelObject)).thenReturn(entityObject)
        whenever(mapper.toModel(entityObject)).thenReturn(modelObject)
    }

    @Test
    fun `creates a movie`() {
        // arrange
        whenever(repository.save(entityObject)).thenReturn(entityObject)

        // act
        movieService.create(modelObject)

        // assert
        verify(mapper).toEntity(modelObject)
        verify(repository).save(entityObject)
        verify(mapper).toModel(entityObject)
    }

    @Test
    fun `it gets movies by id`() {
        // arrange
        val id: Int = 0
        whenever(repository.findById(id))
            .thenReturn(Optional.of(entityObject))

        // act
        val result: Movie? =
            movieService.get(id)

        // assert
        Assertions.assertThat(modelObject).isEqualTo(result)
    }

    @Test
    fun `it gets all movies`() {
        // arrange
        whenever(repository.getAllByOrderByName())
            .thenReturn(listOf(entityObject))

        // act
        val result: List<Movie> =
            movieService.getAll()

        // assert
        verify(mapper).toModel(entityObject)
        Assertions.assertThat(result.size).isEqualTo(1)
        Assertions.assertThat(modelObject).isEqualTo(result.first())
    }

    @Test
    fun `it gets movies by year`() {
        // arrange
        val year: Int = 1999
        whenever(repository.getAllByYearOrderByName(year))
            .thenReturn(listOf(entityObject))

        // act
        val result: List<Movie> =
            movieService.getByYear(year)

        // assert
        verify(mapper).toModel(entityObject)
        Assertions.assertThat(result.size).isEqualTo(1)
        Assertions.assertThat(modelObject).isEqualTo(result.first())
    }

    @Test
    fun `updates a movie`() {
        // arrange
        whenever(repository.save(entityObject)).thenReturn(entityObject)

        // act
        movieService.update(modelObject)

        // assert
        verify(mapper).toEntity(modelObject)
        verify(repository).save(entityObject)
        verify(mapper).toModel(entityObject)
    }

    @Test
    fun `it deletes a movie`() {
        // arrange
        val id: Int = 0

        // act
        movieService.delete(id)

        // assert
        verify(repository).deleteById(id)
    }
}