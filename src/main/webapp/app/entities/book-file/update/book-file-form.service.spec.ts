import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../book-file.test-samples';

import { BookFileFormService } from './book-file-form.service';

describe('BookFile Form Service', () => {
  let service: BookFileFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookFileFormService);
  });

  describe('Service methods', () => {
    describe('createBookFileFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBookFileFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pathNorm: expect.any(Object),
            sha256: expect.any(Object),
            sizeBytes: expect.any(Object),
            mtime: expect.any(Object),
            storageUri: expect.any(Object),
            firstSeenAt: expect.any(Object),
            lastSeenAt: expect.any(Object),
            book: expect.any(Object),
          }),
        );
      });

      it('passing IBookFile should create a new form with FormGroup', () => {
        const formGroup = service.createBookFileFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pathNorm: expect.any(Object),
            sha256: expect.any(Object),
            sizeBytes: expect.any(Object),
            mtime: expect.any(Object),
            storageUri: expect.any(Object),
            firstSeenAt: expect.any(Object),
            lastSeenAt: expect.any(Object),
            book: expect.any(Object),
          }),
        );
      });
    });

    describe('getBookFile', () => {
      it('should return NewBookFile for default BookFile initial value', () => {
        const formGroup = service.createBookFileFormGroup(sampleWithNewData);

        const bookFile = service.getBookFile(formGroup) as any;

        expect(bookFile).toMatchObject(sampleWithNewData);
      });

      it('should return NewBookFile for empty BookFile initial value', () => {
        const formGroup = service.createBookFileFormGroup();

        const bookFile = service.getBookFile(formGroup) as any;

        expect(bookFile).toMatchObject({});
      });

      it('should return IBookFile', () => {
        const formGroup = service.createBookFileFormGroup(sampleWithRequiredData);

        const bookFile = service.getBookFile(formGroup) as any;

        expect(bookFile).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBookFile should not enable id FormControl', () => {
        const formGroup = service.createBookFileFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBookFile should disable id FormControl', () => {
        const formGroup = service.createBookFileFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
