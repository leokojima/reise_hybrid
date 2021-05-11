import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ViajanteComponent } from '../list/viajante.component';
import { ViajanteDetailComponent } from '../detail/viajante-detail.component';
import { ViajanteUpdateComponent } from '../update/viajante-update.component';
import { ViajanteRoutingResolveService } from './viajante-routing-resolve.service';

const viajanteRoute: Routes = [
  {
    path: '',
    component: ViajanteComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ViajanteDetailComponent,
    resolve: {
      viajante: ViajanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ViajanteUpdateComponent,
    resolve: {
      viajante: ViajanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ViajanteUpdateComponent,
    resolve: {
      viajante: ViajanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(viajanteRoute)],
  exports: [RouterModule],
})
export class ViajanteRoutingModule {}
