import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IBook } from 'app/entities/book/book.model';
import { BookService } from 'app/entities/book/service/book.service';
import { BookFileService } from '../service/book-file.service';
import { IBookFile } from '../book-file.model';
import { BookFileFormService } from './book-file-form.service';

import { BookFileUpdateComponent } from './book-file-update.component';

describe('BookFile Management Update Component', () => {
  let comp: BookFileUpdateComponent;
  let fixture: ComponentFixture<BookFileUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bookFileFormService: BookFileFormService;
  let bookFileService: BookFileService;
  let bookService: BookService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BookFileUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BookFileUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BookFileUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bookFileFormService = TestBed.inject(BookFileFormService);
    bookFileService = TestBed.inject(BookFileService);
    bookService = TestBed.inject(BookService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Book query and add missing value', () => {
      const bookFile: IBookFile = { id: 19907 };
      const book: IBook = { id: 32624 };
      bookFile.book = book;

      const bookCollection: IBook[] = [{ id: 32624 }];
      jest.spyOn(bookService, 'query').mockReturnValue(of(new HttpResponse({ body: bookCollection })));
      const additionalBooks = [book];
      const expectedCollection: IBook[] = [...additionalBooks, ...bookCollection];
      jest.spyOn(bookService, 'addBookToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bookFile });
      comp.ngOnInit();

      expect(bookService.query).toHaveBeenCalled();
      expect(bookService.addBookToCollectionIfMissing).toHaveBeenCalledWith(
        bookCollection,
        ...additionalBooks.map(expect.objectContaining),
      );
      expect(comp.booksSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const bookFile: IBookFile = { id: 19907 };
      const book: IBook = { id: 32624 };
      bookFile.book = book;

      activatedRoute.data = of({ bookFile });
      comp.ngOnInit();

      expect(comp.booksSharedCollection).toContainEqual(book);
      expect(comp.bookFile).toEqual(bookFile);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookFile>>();
      const bookFile = { id: 29608 };
      jest.spyOn(bookFileFormService, 'getBookFile').mockReturnValue(bookFile);
      jest.spyOn(bookFileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookFile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bookFile }));
      saveSubject.complete();

      // THEN
      expect(bookFileFormService.getBookFile).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bookFileService.update).toHaveBeenCalledWith(expect.objectContaining(bookFile));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookFile>>();
      const bookFile = { id: 29608 };
      jest.spyOn(bookFileFormService, 'getBookFile').mockReturnValue({ id: null });
      jest.spyOn(bookFileService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookFile: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bookFile }));
      saveSubject.complete();

      // THEN
      expect(bookFileFormService.getBookFile).toHaveBeenCalled();
      expect(bookFileService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookFile>>();
      const bookFile = { id: 29608 };
      jest.spyOn(bookFileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookFile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bookFileService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBook', () => {
      it('should forward to bookService', () => {
        const entity = { id: 32624 };
        const entity2 = { id: 17120 };
        jest.spyOn(bookService, 'compareBook');
        comp.compareBook(entity, entity2);
        expect(bookService.compareBook).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
