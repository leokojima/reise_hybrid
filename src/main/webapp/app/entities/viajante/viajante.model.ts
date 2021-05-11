import * as dayjs from 'dayjs';
import { ILocal } from 'app/entities/local/local.model';
import { IRoteiro } from 'app/entities/roteiro/roteiro.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IViajante {
  id?: number;
  nome?: string;
  email?: string;
  fotoContentType?: string | null;
  foto?: string | null;
  dataNascimento?: dayjs.Dayjs | null;
  statusv?: Status | null;
  locals?: ILocal[] | null;
  roteiros?: IRoteiro[] | null;
}

export class Viajante implements IViajante {
  constructor(
    public id?: number,
    public nome?: string,
    public email?: string,
    public fotoContentType?: string | null,
    public foto?: string | null,
    public dataNascimento?: dayjs.Dayjs | null,
    public statusv?: Status | null,
    public locals?: ILocal[] | null,
    public roteiros?: IRoteiro[] | null
  ) {}
}

export function getViajanteIdentifier(viajante: IViajante): number | undefined {
  return viajante.id;
}
