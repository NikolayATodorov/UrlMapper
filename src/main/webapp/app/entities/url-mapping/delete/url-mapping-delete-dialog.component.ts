import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUrlMapping } from '../url-mapping.model';
import { UrlMappingService } from '../service/url-mapping.service';

@Component({
  standalone: true,
  templateUrl: './url-mapping-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UrlMappingDeleteDialogComponent {
  urlMapping?: IUrlMapping;

  protected urlMappingService = inject(UrlMappingService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.urlMappingService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
