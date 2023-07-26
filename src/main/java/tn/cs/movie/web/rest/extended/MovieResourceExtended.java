package tn.cs.movie.web.rest.extended;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import tn.cs.movie.domain.Movie;
import tn.cs.movie.repository.extended.MovieRepositoryExtended;
import tn.cs.movie.service.extended.MovieServiceExtended;
import tn.cs.movie.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping("/api/extended")
public class MovieResourceExtended {

    private final Logger log = LoggerFactory.getLogger(MovieResourceExtended.class);
    private final MovieServiceExtended movieServiceExtended;
    private final MovieRepositoryExtended movieRepositoryExtended;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public MovieResourceExtended(MovieServiceExtended movieServiceExtended, MovieRepositoryExtended movieRepositoryExtended) {
        this.movieServiceExtended = movieServiceExtended;
        this.movieRepositoryExtended = movieRepositoryExtended;
    }

    @GetMapping("/movies")
    public List<Movie> getAllMoviesByFilter(@RequestParam(required = false, name = "search") String search) {
        log.debug("REST request to get all Movies");
        return movieServiceExtended.getMoviesBySearch(search);
    }

    @PostMapping("/movies")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        log.debug("Create  Movies");
        Movie createdMovied = movieServiceExtended.createMovie(movie);
        return ResponseEntity.status(201).body(createdMovied);
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable Long id) {
        log.debug("REST request to get Movie : {}", id);
        Optional<Movie> movie = movieServiceExtended.getMovieById(id);
        return ResponseUtil.wrapOrNotFound(movie);
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<Movie> movieUpdate(@PathVariable(value = "id", required = false) final Long id, @RequestBody Movie movie)
        throws URISyntaxException {
        log.debug("REST request to update Movie : {}, {}", id, movie);
        if (movie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, movie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!movieRepositoryExtended.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Movie result = movieServiceExtended.movieUpdate(movie);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movie.getId().toString()))
            .body(result);
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        log.debug("REST request to delete Movie : {}", id);
        movieServiceExtended.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
