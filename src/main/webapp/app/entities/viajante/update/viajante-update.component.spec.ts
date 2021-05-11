jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ViajanteService } from '../service/viajante.service';
import { IViajante, Viajante } from '../viajante.model';
import { ILocal } from 'app/entities/local/local.model';
import { LocalService } from 'app/entities/local/service/local.service';
import { IRoteiro } from 'app/entities/roteiro/roteiro.model';
import { RoteiroService } from 'app/entities/roteiro/service/roteiro.service';

import { ViajanteUpdateComponent } from './viajante-update.component';

describe('Component Tests', () => {
  describe('Viajante Management Update Component', () => {
    let comp: ViajanteUpdateComponent;
    let fixture: ComponentFixture<ViajanteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let viajanteService: ViajanteService;
    let localService: LocalService;
    let roteiroService: RoteiroService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ViajanteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ViajanteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ViajanteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      viajanteService = TestBed.inject(ViajanteService);
      localService = TestBed.inject(LocalService);
      roteiroService = TestBed.inject(RoteiroService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Local query and add missing value', () => {
        const viajante: IViajante = { id: 456 };
        const locals: ILocal[] = [{ id: 52873 }];
        viajante.locals = locals;

        const localCollection: ILocal[] = [{ id: 64879 }];
        spyOn(localService, 'query').and.returnValue(of(new HttpResponse({ body: localCollection })));
        const additionalLocals = [...locals];
        const expectedCollection: ILocal[] = [...additionalLocals, ...localCollection];
        spyOn(localService, 'addLocalToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ viajante });
        comp.ngOnInit();

        expect(localService.query).toHaveBeenCalled();
        expect(localService.addLocalToCollectionIfMissing).toHaveBeenCalledWith(localCollection, ...additionalLocals);
        expect(comp.localsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Roteiro query and add missing value', () => {
        const viajante: IViajante = { id: 456 };
        const roteiros: IRoteiro[] = [{ id: 72197 }];
        viajante.roteiros = roteiros;

        const roteiroCollection: IRoteiro[] = [{ id: 86785 }];
        spyOn(roteiroService, 'query').and.returnValue(of(new HttpResponse({ body: roteiroCollection })));
        const additionalRoteiros = [...roteiros];
        const expectedCollection: IRoteiro[] = [...additionalRoteiros, ...roteiroCollection];
        spyOn(roteiroService, 'addRoteiroToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ viajante });
        comp.ngOnInit();

        expect(roteiroService.query).toHaveBeenCalled();
        expect(roteiroService.addRoteiroToCollectionIfMissing).toHaveBeenCalledWith(roteiroCollection, ...additionalRoteiros);
        expect(comp.roteirosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const viajante: IViajante = { id: 456 };
        const locals: ILocal = { id: 68111 };
        viajante.locals = [locals];
        const roteiros: IRoteiro = { id: 36253 };
        viajante.roteiros = [roteiros];

        activatedRoute.data = of({ viajante });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(viajante));
        expect(comp.localsSharedCollection).toContain(locals);
        expect(comp.roteirosSharedCollection).toContain(roteiros);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const viajante = { id: 123 };
        spyOn(viajanteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ viajante });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: viajante }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(viajanteService.update).toHaveBeenCalledWith(viajante);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const viajante = new Viajante();
        spyOn(viajanteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ viajante });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: viajante }));
        saveSubject.complete();

        // THEN
        expect(viajanteService.create).toHaveBeenCalledWith(viajante);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const viajante = { id: 123 };
        spyOn(viajanteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ viajante });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(viajanteService.update).toHaveBeenCalledWith(viajante);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackLocalById', () => {
        it('Should return tracked Local primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLocalById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackRoteiroById', () => {
        it('Should return tracked Roteiro primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRoteiroById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedLocal', () => {
        it('Should return option if no Local is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedLocal(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Local for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedLocal(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Local is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedLocal(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });

      describe('getSelectedRoteiro', () => {
        it('Should return option if no Roteiro is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedRoteiro(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Roteiro for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedRoteiro(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Roteiro is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedRoteiro(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
