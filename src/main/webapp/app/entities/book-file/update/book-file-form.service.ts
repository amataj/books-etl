import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBookFile, NewBookFile } from '../book-file.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBookFile for edit and NewBookFileFormGroupInput for create.
 */
type BookFileFormGroupInput = IBookFile | PartialWithRequiredKeyOf<NewBookFile>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBookFile | NewBookFile> = Omit<T, 'mtime' | 'firstSeenAt' | 'lastSeenAt'> & {
  mtime?: string | null;
  firstSeenAt?: string | null;
  lastSeenAt?: string | null;
};

type BookFileFormRawValue = FormValueOf<IBookFile>;

type NewBookFileFormRawValue = FormValueOf<NewBookFile>;

type BookFileFormDefaults = Pick<NewBookFile, 'id' | 'mtime' | 'firstSeenAt' | 'lastSeenAt'>;

type BookFileFormGroupContent = {
  id: FormControl<BookFileFormRawValue['id'] | NewBookFile['id']>;
  pathNorm: FormControl<BookFileFormRawValue['pathNorm']>;
  sha256: FormControl<BookFileFormRawValue['sha256']>;
  sizeBytes: FormControl<BookFileFormRawValue['sizeBytes']>;
  mtime: FormControl<BookFileFormRawValue['mtime']>;
  storageUri: FormControl<BookFileFormRawValue['storageUri']>;
  firstSeenAt: FormControl<BookFileFormRawValue['firstSeenAt']>;
  lastSeenAt: FormControl<BookFileFormRawValue['lastSeenAt']>;
  book: FormControl<BookFileFormRawValue['book']>;
};

export type BookFileFormGroup = FormGroup<BookFileFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BookFileFormService {
  createBookFileFormGroup(bookFile: BookFileFormGroupInput = { id: null }): BookFileFormGroup {
    const bookFileRawValue = this.convertBookFileToBookFileRawValue({
      ...this.getFormDefaults(),
      ...bookFile,
    });
    return new FormGroup<BookFileFormGroupContent>({
      id: new FormControl(
        { value: bookFileRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      pathNorm: new FormControl(bookFileRawValue.pathNorm, {
        validators: [Validators.required],
      }),
      sha256: new FormControl(bookFileRawValue.sha256, {
        validators: [Validators.required, Validators.minLength(10), Validators.maxLength(64)],
      }),
      sizeBytes: new FormControl(bookFileRawValue.sizeBytes),
      mtime: new FormControl(bookFileRawValue.mtime),
      storageUri: new FormControl(bookFileRawValue.storageUri),
      firstSeenAt: new FormControl(bookFileRawValue.firstSeenAt),
      lastSeenAt: new FormControl(bookFileRawValue.lastSeenAt),
      book: new FormControl(bookFileRawValue.book),
    });
  }

  getBookFile(form: BookFileFormGroup): IBookFile | NewBookFile {
    return this.convertBookFileRawValueToBookFile(form.getRawValue() as BookFileFormRawValue | NewBookFileFormRawValue);
  }

  resetForm(form: BookFileFormGroup, bookFile: BookFileFormGroupInput): void {
    const bookFileRawValue = this.convertBookFileToBookFileRawValue({ ...this.getFormDefaults(), ...bookFile });
    form.reset(
      {
        ...bookFileRawValue,
        id: { value: bookFileRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BookFileFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      mtime: currentTime,
      firstSeenAt: currentTime,
      lastSeenAt: currentTime,
    };
  }

  private convertBookFileRawValueToBookFile(rawBookFile: BookFileFormRawValue | NewBookFileFormRawValue): IBookFile | NewBookFile {
    return {
      ...rawBookFile,
      mtime: dayjs(rawBookFile.mtime, DATE_TIME_FORMAT),
      firstSeenAt: dayjs(rawBookFile.firstSeenAt, DATE_TIME_FORMAT),
      lastSeenAt: dayjs(rawBookFile.lastSeenAt, DATE_TIME_FORMAT),
    };
  }

  private convertBookFileToBookFileRawValue(
    bookFile: IBookFile | (Partial<NewBookFile> & BookFileFormDefaults),
  ): BookFileFormRawValue | PartialWithRequiredKeyOf<NewBookFileFormRawValue> {
    return {
      ...bookFile,
      mtime: bookFile.mtime ? bookFile.mtime.format(DATE_TIME_FORMAT) : undefined,
      firstSeenAt: bookFile.firstSeenAt ? bookFile.firstSeenAt.format(DATE_TIME_FORMAT) : undefined,
      lastSeenAt: bookFile.lastSeenAt ? bookFile.lastSeenAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
