import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { ProgramaCompletoDTO } from './programa-completo.service';

export interface UserProgress {
  id: number;
  usuario_id: string;
  leccion_id: number;
  estado: 'inscrito' | 'iniciado' | 'en_progreso' | 'completado';
  porcentaje: number; // Porcentaje de 0 a 100
  fecha_actualizacion: string;
  leccion?: {
    id: number;
    titulo: string;
    programaId: number;
    programa?: ProgramaCompletoDTO;
  };
}

export interface Insignia {
  id: number;
  nombre: string;
  descripcion: string;
  url_imagen: string;
}

export interface InsigniaOtorgada {
  id: number;
  usuario_id: string;
  insignia_id: number;
  fecha_otorgada: string;
  insignia?: Insignia;
}

export interface UserStats {
  totalCursos: number;
  cursosIniciados: number;
  cursosCompletados: number;
  progresoPromedio: number;
  tiempoTotalEstudio: number; // en minutos
  totalInsignias: number;
  leccionesCompletadas: number;
  leccionesEnProgreso: number;
}



@Injectable({
  providedIn: 'root'
})
export class UserProgressService {
  private readonly API_URL = 'http://localhost:8081/courses-service/api';
  private readonly PROGRESS_KEY = 'user_progress';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  // Obtener progreso del usuario actual (detalle por lección)
  getUserProgress(): Observable<UserProgress[]> {
    const user = this.authService.getCurrentUser();
    console.log('🔍 UserProgressService - Usuario actual:', user);
    
    if (!user) {
      console.log('❌ UserProgressService - No hay usuario autenticado');
      return of([]);
    }

    const url = `${this.API_URL}/progreso/usuario/${user.id}`;
    console.log('🌐 UserProgressService - Consultando URL:', url);

    return this.http.get<UserProgress[]>(url).pipe(
      catchError(error => {
        console.error('❌ UserProgressService - Error obteniendo progreso del usuario:', error);
        return of([]);
      })
    );
  }


  // Obtener estadísticas del usuario
  getUserStats(): Observable<UserStats> {
    const user = this.authService.getCurrentUser();
    console.log('📊 UserProgressService - Obteniendo estadísticas para usuario:', user);
    
    if (!user) {
      console.log('❌ UserProgressService - No hay usuario para estadísticas');
      return of({
        totalCursos: 0,
        cursosIniciados: 0,
        cursosCompletados: 0,
        progresoPromedio: 0,
        tiempoTotalEstudio: 0,
        totalInsignias: 0,
        leccionesCompletadas: 0,
        leccionesEnProgreso: 0
      });
    }

    const url = `${this.API_URL}/progreso/usuario/${user.id}/estadisticas`;
    console.log('🌐 UserProgressService - Consultando estadísticas URL:', url);

    return this.http.get<UserStats>(url).pipe(
      catchError(error => {
        console.error('❌ UserProgressService - Error obteniendo estadísticas del usuario:', error);
        return of({
          totalCursos: 0,
          cursosIniciados: 0,
          cursosCompletados: 0,
          progresoPromedio: 0,
          tiempoTotalEstudio: 0,
          totalInsignias: 0,
          leccionesCompletadas: 0,
          leccionesEnProgreso: 0
        });
      })
    );
  }

  // Obtener cursos iniciados
  getCursosIniciados(): Observable<ProgramaCompletoDTO[]> {
    const user = this.authService.getCurrentUser();
    if (!user) {
      return of([]);
    }

    return this.getUserProgress().pipe(
      map(progress => {
        // Agrupar por programa y calcular progreso
        const programasMap = new Map<number, ProgramaCompletoDTO>();
        
        progress
          .filter(p => p.estado === 'en_progreso' || p.estado === 'iniciado')
          .forEach(p => {
            if (p.leccion?.programa) {
              const programa = p.leccion.programa;
              if (!programasMap.has(programa.id)) {
                programasMap.set(programa.id, programa);
              }
            }
          });
        
        return Array.from(programasMap.values());
      }),
      catchError(error => {
        console.error('Error obteniendo cursos iniciados:', error);
        return of([]);
      })
    );
  }

  // Obtener cursos completados
  getCursosCompletados(): Observable<ProgramaCompletoDTO[]> {
    const user = this.authService.getCurrentUser();
    if (!user) {
      return of([]);
    }

    return this.getUserProgress().pipe(
      map(progress => {
        // Agrupar por programa y verificar si está completado
        const programasMap = new Map<number, ProgramaCompletoDTO>();
        
        progress
          .filter(p => p.estado === 'completado')
          .forEach(p => {
            if (p.leccion?.programa) {
              const programa = p.leccion.programa;
              if (!programasMap.has(programa.id)) {
                programasMap.set(programa.id, programa);
              }
            }
          });
        
        return Array.from(programasMap.values());
      }),
      catchError(error => {
        console.error('Error obteniendo cursos completados:', error);
        return of([]);
      })
    );
  }


  // Marcar curso como iniciado
  iniciarCurso(leccionId: number): Observable<UserProgress> {
    const user = this.authService.getCurrentUser();
    if (!user) {
      throw new Error('Usuario no autenticado');
    }

    const progressData = {
      usuario_id: user.id,
      leccion_id: leccionId, // El backend espera Integer
      estado: 'iniciado',
      porcentaje: 0
    };

    return this.http.post<UserProgress>(`${this.API_URL}/progreso`, progressData).pipe(
      catchError(error => {
        console.error('Error iniciando curso:', error);
        throw error;
      })
    );
  }

  // Actualizar progreso de una lección
  actualizarProgreso(leccionId: number, nuevoProgreso: number): Observable<UserProgress> {
    const user = this.authService.getCurrentUser();
    if (!user) {
      throw new Error('Usuario no autenticado');
    }

    const progressData = {
      usuario_id: user.id,
      leccion_id: leccionId, // El backend espera Integer, pero el frontend puede enviar number
      estado: nuevoProgreso >= 100 ? 'completado' : 'en_progreso',
      porcentaje: Math.min(100, Math.max(0, nuevoProgreso))
    };

    return this.http.post<UserProgress>(`${this.API_URL}/progreso`, progressData).pipe(
      catchError(error => {
        console.error('Error actualizando progreso:', error);
        throw error;
      })
    );
  }

  // Obtener insignias del usuario
  getInsigniasUsuario(): Observable<InsigniaOtorgada[]> {
    const user = this.authService.getCurrentUser();
    console.log('🏆 UserProgressService - Obteniendo insignias para usuario:', user);
    
    if (!user) {
      console.log('❌ UserProgressService - No hay usuario para insignias');
      return of([]);
    }

    const url = `${this.API_URL}/insignias/usuario/${user.id}`;
    console.log('🌐 UserProgressService - Consultando insignias URL:', url);

    return this.http.get<InsigniaOtorgada[]>(url).pipe(
      catchError(error => {
        console.error('❌ UserProgressService - Error obteniendo insignias del usuario:', error);
        return of([]);
      })
    );
  }

  // Obtener todas las insignias disponibles
  getInsigniasDisponibles(): Observable<Insignia[]> {
    return this.http.get<Insignia[]>(`${this.API_URL}/insignias`).pipe(
      catchError(error => {
        console.error('Error obteniendo insignias disponibles:', error);
        return of([]);
      })
    );
  }

  // Marcar lección como completada
  completarLeccion(leccionId: number): Observable<UserProgress> {
    return this.actualizarProgreso(leccionId, 100);
  }
}
