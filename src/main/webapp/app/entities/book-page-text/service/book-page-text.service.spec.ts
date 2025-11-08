import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IBookPageText } from '../book-page-text.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../book-page-text.test-samples';

import { BookPageTextService } from './book-page-text.service';

const requireRestSample: IBookPageText = {
  ...sampleWithRequiredData,
};

describe('BookPageText Service', () => {
  let service: BookPageTextService;
  let httpMock: HttpTestingController;
  let expectedResult: IBookPageText | IBookPageText[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BookPageTextService);
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

    it('should create a BookPageText', () => {
      const bookPageText = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bookPageText).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BookPageText', () => {
      const bookPageText = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bookPageText).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BookPageText', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BookPageText', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BookPageText', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBookPageTextToCollectionIfMissing', () => {
      it('should add a BookPageText to an empty array', () => {
        const bookPageText: IBookPageText = sampleWithRequiredData;
        expectedResult = service.addBookPageTextToCollectionIfMissing([], bookPageText);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bookPageText);
      });

      it('should not add a BookPageText to an array that contains it', () => {
        const bookPageText: IBookPageText = sampleWithRequiredData;
        const bookPageTextCollection: IBookPageText[] = [
          {
            ...bookPageText,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBookPageTextToCollectionIfMissing(bookPageTextCollection, bookPageText);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BookPageText to an array that doesn't contain it", () => {
        const bookPageText: IBookPageText = sampleWithRequiredData;
        const bookPageTextCollection: IBookPageText[] = [sampleWithPartialData];
        expectedResult = service.addBookPageTextToCollectionIfMissing(bookPageTextCollection, bookPageText);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bookPageText);
      });

      it('should add only unique BookPageText to an array', () => {
        const bookPageTextArray: IBookPageText[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bookPageTextCollection: IBookPageText[] = [sampleWithRequiredData];
        expectedResult = service.addBookPageTextToCollectionIfMissing(bookPageTextCollection, ...bookPageTextArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bookPageText: IBookPageText = sampleWithRequiredData;
        const bookPageText2: IBookPageText = sampleWithPartialData;
        expectedResult = service.addBookPageTextToCollectionIfMissing([], bookPageText, bookPageText2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bookPageText);
        expect(expectedResult).toContain(bookPageText2);
      });

      it('should accept null and undefined values', () => {
        const bookPageText: IBookPageText = sampleWithRequiredData;
        expectedResult = service.addBookPageTextToCollectionIfMissing([], null, bookPageText, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bookPageText);
      });

      it('should return initial array if no BookPageText is added', () => {
        const bookPageTextCollection: IBookPageText[] = [sampleWithRequiredData];
        expectedResult = service.addBookPageTextToCollectionIfMissing(bookPageTextCollection, undefined, null);
        expect(expectedResult).toEqual(bookPageTextCollection);
      });
    });

    describe('compareBookPageText', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBookPageText(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25582 };
        const entity2 = null;

        const compareResult1 = service.compareBookPageText(entity1, entity2);
        const compareResult2 = service.compareBookPageText(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25582 };
        const entity2 = { id: 7733 };

        const compareResult1 = service.compareBookPageText(entity1, entity2);
        const compareResult2 = service.compareBookPageText(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 25582 };
        const entity2 = { id: 25582 };

        const compareResult1 = service.compareBookPageText(entity1, entity2);
        const compareResult2 = service.compareBookPageText(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
