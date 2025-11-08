import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IIngestEvent, NewIngestEvent } from '../ingest-event.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIngestEvent for edit and NewIngestEventFormGroupInput for create.
 */
type IngestEventFormGroupInput = IIngestEvent | PartialWithRequiredKeyOf<NewIngestEvent>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IIngestEvent | NewIngestEvent> = Omit<T, 'createdAt'> & {
  createdAt?: string | null;
};

type IngestEventFormRawValue = FormValueOf<IIngestEvent>;

type NewIngestEventFormRawValue = FormValueOf<NewIngestEvent>;

type IngestEventFormDefaults = Pick<NewIngestEvent, 'id' | 'createdAt'>;

type IngestEventFormGroupContent = {
  id: FormControl<IngestEventFormRawValue['id'] | NewIngestEvent['id']>;
  runId: FormControl<IngestEventFormRawValue['runId']>;
  documentId: FormControl<IngestEventFormRawValue['documentId']>;
  topic: FormControl<IngestEventFormRawValue['topic']>;
  payload: FormControl<IngestEventFormRawValue['payload']>;
  createdAt: FormControl<IngestEventFormRawValue['createdAt']>;
  ingestRun: FormControl<IngestEventFormRawValue['ingestRun']>;
};

export type IngestEventFormGroup = FormGroup<IngestEventFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IngestEventFormService {
  createIngestEventFormGroup(ingestEvent: IngestEventFormGroupInput = { id: null }): IngestEventFormGroup {
    const ingestEventRawValue = this.convertIngestEventToIngestEventRawValue({
      ...this.getFormDefaults(),
      ...ingestEvent,
    });
    return new FormGroup<IngestEventFormGroupContent>({
      id: new FormControl(
        { value: ingestEventRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      runId: new FormControl(ingestEventRawValue.runId),
      documentId: new FormControl(ingestEventRawValue.documentId, {
        validators: [Validators.maxLength(64)],
      }),
      topic: new FormControl(ingestEventRawValue.topic, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      payload: new FormControl(ingestEventRawValue.payload, {
        validators: [Validators.required],
      }),
      createdAt: new FormControl(ingestEventRawValue.createdAt),
      ingestRun: new FormControl(ingestEventRawValue.ingestRun),
    });
  }

  getIngestEvent(form: IngestEventFormGroup): IIngestEvent | NewIngestEvent {
    return this.convertIngestEventRawValueToIngestEvent(form.getRawValue() as IngestEventFormRawValue | NewIngestEventFormRawValue);
  }

  resetForm(form: IngestEventFormGroup, ingestEvent: IngestEventFormGroupInput): void {
    const ingestEventRawValue = this.convertIngestEventToIngestEventRawValue({ ...this.getFormDefaults(), ...ingestEvent });
    form.reset(
      {
        ...ingestEventRawValue,
        id: { value: ingestEventRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): IngestEventFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
    };
  }

  private convertIngestEventRawValueToIngestEvent(
    rawIngestEvent: IngestEventFormRawValue | NewIngestEventFormRawValue,
  ): IIngestEvent | NewIngestEvent {
    return {
      ...rawIngestEvent,
      createdAt: dayjs(rawIngestEvent.createdAt, DATE_TIME_FORMAT),
    };
  }

  private convertIngestEventToIngestEventRawValue(
    ingestEvent: IIngestEvent | (Partial<NewIngestEvent> & IngestEventFormDefaults),
  ): IngestEventFormRawValue | PartialWithRequiredKeyOf<NewIngestEventFormRawValue> {
    return {
      ...ingestEvent,
      createdAt: ingestEvent.createdAt ? ingestEvent.createdAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
