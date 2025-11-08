import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBookPageText } from '../book-page-text.model';
import { BookPageTextService } from '../service/book-page-text.service';

@Component({
  templateUrl: './book-page-text-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BookPageTextDeleteDialogComponent {
  bookPageText?: IBookPageText;

  protected bookPageTextService = inject(BookPageTextService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bookPageTextService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
