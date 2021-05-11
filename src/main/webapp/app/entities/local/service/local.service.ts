import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILocal, getLocalIdentifier } from '../local.model';

export type EntityResponseType = HttpResponse<ILocal>;
export type EntityArrayResponseType = HttpResponse<ILocal[]>;

@Injectable({ providedIn: 'root' })
export class LocalService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/locals');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(local: ILocal): Observable<EntityResponseType> {
    return this.http.post<ILocal>(this.resourceUrl, local, { observe: 'response' });
  }

  update(local: ILocal): Observable<EntityResponseType> {
    return this.http.put<ILocal>(`${this.resourceUrl}/${getLocalIdentifier(local) as number}`, local, { observe: 'response' });
  }

  partialUpdate(local: ILocal): Observable<EntityResponseType> {
    return this.http.patch<ILocal>(`${this.resourceUrl}/${getLocalIdentifier(local) as number}`, local, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILocal>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILocal[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLocalToCollectionIfMissing(localCollection: ILocal[], ...localsToCheck: (ILocal | null | undefined)[]): ILocal[] {
    const locals: ILocal[] = localsToCheck.filter(isPresent);
    if (locals.length > 0) {
      const localCollectionIdentifiers = localCollection.map(localItem => getLocalIdentifier(localItem)!);
      const localsToAdd = locals.filter(localItem => {
        const localIdentifier = getLocalIdentifier(localItem);
        if (localIdentifier == null || localCollectionIdentifiers.includes(localIdentifier)) {
          return false;
        }
        localCollectionIdentifiers.push(localIdentifier);
        return true;
      });
      return [...localsToAdd, ...localCollection];
    }
    return localCollection;
  }
}
