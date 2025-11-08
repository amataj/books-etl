import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IBook } from 'app/entities/book/book.model';
import { BookService } from 'app/entities/book/service/book.service';
import { BookPageTextService } from '../service/book-page-text.service';
import { IBookPageText } from '../book-page-text.model';
import { BookPageTextFormGroup, BookPageTextFormService } from './book-page-text-form.service';

@Component({
  selector: 'jhi-book-page-text-update',
  templateUrl: './book-page-text-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BookPageTextUpdateComponent implements OnInit {
  isSaving = false;
  bookPageText: IBookPageText | null = null;

  booksSharedCollection: IBook[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected bookPageTextService = inject(BookPageTextService);
  protected bookPageTextFormService = inject(BookPageTextFormService);
  protected bookService = inject(BookService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BookPageTextFormGroup = this.bookPageTextFormService.createBookPageTextFormGroup();

  compareBook = (o1: IBook | null, o2: IBook | null): boolean => this.bookService.compareBook(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bookPageText }) => {
      this.bookPageText = bookPageText;
      if (bookPageText) {
        this.updateForm(bookPageText);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('booksEtlApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bookPageText = this.bookPageTextFormService.getBookPageText(this.editForm);
    if (bookPageText.id !== null) {
      this.subscribeToSaveResponse(this.bookPageTextService.update(bookPageText));
    } else {
      this.subscribeToSaveResponse(this.bookPageTextService.create(bookPageText));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBookPageText>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(bookPageText: IBookPageText): void {
    this.bookPageText = bookPageText;
    this.bookPageTextFormService.resetForm(this.editForm, bookPageText);

    this.booksSharedCollection = this.bookService.addBookToCollectionIfMissing<IBook>(this.booksSharedCollection, bookPageText.book);
  }

  protected loadRelationshipsOptions(): void {
    this.bookService
      .query()
      .pipe(map((res: HttpResponse<IBook[]>) => res.body ?? []))
      .pipe(map((books: IBook[]) => this.bookService.addBookToCollectionIfMissing<IBook>(books, this.bookPageText?.book)))
      .subscribe((books: IBook[]) => (this.booksSharedCollection = books));
  }
}
