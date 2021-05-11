jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RoteiroService } from '../service/roteiro.service';
import { IRoteiro, Roteiro } from '../roteiro.model';

import { RoteiroUpdateComponent } from './roteiro-update.component';

describe('Component Tests', () => {
  describe('Roteiro Management Update Component', () => {
    let comp: RoteiroUpdateComponent;
    let fixture: ComponentFixture<RoteiroUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let roteiroService: RoteiroService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RoteiroUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RoteiroUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RoteiroUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      roteiroService = TestBed.inject(RoteiroService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const roteiro: IRoteiro = { id: 456 };

        activatedRoute.data = of({ roteiro });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(roteiro));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const roteiro = { id: 123 };
        spyOn(roteiroService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ roteiro });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: roteiro }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(roteiroService.update).toHaveBeenCalledWith(roteiro);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const roteiro = new Roteiro();
        spyOn(roteiroService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ roteiro });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: roteiro }));
        saveSubject.complete();

        // THEN
        expect(roteiroService.create).toHaveBeenCalledWith(roteiro);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const roteiro = { id: 123 };
        spyOn(roteiroService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ roteiro });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(roteiroService.update).toHaveBeenCalledWith(roteiro);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
