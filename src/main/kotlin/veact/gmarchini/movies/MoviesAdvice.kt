package veact.gmarchini.movies

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import veact.gmarchini.movies.application.ErrorResponse
import veact.gmarchini.movies.application.MovieNotFoundException
import veact.gmarchini.movies.application.ValidationError

/**
 * Defines the exceptions return http status code
 */
@ControllerAdvice
class MoviesAdvice {
    companion object {
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(MoviesAdvice::class.java)
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun entityNotFoundHandler(exception: MovieNotFoundException): ErrorResponse {
        logger.error(exception.toString(), exception)
        return ErrorResponse(exception.localizedMessage)
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun invalidRequestHandler(error: ValidationError): ErrorResponse {
        logger.error("validation error: $error", error)
        return ErrorResponse(error.localizedMessage)
    }
}