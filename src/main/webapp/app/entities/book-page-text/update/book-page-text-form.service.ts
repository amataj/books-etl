import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IBookPageText, NewBookPageText } from '../book-page-text.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBookPageText for edit and NewBookPageTextFormGroupInput for create.
 */
type BookPageTextFormGroupInput = IBookPageText | PartialWithRequiredKeyOf<NewBookPageText>;

type BookPageTextFormDefaults = Pick<NewBookPageText, 'id'>;

type BookPageTextFormGroupContent = {
  id: FormControl<IBookPageText['id'] | NewBookPageText['id']>;
  documentId: FormControl<IBookPageText['documentId']>;
  pageNo: FormControl<IBookPageText['pageNo']>;
  text: FormControl<IBookPageText['text']>;
  book: FormControl<IBookPageText['book']>;
};

export type BookPageTextFormGroup = FormGroup<BookPageTextFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BookPageTextFormService {
  createBookPageTextFormGroup(bookPageText: BookPageTextFormGroupInput = { id: null }): BookPageTextFormGroup {
    const bookPageTextRawValue = {
      ...this.getFormDefaults(),
      ...bookPageText,
    };
    return new FormGroup<BookPageTextFormGroupContent>({
      id: new FormControl(
        { value: bookPageTextRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(bookPageTextRawValue.documentId, {
        validators: [Validators.required, Validators.minLength(10), Validators.maxLength(64)],
      }),
      pageNo: new FormControl(bookPageTextRawValue.pageNo, {
        validators: [Validators.required, Validators.min(1)],
      }),
      text: new FormControl(bookPageTextRawValue.text),
      book: new FormControl(bookPageTextRawValue.book),
    });
  }

  getBookPageText(form: BookPageTextFormGroup): IBookPageText | NewBookPageText {
    return form.getRawValue() as IBookPageText | NewBookPageText;
  }

  resetForm(form: BookPageTextFormGroup, bookPageText: BookPageTextFormGroupInput): void {
    const bookPageTextRawValue = { ...this.getFormDefaults(), ...bookPageText };
    form.reset(
      {
        ...bookPageTextRawValue,
        id: { value: bookPageTextRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BookPageTextFormDefaults {
    return {
      id: null,
    };
  }
}
