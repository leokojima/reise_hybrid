import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILocal, Local } from '../local.model';
import { LocalService } from '../service/local.service';

@Injectable({ providedIn: 'root' })
export class LocalRoutingResolveService implements Resolve<ILocal> {
  constructor(protected service: LocalService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILocal> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((local: HttpResponse<Local>) => {
          if (local.body) {
            return of(local.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Local());
  }
}
