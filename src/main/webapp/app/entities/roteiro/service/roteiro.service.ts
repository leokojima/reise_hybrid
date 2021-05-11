import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRoteiro, getRoteiroIdentifier } from '../roteiro.model';

export type EntityResponseType = HttpResponse<IRoteiro>;
export type EntityArrayResponseType = HttpResponse<IRoteiro[]>;

@Injectable({ providedIn: 'root' })
export class RoteiroService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/roteiros');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(roteiro: IRoteiro): Observable<EntityResponseType> {
    return this.http.post<IRoteiro>(this.resourceUrl, roteiro, { observe: 'response' });
  }

  update(roteiro: IRoteiro): Observable<EntityResponseType> {
    return this.http.put<IRoteiro>(`${this.resourceUrl}/${getRoteiroIdentifier(roteiro) as number}`, roteiro, { observe: 'response' });
  }

  partialUpdate(roteiro: IRoteiro): Observable<EntityResponseType> {
    return this.http.patch<IRoteiro>(`${this.resourceUrl}/${getRoteiroIdentifier(roteiro) as number}`, roteiro, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRoteiro>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRoteiro[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRoteiroToCollectionIfMissing(roteiroCollection: IRoteiro[], ...roteirosToCheck: (IRoteiro | null | undefined)[]): IRoteiro[] {
    const roteiros: IRoteiro[] = roteirosToCheck.filter(isPresent);
    if (roteiros.length > 0) {
      const roteiroCollectionIdentifiers = roteiroCollection.map(roteiroItem => getRoteiroIdentifier(roteiroItem)!);
      const roteirosToAdd = roteiros.filter(roteiroItem => {
        const roteiroIdentifier = getRoteiroIdentifier(roteiroItem);
        if (roteiroIdentifier == null || roteiroCollectionIdentifiers.includes(roteiroIdentifier)) {
          return false;
        }
        roteiroCollectionIdentifiers.push(roteiroIdentifier);
        return true;
      });
      return [...roteirosToAdd, ...roteiroCollection];
    }
    return roteiroCollection;
  }
}
