import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../book-page-text.test-samples';

import { BookPageTextFormService } from './book-page-text-form.service';

describe('BookPageText Form Service', () => {
  let service: BookPageTextFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookPageTextFormService);
  });

  describe('Service methods', () => {
    describe('createBookPageTextFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBookPageTextFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            pageNo: expect.any(Object),
            text: expect.any(Object),
            book: expect.any(Object),
          }),
        );
      });

      it('passing IBookPageText should create a new form with FormGroup', () => {
        const formGroup = service.createBookPageTextFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            pageNo: expect.any(Object),
            text: expect.any(Object),
            book: expect.any(Object),
          }),
        );
      });
    });

    describe('getBookPageText', () => {
      it('should return NewBookPageText for default BookPageText initial value', () => {
        const formGroup = service.createBookPageTextFormGroup(sampleWithNewData);

        const bookPageText = service.getBookPageText(formGroup) as any;

        expect(bookPageText).toMatchObject(sampleWithNewData);
      });

      it('should return NewBookPageText for empty BookPageText initial value', () => {
        const formGroup = service.createBookPageTextFormGroup();

        const bookPageText = service.getBookPageText(formGroup) as any;

        expect(bookPageText).toMatchObject({});
      });

      it('should return IBookPageText', () => {
        const formGroup = service.createBookPageTextFormGroup(sampleWithRequiredData);

        const bookPageText = service.getBookPageText(formGroup) as any;

        expect(bookPageText).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBookPageText should not enable id FormControl', () => {
        const formGroup = service.createBookPageTextFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBookPageText should disable id FormControl', () => {
        const formGroup = service.createBookPageTextFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
