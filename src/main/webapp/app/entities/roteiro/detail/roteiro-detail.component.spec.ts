import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoteiroDetailComponent } from './roteiro-detail.component';

describe('Component Tests', () => {
  describe('Roteiro Management Detail Component', () => {
    let comp: RoteiroDetailComponent;
    let fixture: ComponentFixture<RoteiroDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RoteiroDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ roteiro: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RoteiroDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RoteiroDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load roteiro on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.roteiro).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
