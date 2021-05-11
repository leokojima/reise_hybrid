import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { LocalComponent } from './list/local.component';
import { LocalDetailComponent } from './detail/local-detail.component';
import { LocalUpdateComponent } from './update/local-update.component';
import { LocalDeleteDialogComponent } from './delete/local-delete-dialog.component';
import { LocalRoutingModule } from './route/local-routing.module';

@NgModule({
  imports: [SharedModule, LocalRoutingModule],
  declarations: [LocalComponent, LocalDetailComponent, LocalUpdateComponent, LocalDeleteDialogComponent],
  entryComponents: [LocalDeleteDialogComponent],
})
export class LocalModule {}
