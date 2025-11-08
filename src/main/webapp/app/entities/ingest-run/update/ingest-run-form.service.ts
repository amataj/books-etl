import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IIngestRun, NewIngestRun } from '../ingest-run.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIngestRun for edit and NewIngestRunFormGroupInput for create.
 */
type IngestRunFormGroupInput = IIngestRun | PartialWithRequiredKeyOf<NewIngestRun>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IIngestRun | NewIngestRun> = Omit<T, 'startedAt' | 'finishedAt'> & {
  startedAt?: string | null;
  finishedAt?: string | null;
};

type IngestRunFormRawValue = FormValueOf<IIngestRun>;

type NewIngestRunFormRawValue = FormValueOf<NewIngestRun>;

type IngestRunFormDefaults = Pick<NewIngestRun, 'id' | 'startedAt' | 'finishedAt'>;

type IngestRunFormGroupContent = {
  id: FormControl<IngestRunFormRawValue['id'] | NewIngestRun['id']>;
  startedAt: FormControl<IngestRunFormRawValue['startedAt']>;
  finishedAt: FormControl<IngestRunFormRawValue['finishedAt']>;
  status: FormControl<IngestRunFormRawValue['status']>;
  filesSeen: FormControl<IngestRunFormRawValue['filesSeen']>;
  filesParsed: FormControl<IngestRunFormRawValue['filesParsed']>;
  filesFailed: FormControl<IngestRunFormRawValue['filesFailed']>;
};

export type IngestRunFormGroup = FormGroup<IngestRunFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IngestRunFormService {
  createIngestRunFormGroup(ingestRun: IngestRunFormGroupInput = { id: null }): IngestRunFormGroup {
    const ingestRunRawValue = this.convertIngestRunToIngestRunRawValue({
      ...this.getFormDefaults(),
      ...ingestRun,
    });
    return new FormGroup<IngestRunFormGroupContent>({
      id: new FormControl(
        { value: ingestRunRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      startedAt: new FormControl(ingestRunRawValue.startedAt),
      finishedAt: new FormControl(ingestRunRawValue.finishedAt),
      status: new FormControl(ingestRunRawValue.status, {
        validators: [Validators.required, Validators.maxLength(32)],
      }),
      filesSeen: new FormControl(ingestRunRawValue.filesSeen, {
        validators: [Validators.min(0)],
      }),
      filesParsed: new FormControl(ingestRunRawValue.filesParsed, {
        validators: [Validators.min(0)],
      }),
      filesFailed: new FormControl(ingestRunRawValue.filesFailed, {
        validators: [Validators.min(0)],
      }),
    });
  }

  getIngestRun(form: IngestRunFormGroup): IIngestRun | NewIngestRun {
    return this.convertIngestRunRawValueToIngestRun(form.getRawValue() as IngestRunFormRawValue | NewIngestRunFormRawValue);
  }

  resetForm(form: IngestRunFormGroup, ingestRun: IngestRunFormGroupInput): void {
    const ingestRunRawValue = this.convertIngestRunToIngestRunRawValue({ ...this.getFormDefaults(), ...ingestRun });
    form.reset(
      {
        ...ingestRunRawValue,
        id: { value: ingestRunRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): IngestRunFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startedAt: currentTime,
      finishedAt: currentTime,
    };
  }

  private convertIngestRunRawValueToIngestRun(rawIngestRun: IngestRunFormRawValue | NewIngestRunFormRawValue): IIngestRun | NewIngestRun {
    return {
      ...rawIngestRun,
      startedAt: dayjs(rawIngestRun.startedAt, DATE_TIME_FORMAT),
      finishedAt: dayjs(rawIngestRun.finishedAt, DATE_TIME_FORMAT),
    };
  }

  private convertIngestRunToIngestRunRawValue(
    ingestRun: IIngestRun | (Partial<NewIngestRun> & IngestRunFormDefaults),
  ): IngestRunFormRawValue | PartialWithRequiredKeyOf<NewIngestRunFormRawValue> {
    return {
      ...ingestRun,
      startedAt: ingestRun.startedAt ? ingestRun.startedAt.format(DATE_TIME_FORMAT) : undefined,
      finishedAt: ingestRun.finishedAt ? ingestRun.finishedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
