import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getBooks } from 'app/entities/book/book.reducer';
import { createEntity, getEntity, reset, updateEntity } from './book-file.reducer';

export const BookFileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const books = useAppSelector(state => state.book.entities);
  const bookFileEntity = useAppSelector(state => state.bookFile.entity);
  const loading = useAppSelector(state => state.bookFile.loading);
  const updating = useAppSelector(state => state.bookFile.updating);
  const updateSuccess = useAppSelector(state => state.bookFile.updateSuccess);

  const handleClose = () => {
    navigate(`/book-file${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBooks({}));
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
    if (values.sizeBytes !== undefined && typeof values.sizeBytes !== 'number') {
      values.sizeBytes = Number(values.sizeBytes);
    }
    values.mtime = convertDateTimeToServer(values.mtime);
    values.firstSeenAt = convertDateTimeToServer(values.firstSeenAt);
    values.lastSeenAt = convertDateTimeToServer(values.lastSeenAt);

    const entity = {
      ...bookFileEntity,
      ...values,
      book: books.find(it => it.id.toString() === values.book?.toString()),
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
          mtime: displayDefaultDateTime(),
          firstSeenAt: displayDefaultDateTime(),
          lastSeenAt: displayDefaultDateTime(),
        }
      : {
          ...bookFileEntity,
          mtime: convertDateTimeFromServer(bookFileEntity.mtime),
          firstSeenAt: convertDateTimeFromServer(bookFileEntity.firstSeenAt),
          lastSeenAt: convertDateTimeFromServer(bookFileEntity.lastSeenAt),
          book: bookFileEntity?.book?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="booksEtlApp.bookFile.home.createOrEditLabel" data-cy="BookFileCreateUpdateHeading">
            <Translate contentKey="booksEtlApp.bookFile.home.createOrEditLabel">Create or edit a BookFile</Translate>
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
                  id="book-file-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('booksEtlApp.bookFile.pathNorm')}
                id="book-file-pathNorm"
                name="pathNorm"
                data-cy="pathNorm"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('booksEtlApp.bookFile.sha256')}
                id="book-file-sha256"
                name="sha256"
                data-cy="sha256"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 10, message: translate('entity.validation.minlength', { min: 10 }) },
                  maxLength: { value: 64, message: translate('entity.validation.maxlength', { max: 64 }) },
                }}
              />
              <ValidatedField
                label={translate('booksEtlApp.bookFile.sizeBytes')}
                id="book-file-sizeBytes"
                name="sizeBytes"
                data-cy="sizeBytes"
                type="text"
              />
              <ValidatedField
                label={translate('booksEtlApp.bookFile.mtime')}
                id="book-file-mtime"
                name="mtime"
                data-cy="mtime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('booksEtlApp.bookFile.storageUri')}
                id="book-file-storageUri"
                name="storageUri"
                data-cy="storageUri"
                type="textarea"
              />
              <ValidatedField
                label={translate('booksEtlApp.bookFile.firstSeenAt')}
                id="book-file-firstSeenAt"
                name="firstSeenAt"
                data-cy="firstSeenAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('booksEtlApp.bookFile.lastSeenAt')}
                id="book-file-lastSeenAt"
                name="lastSeenAt"
                data-cy="lastSeenAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="book-file-book" name="book" data-cy="book" label={translate('booksEtlApp.bookFile.book')} type="select">
                <option value="" key="0" />
                {books
                  ? books.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.documentId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/book-file" replace color="info">
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

export default BookFileUpdate;
