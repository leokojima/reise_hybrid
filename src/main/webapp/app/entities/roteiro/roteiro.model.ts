import { IViajante } from 'app/entities/viajante/viajante.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IRoteiro {
  id?: number;
  nomeroteiro?: string;
  tipo?: string;
  statusr?: Status | null;
  viajantes?: IViajante[] | null;
}

export class Roteiro implements IRoteiro {
  constructor(
    public id?: number,
    public nomeroteiro?: string,
    public tipo?: string,
    public statusr?: Status | null,
    public viajantes?: IViajante[] | null
  ) {}
}

export function getRoteiroIdentifier(roteiro: IRoteiro): number | undefined {
  return roteiro.id;
}
