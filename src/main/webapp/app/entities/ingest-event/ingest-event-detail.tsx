import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ingest-event.reducer';

export const IngestEventDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ingestEventEntity = useAppSelector(state => state.ingestEvent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ingestEventDetailsHeading">
          <Translate contentKey="booksEtlApp.ingestEvent.detail.title">IngestEvent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ingestEventEntity.id}</dd>
          <dt>
            <span id="runId">
              <Translate contentKey="booksEtlApp.ingestEvent.runId">Run Id</Translate>
            </span>
          </dt>
          <dd>{ingestEventEntity.runId}</dd>
          <dt>
            <span id="documentId">
              <Translate contentKey="booksEtlApp.ingestEvent.documentId">Document Id</Translate>
            </span>
          </dt>
          <dd>{ingestEventEntity.documentId}</dd>
          <dt>
            <span id="topic">
              <Translate contentKey="booksEtlApp.ingestEvent.topic">Topic</Translate>
            </span>
          </dt>
          <dd>{ingestEventEntity.topic}</dd>
          <dt>
            <span id="payload">
              <Translate contentKey="booksEtlApp.ingestEvent.payload">Payload</Translate>
            </span>
          </dt>
          <dd>{ingestEventEntity.payload}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="booksEtlApp.ingestEvent.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {ingestEventEntity.createdAt ? <TextFormat value={ingestEventEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="booksEtlApp.ingestEvent.ingestRun">Ingest Run</Translate>
          </dt>
          <dd>{ingestEventEntity.ingestRun ? ingestEventEntity.ingestRun.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ingest-event" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ingest-event/${ingestEventEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IngestEventDetail;
