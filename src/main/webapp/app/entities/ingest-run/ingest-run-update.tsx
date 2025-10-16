import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './ingest-run.reducer';

export const IngestRunUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ingestRunEntity = useAppSelector(state => state.ingestRun.entity);
  const loading = useAppSelector(state => state.ingestRun.loading);
  const updating = useAppSelector(state => state.ingestRun.updating);
  const updateSuccess = useAppSelector(state => state.ingestRun.updateSuccess);

  const handleClose = () => {
    navigate('/ingest-run');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    values.startedAt = convertDateTimeToServer(values.startedAt);
    values.finishedAt = convertDateTimeToServer(values.finishedAt);
    if (values.filesSeen !== undefined && typeof values.filesSeen !== 'number') {
      values.filesSeen = Number(values.filesSeen);
    }
    if (values.filesParsed !== undefined && typeof values.filesParsed !== 'number') {
      values.filesParsed = Number(values.filesParsed);
    }
    if (values.filesFailed !== undefined && typeof values.filesFailed !== 'number') {
      values.filesFailed = Number(values.filesFailed);
    }

    const entity = {
      ...ingestRunEntity,
      ...values,
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
          startedAt: displayDefaultDateTime(),
          finishedAt: displayDefaultDateTime(),
        }
      : {
          ...ingestRunEntity,
          startedAt: convertDateTimeFromServer(ingestRunEntity.startedAt),
          finishedAt: convertDateTimeFromServer(ingestRunEntity.finishedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="booksEtlApp.ingestRun.home.createOrEditLabel" data-cy="IngestRunCreateUpdateHeading">
            <Translate contentKey="booksEtlApp.ingestRun.home.createOrEditLabel">Create or edit a IngestRun</Translate>
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
                  id="ingest-run-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('booksEtlApp.ingestRun.startedAt')}
                id="ingest-run-startedAt"
                name="startedAt"
                data-cy="startedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('booksEtlApp.ingestRun.finishedAt')}
                id="ingest-run-finishedAt"
                name="finishedAt"
                data-cy="finishedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('booksEtlApp.ingestRun.status')}
                id="ingest-run-status"
                name="status"
                data-cy="status"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 32, message: translate('entity.validation.maxlength', { max: 32 }) },
                }}
              />
              <ValidatedField
                label={translate('booksEtlApp.ingestRun.filesSeen')}
                id="ingest-run-filesSeen"
                name="filesSeen"
                data-cy="filesSeen"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('booksEtlApp.ingestRun.filesParsed')}
                id="ingest-run-filesParsed"
                name="filesParsed"
                data-cy="filesParsed"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('booksEtlApp.ingestRun.filesFailed')}
                id="ingest-run-filesFailed"
                name="filesFailed"
                data-cy="filesFailed"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ingest-run" replace color="info">
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

export default IngestRunUpdate;
