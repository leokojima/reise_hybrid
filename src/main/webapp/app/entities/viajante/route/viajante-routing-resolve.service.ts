import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IViajante, Viajante } from '../viajante.model';
import { ViajanteService } from '../service/viajante.service';

@Injectable({ providedIn: 'root' })
export class ViajanteRoutingResolveService implements Resolve<IViajante> {
  constructor(protected service: ViajanteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IViajante> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((viajante: HttpResponse<Viajante>) => {
          if (viajante.body) {
            return of(viajante.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Viajante());
  }
}
