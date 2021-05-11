import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICompartilhamento, Compartilhamento } from '../compartilhamento.model';

import { CompartilhamentoService } from './compartilhamento.service';

describe('Service Tests', () => {
  describe('Compartilhamento Service', () => {
    let service: CompartilhamentoService;
    let httpMock: HttpTestingController;
    let elemDefault: ICompartilhamento;
    let expectedResult: ICompartilhamento | ICompartilhamento[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CompartilhamentoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nomeComp: 'AAAAAAA',
        descricaoComp: 'AAAAAAA',
        dataCriacao: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dataCriacao: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Compartilhamento', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataCriacao: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataCriacao: currentDate,
          },
          returnedFromService
        );

        service.create(new Compartilhamento()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Compartilhamento', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomeComp: 'BBBBBB',
            descricaoComp: 'BBBBBB',
            dataCriacao: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataCriacao: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Compartilhamento', () => {
        const patchObject = Object.assign(
          {
            descricaoComp: 'BBBBBB',
          },
          new Compartilhamento()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dataCriacao: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Compartilhamento', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomeComp: 'BBBBBB',
            descricaoComp: 'BBBBBB',
            dataCriacao: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataCriacao: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Compartilhamento', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCompartilhamentoToCollectionIfMissing', () => {
        it('should add a Compartilhamento to an empty array', () => {
          const compartilhamento: ICompartilhamento = { id: 123 };
          expectedResult = service.addCompartilhamentoToCollectionIfMissing([], compartilhamento);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(compartilhamento);
        });

        it('should not add a Compartilhamento to an array that contains it', () => {
          const compartilhamento: ICompartilhamento = { id: 123 };
          const compartilhamentoCollection: ICompartilhamento[] = [
            {
              ...compartilhamento,
            },
            { id: 456 },
          ];
          expectedResult = service.addCompartilhamentoToCollectionIfMissing(compartilhamentoCollection, compartilhamento);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Compartilhamento to an array that doesn't contain it", () => {
          const compartilhamento: ICompartilhamento = { id: 123 };
          const compartilhamentoCollection: ICompartilhamento[] = [{ id: 456 }];
          expectedResult = service.addCompartilhamentoToCollectionIfMissing(compartilhamentoCollection, compartilhamento);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(compartilhamento);
        });

        it('should add only unique Compartilhamento to an array', () => {
          const compartilhamentoArray: ICompartilhamento[] = [{ id: 123 }, { id: 456 }, { id: 20321 }];
          const compartilhamentoCollection: ICompartilhamento[] = [{ id: 123 }];
          expectedResult = service.addCompartilhamentoToCollectionIfMissing(compartilhamentoCollection, ...compartilhamentoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const compartilhamento: ICompartilhamento = { id: 123 };
          const compartilhamento2: ICompartilhamento = { id: 456 };
          expectedResult = service.addCompartilhamentoToCollectionIfMissing([], compartilhamento, compartilhamento2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(compartilhamento);
          expect(expectedResult).toContain(compartilhamento2);
        });

        it('should accept null and undefined values', () => {
          const compartilhamento: ICompartilhamento = { id: 123 };
          expectedResult = service.addCompartilhamentoToCollectionIfMissing([], null, compartilhamento, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(compartilhamento);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
