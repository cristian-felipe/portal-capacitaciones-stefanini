import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ProgramaCompletoService, ProgramaCompletoDTO } from '../../services/programa-completo.service';
import { ProgramaCompletoFormComponent } from '../programa-completo-form/programa-completo-form.component';

@Component({
  selector: 'app-admin-course-management',
  templateUrl: './admin-course-management.component.html',
  styleUrls: ['./admin-course-management.component.scss']
})
export class AdminCourseManagementComponent implements OnInit, OnDestroy {
  programas: ProgramaCompletoDTO[] = [];
  loading = false;
  searchTerm = '';
  searchBy = 'titulo'; // 'titulo' o 'area'
  filteredProgramas: ProgramaCompletoDTO[] = [];

  constructor(
    private programaCompletoService: ProgramaCompletoService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.resetLoadingState();
    this.loadProgramas();
  }

  ngOnDestroy(): void {
    // Limpiar estado al salir del componente
    this.loading = false;
  }

  private resetLoadingState(): void {
    this.loading = false;
  }

  loadProgramas(): void {
    this.loading = true;
    this.programaCompletoService.obtenerTodosLosProgramasCompletos()
      .subscribe({
        next: (programas) => {
          this.programas = programas || [];
          this.filteredProgramas = programas || [];
          this.loading = false;
        },
        error: (error) => {
          this.loading = false;
          this.programas = [];
          this.filteredProgramas = [];
          
          let errorMessage = 'Error al cargar los programas';
          if (error.status === 0) {
            errorMessage = 'Error de conexión con el servidor';
          } else if (error.status === 401) {
            errorMessage = 'Sesión expirada. Por favor, inicia sesión nuevamente';
          } else if (error.status === 403) {
            errorMessage = 'No tienes permisos para acceder a esta información';
          }
          
          this.snackBar.open(errorMessage, 'Cerrar', { duration: 5000 });
        }
      });
  }

  searchProgramas(): void {
    if (!this.searchTerm.trim()) {
      this.filteredProgramas = this.programas;
      return;
    }

    this.loading = true;
    const searchObservable = this.searchBy === 'titulo' 
      ? this.programaCompletoService.buscarProgramasCompletosPorTitulo(this.searchTerm)
      : this.programaCompletoService.buscarProgramasCompletosPorArea(this.searchTerm);

    searchObservable.subscribe({
      next: (programas) => {
        this.filteredProgramas = programas;
        this.loading = false;
      },
      error: (error) => {
        this.snackBar.open('Error al buscar programas', 'Cerrar', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  clearSearch(): void {
    this.searchTerm = '';
    this.filteredProgramas = this.programas;
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(ProgramaCompletoFormComponent, {
      width: '90%',
      maxWidth: '1200px',
      height: '90%',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadProgramas();
        this.snackBar.open('Programa creado exitosamente', 'Cerrar', { duration: 3000 });
      }
    });
  }

  openEditDialog(programa: ProgramaCompletoDTO): void {
    const dialogRef = this.dialog.open(ProgramaCompletoFormComponent, {
      width: '90%',
      maxWidth: '1200px',
      height: '90%',
      data: { mode: 'edit', programa }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadProgramas();
        this.snackBar.open('Programa actualizado exitosamente', 'Cerrar', { duration: 3000 });
      }
    });
  }

  deletePrograma(programa: ProgramaCompletoDTO): void {
    if (!programa.id) return;

    if (confirm(`¿Estás seguro de que deseas eliminar el programa "${programa.titulo}"? Esta acción eliminará todas las unidades y lecciones asociadas.`)) {
      this.loading = true;
      this.programaCompletoService.eliminarProgramaCompleto(programa.id)
        .subscribe({
          next: (response) => {
            this.loading = false;
            this.loadProgramas();
            this.snackBar.open('Programa eliminado exitosamente', 'Cerrar', { duration: 3000 });
          },
          error: (error) => {
            this.loading = false;
            let errorMessage = 'Error al eliminar el programa';
            
            if (error.status === 404) {
              errorMessage = 'El programa no fue encontrado';
            } else if (error.status === 403) {
              errorMessage = 'No tienes permisos para eliminar este programa';
            } else if (error.status === 500) {
              errorMessage = 'Error interno del servidor';
            }
            
            this.snackBar.open(errorMessage, 'Cerrar', { duration: 5000 });
          }
        });
    }
  }

  viewPrograma(programa: ProgramaCompletoDTO): void {
    if (programa.id) {
      this.router.navigate(['/admin/courses', programa.id]);
    }
  }

  getTotalUnidades(programa: ProgramaCompletoDTO): number {
    return programa.unidades ? programa.unidades.length : 0;
  }

  getTotalLecciones(programa: ProgramaCompletoDTO): number {
    if (!programa.unidades) return 0;
    return programa.unidades.reduce((total, unidad) => {
      return total + (unidad.lecciones ? unidad.lecciones.length : 0);
    }, 0);
  }

  goBack(): void {
    this.router.navigate(['/admin']);
  }
}
