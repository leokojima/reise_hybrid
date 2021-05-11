import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompartilhamento } from '../compartilhamento.model';
import { CompartilhamentoService } from '../service/compartilhamento.service';

@Component({
  templateUrl: './compartilhamento-delete-dialog.component.html',
})
export class CompartilhamentoDeleteDialogComponent {
  compartilhamento?: ICompartilhamento;

  constructor(protected compartilhamentoService: CompartilhamentoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.compartilhamentoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
