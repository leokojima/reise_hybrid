import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'viajante',
        data: { pageTitle: 'reiseHybridApp.viajante.home.title' },
        loadChildren: () => import('./viajante/viajante.module').then(m => m.ViajanteModule),
      },
      {
        path: 'local',
        data: { pageTitle: 'reiseHybridApp.local.home.title' },
        loadChildren: () => import('./local/local.module').then(m => m.LocalModule),
      },
      {
        path: 'roteiro',
        data: { pageTitle: 'reiseHybridApp.roteiro.home.title' },
        loadChildren: () => import('./roteiro/roteiro.module').then(m => m.RoteiroModule),
      },
      {
        path: 'compartilhamento',
        data: { pageTitle: 'reiseHybridApp.compartilhamento.home.title' },
        loadChildren: () => import('./compartilhamento/compartilhamento.module').then(m => m.CompartilhamentoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
