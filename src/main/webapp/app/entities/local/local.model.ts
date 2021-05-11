import { IViajante } from 'app/entities/viajante/viajante.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface ILocal {
  id?: number;
  nomelocal?: string;
  email?: string;
  fotoContentType?: string | null;
  foto?: string | null;
  tipo?: string | null;
  statusl?: Status | null;
  viajantes?: IViajante[] | null;
}

export class Local implements ILocal {
  constructor(
    public id?: number,
    public nomelocal?: string,
    public email?: string,
    public fotoContentType?: string | null,
    public foto?: string | null,
    public tipo?: string | null,
    public statusl?: Status | null,
    public viajantes?: IViajante[] | null
  ) {}
}

export function getLocalIdentifier(local: ILocal): number | undefined {
  return local.id;
}
