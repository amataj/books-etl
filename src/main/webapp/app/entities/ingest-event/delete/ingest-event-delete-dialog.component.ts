import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IIngestEvent } from '../ingest-event.model';
import { IngestEventService } from '../service/ingest-event.service';

@Component({
  templateUrl: './ingest-event-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class IngestEventDeleteDialogComponent {
  ingestEvent?: IIngestEvent;

  protected ingestEventService = inject(IngestEventService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ingestEventService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
