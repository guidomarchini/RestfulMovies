package veact.gmarchini.movies.persistence

import org.springframework.data.repository.CrudRepository
import veact.gmarchini.movies.persistence.entity.MovieEntity

/**
 * A Movie repository.
 * Aside of the CRUD functionality, it can fetch all movies or all movies for the given year.
 */
interface MovieRepository : CrudRepository<MovieEntity, Int> {
    fun getByNameIgnoreCase(name: String): MovieEntity?
    fun getAllByOrderByName(): Iterable<MovieEntity>
    fun getAllByYearOrderByName(year: Int): Iterable<MovieEntity>
}