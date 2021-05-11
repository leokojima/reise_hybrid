import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RoteiroComponent } from '../list/roteiro.component';
import { RoteiroDetailComponent } from '../detail/roteiro-detail.component';
import { RoteiroUpdateComponent } from '../update/roteiro-update.component';
import { RoteiroRoutingResolveService } from './roteiro-routing-resolve.service';

const roteiroRoute: Routes = [
  {
    path: '',
    component: RoteiroComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoteiroDetailComponent,
    resolve: {
      roteiro: RoteiroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoteiroUpdateComponent,
    resolve: {
      roteiro: RoteiroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoteiroUpdateComponent,
    resolve: {
      roteiro: RoteiroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(roteiroRoute)],
  exports: [RouterModule],
})
export class RoteiroRoutingModule {}
