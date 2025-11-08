import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ingest-run.test-samples';

import { IngestRunFormService } from './ingest-run-form.service';

describe('IngestRun Form Service', () => {
  let service: IngestRunFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IngestRunFormService);
  });

  describe('Service methods', () => {
    describe('createIngestRunFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIngestRunFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startedAt: expect.any(Object),
            finishedAt: expect.any(Object),
            status: expect.any(Object),
            filesSeen: expect.any(Object),
            filesParsed: expect.any(Object),
            filesFailed: expect.any(Object),
          }),
        );
      });

      it('passing IIngestRun should create a new form with FormGroup', () => {
        const formGroup = service.createIngestRunFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startedAt: expect.any(Object),
            finishedAt: expect.any(Object),
            status: expect.any(Object),
            filesSeen: expect.any(Object),
            filesParsed: expect.any(Object),
            filesFailed: expect.any(Object),
          }),
        );
      });
    });

    describe('getIngestRun', () => {
      it('should return NewIngestRun for default IngestRun initial value', () => {
        const formGroup = service.createIngestRunFormGroup(sampleWithNewData);

        const ingestRun = service.getIngestRun(formGroup) as any;

        expect(ingestRun).toMatchObject(sampleWithNewData);
      });

      it('should return NewIngestRun for empty IngestRun initial value', () => {
        const formGroup = service.createIngestRunFormGroup();

        const ingestRun = service.getIngestRun(formGroup) as any;

        expect(ingestRun).toMatchObject({});
      });

      it('should return IIngestRun', () => {
        const formGroup = service.createIngestRunFormGroup(sampleWithRequiredData);

        const ingestRun = service.getIngestRun(formGroup) as any;

        expect(ingestRun).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIngestRun should not enable id FormControl', () => {
        const formGroup = service.createIngestRunFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIngestRun should disable id FormControl', () => {
        const formGroup = service.createIngestRunFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
