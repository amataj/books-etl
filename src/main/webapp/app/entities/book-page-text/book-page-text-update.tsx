import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getBooks } from 'app/entities/book/book.reducer';
import { createEntity, getEntity, reset, updateEntity } from './book-page-text.reducer';

export const BookPageTextUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const books = useAppSelector(state => state.book.entities);
  const bookPageTextEntity = useAppSelector(state => state.bookPageText.entity);
  const loading = useAppSelector(state => state.bookPageText.loading);
  const updating = useAppSelector(state => state.bookPageText.updating);
  const updateSuccess = useAppSelector(state => state.bookPageText.updateSuccess);

  const handleClose = () => {
    navigate(`/book-page-text${location.search}`);
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
    if (values.pageNo !== undefined && typeof values.pageNo !== 'number') {
      values.pageNo = Number(values.pageNo);
    }

    const entity = {
      ...bookPageTextEntity,
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
      ? {}
      : {
          ...bookPageTextEntity,
          book: bookPageTextEntity?.book?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="booksEtlApp.bookPageText.home.createOrEditLabel" data-cy="BookPageTextCreateUpdateHeading">
            <Translate contentKey="booksEtlApp.bookPageText.home.createOrEditLabel">Create or edit a BookPageText</Translate>
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
                  id="book-page-text-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('booksEtlApp.bookPageText.documentId')}
                id="book-page-text-documentId"
                name="documentId"
                data-cy="documentId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 10, message: translate('entity.validation.minlength', { min: 10 }) },
                  maxLength: { value: 64, message: translate('entity.validation.maxlength', { max: 64 }) },
                }}
              />
              <ValidatedField
                label={translate('booksEtlApp.bookPageText.pageNo')}
                id="book-page-text-pageNo"
                name="pageNo"
                data-cy="pageNo"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('booksEtlApp.bookPageText.text')}
                id="book-page-text-text"
                name="text"
                data-cy="text"
                type="textarea"
              />
              <ValidatedField
                id="book-page-text-book"
                name="book"
                data-cy="book"
                label={translate('booksEtlApp.bookPageText.book')}
                type="select"
              >
                <option value="" key="0" />
                {books
                  ? books.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.documentId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/book-page-text" replace color="info">
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

export default BookPageTextUpdate;
