import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILocal, Local } from '../local.model';
import { LocalService } from '../service/local.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-local-update',
  templateUrl: './local-update.component.html',
})
export class LocalUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomelocal: [null, [Validators.required]],
    email: [null, [Validators.required]],
    foto: [],
    fotoContentType: [],
    tipo: [],
    statusl: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected localService: LocalService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ local }) => {
      this.updateForm(local);
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
    const local = this.createFromForm();
    if (local.id !== undefined) {
      this.subscribeToSaveResponse(this.localService.update(local));
    } else {
      this.subscribeToSaveResponse(this.localService.create(local));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocal>>): void {
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

  protected updateForm(local: ILocal): void {
    this.editForm.patchValue({
      id: local.id,
      nomelocal: local.nomelocal,
      email: local.email,
      foto: local.foto,
      fotoContentType: local.fotoContentType,
      tipo: local.tipo,
      statusl: local.statusl,
    });
  }

  protected createFromForm(): ILocal {
    return {
      ...new Local(),
      id: this.editForm.get(['id'])!.value,
      nomelocal: this.editForm.get(['nomelocal'])!.value,
      email: this.editForm.get(['email'])!.value,
      fotoContentType: this.editForm.get(['fotoContentType'])!.value,
      foto: this.editForm.get(['foto'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      statusl: this.editForm.get(['statusl'])!.value,
    };
  }
}
