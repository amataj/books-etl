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
import { IIngestRun } from 'app/entities/ingest-run/ingest-run.model';
import { IngestRunService } from 'app/entities/ingest-run/service/ingest-run.service';
import { IngestEventService } from '../service/ingest-event.service';
import { IIngestEvent } from '../ingest-event.model';
import { IngestEventFormGroup, IngestEventFormService } from './ingest-event-form.service';

@Component({
  selector: 'jhi-ingest-event-update',
  templateUrl: './ingest-event-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class IngestEventUpdateComponent implements OnInit {
  isSaving = false;
  ingestEvent: IIngestEvent | null = null;

  ingestRunsSharedCollection: IIngestRun[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected ingestEventService = inject(IngestEventService);
  protected ingestEventFormService = inject(IngestEventFormService);
  protected ingestRunService = inject(IngestRunService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: IngestEventFormGroup = this.ingestEventFormService.createIngestEventFormGroup();

  compareIngestRun = (o1: IIngestRun | null, o2: IIngestRun | null): boolean => this.ingestRunService.compareIngestRun(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ingestEvent }) => {
      this.ingestEvent = ingestEvent;
      if (ingestEvent) {
        this.updateForm(ingestEvent);
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
    const ingestEvent = this.ingestEventFormService.getIngestEvent(this.editForm);
    if (ingestEvent.id !== null) {
      this.subscribeToSaveResponse(this.ingestEventService.update(ingestEvent));
    } else {
      this.subscribeToSaveResponse(this.ingestEventService.create(ingestEvent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIngestEvent>>): void {
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

  protected updateForm(ingestEvent: IIngestEvent): void {
    this.ingestEvent = ingestEvent;
    this.ingestEventFormService.resetForm(this.editForm, ingestEvent);

    this.ingestRunsSharedCollection = this.ingestRunService.addIngestRunToCollectionIfMissing<IIngestRun>(
      this.ingestRunsSharedCollection,
      ingestEvent.ingestRun,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ingestRunService
      .query()
      .pipe(map((res: HttpResponse<IIngestRun[]>) => res.body ?? []))
      .pipe(
        map((ingestRuns: IIngestRun[]) =>
          this.ingestRunService.addIngestRunToCollectionIfMissing<IIngestRun>(ingestRuns, this.ingestEvent?.ingestRun),
        ),
      )
      .subscribe((ingestRuns: IIngestRun[]) => (this.ingestRunsSharedCollection = ingestRuns));
  }
}
