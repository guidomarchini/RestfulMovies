package veact.gmarchini.movies.service.mapper

import org.springframework.stereotype.Component
import veact.gmarchini.movies.model.Movie
import veact.gmarchini.movies.persistence.entity.MovieEntity

@Component
class MovieMapper {
    fun toModel(entity: MovieEntity): Movie =
        Movie(
            id = entity.id,
            name = entity.name,
            year = entity.year
        )

    fun toEntity(model: Movie): MovieEntity =
        MovieEntity(
            id = model.id,
            name = model.name,
            year = model.year
        )
}