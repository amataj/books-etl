import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ingest-run.reducer';

export const IngestRunDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ingestRunEntity = useAppSelector(state => state.ingestRun.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ingestRunDetailsHeading">
          <Translate contentKey="booksEtlApp.ingestRun.detail.title">IngestRun</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ingestRunEntity.id}</dd>
          <dt>
            <span id="startedAt">
              <Translate contentKey="booksEtlApp.ingestRun.startedAt">Started At</Translate>
            </span>
          </dt>
          <dd>
            {ingestRunEntity.startedAt ? <TextFormat value={ingestRunEntity.startedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="finishedAt">
              <Translate contentKey="booksEtlApp.ingestRun.finishedAt">Finished At</Translate>
            </span>
          </dt>
          <dd>
            {ingestRunEntity.finishedAt ? <TextFormat value={ingestRunEntity.finishedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="booksEtlApp.ingestRun.status">Status</Translate>
            </span>
          </dt>
          <dd>{ingestRunEntity.status}</dd>
          <dt>
            <span id="filesSeen">
              <Translate contentKey="booksEtlApp.ingestRun.filesSeen">Files Seen</Translate>
            </span>
          </dt>
          <dd>{ingestRunEntity.filesSeen}</dd>
          <dt>
            <span id="filesParsed">
              <Translate contentKey="booksEtlApp.ingestRun.filesParsed">Files Parsed</Translate>
            </span>
          </dt>
          <dd>{ingestRunEntity.filesParsed}</dd>
          <dt>
            <span id="filesFailed">
              <Translate contentKey="booksEtlApp.ingestRun.filesFailed">Files Failed</Translate>
            </span>
          </dt>
          <dd>{ingestRunEntity.filesFailed}</dd>
        </dl>
        <Button tag={Link} to="/ingest-run" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ingest-run/${ingestRunEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IngestRunDetail;
