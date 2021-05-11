import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoteiro } from '../roteiro.model';

@Component({
  selector: 'jhi-roteiro-detail',
  templateUrl: './roteiro-detail.component.html',
})
export class RoteiroDetailComponent implements OnInit {
  roteiro: IRoteiro | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roteiro }) => {
      this.roteiro = roteiro;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
