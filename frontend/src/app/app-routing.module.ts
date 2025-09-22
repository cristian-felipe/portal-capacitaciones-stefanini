import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard, RoleGuard } from './guards/auth.guard';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { CoursesCatalogComponent } from './components/courses-catalog/courses-catalog.component';
import { CourseDetailsComponent } from './components/course-details/course-details.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { AdminCourseManagementComponent } from './components/admin-course-management/admin-course-management.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { UnauthorizedComponent } from './components/unauthorized/unauthorized.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'courses',
    component: CoursesCatalogComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'courses/:id',
    component: CourseDetailsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'profile',
    component: UserProfileComponent,
    canActivate: [AuthGuard]
  },
  // Admin routes - Solo accesible para administradores
  {
    path: 'admin',
    children: [
      {
        path: '',
        component: AdminDashboardComponent,
        canActivate: [RoleGuard],
        data: { roles: ['admin'] }
      },
      {
        path: 'courses',
        component: AdminCourseManagementComponent,
        canActivate: [RoleGuard],
        data: { roles: ['admin'] }
      },
      {
        path: 'courses/:id',
        component: CourseDetailsComponent,
        canActivate: [RoleGuard],
        data: { roles: ['admin'] }
      }
    ]
  },
  // Página de acceso denegado
  {
    path: 'unauthorized',
    component: UnauthorizedComponent
  },
  // TODO: Implementar módulos lazy loading para admin
  // {
  //   path: 'admin',
  //   loadChildren: () => import('./modules/admin/admin.module').then(m => m.AdminModule),
  //   canActivate: [RoleGuard],
  //   data: { roles: ['admin'] }
  // },
  {
    path: '**',
    redirectTo: '/login'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
