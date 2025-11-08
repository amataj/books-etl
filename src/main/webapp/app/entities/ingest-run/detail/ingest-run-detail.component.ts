import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IIngestRun } from '../ingest-run.model';

@Component({
  selector: 'jhi-ingest-run-detail',
  templateUrl: './ingest-run-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class IngestRunDetailComponent {
  ingestRun = input<IIngestRun | null>(null);

  previousState(): void {
    window.history.back();
  }
}
