import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ViajanteComponent } from './list/viajante.component';
import { ViajanteDetailComponent } from './detail/viajante-detail.component';
import { ViajanteUpdateComponent } from './update/viajante-update.component';
import { ViajanteDeleteDialogComponent } from './delete/viajante-delete-dialog.component';
import { ViajanteRoutingModule } from './route/viajante-routing.module';

@NgModule({
  imports: [SharedModule, ViajanteRoutingModule],
  declarations: [ViajanteComponent, ViajanteDetailComponent, ViajanteUpdateComponent, ViajanteDeleteDialogComponent],
  entryComponents: [ViajanteDeleteDialogComponent],
})
export class ViajanteModule {}
