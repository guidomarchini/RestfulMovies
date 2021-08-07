package veact.gmarchini.movies.application

class MovieNotFoundException(idNotFound: Int): Exception("Can't find a movie for the given id: $idNotFound")

class ValidationError(message: String): Exception(message)
