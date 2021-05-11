jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILocal, Local } from '../local.model';
import { LocalService } from '../service/local.service';

import { LocalRoutingResolveService } from './local-routing-resolve.service';

describe('Service Tests', () => {
  describe('Local routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LocalRoutingResolveService;
    let service: LocalService;
    let resultLocal: ILocal | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LocalRoutingResolveService);
      service = TestBed.inject(LocalService);
      resultLocal = undefined;
    });

    describe('resolve', () => {
      it('should return ILocal returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocal = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLocal).toEqual({ id: 123 });
      });

      it('should return new ILocal if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocal = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLocal).toEqual(new Local());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocal = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLocal).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
