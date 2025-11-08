import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IBook, NewBook } from '../book.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBook for edit and NewBookFormGroupInput for create.
 */
type BookFormGroupInput = IBook | PartialWithRequiredKeyOf<NewBook>;

type BookFormDefaults = Pick<NewBook, 'id'>;

type BookFormGroupContent = {
  id: FormControl<IBook['id'] | NewBook['id']>;
  documentId: FormControl<IBook['documentId']>;
  title: FormControl<IBook['title']>;
  author: FormControl<IBook['author']>;
  lang: FormControl<IBook['lang']>;
  pages: FormControl<IBook['pages']>;
};

export type BookFormGroup = FormGroup<BookFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BookFormService {
  createBookFormGroup(book: BookFormGroupInput = { id: null }): BookFormGroup {
    const bookRawValue = {
      ...this.getFormDefaults(),
      ...book,
    };
    return new FormGroup<BookFormGroupContent>({
      id: new FormControl(
        { value: bookRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(bookRawValue.documentId, {
        validators: [Validators.required, Validators.minLength(10), Validators.maxLength(64)],
      }),
      title: new FormControl(bookRawValue.title, {
        validators: [Validators.maxLength(512)],
      }),
      author: new FormControl(bookRawValue.author, {
        validators: [Validators.maxLength(256)],
      }),
      lang: new FormControl(bookRawValue.lang, {
        validators: [Validators.maxLength(16)],
      }),
      pages: new FormControl(bookRawValue.pages, {
        validators: [Validators.min(1)],
      }),
    });
  }

  getBook(form: BookFormGroup): IBook | NewBook {
    return form.getRawValue() as IBook | NewBook;
  }

  resetForm(form: BookFormGroup, book: BookFormGroupInput): void {
    const bookRawValue = { ...this.getFormDefaults(), ...book };
    form.reset(
      {
        ...bookRawValue,
        id: { value: bookRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BookFormDefaults {
    return {
      id: null,
    };
  }
}
