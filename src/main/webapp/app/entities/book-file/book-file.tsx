import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './book-file.reducer';

export const BookFile = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const bookFileList = useAppSelector(state => state.bookFile.entities);
  const loading = useAppSelector(state => state.bookFile.loading);
  const totalItems = useAppSelector(state => state.bookFile.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="book-file-heading" data-cy="BookFileHeading">
        <Translate contentKey="booksEtlApp.bookFile.home.title">Book Files</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="booksEtlApp.bookFile.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/book-file/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="booksEtlApp.bookFile.home.createLabel">Create new Book File</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {bookFileList && bookFileList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="booksEtlApp.bookFile.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('pathNorm')}>
                  <Translate contentKey="booksEtlApp.bookFile.pathNorm">Path Norm</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('pathNorm')} />
                </th>
                <th className="hand" onClick={sort('sha256')}>
                  <Translate contentKey="booksEtlApp.bookFile.sha256">Sha 256</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sha256')} />
                </th>
                <th className="hand" onClick={sort('sizeBytes')}>
                  <Translate contentKey="booksEtlApp.bookFile.sizeBytes">Size Bytes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sizeBytes')} />
                </th>
                <th className="hand" onClick={sort('mtime')}>
                  <Translate contentKey="booksEtlApp.bookFile.mtime">Mtime</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mtime')} />
                </th>
                <th className="hand" onClick={sort('storageUri')}>
                  <Translate contentKey="booksEtlApp.bookFile.storageUri">Storage Uri</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('storageUri')} />
                </th>
                <th className="hand" onClick={sort('firstSeenAt')}>
                  <Translate contentKey="booksEtlApp.bookFile.firstSeenAt">First Seen At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('firstSeenAt')} />
                </th>
                <th className="hand" onClick={sort('lastSeenAt')}>
                  <Translate contentKey="booksEtlApp.bookFile.lastSeenAt">Last Seen At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastSeenAt')} />
                </th>
                <th>
                  <Translate contentKey="booksEtlApp.bookFile.book">Book</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {bookFileList.map((bookFile, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/book-file/${bookFile.id}`} color="link" size="sm">
                      {bookFile.id}
                    </Button>
                  </td>
                  <td>{bookFile.pathNorm}</td>
                  <td>{bookFile.sha256}</td>
                  <td>{bookFile.sizeBytes}</td>
                  <td>{bookFile.mtime ? <TextFormat type="date" value={bookFile.mtime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{bookFile.storageUri}</td>
                  <td>{bookFile.firstSeenAt ? <TextFormat type="date" value={bookFile.firstSeenAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{bookFile.lastSeenAt ? <TextFormat type="date" value={bookFile.lastSeenAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{bookFile.book ? <Link to={`/book/${bookFile.book.id}`}>{bookFile.book.documentId}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/book-file/${bookFile.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/book-file/${bookFile.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/book-file/${bookFile.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
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
              <Translate contentKey="booksEtlApp.bookFile.home.notFound">No Book Files found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={bookFileList && bookFileList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default BookFile;
