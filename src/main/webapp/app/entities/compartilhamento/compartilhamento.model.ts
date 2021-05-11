import * as dayjs from 'dayjs';
import { IViajante } from 'app/entities/viajante/viajante.model';

export interface ICompartilhamento {
  id?: number;
  nomeComp?: string;
  descricaoComp?: string | null;
  dataCriacao?: dayjs.Dayjs | null;
  viajante?: IViajante | null;
}

export class Compartilhamento implements ICompartilhamento {
  constructor(
    public id?: number,
    public nomeComp?: string,
    public descricaoComp?: string | null,
    public dataCriacao?: dayjs.Dayjs | null,
    public viajante?: IViajante | null
  ) {}
}

export function getCompartilhamentoIdentifier(compartilhamento: ICompartilhamento): number | undefined {
  return compartilhamento.id;
}
