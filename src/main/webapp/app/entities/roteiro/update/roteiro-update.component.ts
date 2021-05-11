import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRoteiro, Roteiro } from '../roteiro.model';
import { RoteiroService } from '../service/roteiro.service';

@Component({
  selector: 'jhi-roteiro-update',
  templateUrl: './roteiro-update.component.html',
})
export class RoteiroUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomeroteiro: [null, [Validators.required]],
    tipo: [null, [Validators.required]],
    statusr: [],
  });

  constructor(protected roteiroService: RoteiroService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roteiro }) => {
      this.updateForm(roteiro);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const roteiro = this.createFromForm();
    if (roteiro.id !== undefined) {
      this.subscribeToSaveResponse(this.roteiroService.update(roteiro));
    } else {
      this.subscribeToSaveResponse(this.roteiroService.create(roteiro));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoteiro>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(roteiro: IRoteiro): void {
    this.editForm.patchValue({
      id: roteiro.id,
      nomeroteiro: roteiro.nomeroteiro,
      tipo: roteiro.tipo,
      statusr: roteiro.statusr,
    });
  }

  protected createFromForm(): IRoteiro {
    return {
      ...new Roteiro(),
      id: this.editForm.get(['id'])!.value,
      nomeroteiro: this.editForm.get(['nomeroteiro'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      statusr: this.editForm.get(['statusr'])!.value,
    };
  }
}
