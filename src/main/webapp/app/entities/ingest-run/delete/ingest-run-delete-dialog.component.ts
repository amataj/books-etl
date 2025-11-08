import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IIngestRun } from '../ingest-run.model';
import { IngestRunService } from '../service/ingest-run.service';

@Component({
  templateUrl: './ingest-run-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class IngestRunDeleteDialogComponent {
  ingestRun?: IIngestRun;

  protected ingestRunService = inject(IngestRunService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ingestRunService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
