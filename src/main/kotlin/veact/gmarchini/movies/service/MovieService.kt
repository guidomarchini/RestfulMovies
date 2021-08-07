package veact.gmarchini.movies.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import veact.gmarchini.movies.model.Movie
import veact.gmarchini.movies.persistence.MovieRepository
import veact.gmarchini.movies.service.mapper.MovieMapper

@Service
class MovieService @Autowired constructor(
    val repository: MovieRepository,
    val mapper: MovieMapper
) {
    fun create(movie: Movie): Movie =
        this.createOrUpdate(movie)

    fun get(id: Int): Movie? =
        repository.findByIdOrNull(id)?.let { mapper.toModel(it) }

    fun getByName(name: String): Movie? =
        repository.getByNameContainingIgnoreCase(name)?.let { mapper.toModel(it) }

    fun getAll(): List<Movie> =
        repository.getAllByOrderByName().map { mapper.toModel(it) }

    fun getByYear(year: Int) =
        repository.getAllByYearOrderByName(year).map { mapper.toModel(it) }

    fun update(movie: Movie) =
        this.createOrUpdate(movie)

    fun delete(id: Int) =
        repository.deleteById(id)

    private fun createOrUpdate(movie: Movie): Movie =
        mapper.toModel(
            repository.save(
                mapper.toEntity(movie)
            )
        )
}