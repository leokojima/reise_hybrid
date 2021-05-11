jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IViajante, Viajante } from '../viajante.model';
import { ViajanteService } from '../service/viajante.service';

import { ViajanteRoutingResolveService } from './viajante-routing-resolve.service';

describe('Service Tests', () => {
  describe('Viajante routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ViajanteRoutingResolveService;
    let service: ViajanteService;
    let resultViajante: IViajante | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ViajanteRoutingResolveService);
      service = TestBed.inject(ViajanteService);
      resultViajante = undefined;
    });

    describe('resolve', () => {
      it('should return IViajante returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultViajante = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultViajante).toEqual({ id: 123 });
      });

      it('should return new IViajante if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultViajante = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultViajante).toEqual(new Viajante());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultViajante = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultViajante).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
