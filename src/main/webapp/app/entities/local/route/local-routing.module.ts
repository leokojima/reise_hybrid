import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LocalComponent } from '../list/local.component';
import { LocalDetailComponent } from '../detail/local-detail.component';
import { LocalUpdateComponent } from '../update/local-update.component';
import { LocalRoutingResolveService } from './local-routing-resolve.service';

const localRoute: Routes = [
  {
    path: '',
    component: LocalComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LocalDetailComponent,
    resolve: {
      local: LocalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LocalUpdateComponent,
    resolve: {
      local: LocalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LocalUpdateComponent,
    resolve: {
      local: LocalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(localRoute)],
  exports: [RouterModule],
})
export class LocalRoutingModule {}
