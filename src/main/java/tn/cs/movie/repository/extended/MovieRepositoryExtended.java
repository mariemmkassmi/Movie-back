package tn.cs.movie.repository.extended;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.cs.movie.domain.Movie;
import tn.cs.movie.repository.MovieRepository;
import tn.cs.movie.repository.MovieRepositoryWithBagRelationships;

@Repository
public interface MovieRepositoryExtended extends MovieRepositoryWithBagRelationships, MovieRepository {
    @Query(
        "SELECT m FROM Movie m LEFT JOIN m.categories category " +
        "LEFT JOIN m.membreStaffs staff " +
        "WHERE (:search IS NULL OR :search = '' OR " +
        "LOWER(m.name) LIKE CONCAT('%', LOWER(:search), '%') OR " +
        "LOWER(category.name) LIKE CONCAT('%', LOWER(:search), '%') OR " +
        "LOWER(staff.firstName) LIKE CONCAT('%', LOWER(:search), '%') OR " +
        "LOWER(staff.lastName) LIKE CONCAT('%', LOWER(:search), '%'))"
    )
    List<Movie> getMoviesBySearch(@Param("search") String search);
}
