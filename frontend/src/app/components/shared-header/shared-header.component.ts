import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-shared-header',
  templateUrl: './shared-header.component.html',
  styleUrls: ['./shared-header.component.scss']
})
export class SharedHeaderComponent {

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  getWelcomeMessage(): string {
    const user = this.authService.getCurrentUser();
    if (user) {
      return `¡Bienvenido, ${user.nombre || 'Usuario'} Portal!`;
    }
    return '¡Bienvenido, Usuario Portal!';
  }

  getUserRole(): string {
    const user = this.authService.getCurrentUser();
    if (user) {
      return this.getRolDisplayName(user.rol);
    }
    return 'Usuario';
  }

  getRolDisplayName(rol: string): string {
    const roles: { [key: string]: string } = {
      'admin': 'Administrador',
      'instructor': 'Instructor',
      'usuario': 'Usuario',
      'estudiante': 'Estudiante'
    };
    return roles[rol] || 'Usuario';
  }

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

