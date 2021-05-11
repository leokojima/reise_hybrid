jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CompartilhamentoService } from '../service/compartilhamento.service';
import { ICompartilhamento, Compartilhamento } from '../compartilhamento.model';
import { IViajante } from 'app/entities/viajante/viajante.model';
import { ViajanteService } from 'app/entities/viajante/service/viajante.service';

import { CompartilhamentoUpdateComponent } from './compartilhamento-update.component';

describe('Component Tests', () => {
  describe('Compartilhamento Management Update Component', () => {
    let comp: CompartilhamentoUpdateComponent;
    let fixture: ComponentFixture<CompartilhamentoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let compartilhamentoService: CompartilhamentoService;
    let viajanteService: ViajanteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CompartilhamentoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CompartilhamentoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CompartilhamentoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      compartilhamentoService = TestBed.inject(CompartilhamentoService);
      viajanteService = TestBed.inject(ViajanteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Viajante query and add missing value', () => {
        const compartilhamento: ICompartilhamento = { id: 456 };
        const viajante: IViajante = { id: 74499 };
        compartilhamento.viajante = viajante;

        const viajanteCollection: IViajante[] = [{ id: 61753 }];
        spyOn(viajanteService, 'query').and.returnValue(of(new HttpResponse({ body: viajanteCollection })));
        const additionalViajantes = [viajante];
        const expectedCollection: IViajante[] = [...additionalViajantes, ...viajanteCollection];
        spyOn(viajanteService, 'addViajanteToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ compartilhamento });
        comp.ngOnInit();

        expect(viajanteService.query).toHaveBeenCalled();
        expect(viajanteService.addViajanteToCollectionIfMissing).toHaveBeenCalledWith(viajanteCollection, ...additionalViajantes);
        expect(comp.viajantesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const compartilhamento: ICompartilhamento = { id: 456 };
        const viajante: IViajante = { id: 45366 };
        compartilhamento.viajante = viajante;

        activatedRoute.data = of({ compartilhamento });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(compartilhamento));
        expect(comp.viajantesSharedCollection).toContain(viajante);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const compartilhamento = { id: 123 };
        spyOn(compartilhamentoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ compartilhamento });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: compartilhamento }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(compartilhamentoService.update).toHaveBeenCalledWith(compartilhamento);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const compartilhamento = new Compartilhamento();
        spyOn(compartilhamentoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ compartilhamento });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: compartilhamento }));
        saveSubject.complete();

        // THEN
        expect(compartilhamentoService.create).toHaveBeenCalledWith(compartilhamento);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const compartilhamento = { id: 123 };
        spyOn(compartilhamentoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ compartilhamento });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(compartilhamentoService.update).toHaveBeenCalledWith(compartilhamento);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackViajanteById', () => {
        it('Should return tracked Viajante primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackViajanteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
