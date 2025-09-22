import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ProgramaCompletoService, ProgramaCompletoDTO } from '../../services/programa-completo.service';
import { UserProgressService, UserProgress, UserStats, InsigniaOtorgada } from '../../services/user-progress.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  user: any = null;
  cursosInscritos: ProgramaCompletoDTO[] = [];
  cursosEnProgreso: ProgramaCompletoDTO[] = [];
  cursosCompletados: ProgramaCompletoDTO[] = [];
  userStats: UserStats | null = null;
  insigniasUsuario: InsigniaOtorgada[] = [];
  isLoading = false;
  activeTab = 0;

  constructor(
    private authService: AuthService,
    private programaCompletoService: ProgramaCompletoService,
    private userProgressService: UserProgressService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.cargarDatosUsuario();
    this.cargarCursosUsuario();
    this.cargarInsigniasUsuario();
  }

  cargarDatosUsuario(): void {
    this.user = this.authService.getCurrentUser();
    if (!this.user) {
      // Si no hay usuario autenticado, usar datos por defecto para pruebas
      this.user = {
        id: 'd1234567-89ab-4cde-9012-3456789abcde', // UUID que coincide con los datos del backend
        nombre: 'Usuario',
        apellido: 'Demo',
        email: 'usuario@stefanini.com',
        correoElectronico: 'usuario@stefanini.com',
        rol: 'usuario',
        fechaCreacion: new Date().toISOString()
      };
    }
  }

  cargarCursosUsuario(): void {
    this.isLoading = true;
    
    // Cargar estadísticas del usuario
    this.userProgressService.getUserStats().subscribe({
      next: (stats) => {
        this.userStats = stats;
      },
      error: (error) => {
        // Error silencioso para estadísticas
      }
    });

    // OPTIMIZACIÓN: Una sola consulta para obtener progreso y procesar en el frontend
    this.userProgressService.getUserProgress().subscribe({
      next: (progress) => {
        // Procesar cursos inscritos en el frontend
        this.cursosInscritos = this.procesarCursosInscritos(progress);
        
        // Procesar cursos en progreso en el frontend
        this.cursosEnProgreso = this.procesarCursosEnProgreso(progress);
        
        // Procesar cursos completados en el frontend
        this.cursosCompletados = this.procesarCursosCompletados(progress);
        
        this.isLoading = false;
      },
      error: (error) => {
        this.snackBar.open('Error cargando progreso del usuario', 'Cerrar', {
          duration: 3000
        });
        this.isLoading = false;
      }
    });
  }

  cargarInsigniasUsuario(): void {
    this.userProgressService.getInsigniasUsuario().subscribe({
      next: (insignias) => {
        this.insigniasUsuario = insignias;
      },
      error: (error) => {
        // Error silencioso para insignias
      }
    });
  }

  cambiarTab(index: number): void {
    this.activeTab = index;
  }

  getRolDisplayName(rol: string): string {
    const roles: { [key: string]: string } = {
      'admin': 'Administrador',
      'instructor': 'Instructor',
      'usuario': 'Usuario'
    };
    return roles[rol] || rol;
  }

  getRolColor(rol: string): string {
    const colores: { [key: string]: string } = {
      'admin': '#f44336',
      'instructor': '#ff9800',
      'usuario': '#2196f3'
    };
    return colores[rol] || '#757575';
  }

  getRolIcon(rol: string): string {
    const iconos: { [key: string]: string } = {
      'admin': 'admin_panel_settings',
      'instructor': 'book',
      'usuario': 'person'
    };
    return iconos[rol] || 'person';
  }

  calcularProgreso(curso: ProgramaCompletoDTO): number {
    // El progreso real viene del backend a través de userStats
    return this.userStats?.progresoPromedio || 0;
  }

  getProgresoColor(progreso: number): string {
    if (progreso >= 80) return '#4caf50';
    if (progreso >= 50) return '#ff9800';
    return '#f44336';
  }

  logout(): void {
    this.authService.logout();
  }

  // Métodos helper para procesar datos en el frontend
  private procesarCursosInscritos(progress: any[]): ProgramaCompletoDTO[] {
    const programasMap = new Map<string, ProgramaCompletoDTO>();
    
    progress
      .filter(p => p.estado === 'inscrito')
      .forEach(p => {
        if (p.tituloPrograma) {
          const programaId = p.tituloPrograma;
          if (!programasMap.has(programaId)) {
            const programa: ProgramaCompletoDTO = {
              id: p.leccionId || 0,
              titulo: p.tituloPrograma,
              descripcion: `Curso inscrito - ${p.tituloLeccion}`,
              areaConocimiento: 'Capacitación',
              fechaCreacion: new Date().toISOString(),
              unidades: []
            };
            programasMap.set(programaId, programa);
          }
        }
      });
    
    return Array.from(programasMap.values());
  }

  private procesarCursosEnProgreso(progress: any[]): ProgramaCompletoDTO[] {
    const programasMap = new Map<string, ProgramaCompletoDTO>();
    
    progress
      .filter(p => p.estado === 'en_progreso' || p.estado === 'iniciado')
      .forEach(p => {
        if (p.tituloPrograma) {
          const programaId = p.tituloPrograma;
          if (!programasMap.has(programaId)) {
            const programa: ProgramaCompletoDTO = {
              id: p.leccionId || 0,
              titulo: p.tituloPrograma,
              descripcion: `Curso en progreso - ${p.tituloLeccion}`,
              areaConocimiento: 'Capacitación',
              fechaCreacion: new Date().toISOString(),
              unidades: []
            };
            programasMap.set(programaId, programa);
          }
        }
      });
    
    return Array.from(programasMap.values());
  }

  private procesarCursosCompletados(progress: any[]): ProgramaCompletoDTO[] {
    const programasMap = new Map<string, ProgramaCompletoDTO>();
    
    progress
      .filter(p => p.estado === 'completado')
      .forEach(p => {
        if (p.tituloPrograma) {
          const programaId = p.tituloPrograma;
          if (!programasMap.has(programaId)) {
            const programa: ProgramaCompletoDTO = {
              id: p.leccionId || 0,
              titulo: p.tituloPrograma,
              descripcion: `Curso completado - ${p.tituloLeccion}`,
              areaConocimiento: 'Capacitación',
              fechaCreacion: new Date().toISOString(),
              unidades: []
            };
            programasMap.set(programaId, programa);
          }
        }
      });
    
    return Array.from(programasMap.values());
  }

}

