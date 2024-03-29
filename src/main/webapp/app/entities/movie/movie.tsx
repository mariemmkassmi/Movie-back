import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { DurationFormat } from 'app/shared/DurationFormat';

import { IMovie } from 'app/shared/model/movie.model';
import { getEntities } from './movie.reducer';

export const Movie = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const movieList = useAppSelector(state => state.movie.entities);
  const loading = useAppSelector(state => state.movie.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="movie-heading" data-cy="MovieHeading">
        <Translate contentKey="movieApp.movie.home.title">Movies</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="movieApp.movie.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/movie/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="movieApp.movie.home.createLabel">Create new Movie</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {movieList && movieList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="movieApp.movie.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="movieApp.movie.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="movieApp.movie.duration">Duration</Translate>
                </th>
                <th>
                  <Translate contentKey="movieApp.movie.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="movieApp.movie.language">Language</Translate>
                </th>
                <th>
                  <Translate contentKey="movieApp.movie.imageUrl">Image Url</Translate>
                </th>
                <th>
                  <Translate contentKey="movieApp.movie.publishDate">Publish Date</Translate>
                </th>
                <th>
                  <Translate contentKey="movieApp.movie.membreStaff">Membre Staff</Translate>
                </th>
                <th>
                  <Translate contentKey="movieApp.movie.category">Category</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {movieList.map((movie, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/movie/${movie.id}`} color="link" size="sm">
                      {movie.id}
                    </Button>
                  </td>
                  <td>{movie.name}</td>
                  <td>{movie.duration ? <DurationFormat value={movie.duration} /> : null}</td>
                  <td>{movie.description}</td>
                  <td>
                    <Translate contentKey={`movieApp.Langue.${movie.language}`} />
                  </td>
                  <td>{movie.imageUrl}</td>
                  <td>{movie.publishDate ? <TextFormat type="date" value={movie.publishDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {movie.membreStaffs
                      ? movie.membreStaffs.map((val, j) => (
                          <span key={j}>
                            <Link to={`/staff/${val.id}`}>{val.id}</Link>
                            {j === movie.membreStaffs.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {movie.categories
                      ? movie.categories.map((val, j) => (
                          <span key={j}>
                            <Link to={`/category/${val.id}`}>{val.id}</Link>
                            {j === movie.categories.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/movie/${movie.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/movie/${movie.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/movie/${movie.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="movieApp.movie.home.notFound">No Movies found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Movie;
