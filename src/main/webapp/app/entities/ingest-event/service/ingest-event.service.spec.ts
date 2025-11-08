import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IIngestEvent } from '../ingest-event.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ingest-event.test-samples';

import { IngestEventService, RestIngestEvent } from './ingest-event.service';

const requireRestSample: RestIngestEvent = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
};

describe('IngestEvent Service', () => {
  let service: IngestEventService;
  let httpMock: HttpTestingController;
  let expectedResult: IIngestEvent | IIngestEvent[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(IngestEventService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a IngestEvent', () => {
      const ingestEvent = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ingestEvent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IngestEvent', () => {
      const ingestEvent = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ingestEvent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IngestEvent', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IngestEvent', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IngestEvent', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIngestEventToCollectionIfMissing', () => {
      it('should add a IngestEvent to an empty array', () => {
        const ingestEvent: IIngestEvent = sampleWithRequiredData;
        expectedResult = service.addIngestEventToCollectionIfMissing([], ingestEvent);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ingestEvent);
      });

      it('should not add a IngestEvent to an array that contains it', () => {
        const ingestEvent: IIngestEvent = sampleWithRequiredData;
        const ingestEventCollection: IIngestEvent[] = [
          {
            ...ingestEvent,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIngestEventToCollectionIfMissing(ingestEventCollection, ingestEvent);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IngestEvent to an array that doesn't contain it", () => {
        const ingestEvent: IIngestEvent = sampleWithRequiredData;
        const ingestEventCollection: IIngestEvent[] = [sampleWithPartialData];
        expectedResult = service.addIngestEventToCollectionIfMissing(ingestEventCollection, ingestEvent);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ingestEvent);
      });

      it('should add only unique IngestEvent to an array', () => {
        const ingestEventArray: IIngestEvent[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ingestEventCollection: IIngestEvent[] = [sampleWithRequiredData];
        expectedResult = service.addIngestEventToCollectionIfMissing(ingestEventCollection, ...ingestEventArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ingestEvent: IIngestEvent = sampleWithRequiredData;
        const ingestEvent2: IIngestEvent = sampleWithPartialData;
        expectedResult = service.addIngestEventToCollectionIfMissing([], ingestEvent, ingestEvent2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ingestEvent);
        expect(expectedResult).toContain(ingestEvent2);
      });

      it('should accept null and undefined values', () => {
        const ingestEvent: IIngestEvent = sampleWithRequiredData;
        expectedResult = service.addIngestEventToCollectionIfMissing([], null, ingestEvent, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ingestEvent);
      });

      it('should return initial array if no IngestEvent is added', () => {
        const ingestEventCollection: IIngestEvent[] = [sampleWithRequiredData];
        expectedResult = service.addIngestEventToCollectionIfMissing(ingestEventCollection, undefined, null);
        expect(expectedResult).toEqual(ingestEventCollection);
      });
    });

    describe('compareIngestEvent', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIngestEvent(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 10737 };
        const entity2 = null;

        const compareResult1 = service.compareIngestEvent(entity1, entity2);
        const compareResult2 = service.compareIngestEvent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 10737 };
        const entity2 = { id: 14502 };

        const compareResult1 = service.compareIngestEvent(entity1, entity2);
        const compareResult2 = service.compareIngestEvent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 10737 };
        const entity2 = { id: 10737 };

        const compareResult1 = service.compareIngestEvent(entity1, entity2);
        const compareResult2 = service.compareIngestEvent(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
