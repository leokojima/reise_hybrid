import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocal } from '../local.model';
import { LocalService } from '../service/local.service';

@Component({
  templateUrl: './local-delete-dialog.component.html',
})
export class LocalDeleteDialogComponent {
  local?: ILocal;

  constructor(protected localService: LocalService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.localService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
