package veact.gmarchini.movies.application.validation

import org.springframework.stereotype.Service
import veact.gmarchini.movies.application.MovieNotFoundException
import veact.gmarchini.movies.application.ValidationError
import veact.gmarchini.movies.model.Movie
import veact.gmarchini.movies.service.MovieService

/**
 * Movie validations
 */
@Service
class MovieValidator constructor(
    private val movieService: MovieService
){
    /**
     * Validates that a movie with the given name isn't created
     */
    fun validateForCreate(movie: Movie): Unit {
        movieService.getByName(movie.name)?.let {
            throw ValidationError("A movie with name ${movie.name} already exists")
        }
    }

    /**
     * Validates that the given movie has an id,
     * the movie exists
     * and no other movie with that name exists
     */
    fun validateForUpdate(movie: Movie) {
        // id should not be null
        if (movie.id == null){
            throw ValidationError("movie id should not be null")
        }

        // name must be unique
        movieService.getByName(movie.name)?.let {
            if (it.id != movie.id) {
                throw ValidationError("A movie with name ${movie.name} already exists")
            }
        }
    }
}