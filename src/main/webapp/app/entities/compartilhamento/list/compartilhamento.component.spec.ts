import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CompartilhamentoService } from '../service/compartilhamento.service';

import { CompartilhamentoComponent } from './compartilhamento.component';

describe('Component Tests', () => {
  describe('Compartilhamento Management Component', () => {
    let comp: CompartilhamentoComponent;
    let fixture: ComponentFixture<CompartilhamentoComponent>;
    let service: CompartilhamentoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CompartilhamentoComponent],
      })
        .overrideTemplate(CompartilhamentoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CompartilhamentoComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CompartilhamentoService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.compartilhamentos?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
