jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LocalService } from '../service/local.service';
import { ILocal, Local } from '../local.model';

import { LocalUpdateComponent } from './local-update.component';

describe('Component Tests', () => {
  describe('Local Management Update Component', () => {
    let comp: LocalUpdateComponent;
    let fixture: ComponentFixture<LocalUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let localService: LocalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LocalUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LocalUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LocalUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      localService = TestBed.inject(LocalService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const local: ILocal = { id: 456 };

        activatedRoute.data = of({ local });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(local));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const local = { id: 123 };
        spyOn(localService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ local });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: local }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(localService.update).toHaveBeenCalledWith(local);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const local = new Local();
        spyOn(localService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ local });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: local }));
        saveSubject.complete();

        // THEN
        expect(localService.create).toHaveBeenCalledWith(local);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const local = { id: 123 };
        spyOn(localService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ local });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(localService.update).toHaveBeenCalledWith(local);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
