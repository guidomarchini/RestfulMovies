package veact.gmarchini.movies.service.mapper

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import veact.gmarchini.movies.model.Movie
import veact.gmarchini.movies.persistence.entity.MovieEntity

internal class MovieMapperTest {
    lateinit var movieMapper: MovieMapper

    @BeforeEach
    fun setUp() {
       movieMapper = MovieMapper()
    }

    @Test
    fun `maps model to entity`() {
        // arrange
        val model: Movie = Movie(
            name = "Fight club",
            year = 1999,
            id = 1
        )

        // act
        val entity: MovieEntity = movieMapper.toEntity(model)

        // assert
        Assertions.assertThat(entity.name).isEqualTo(model.name)
        Assertions.assertThat(entity.year).isEqualTo(model.year)
        Assertions.assertThat(entity.id).isEqualTo(model.id)
    }

    @Test
    fun `maps entity to model`() {
        // arrange
        val entity: MovieEntity = MovieEntity(
            name = "Fight club",
            year = 1999,
            id = 1
        )

        // act
        val model: Movie = movieMapper.toModel(entity)

        // assert
        Assertions.assertThat(model.name).isEqualTo(entity.name)
        Assertions.assertThat(model.year).isEqualTo(entity.year)
        Assertions.assertThat(model.id).isEqualTo(entity.id)
    }
}