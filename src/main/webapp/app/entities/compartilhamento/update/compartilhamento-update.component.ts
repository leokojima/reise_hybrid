import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICompartilhamento, Compartilhamento } from '../compartilhamento.model';
import { CompartilhamentoService } from '../service/compartilhamento.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IViajante } from 'app/entities/viajante/viajante.model';
import { ViajanteService } from 'app/entities/viajante/service/viajante.service';

@Component({
  selector: 'jhi-compartilhamento-update',
  templateUrl: './compartilhamento-update.component.html',
})
export class CompartilhamentoUpdateComponent implements OnInit {
  isSaving = false;

  viajantesSharedCollection: IViajante[] = [];

  editForm = this.fb.group({
    id: [],
    nomeComp: [null, [Validators.required]],
    descricaoComp: [],
    dataCriacao: [],
    viajante: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected compartilhamentoService: CompartilhamentoService,
    protected viajanteService: ViajanteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compartilhamento }) => {
      this.updateForm(compartilhamento);

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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compartilhamento = this.createFromForm();
    if (compartilhamento.id !== undefined) {
      this.subscribeToSaveResponse(this.compartilhamentoService.update(compartilhamento));
    } else {
      this.subscribeToSaveResponse(this.compartilhamentoService.create(compartilhamento));
    }
  }

  trackViajanteById(index: number, item: IViajante): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompartilhamento>>): void {
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

  protected updateForm(compartilhamento: ICompartilhamento): void {
    this.editForm.patchValue({
      id: compartilhamento.id,
      nomeComp: compartilhamento.nomeComp,
      descricaoComp: compartilhamento.descricaoComp,
      dataCriacao: compartilhamento.dataCriacao,
      viajante: compartilhamento.viajante,
    });

    this.viajantesSharedCollection = this.viajanteService.addViajanteToCollectionIfMissing(
      this.viajantesSharedCollection,
      compartilhamento.viajante
    );
  }

  protected loadRelationshipsOptions(): void {
    this.viajanteService
      .query()
      .pipe(map((res: HttpResponse<IViajante[]>) => res.body ?? []))
      .pipe(
        map((viajantes: IViajante[]) =>
          this.viajanteService.addViajanteToCollectionIfMissing(viajantes, this.editForm.get('viajante')!.value)
        )
      )
      .subscribe((viajantes: IViajante[]) => (this.viajantesSharedCollection = viajantes));
  }

  protected createFromForm(): ICompartilhamento {
    return {
      ...new Compartilhamento(),
      id: this.editForm.get(['id'])!.value,
      nomeComp: this.editForm.get(['nomeComp'])!.value,
      descricaoComp: this.editForm.get(['descricaoComp'])!.value,
      dataCriacao: this.editForm.get(['dataCriacao'])!.value,
      viajante: this.editForm.get(['viajante'])!.value,
    };
  }
}
