import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './book-file.reducer';

export const BookFileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bookFileEntity = useAppSelector(state => state.bookFile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookFileDetailsHeading">
          <Translate contentKey="booksEtlApp.bookFile.detail.title">BookFile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bookFileEntity.id}</dd>
          <dt>
            <span id="pathNorm">
              <Translate contentKey="booksEtlApp.bookFile.pathNorm">Path Norm</Translate>
            </span>
          </dt>
          <dd>{bookFileEntity.pathNorm}</dd>
          <dt>
            <span id="sha256">
              <Translate contentKey="booksEtlApp.bookFile.sha256">Sha 256</Translate>
            </span>
          </dt>
          <dd>{bookFileEntity.sha256}</dd>
          <dt>
            <span id="sizeBytes">
              <Translate contentKey="booksEtlApp.bookFile.sizeBytes">Size Bytes</Translate>
            </span>
          </dt>
          <dd>{bookFileEntity.sizeBytes}</dd>
          <dt>
            <span id="mtime">
              <Translate contentKey="booksEtlApp.bookFile.mtime">Mtime</Translate>
            </span>
          </dt>
          <dd>{bookFileEntity.mtime ? <TextFormat value={bookFileEntity.mtime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="storageUri">
              <Translate contentKey="booksEtlApp.bookFile.storageUri">Storage Uri</Translate>
            </span>
          </dt>
          <dd>{bookFileEntity.storageUri}</dd>
          <dt>
            <span id="firstSeenAt">
              <Translate contentKey="booksEtlApp.bookFile.firstSeenAt">First Seen At</Translate>
            </span>
          </dt>
          <dd>
            {bookFileEntity.firstSeenAt ? <TextFormat value={bookFileEntity.firstSeenAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastSeenAt">
              <Translate contentKey="booksEtlApp.bookFile.lastSeenAt">Last Seen At</Translate>
            </span>
          </dt>
          <dd>
            {bookFileEntity.lastSeenAt ? <TextFormat value={bookFileEntity.lastSeenAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="booksEtlApp.bookFile.book">Book</Translate>
          </dt>
          <dd>{bookFileEntity.book ? bookFileEntity.book.documentId : ''}</dd>
        </dl>
        <Button tag={Link} to="/book-file" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/book-file/${bookFileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BookFileDetail;
