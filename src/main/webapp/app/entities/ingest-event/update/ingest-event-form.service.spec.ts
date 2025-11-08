import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ingest-event.test-samples';

import { IngestEventFormService } from './ingest-event-form.service';

describe('IngestEvent Form Service', () => {
  let service: IngestEventFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IngestEventFormService);
  });

  describe('Service methods', () => {
    describe('createIngestEventFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIngestEventFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            runId: expect.any(Object),
            documentId: expect.any(Object),
            topic: expect.any(Object),
            payload: expect.any(Object),
            createdAt: expect.any(Object),
            ingestRun: expect.any(Object),
          }),
        );
      });

      it('passing IIngestEvent should create a new form with FormGroup', () => {
        const formGroup = service.createIngestEventFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            runId: expect.any(Object),
            documentId: expect.any(Object),
            topic: expect.any(Object),
            payload: expect.any(Object),
            createdAt: expect.any(Object),
            ingestRun: expect.any(Object),
          }),
        );
      });
    });

    describe('getIngestEvent', () => {
      it('should return NewIngestEvent for default IngestEvent initial value', () => {
        const formGroup = service.createIngestEventFormGroup(sampleWithNewData);

        const ingestEvent = service.getIngestEvent(formGroup) as any;

        expect(ingestEvent).toMatchObject(sampleWithNewData);
      });

      it('should return NewIngestEvent for empty IngestEvent initial value', () => {
        const formGroup = service.createIngestEventFormGroup();

        const ingestEvent = service.getIngestEvent(formGroup) as any;

        expect(ingestEvent).toMatchObject({});
      });

      it('should return IIngestEvent', () => {
        const formGroup = service.createIngestEventFormGroup(sampleWithRequiredData);

        const ingestEvent = service.getIngestEvent(formGroup) as any;

        expect(ingestEvent).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIngestEvent should not enable id FormControl', () => {
        const formGroup = service.createIngestEventFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIngestEvent should disable id FormControl', () => {
        const formGroup = service.createIngestEventFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
