import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoteiro, Roteiro } from '../roteiro.model';
import { RoteiroService } from '../service/roteiro.service';

@Injectable({ providedIn: 'root' })
export class RoteiroRoutingResolveService implements Resolve<IRoteiro> {
  constructor(protected service: RoteiroService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRoteiro> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((roteiro: HttpResponse<Roteiro>) => {
          if (roteiro.body) {
            return of(roteiro.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Roteiro());
  }
}
