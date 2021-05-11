import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IViajante } from '../viajante.model';
import { ViajanteService } from '../service/viajante.service';

@Component({
  templateUrl: './viajante-delete-dialog.component.html',
})
export class ViajanteDeleteDialogComponent {
  viajante?: IViajante;

  constructor(protected viajanteService: ViajanteService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.viajanteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
