import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IViajante, Viajante } from '../viajante.model';
import { ViajanteService } from '../service/viajante.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ILocal } from 'app/entities/local/local.model';
import { LocalService } from 'app/entities/local/service/local.service';
import { IRoteiro } from 'app/entities/roteiro/roteiro.model';
import { RoteiroService } from 'app/entities/roteiro/service/roteiro.service';

@Component({
  selector: 'jhi-viajante-update',
  templateUrl: './viajante-update.component.html',
})
export class ViajanteUpdateComponent implements OnInit {
  isSaving = false;

  localsSharedCollection: ILocal[] = [];
  roteirosSharedCollection: IRoteiro[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    email: [null, [Validators.required]],
    foto: [],
    fotoContentType: [],
    dataNascimento: [],
    statusv: [],
    locals: [],
    roteiros: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected viajanteService: ViajanteService,
    protected localService: LocalService,
    protected roteiroService: RoteiroService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ viajante }) => {
      this.updateForm(viajante);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('reiseHybridApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const viajante = this.createFromForm();
    if (viajante.id !== undefined) {
      this.subscribeToSaveResponse(this.viajanteService.update(viajante));
    } else {
      this.subscribeToSaveResponse(this.viajanteService.create(viajante));
    }
  }

  trackLocalById(index: number, item: ILocal): number {
    return item.id!;
  }

  trackRoteiroById(index: number, item: IRoteiro): number {
    return item.id!;
  }

  getSelectedLocal(option: ILocal, selectedVals?: ILocal[]): ILocal {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedRoteiro(option: IRoteiro, selectedVals?: IRoteiro[]): IRoteiro {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IViajante>>): void {
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

  protected updateForm(viajante: IViajante): void {
    this.editForm.patchValue({
      id: viajante.id,
      nome: viajante.nome,
      email: viajante.email,
      foto: viajante.foto,
      fotoContentType: viajante.fotoContentType,
      dataNascimento: viajante.dataNascimento,
      statusv: viajante.statusv,
      locals: viajante.locals,
      roteiros: viajante.roteiros,
    });

    this.localsSharedCollection = this.localService.addLocalToCollectionIfMissing(this.localsSharedCollection, ...(viajante.locals ?? []));
    this.roteirosSharedCollection = this.roteiroService.addRoteiroToCollectionIfMissing(
      this.roteirosSharedCollection,
      ...(viajante.roteiros ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.localService
      .query()
      .pipe(map((res: HttpResponse<ILocal[]>) => res.body ?? []))
      .pipe(
        map((locals: ILocal[]) => this.localService.addLocalToCollectionIfMissing(locals, ...(this.editForm.get('locals')!.value ?? [])))
      )
      .subscribe((locals: ILocal[]) => (this.localsSharedCollection = locals));

    this.roteiroService
      .query()
      .pipe(map((res: HttpResponse<IRoteiro[]>) => res.body ?? []))
      .pipe(
        map((roteiros: IRoteiro[]) =>
          this.roteiroService.addRoteiroToCollectionIfMissing(roteiros, ...(this.editForm.get('roteiros')!.value ?? []))
        )
      )
      .subscribe((roteiros: IRoteiro[]) => (this.roteirosSharedCollection = roteiros));
  }

  protected createFromForm(): IViajante {
    return {
      ...new Viajante(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      email: this.editForm.get(['email'])!.value,
      fotoContentType: this.editForm.get(['fotoContentType'])!.value,
      foto: this.editForm.get(['foto'])!.value,
      dataNascimento: this.editForm.get(['dataNascimento'])!.value,
      statusv: this.editForm.get(['statusv'])!.value,
      locals: this.editForm.get(['locals'])!.value,
      roteiros: this.editForm.get(['roteiros'])!.value,
    };
  }
}
