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

import { getEntities } from './ingest-run.reducer';

export const IngestRun = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const ingestRunList = useAppSelector(state => state.ingestRun.entities);
  const loading = useAppSelector(state => state.ingestRun.loading);

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
      <h2 id="ingest-run-heading" data-cy="IngestRunHeading">
        <Translate contentKey="booksEtlApp.ingestRun.home.title">Ingest Runs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="booksEtlApp.ingestRun.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/ingest-run/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="booksEtlApp.ingestRun.home.createLabel">Create new Ingest Run</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ingestRunList && ingestRunList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="booksEtlApp.ingestRun.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('startedAt')}>
                  <Translate contentKey="booksEtlApp.ingestRun.startedAt">Started At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startedAt')} />
                </th>
                <th className="hand" onClick={sort('finishedAt')}>
                  <Translate contentKey="booksEtlApp.ingestRun.finishedAt">Finished At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('finishedAt')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="booksEtlApp.ingestRun.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('filesSeen')}>
                  <Translate contentKey="booksEtlApp.ingestRun.filesSeen">Files Seen</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('filesSeen')} />
                </th>
                <th className="hand" onClick={sort('filesParsed')}>
                  <Translate contentKey="booksEtlApp.ingestRun.filesParsed">Files Parsed</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('filesParsed')} />
                </th>
                <th className="hand" onClick={sort('filesFailed')}>
                  <Translate contentKey="booksEtlApp.ingestRun.filesFailed">Files Failed</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('filesFailed')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ingestRunList.map((ingestRun, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/ingest-run/${ingestRun.id}`} color="link" size="sm">
                      {ingestRun.id}
                    </Button>
                  </td>
                  <td>{ingestRun.startedAt ? <TextFormat type="date" value={ingestRun.startedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{ingestRun.finishedAt ? <TextFormat type="date" value={ingestRun.finishedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{ingestRun.status}</td>
                  <td>{ingestRun.filesSeen}</td>
                  <td>{ingestRun.filesParsed}</td>
                  <td>{ingestRun.filesFailed}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/ingest-run/${ingestRun.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/ingest-run/${ingestRun.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/ingest-run/${ingestRun.id}/delete`)}
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
              <Translate contentKey="booksEtlApp.ingestRun.home.notFound">No Ingest Runs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default IngestRun;
