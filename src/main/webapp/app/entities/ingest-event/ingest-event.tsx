import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './ingest-event.reducer';

export const IngestEvent = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const ingestEventList = useAppSelector(state => state.ingestEvent.entities);
  const loading = useAppSelector(state => state.ingestEvent.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="ingest-event-heading" data-cy="IngestEventHeading">
        <Translate contentKey="booksEtlApp.ingestEvent.home.title">Ingest Events</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="booksEtlApp.ingestEvent.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/ingest-event/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="booksEtlApp.ingestEvent.home.createLabel">Create new Ingest Event</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ingestEventList && ingestEventList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="booksEtlApp.ingestEvent.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('runId')}>
                  <Translate contentKey="booksEtlApp.ingestEvent.runId">Run Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('runId')} />
                </th>
                <th className="hand" onClick={sort('documentId')}>
                  <Translate contentKey="booksEtlApp.ingestEvent.documentId">Document Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('documentId')} />
                </th>
                <th className="hand" onClick={sort('topic')}>
                  <Translate contentKey="booksEtlApp.ingestEvent.topic">Topic</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('topic')} />
                </th>
                <th className="hand" onClick={sort('payload')}>
                  <Translate contentKey="booksEtlApp.ingestEvent.payload">Payload</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('payload')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="booksEtlApp.ingestEvent.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th>
                  <Translate contentKey="booksEtlApp.ingestEvent.ingestRun">Ingest Run</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ingestEventList.map((ingestEvent, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/ingest-event/${ingestEvent.id}`} color="link" size="sm">
                      {ingestEvent.id}
                    </Button>
                  </td>
                  <td>{ingestEvent.runId}</td>
                  <td>{ingestEvent.documentId}</td>
                  <td>{ingestEvent.topic}</td>
                  <td>{ingestEvent.payload}</td>
                  <td>
                    {ingestEvent.createdAt ? <TextFormat type="date" value={ingestEvent.createdAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {ingestEvent.ingestRun ? <Link to={`/ingest-run/${ingestEvent.ingestRun.id}`}>{ingestEvent.ingestRun.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/ingest-event/${ingestEvent.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/ingest-event/${ingestEvent.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/ingest-event/${ingestEvent.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="booksEtlApp.ingestEvent.home.notFound">No Ingest Events found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default IngestEvent;
