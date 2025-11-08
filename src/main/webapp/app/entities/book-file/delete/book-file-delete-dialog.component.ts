import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBookFile } from '../book-file.model';
import { BookFileService } from '../service/book-file.service';

@Component({
  templateUrl: './book-file-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BookFileDeleteDialogComponent {
  bookFile?: IBookFile;

  protected bookFileService = inject(BookFileService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bookFileService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
