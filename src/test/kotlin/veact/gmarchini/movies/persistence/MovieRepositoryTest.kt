package veact.gmarchini.movies.persistence

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull
import veact.gmarchini.movies.persistence.entity.MovieEntity

@DataJpaTest
internal class MovieRepositoryTest @Autowired constructor(
    val entityManager: TestEntityManager,
    val repository: MovieRepository
) {
    @AfterEach
    fun afterEach() {
        repository.deleteAll()
    }

    @Test
    fun `it saves a new movie`() {
        // arrange
        val newInstance: MovieEntity = sampleEntity()

        // act
        val savedInstance: MovieEntity = repository.save(newInstance)

        // assert
        Assertions.assertThat(savedInstance.id).isNotNull
        Assertions.assertThat(savedInstance.name).isEqualTo(newInstance.name)
        Assertions.assertThat(savedInstance.year).isEqualTo(newInstance.year)
    }

    @Test
    fun `it returns a saved movie`() {
        // arrange
        val movie: MovieEntity = entityManager.persistAndFlush(sampleEntity())

        // act
        val found = repository.findByIdOrNull(movie.id!!)

        // assert
        Assertions.assertThat(found).isEqualTo(movie)
    }

    @Test
    fun `it removes a movie`() {
        // arrange
        val movie: MovieEntity = entityManager.persistAndFlush(sampleEntity())
        val entityId: Int = movie.id!!

        // act
        repository.deleteById(entityId)

        // assert
        Assertions.assertThat(repository.findByIdOrNull(entityId)).isNull()
    }

    @Test
    fun `it returns the movies ordered by name`() {
        // arrange
        entityManager.persistAndFlush(
            MovieEntity(
                name = "B",
                year = 1900
            )
        )
        val firstMovie: MovieEntity = entityManager.persistAndFlush(
            MovieEntity(
                name = "A",
                year = 1900
            )
        )

        // act
        val found = repository.getAllByOrderByName()

        // assert
        Assertions.assertThat(found).isNotEmpty
        Assertions.assertThat(found).hasSize(2)
        Assertions.assertThat(found.first()).isEqualTo(firstMovie)
    }

    @Test
    fun `it gets movies by year`() {
        // arrange
        val firstMovie: MovieEntity = entityManager.persistAndFlush(
            MovieEntity(
                name = "A",
                year = 1900
            )
        )
        val secondMovie: MovieEntity = entityManager.persistAndFlush(
            MovieEntity(
                name = "B",
                year = 1900
            )
        )
        entityManager.persistAndFlush(
            MovieEntity(
                name = "C",
                year = 1901
            )
        )

        // act
        val found = repository.getAllByYearOrderByName(1900)

        // assert
        Assertions.assertThat(found).isNotEmpty
        Assertions.assertThat(found).hasSize(2)
        Assertions.assertThat(found.first()).isEqualTo(firstMovie)
        Assertions.assertThat(found.toList()[1]).isEqualTo(secondMovie)
    }


    fun sampleEntity(): MovieEntity =
        MovieEntity(
            name = "Fight club",
            year = 1999
        )
}