jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICompartilhamento, Compartilhamento } from '../compartilhamento.model';
import { CompartilhamentoService } from '../service/compartilhamento.service';

import { CompartilhamentoRoutingResolveService } from './compartilhamento-routing-resolve.service';

describe('Service Tests', () => {
  describe('Compartilhamento routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CompartilhamentoRoutingResolveService;
    let service: CompartilhamentoService;
    let resultCompartilhamento: ICompartilhamento | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CompartilhamentoRoutingResolveService);
      service = TestBed.inject(CompartilhamentoService);
      resultCompartilhamento = undefined;
    });

    describe('resolve', () => {
      it('should return ICompartilhamento returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCompartilhamento = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCompartilhamento).toEqual({ id: 123 });
      });

      it('should return new ICompartilhamento if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCompartilhamento = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCompartilhamento).toEqual(new Compartilhamento());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCompartilhamento = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCompartilhamento).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
