import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IBookFile } from '../book-file.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../book-file.test-samples';

import { BookFileService, RestBookFile } from './book-file.service';

const requireRestSample: RestBookFile = {
  ...sampleWithRequiredData,
  mtime: sampleWithRequiredData.mtime?.toJSON(),
  firstSeenAt: sampleWithRequiredData.firstSeenAt?.toJSON(),
  lastSeenAt: sampleWithRequiredData.lastSeenAt?.toJSON(),
};

describe('BookFile Service', () => {
  let service: BookFileService;
  let httpMock: HttpTestingController;
  let expectedResult: IBookFile | IBookFile[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BookFileService);
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

    it('should create a BookFile', () => {
      const bookFile = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bookFile).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BookFile', () => {
      const bookFile = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bookFile).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BookFile', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BookFile', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BookFile', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBookFileToCollectionIfMissing', () => {
      it('should add a BookFile to an empty array', () => {
        const bookFile: IBookFile = sampleWithRequiredData;
        expectedResult = service.addBookFileToCollectionIfMissing([], bookFile);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bookFile);
      });

      it('should not add a BookFile to an array that contains it', () => {
        const bookFile: IBookFile = sampleWithRequiredData;
        const bookFileCollection: IBookFile[] = [
          {
            ...bookFile,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBookFileToCollectionIfMissing(bookFileCollection, bookFile);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BookFile to an array that doesn't contain it", () => {
        const bookFile: IBookFile = sampleWithRequiredData;
        const bookFileCollection: IBookFile[] = [sampleWithPartialData];
        expectedResult = service.addBookFileToCollectionIfMissing(bookFileCollection, bookFile);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bookFile);
      });

      it('should add only unique BookFile to an array', () => {
        const bookFileArray: IBookFile[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bookFileCollection: IBookFile[] = [sampleWithRequiredData];
        expectedResult = service.addBookFileToCollectionIfMissing(bookFileCollection, ...bookFileArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bookFile: IBookFile = sampleWithRequiredData;
        const bookFile2: IBookFile = sampleWithPartialData;
        expectedResult = service.addBookFileToCollectionIfMissing([], bookFile, bookFile2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bookFile);
        expect(expectedResult).toContain(bookFile2);
      });

      it('should accept null and undefined values', () => {
        const bookFile: IBookFile = sampleWithRequiredData;
        expectedResult = service.addBookFileToCollectionIfMissing([], null, bookFile, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bookFile);
      });

      it('should return initial array if no BookFile is added', () => {
        const bookFileCollection: IBookFile[] = [sampleWithRequiredData];
        expectedResult = service.addBookFileToCollectionIfMissing(bookFileCollection, undefined, null);
        expect(expectedResult).toEqual(bookFileCollection);
      });
    });

    describe('compareBookFile', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBookFile(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 29608 };
        const entity2 = null;

        const compareResult1 = service.compareBookFile(entity1, entity2);
        const compareResult2 = service.compareBookFile(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 29608 };
        const entity2 = { id: 19907 };

        const compareResult1 = service.compareBookFile(entity1, entity2);
        const compareResult2 = service.compareBookFile(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 29608 };
        const entity2 = { id: 29608 };

        const compareResult1 = service.compareBookFile(entity1, entity2);
        const compareResult2 = service.compareBookFile(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
