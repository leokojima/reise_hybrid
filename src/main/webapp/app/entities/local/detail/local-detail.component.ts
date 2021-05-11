import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILocal } from '../local.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-local-detail',
  templateUrl: './local-detail.component.html',
})
export class LocalDetailComponent implements OnInit {
  local: ILocal | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ local }) => {
      this.local = local;
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
