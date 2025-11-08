import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IIngestRun } from '../ingest-run.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ingest-run.test-samples';

import { IngestRunService, RestIngestRun } from './ingest-run.service';

const requireRestSample: RestIngestRun = {
  ...sampleWithRequiredData,
  startedAt: sampleWithRequiredData.startedAt?.toJSON(),
  finishedAt: sampleWithRequiredData.finishedAt?.toJSON(),
};

describe('IngestRun Service', () => {
  let service: IngestRunService;
  let httpMock: HttpTestingController;
  let expectedResult: IIngestRun | IIngestRun[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(IngestRunService);
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

    it('should create a IngestRun', () => {
      const ingestRun = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ingestRun).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IngestRun', () => {
      const ingestRun = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ingestRun).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IngestRun', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IngestRun', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IngestRun', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIngestRunToCollectionIfMissing', () => {
      it('should add a IngestRun to an empty array', () => {
        const ingestRun: IIngestRun = sampleWithRequiredData;
        expectedResult = service.addIngestRunToCollectionIfMissing([], ingestRun);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ingestRun);
      });

      it('should not add a IngestRun to an array that contains it', () => {
        const ingestRun: IIngestRun = sampleWithRequiredData;
        const ingestRunCollection: IIngestRun[] = [
          {
            ...ingestRun,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIngestRunToCollectionIfMissing(ingestRunCollection, ingestRun);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IngestRun to an array that doesn't contain it", () => {
        const ingestRun: IIngestRun = sampleWithRequiredData;
        const ingestRunCollection: IIngestRun[] = [sampleWithPartialData];
        expectedResult = service.addIngestRunToCollectionIfMissing(ingestRunCollection, ingestRun);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ingestRun);
      });

      it('should add only unique IngestRun to an array', () => {
        const ingestRunArray: IIngestRun[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ingestRunCollection: IIngestRun[] = [sampleWithRequiredData];
        expectedResult = service.addIngestRunToCollectionIfMissing(ingestRunCollection, ...ingestRunArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ingestRun: IIngestRun = sampleWithRequiredData;
        const ingestRun2: IIngestRun = sampleWithPartialData;
        expectedResult = service.addIngestRunToCollectionIfMissing([], ingestRun, ingestRun2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ingestRun);
        expect(expectedResult).toContain(ingestRun2);
      });

      it('should accept null and undefined values', () => {
        const ingestRun: IIngestRun = sampleWithRequiredData;
        expectedResult = service.addIngestRunToCollectionIfMissing([], null, ingestRun, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ingestRun);
      });

      it('should return initial array if no IngestRun is added', () => {
        const ingestRunCollection: IIngestRun[] = [sampleWithRequiredData];
        expectedResult = service.addIngestRunToCollectionIfMissing(ingestRunCollection, undefined, null);
        expect(expectedResult).toEqual(ingestRunCollection);
      });
    });

    describe('compareIngestRun', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIngestRun(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 26594 };
        const entity2 = null;

        const compareResult1 = service.compareIngestRun(entity1, entity2);
        const compareResult2 = service.compareIngestRun(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 26594 };
        const entity2 = { id: 26980 };

        const compareResult1 = service.compareIngestRun(entity1, entity2);
        const compareResult2 = service.compareIngestRun(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 26594 };
        const entity2 = { id: 26594 };

        const compareResult1 = service.compareIngestRun(entity1, entity2);
        const compareResult2 = service.compareIngestRun(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
