import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CompartilhamentoComponent } from './list/compartilhamento.component';
import { CompartilhamentoDetailComponent } from './detail/compartilhamento-detail.component';
import { CompartilhamentoUpdateComponent } from './update/compartilhamento-update.component';
import { CompartilhamentoDeleteDialogComponent } from './delete/compartilhamento-delete-dialog.component';
import { CompartilhamentoRoutingModule } from './route/compartilhamento-routing.module';

@NgModule({
  imports: [SharedModule, CompartilhamentoRoutingModule],
  declarations: [
    CompartilhamentoComponent,
    CompartilhamentoDetailComponent,
    CompartilhamentoUpdateComponent,
    CompartilhamentoDeleteDialogComponent,
  ],
  entryComponents: [CompartilhamentoDeleteDialogComponent],
})
export class CompartilhamentoModule {}
