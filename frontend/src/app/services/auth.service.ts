import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { map, catchError, tap } from 'rxjs/operators';
import { Router } from '@angular/router';

export interface LoginResponse {
  success: boolean;
  token?: string;
  user?: any;
  message?: string;
}

export interface User {
  id: string;
  email: string;
  nombre: string;
  apellido: string;
  rol: string;
  activo: boolean;
  fechaCreacion: string;
  proveedorOauth?: string;
  permisos?: string[];
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api';
  private readonly TOKEN_KEY = 'auth_token';
  private readonly USER_KEY = 'user_data';

  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.initializeAuth();
  }

  private initializeAuth(): void {
    const token = this.getToken();
    const user = this.getUser();
    
    if (token && user) {
      this.currentUserSubject.next(user);
      this.isAuthenticatedSubject.next(true);
    }
  }

  login(email: string, password: string): Observable<LoginResponse> {
    const loginData = { email, password };
    
    console.log('Intentando login con:', { email, endpoint: `${this.API_URL}/auth/basic/login` });
    
    return this.http.post<LoginResponse>(`${this.API_URL}/auth/basic/login`, loginData)
      .pipe(
        tap(response => {
          console.log('Respuesta del servidor:', response);
          if (response.success && response.token && response.user) {
            this.setAuthData(response.token, response.user);
          }
        }),
        catchError(error => {
          console.error('Login error:', error);
          // Mostrar error más detallado para debugging
          if (error.status === 0) {
            console.error('Error de conexión: No se puede conectar al servidor');
          } else if (error.status === 404) {
            console.error('Error 404: Endpoint no encontrado');
          } else if (error.status === 500) {
            console.error('Error 500: Error interno del servidor');
          }
          return throwError(error);
        })
      );
  }

  loginWithGoogle(): Observable<LoginResponse> {
    // Para OAuth2 con Google, redirigir al endpoint de autorización
    window.location.href = `${this.API_URL}/oauth2/authorization/google`;
    return new Observable(observer => {
      // Este observable nunca se completará porque redirigimos
      observer.next({ success: true });
    });
  }

  register(userData: any): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.API_URL}/auth/basic/register`, userData)
      .pipe(
        tap(response => {
          if (response.success && response.token && response.user) {
            this.setAuthData(response.token, response.user);
          }
        }),
        catchError(error => {
          console.error('Register error:', error);
          return throwError(error);
        })
      );
  }

  logout(): void {
    this.clearAuthData();
    this.router.navigate(['/login']);
  }

  // Método para limpiar completamente el estado de autenticación
  clearAllAuthData(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    // Limpiar cualquier otro dato relacionado con auth
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user_data');
    localStorage.removeItem('authState');
    sessionStorage.clear();
    this.currentUserSubject.next(null);
    this.isAuthenticatedSubject.next(false);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) {
      return false;
    }

    // Verificar si el token no ha expirado
    try {
      const payload = this.parseJwt(token);
      const currentTime = Date.now() / 1000;
      return payload.exp > currentTime;
    } catch (error) {
      console.error('Error parsing JWT:', error);
      return false;
    }
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getUser(): User | null {
    const userStr = localStorage.getItem(this.USER_KEY);
    return userStr ? JSON.parse(userStr) : null;
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  hasRole(role: string): boolean {
    const user = this.getCurrentUser();
    return user ? user.rol === role : false;
  }

  hasPermission(permission: string): boolean {
    const user = this.getCurrentUser();
    return user && user.permisos ? user.permisos.includes(permission) : false;
  }

  isAdmin(): boolean {
    return this.hasRole('admin');
  }

  isInstructor(): boolean {
    return this.hasRole('instructor');
  }

  isUser(): boolean {
    return this.hasRole('usuario');
  }

  refreshToken(): Observable<any> {
    const token = this.getToken();
    if (!token) {
      return throwError('No token available');
    }

    return this.http.post(`${this.API_URL}/auth/refresh`, { token })
      .pipe(
        tap((response: any) => {
          if (response.success && response.token) {
            this.setToken(response.token);
          }
        }),
        catchError(error => {
          console.error('Token refresh error:', error);
          this.logout();
          return throwError(error);
        })
      );
  }

  private setAuthData(token: string, user: User): void {
    this.setToken(token);
    this.setUser(user);
    this.currentUserSubject.next(user);
    this.isAuthenticatedSubject.next(true);
  }

  private setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  private setUser(user: User): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  private clearAuthData(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.currentUserSubject.next(null);
    this.isAuthenticatedSubject.next(false);
  }

  private parseJwt(token: string): any {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
  }

  // Método para verificar si el usuario puede acceder a una ruta
  canActivate(requiredRoles: string[] = []): boolean {
    if (!this.isAuthenticated()) {
      return false;
    }

    if (requiredRoles.length === 0) {
      return true;
    }

    const user = this.getCurrentUser();
    return user ? requiredRoles.includes(user.rol) : false;
  }

}
