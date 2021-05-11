import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompartilhamento } from '../compartilhamento.model';
import { CompartilhamentoService } from '../service/compartilhamento.service';
import { CompartilhamentoDeleteDialogComponent } from '../delete/compartilhamento-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-compartilhamento',
  templateUrl: './compartilhamento.component.html',
})
export class CompartilhamentoComponent implements OnInit {
  compartilhamentos?: ICompartilhamento[];
  isLoading = false;

  constructor(
    protected compartilhamentoService: CompartilhamentoService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.compartilhamentoService.query().subscribe(
      (res: HttpResponse<ICompartilhamento[]>) => {
        this.isLoading = false;
        this.compartilhamentos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICompartilhamento): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(compartilhamento: ICompartilhamento): void {
    const modalRef = this.modalService.open(CompartilhamentoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.compartilhamento = compartilhamento;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
