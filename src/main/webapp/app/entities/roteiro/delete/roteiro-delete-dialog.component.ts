import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoteiro } from '../roteiro.model';
import { RoteiroService } from '../service/roteiro.service';

@Component({
  templateUrl: './roteiro-delete-dialog.component.html',
})
export class RoteiroDeleteDialogComponent {
  roteiro?: IRoteiro;

  constructor(protected roteiroService: RoteiroService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roteiroService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
