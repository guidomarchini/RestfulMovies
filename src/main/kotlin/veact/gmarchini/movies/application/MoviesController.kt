package veact.gmarchini.movies.application

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import veact.gmarchini.movies.application.validation.MovieValidator
import veact.gmarchini.movies.model.Movie
import veact.gmarchini.movies.service.MovieService

@RestController
@RequestMapping("/movies")
class MoviesController constructor(
    private val movieService: MovieService,
    private val movieValidator: MovieValidator
) {

    companion object {
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(MoviesController::class.java)
    }

    @GetMapping
    fun getAllMovies(@RequestParam year: Int?): List<Movie> {
        logger.info("Retrieving all Movies" + year?.let { " with year $it" })
        return if(year != null) movieService.getByYear(year) else movieService.getAll()
    }

    @GetMapping("/{id}")
    fun getMovie(@PathVariable id: Int): Movie {
        logger.info("Retrieving movie with id: $id")
        return movieService.get(id) ?: throw MovieNotFoundException(id)
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun createMovie(@RequestBody movie: Movie): Movie {
        logger.info("Creating movie: $movie")
        movieValidator.validateForCreate(movie)
        return movieService.create(movie)
    }

    @PutMapping()
    fun update(@RequestBody movie: Movie): Movie {
        logger.info("Updating movie: $movie")
        movieValidator.validateForUpdate(movie)
        return movieService.update(movie)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteExercise(@PathVariable id: Int): Unit {
        logger.info("Deleting Exercise with id: $id")
        movieService.delete(id)
    }
}