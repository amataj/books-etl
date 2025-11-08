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
import { BookFileService } from '../service/book-file.service';
import { IBookFile } from '../book-file.model';
import { BookFileFormGroup, BookFileFormService } from './book-file-form.service';

@Component({
  selector: 'jhi-book-file-update',
  templateUrl: './book-file-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BookFileUpdateComponent implements OnInit {
  isSaving = false;
  bookFile: IBookFile | null = null;

  booksSharedCollection: IBook[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected bookFileService = inject(BookFileService);
  protected bookFileFormService = inject(BookFileFormService);
  protected bookService = inject(BookService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BookFileFormGroup = this.bookFileFormService.createBookFileFormGroup();

  compareBook = (o1: IBook | null, o2: IBook | null): boolean => this.bookService.compareBook(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bookFile }) => {
      this.bookFile = bookFile;
      if (bookFile) {
        this.updateForm(bookFile);
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
    const bookFile = this.bookFileFormService.getBookFile(this.editForm);
    if (bookFile.id !== null) {
      this.subscribeToSaveResponse(this.bookFileService.update(bookFile));
    } else {
      this.subscribeToSaveResponse(this.bookFileService.create(bookFile));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBookFile>>): void {
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

  protected updateForm(bookFile: IBookFile): void {
    this.bookFile = bookFile;
    this.bookFileFormService.resetForm(this.editForm, bookFile);

    this.booksSharedCollection = this.bookService.addBookToCollectionIfMissing<IBook>(this.booksSharedCollection, bookFile.book);
  }

  protected loadRelationshipsOptions(): void {
    this.bookService
      .query()
      .pipe(map((res: HttpResponse<IBook[]>) => res.body ?? []))
      .pipe(map((books: IBook[]) => this.bookService.addBookToCollectionIfMissing<IBook>(books, this.bookFile?.book)))
      .subscribe((books: IBook[]) => (this.booksSharedCollection = books));
  }
}
