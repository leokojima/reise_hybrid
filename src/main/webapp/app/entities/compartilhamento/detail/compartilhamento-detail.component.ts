import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompartilhamento } from '../compartilhamento.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-compartilhamento-detail',
  templateUrl: './compartilhamento-detail.component.html',
})
export class CompartilhamentoDetailComponent implements OnInit {
  compartilhamento: ICompartilhamento | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compartilhamento }) => {
      this.compartilhamento = compartilhamento;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
