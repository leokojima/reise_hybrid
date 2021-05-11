import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompartilhamento, getCompartilhamentoIdentifier } from '../compartilhamento.model';

export type EntityResponseType = HttpResponse<ICompartilhamento>;
export type EntityArrayResponseType = HttpResponse<ICompartilhamento[]>;

@Injectable({ providedIn: 'root' })
export class CompartilhamentoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/compartilhamentos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(compartilhamento: ICompartilhamento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compartilhamento);
    return this.http
      .post<ICompartilhamento>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(compartilhamento: ICompartilhamento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compartilhamento);
    return this.http
      .put<ICompartilhamento>(`${this.resourceUrl}/${getCompartilhamentoIdentifier(compartilhamento) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(compartilhamento: ICompartilhamento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compartilhamento);
    return this.http
      .patch<ICompartilhamento>(`${this.resourceUrl}/${getCompartilhamentoIdentifier(compartilhamento) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICompartilhamento>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICompartilhamento[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCompartilhamentoToCollectionIfMissing(
    compartilhamentoCollection: ICompartilhamento[],
    ...compartilhamentosToCheck: (ICompartilhamento | null | undefined)[]
  ): ICompartilhamento[] {
    const compartilhamentos: ICompartilhamento[] = compartilhamentosToCheck.filter(isPresent);
    if (compartilhamentos.length > 0) {
      const compartilhamentoCollectionIdentifiers = compartilhamentoCollection.map(
        compartilhamentoItem => getCompartilhamentoIdentifier(compartilhamentoItem)!
      );
      const compartilhamentosToAdd = compartilhamentos.filter(compartilhamentoItem => {
        const compartilhamentoIdentifier = getCompartilhamentoIdentifier(compartilhamentoItem);
        if (compartilhamentoIdentifier == null || compartilhamentoCollectionIdentifiers.includes(compartilhamentoIdentifier)) {
          return false;
        }
        compartilhamentoCollectionIdentifiers.push(compartilhamentoIdentifier);
        return true;
      });
      return [...compartilhamentosToAdd, ...compartilhamentoCollection];
    }
    return compartilhamentoCollection;
  }

  protected convertDateFromClient(compartilhamento: ICompartilhamento): ICompartilhamento {
    return Object.assign({}, compartilhamento, {
      dataCriacao: compartilhamento.dataCriacao?.isValid() ? compartilhamento.dataCriacao.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataCriacao = res.body.dataCriacao ? dayjs(res.body.dataCriacao) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((compartilhamento: ICompartilhamento) => {
        compartilhamento.dataCriacao = compartilhamento.dataCriacao ? dayjs(compartilhamento.dataCriacao) : undefined;
      });
    }
    return res;
  }
}
