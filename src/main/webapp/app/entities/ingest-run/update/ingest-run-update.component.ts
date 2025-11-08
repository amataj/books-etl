import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IIngestRun } from '../ingest-run.model';
import { IngestRunService } from '../service/ingest-run.service';
import { IngestRunFormGroup, IngestRunFormService } from './ingest-run-form.service';

@Component({
  selector: 'jhi-ingest-run-update',
  templateUrl: './ingest-run-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class IngestRunUpdateComponent implements OnInit {
  isSaving = false;
  ingestRun: IIngestRun | null = null;

  protected ingestRunService = inject(IngestRunService);
  protected ingestRunFormService = inject(IngestRunFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: IngestRunFormGroup = this.ingestRunFormService.createIngestRunFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ingestRun }) => {
      this.ingestRun = ingestRun;
      if (ingestRun) {
        this.updateForm(ingestRun);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ingestRun = this.ingestRunFormService.getIngestRun(this.editForm);
    if (ingestRun.id !== null) {
      this.subscribeToSaveResponse(this.ingestRunService.update(ingestRun));
    } else {
      this.subscribeToSaveResponse(this.ingestRunService.create(ingestRun));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIngestRun>>): void {
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

  protected updateForm(ingestRun: IIngestRun): void {
    this.ingestRun = ingestRun;
    this.ingestRunFormService.resetForm(this.editForm, ingestRun);
  }
}
