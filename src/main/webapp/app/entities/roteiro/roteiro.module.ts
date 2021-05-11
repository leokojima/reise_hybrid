import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { RoteiroComponent } from './list/roteiro.component';
import { RoteiroDetailComponent } from './detail/roteiro-detail.component';
import { RoteiroUpdateComponent } from './update/roteiro-update.component';
import { RoteiroDeleteDialogComponent } from './delete/roteiro-delete-dialog.component';
import { RoteiroRoutingModule } from './route/roteiro-routing.module';

@NgModule({
  imports: [SharedModule, RoteiroRoutingModule],
  declarations: [RoteiroComponent, RoteiroDetailComponent, RoteiroUpdateComponent, RoteiroDeleteDialogComponent],
  entryComponents: [RoteiroDeleteDialogComponent],
})
export class RoteiroModule {}
