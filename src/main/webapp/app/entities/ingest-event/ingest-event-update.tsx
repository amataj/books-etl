import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getIngestRuns } from 'app/entities/ingest-run/ingest-run.reducer';
import { createEntity, getEntity, reset, updateEntity } from './ingest-event.reducer';

export const IngestEventUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ingestRuns = useAppSelector(state => state.ingestRun.entities);
  const ingestEventEntity = useAppSelector(state => state.ingestEvent.entity);
  const loading = useAppSelector(state => state.ingestEvent.loading);
  const updating = useAppSelector(state => state.ingestEvent.updating);
  const updateSuccess = useAppSelector(state => state.ingestEvent.updateSuccess);

  const handleClose = () => {
    navigate('/ingest-event');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getIngestRuns({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);

    const entity = {
      ...ingestEventEntity,
      ...values,
      ingestRun: ingestRuns.find(it => it.id.toString() === values.ingestRun?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
        }
      : {
          ...ingestEventEntity,
          createdAt: convertDateTimeFromServer(ingestEventEntity.createdAt),
          ingestRun: ingestEventEntity?.ingestRun?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="booksEtlApp.ingestEvent.home.createOrEditLabel" data-cy="IngestEventCreateUpdateHeading">
            <Translate contentKey="booksEtlApp.ingestEvent.home.createOrEditLabel">Create or edit a IngestEvent</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="ingest-event-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('booksEtlApp.ingestEvent.runId')}
                id="ingest-event-runId"
                name="runId"
                data-cy="runId"
                type="text"
              />
              <ValidatedField
                label={translate('booksEtlApp.ingestEvent.documentId')}
                id="ingest-event-documentId"
                name="documentId"
                data-cy="documentId"
                type="text"
                validate={{
                  maxLength: { value: 64, message: translate('entity.validation.maxlength', { max: 64 }) },
                }}
              />
              <ValidatedField
                label={translate('booksEtlApp.ingestEvent.topic')}
                id="ingest-event-topic"
                name="topic"
                data-cy="topic"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 64, message: translate('entity.validation.maxlength', { max: 64 }) },
                }}
              />
              <ValidatedField
                label={translate('booksEtlApp.ingestEvent.payload')}
                id="ingest-event-payload"
                name="payload"
                data-cy="payload"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('booksEtlApp.ingestEvent.createdAt')}
                id="ingest-event-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="ingest-event-ingestRun"
                name="ingestRun"
                data-cy="ingestRun"
                label={translate('booksEtlApp.ingestEvent.ingestRun')}
                type="select"
              >
                <option value="" key="0" />
                {ingestRuns
                  ? ingestRuns.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ingest-event" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default IngestEventUpdate;
