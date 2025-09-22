import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { ProgramaCompletoService } from '../../services/programa-completo.service';
import { ProgramaCompletoDTO } from '../../services/programa-completo.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit, OnDestroy {
  totalProgramas = 0;
  totalUnidades = 0;
  totalLecciones = 0;
  programasRecientes: ProgramaCompletoDTO[] = [];
  loading = false;

  constructor(
    private programaCompletoService: ProgramaCompletoService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadDashboardData();
  }

  ngOnDestroy(): void {
    this.loading = false;
  }

  loadDashboardData(): void {
    this.loading = true;
    
    this.programaCompletoService.obtenerTodosLosProgramasCompletos().subscribe({
      next: (programas) => {
        this.totalProgramas = programas ? programas.length : 0;
        this.totalUnidades = programas ? programas.reduce((total, programa) => {
          return total + (programa.unidades ? programa.unidades.length : 0);
        }, 0) : 0;
        this.totalLecciones = programas ? programas.reduce((total, programa) => {
          if (programa.unidades) {
            return total + programa.unidades.reduce((unidadTotal, unidad) => {
              return unidadTotal + (unidad.lecciones ? unidad.lecciones.length : 0);
            }, 0);
          }
          return total;
        }, 0) : 0;
        
        this.programasRecientes = programas ? programas
          .sort((a, b) => new Date(b.fechaCreacion || '').getTime() - new Date(a.fechaCreacion || '').getTime())
          .slice(0, 5) : [];
        
        this.loading = false;
      },
      error: (error) => {
        this.totalProgramas = 0;
        this.totalUnidades = 0;
        this.totalLecciones = 0;
        this.programasRecientes = [];
        this.loading = false;
      }
    });
  }

  goToCourseManagement(): void {
    this.router.navigate(['/admin/courses']);
  }

  goToPrograma(programa: ProgramaCompletoDTO): void {
    if (programa.id) {
      this.router.navigate(['/admin/courses', programa.id]);
    }
  }

  getTotalLecciones(programa: ProgramaCompletoDTO): number {
    if (!programa.unidades) return 0;
    return programa.unidades.reduce((total, unidad) => {
      return total + (unidad.lecciones ? unidad.lecciones.length : 0);
    }, 0);
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }
}