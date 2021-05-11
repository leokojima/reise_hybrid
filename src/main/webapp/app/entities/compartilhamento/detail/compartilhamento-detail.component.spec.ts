import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { CompartilhamentoDetailComponent } from './compartilhamento-detail.component';

describe('Component Tests', () => {
  describe('Compartilhamento Management Detail Component', () => {
    let comp: CompartilhamentoDetailComponent;
    let fixture: ComponentFixture<CompartilhamentoDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CompartilhamentoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ compartilhamento: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CompartilhamentoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CompartilhamentoDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load compartilhamento on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.compartilhamento).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
