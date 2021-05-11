import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IViajante, getViajanteIdentifier } from '../viajante.model';

export type EntityResponseType = HttpResponse<IViajante>;
export type EntityArrayResponseType = HttpResponse<IViajante[]>;

@Injectable({ providedIn: 'root' })
export class ViajanteService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/viajantes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(viajante: IViajante): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(viajante);
    return this.http
      .post<IViajante>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(viajante: IViajante): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(viajante);
    return this.http
      .put<IViajante>(`${this.resourceUrl}/${getViajanteIdentifier(viajante) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(viajante: IViajante): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(viajante);
    return this.http
      .patch<IViajante>(`${this.resourceUrl}/${getViajanteIdentifier(viajante) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IViajante>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IViajante[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addViajanteToCollectionIfMissing(viajanteCollection: IViajante[], ...viajantesToCheck: (IViajante | null | undefined)[]): IViajante[] {
    const viajantes: IViajante[] = viajantesToCheck.filter(isPresent);
    if (viajantes.length > 0) {
      const viajanteCollectionIdentifiers = viajanteCollection.map(viajanteItem => getViajanteIdentifier(viajanteItem)!);
      const viajantesToAdd = viajantes.filter(viajanteItem => {
        const viajanteIdentifier = getViajanteIdentifier(viajanteItem);
        if (viajanteIdentifier == null || viajanteCollectionIdentifiers.includes(viajanteIdentifier)) {
          return false;
        }
        viajanteCollectionIdentifiers.push(viajanteIdentifier);
        return true;
      });
      return [...viajantesToAdd, ...viajanteCollection];
    }
    return viajanteCollection;
  }

  protected convertDateFromClient(viajante: IViajante): IViajante {
    return Object.assign({}, viajante, {
      dataNascimento: viajante.dataNascimento?.isValid() ? viajante.dataNascimento.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataNascimento = res.body.dataNascimento ? dayjs(res.body.dataNascimento) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((viajante: IViajante) => {
        viajante.dataNascimento = viajante.dataNascimento ? dayjs(viajante.dataNascimento) : undefined;
      });
    }
    return res;
  }
}
