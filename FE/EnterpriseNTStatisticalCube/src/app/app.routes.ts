import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { authGuard } from './guards/auth.guard';
import { CubeComponent } from './components/cube/cube.component';

export const routes: Routes = [
    { path: '',   redirectTo: '/login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent, title: 'Login', },
    { path: 'cube', component: CubeComponent, title: 'Cube',  canActivate: [ authGuard ], },
];
