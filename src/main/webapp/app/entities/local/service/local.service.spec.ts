import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Status } from 'app/entities/enumerations/status.model';
import { ILocal, Local } from '../local.model';

import { LocalService } from './local.service';

describe('Service Tests', () => {
  describe('Local Service', () => {
    let service: LocalService;
    let httpMock: HttpTestingController;
    let elemDefault: ILocal;
    let expectedResult: ILocal | ILocal[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LocalService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomelocal: 'AAAAAAA',
        email: 'AAAAAAA',
        fotoContentType: 'image/png',
        foto: 'AAAAAAA',
        tipo: 'AAAAAAA',
        statusl: Status.ATIVO,
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

      it('should create a Local', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Local()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Local', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomelocal: 'BBBBBB',
            email: 'BBBBBB',
            foto: 'BBBBBB',
            tipo: 'BBBBBB',
            statusl: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Local', () => {
        const patchObject = Object.assign(
          {
            nomelocal: 'BBBBBB',
            tipo: 'BBBBBB',
          },
          new Local()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Local', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomelocal: 'BBBBBB',
            email: 'BBBBBB',
            foto: 'BBBBBB',
            tipo: 'BBBBBB',
            statusl: 'BBBBBB',
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

      it('should delete a Local', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLocalToCollectionIfMissing', () => {
        it('should add a Local to an empty array', () => {
          const local: ILocal = { id: 123 };
          expectedResult = service.addLocalToCollectionIfMissing([], local);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(local);
        });

        it('should not add a Local to an array that contains it', () => {
          const local: ILocal = { id: 123 };
          const localCollection: ILocal[] = [
            {
              ...local,
            },
            { id: 456 },
          ];
          expectedResult = service.addLocalToCollectionIfMissing(localCollection, local);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Local to an array that doesn't contain it", () => {
          const local: ILocal = { id: 123 };
          const localCollection: ILocal[] = [{ id: 456 }];
          expectedResult = service.addLocalToCollectionIfMissing(localCollection, local);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(local);
        });

        it('should add only unique Local to an array', () => {
          const localArray: ILocal[] = [{ id: 123 }, { id: 456 }, { id: 10744 }];
          const localCollection: ILocal[] = [{ id: 123 }];
          expectedResult = service.addLocalToCollectionIfMissing(localCollection, ...localArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const local: ILocal = { id: 123 };
          const local2: ILocal = { id: 456 };
          expectedResult = service.addLocalToCollectionIfMissing([], local, local2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(local);
          expect(expectedResult).toContain(local2);
        });

        it('should accept null and undefined values', () => {
          const local: ILocal = { id: 123 };
          expectedResult = service.addLocalToCollectionIfMissing([], null, local, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(local);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
