import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CompartilhamentoComponent } from '../list/compartilhamento.component';
import { CompartilhamentoDetailComponent } from '../detail/compartilhamento-detail.component';
import { CompartilhamentoUpdateComponent } from '../update/compartilhamento-update.component';
import { CompartilhamentoRoutingResolveService } from './compartilhamento-routing-resolve.service';

const compartilhamentoRoute: Routes = [
  {
    path: '',
    component: CompartilhamentoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CompartilhamentoDetailComponent,
    resolve: {
      compartilhamento: CompartilhamentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CompartilhamentoUpdateComponent,
    resolve: {
      compartilhamento: CompartilhamentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompartilhamentoUpdateComponent,
    resolve: {
      compartilhamento: CompartilhamentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(compartilhamentoRoute)],
  exports: [RouterModule],
})
export class CompartilhamentoRoutingModule {}
