import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CubeComponent } from './widgets/cube/cube.component';
import { ResolutionComponent } from './components/resolution/resolution.component';

// Importa los componentes que deseas enrutar

const routes: Routes = [
  { path: 'cube/:id', component: CubeComponent },
  { path: 'resolution', component: ResolutionComponent },
  { path: '', redirectTo: '/resolution', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
