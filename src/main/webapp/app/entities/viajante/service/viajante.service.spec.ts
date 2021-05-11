import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Status } from 'app/entities/enumerations/status.model';
import { IViajante, Viajante } from '../viajante.model';

import { ViajanteService } from './viajante.service';

describe('Service Tests', () => {
  describe('Viajante Service', () => {
    let service: ViajanteService;
    let httpMock: HttpTestingController;
    let elemDefault: IViajante;
    let expectedResult: IViajante | IViajante[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ViajanteService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nome: 'AAAAAAA',
        email: 'AAAAAAA',
        fotoContentType: 'image/png',
        foto: 'AAAAAAA',
        dataNascimento: currentDate,
        statusv: Status.ATIVO,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dataNascimento: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Viajante', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataNascimento: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataNascimento: currentDate,
          },
          returnedFromService
        );

        service.create(new Viajante()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Viajante', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            email: 'BBBBBB',
            foto: 'BBBBBB',
            dataNascimento: currentDate.format(DATE_FORMAT),
            statusv: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataNascimento: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Viajante', () => {
        const patchObject = Object.assign(
          {
            email: 'BBBBBB',
            foto: 'BBBBBB',
          },
          new Viajante()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dataNascimento: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Viajante', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            email: 'BBBBBB',
            foto: 'BBBBBB',
            dataNascimento: currentDate.format(DATE_FORMAT),
            statusv: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataNascimento: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Viajante', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addViajanteToCollectionIfMissing', () => {
        it('should add a Viajante to an empty array', () => {
          const viajante: IViajante = { id: 123 };
          expectedResult = service.addViajanteToCollectionIfMissing([], viajante);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(viajante);
        });

        it('should not add a Viajante to an array that contains it', () => {
          const viajante: IViajante = { id: 123 };
          const viajanteCollection: IViajante[] = [
            {
              ...viajante,
            },
            { id: 456 },
          ];
          expectedResult = service.addViajanteToCollectionIfMissing(viajanteCollection, viajante);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Viajante to an array that doesn't contain it", () => {
          const viajante: IViajante = { id: 123 };
          const viajanteCollection: IViajante[] = [{ id: 456 }];
          expectedResult = service.addViajanteToCollectionIfMissing(viajanteCollection, viajante);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(viajante);
        });

        it('should add only unique Viajante to an array', () => {
          const viajanteArray: IViajante[] = [{ id: 123 }, { id: 456 }, { id: 3819 }];
          const viajanteCollection: IViajante[] = [{ id: 123 }];
          expectedResult = service.addViajanteToCollectionIfMissing(viajanteCollection, ...viajanteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const viajante: IViajante = { id: 123 };
          const viajante2: IViajante = { id: 456 };
          expectedResult = service.addViajanteToCollectionIfMissing([], viajante, viajante2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(viajante);
          expect(expectedResult).toContain(viajante2);
        });

        it('should accept null and undefined values', () => {
          const viajante: IViajante = { id: 123 };
          expectedResult = service.addViajanteToCollectionIfMissing([], null, viajante, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(viajante);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
