import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './book-page-text.reducer';

export const BookPageTextDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bookPageTextEntity = useAppSelector(state => state.bookPageText.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookPageTextDetailsHeading">
          <Translate contentKey="booksEtlApp.bookPageText.detail.title">BookPageText</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bookPageTextEntity.id}</dd>
          <dt>
            <span id="documentId">
              <Translate contentKey="booksEtlApp.bookPageText.documentId">Document Id</Translate>
            </span>
          </dt>
          <dd>{bookPageTextEntity.documentId}</dd>
          <dt>
            <span id="pageNo">
              <Translate contentKey="booksEtlApp.bookPageText.pageNo">Page No</Translate>
            </span>
          </dt>
          <dd>{bookPageTextEntity.pageNo}</dd>
          <dt>
            <span id="text">
              <Translate contentKey="booksEtlApp.bookPageText.text">Text</Translate>
            </span>
          </dt>
          <dd>{bookPageTextEntity.text}</dd>
          <dt>
            <Translate contentKey="booksEtlApp.bookPageText.book">Book</Translate>
          </dt>
          <dd>{bookPageTextEntity.book ? bookPageTextEntity.book.documentId : ''}</dd>
        </dl>
        <Button tag={Link} to="/book-page-text" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/book-page-text/${bookPageTextEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BookPageTextDetail;
