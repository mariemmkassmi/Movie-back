package tn.cs.movie.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import tn.cs.movie.domain.Movie;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class MovieRepositoryWithBagRelationshipsImpl implements MovieRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Movie> fetchBagRelationships(Optional<Movie> movie) {
        return movie.map(this::fetchMembreStaffs).map(this::fetchCategories);
    }

    @Override
    public Page<Movie> fetchBagRelationships(Page<Movie> movies) {
        return new PageImpl<>(fetchBagRelationships(movies.getContent()), movies.getPageable(), movies.getTotalElements());
    }

    @Override
    public List<Movie> fetchBagRelationships(List<Movie> movies) {
        return Optional.of(movies).map(this::fetchMembreStaffs).map(this::fetchCategories).orElse(Collections.emptyList());
    }

    Movie fetchMembreStaffs(Movie result) {
        return entityManager
            .createQuery("select movie from Movie movie left join fetch movie.membreStaffs where movie is :movie", Movie.class)
            .setParameter("movie", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Movie> fetchMembreStaffs(List<Movie> movies) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, movies.size()).forEach(index -> order.put(movies.get(index).getId(), index));
        List<Movie> result = entityManager
            .createQuery("select distinct movie from Movie movie left join fetch movie.membreStaffs where movie in :movies", Movie.class)
            .setParameter("movies", movies)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Movie fetchCategories(Movie result) {
        return entityManager
            .createQuery("select movie from Movie movie left join fetch movie.categories where movie is :movie", Movie.class)
            .setParameter("movie", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Movie> fetchCategories(List<Movie> movies) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, movies.size()).forEach(index -> order.put(movies.get(index).getId(), index));
        List<Movie> result = entityManager
            .createQuery("select distinct movie from Movie movie left join fetch movie.categories where movie in :movies", Movie.class)
            .setParameter("movies", movies)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
