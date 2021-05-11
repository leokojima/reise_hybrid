import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Status } from 'app/entities/enumerations/status.model';
import { IRoteiro, Roteiro } from '../roteiro.model';

import { RoteiroService } from './roteiro.service';

describe('Service Tests', () => {
  describe('Roteiro Service', () => {
    let service: RoteiroService;
    let httpMock: HttpTestingController;
    let elemDefault: IRoteiro;
    let expectedResult: IRoteiro | IRoteiro[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RoteiroService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomeroteiro: 'AAAAAAA',
        tipo: 'AAAAAAA',
        statusr: Status.ATIVO,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Roteiro', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Roteiro()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Roteiro', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomeroteiro: 'BBBBBB',
            tipo: 'BBBBBB',
            statusr: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Roteiro', () => {
        const patchObject = Object.assign(
          {
            nomeroteiro: 'BBBBBB',
            tipo: 'BBBBBB',
          },
          new Roteiro()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Roteiro', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomeroteiro: 'BBBBBB',
            tipo: 'BBBBBB',
            statusr: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Roteiro', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRoteiroToCollectionIfMissing', () => {
        it('should add a Roteiro to an empty array', () => {
          const roteiro: IRoteiro = { id: 123 };
          expectedResult = service.addRoteiroToCollectionIfMissing([], roteiro);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(roteiro);
        });

        it('should not add a Roteiro to an array that contains it', () => {
          const roteiro: IRoteiro = { id: 123 };
          const roteiroCollection: IRoteiro[] = [
            {
              ...roteiro,
            },
            { id: 456 },
          ];
          expectedResult = service.addRoteiroToCollectionIfMissing(roteiroCollection, roteiro);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Roteiro to an array that doesn't contain it", () => {
          const roteiro: IRoteiro = { id: 123 };
          const roteiroCollection: IRoteiro[] = [{ id: 456 }];
          expectedResult = service.addRoteiroToCollectionIfMissing(roteiroCollection, roteiro);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(roteiro);
        });

        it('should add only unique Roteiro to an array', () => {
          const roteiroArray: IRoteiro[] = [{ id: 123 }, { id: 456 }, { id: 19099 }];
          const roteiroCollection: IRoteiro[] = [{ id: 123 }];
          expectedResult = service.addRoteiroToCollectionIfMissing(roteiroCollection, ...roteiroArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const roteiro: IRoteiro = { id: 123 };
          const roteiro2: IRoteiro = { id: 456 };
          expectedResult = service.addRoteiroToCollectionIfMissing([], roteiro, roteiro2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(roteiro);
          expect(expectedResult).toContain(roteiro2);
        });

        it('should accept null and undefined values', () => {
          const roteiro: IRoteiro = { id: 123 };
          expectedResult = service.addRoteiroToCollectionIfMissing([], null, roteiro, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(roteiro);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
