import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompartilhamento, Compartilhamento } from '../compartilhamento.model';
import { CompartilhamentoService } from '../service/compartilhamento.service';

@Injectable({ providedIn: 'root' })
export class CompartilhamentoRoutingResolveService implements Resolve<ICompartilhamento> {
  constructor(protected service: CompartilhamentoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompartilhamento> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((compartilhamento: HttpResponse<Compartilhamento>) => {
          if (compartilhamento.body) {
            return of(compartilhamento.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Compartilhamento());
  }
}
