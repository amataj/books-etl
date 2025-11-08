import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IBook } from 'app/entities/book/book.model';
import { BookService } from 'app/entities/book/service/book.service';
import { BookPageTextService } from '../service/book-page-text.service';
import { IBookPageText } from '../book-page-text.model';
import { BookPageTextFormService } from './book-page-text-form.service';

import { BookPageTextUpdateComponent } from './book-page-text-update.component';

describe('BookPageText Management Update Component', () => {
  let comp: BookPageTextUpdateComponent;
  let fixture: ComponentFixture<BookPageTextUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bookPageTextFormService: BookPageTextFormService;
  let bookPageTextService: BookPageTextService;
  let bookService: BookService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BookPageTextUpdateComponent],
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
      .overrideTemplate(BookPageTextUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BookPageTextUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bookPageTextFormService = TestBed.inject(BookPageTextFormService);
    bookPageTextService = TestBed.inject(BookPageTextService);
    bookService = TestBed.inject(BookService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Book query and add missing value', () => {
      const bookPageText: IBookPageText = { id: 7733 };
      const book: IBook = { id: 32624 };
      bookPageText.book = book;

      const bookCollection: IBook[] = [{ id: 32624 }];
      jest.spyOn(bookService, 'query').mockReturnValue(of(new HttpResponse({ body: bookCollection })));
      const additionalBooks = [book];
      const expectedCollection: IBook[] = [...additionalBooks, ...bookCollection];
      jest.spyOn(bookService, 'addBookToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bookPageText });
      comp.ngOnInit();

      expect(bookService.query).toHaveBeenCalled();
      expect(bookService.addBookToCollectionIfMissing).toHaveBeenCalledWith(
        bookCollection,
        ...additionalBooks.map(expect.objectContaining),
      );
      expect(comp.booksSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const bookPageText: IBookPageText = { id: 7733 };
      const book: IBook = { id: 32624 };
      bookPageText.book = book;

      activatedRoute.data = of({ bookPageText });
      comp.ngOnInit();

      expect(comp.booksSharedCollection).toContainEqual(book);
      expect(comp.bookPageText).toEqual(bookPageText);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookPageText>>();
      const bookPageText = { id: 25582 };
      jest.spyOn(bookPageTextFormService, 'getBookPageText').mockReturnValue(bookPageText);
      jest.spyOn(bookPageTextService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookPageText });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bookPageText }));
      saveSubject.complete();

      // THEN
      expect(bookPageTextFormService.getBookPageText).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bookPageTextService.update).toHaveBeenCalledWith(expect.objectContaining(bookPageText));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookPageText>>();
      const bookPageText = { id: 25582 };
      jest.spyOn(bookPageTextFormService, 'getBookPageText').mockReturnValue({ id: null });
      jest.spyOn(bookPageTextService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookPageText: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bookPageText }));
      saveSubject.complete();

      // THEN
      expect(bookPageTextFormService.getBookPageText).toHaveBeenCalled();
      expect(bookPageTextService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookPageText>>();
      const bookPageText = { id: 25582 };
      jest.spyOn(bookPageTextService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookPageText });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bookPageTextService.update).toHaveBeenCalled();
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
