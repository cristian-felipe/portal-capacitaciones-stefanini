import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProgramaCompletoService, ProgramaCompletoDTO, UnidadDTO, LeccionDTO } from '../../services/programa-completo.service';
import { ArchivoService } from '../../services/archivo.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-course-details',
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.scss']
})
export class CourseDetailsComponent implements OnInit {
  programa: ProgramaCompletoDTO | null = null;
  unidades: UnidadDTO[] = [];
  isLoading = false;
  selectedUnidad: UnidadDTO | null = null;
  expandedUnidades: Set<number> = new Set();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private programaCompletoService: ProgramaCompletoService,
    private archivoService: ArchivoService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const programaId = +params['id'];
      if (programaId) {
        this.cargarProgramaCompleto(programaId);
      }
    });
  }

  cargarProgramaCompleto(id: number): void {
    this.isLoading = true;
    this.programaCompletoService.obtenerProgramaCompletoPorId(id).subscribe({
      next: (programa) => {
        this.programa = programa;
        this.unidades = programa.unidades || [];
        this.isLoading = false;
      },
      error: (error) => {
        this.snackBar.open('Error al cargar el programa', 'Cerrar', {
          duration: 5000,
          panelClass: ['error-snackbar']
        });
        this.isLoading = false;
      }
    });
  }

  toggleUnidad(unidad: UnidadDTO): void {
    if (this.expandedUnidades.has(unidad.id || 0)) {
      this.expandedUnidades.delete(unidad.id || 0);
      this.selectedUnidad = null;
    } else {
      this.expandedUnidades.add(unidad.id || 0);
      this.selectedUnidad = unidad;
    }
  }

  abrirLeccion(leccion: LeccionDTO): void {
    const materialId = leccion.materialId || leccion.archivoId;
    
    if (materialId) {
      this.descargarArchivo(materialId, leccion.titulo);
    } else if (leccion.urlMaterial) {
      window.open(leccion.urlMaterial, '_blank');
    } else {
      this.snackBar.open('No hay material disponible para esta lección', 'Cerrar', {
        duration: 3000,
        panelClass: ['warning-snackbar']
      });
    }
  }

  descargarArchivo(archivoId: number, nombreLeccion: string): void {
    this.archivoService.descargarArchivo(archivoId).subscribe({
      next: (blob) => {
        const timestamp = new Date().toISOString().split('T')[0];
        const nombreArchivo = `${nombreLeccion.replace(/[^a-zA-Z0-9]/g, '_')}_${timestamp}.pdf`;
        
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = nombreArchivo;
        link.style.display = 'none';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
        
        this.snackBar.open('Archivo descargado exitosamente', 'Cerrar', {
          duration: 3000,
          panelClass: ['success-snackbar']
        });
      },
      error: (error) => {
        let errorMessage = 'Error al descargar el archivo';
        
        if (error.status === 404) {
          errorMessage = 'El archivo no fue encontrado';
        } else if (error.status === 403) {
          errorMessage = 'No tienes permisos para descargar este archivo';
        } else if (error.status === 500) {
          errorMessage = 'Error interno del servidor';
        }
        
        this.snackBar.open(errorMessage, 'Cerrar', {
          duration: 5000,
          panelClass: ['error-snackbar']
        });
      }
    });
  }

  volverAlCatalogo(): void {
    this.router.navigate(['/courses']);
  }

  comenzarCurso(): void {
    if (this.unidades.length > 0) {
      const primeraUnidad = this.unidades[0];
      if (primeraUnidad.lecciones && primeraUnidad.lecciones.length > 0) {
        const primeraLeccion = primeraUnidad.lecciones[0];
        this.abrirLeccion(primeraLeccion);
      } else {
        // Expandir la primera unidad para cargar las lecciones
        this.toggleUnidad(primeraUnidad);
      }
    }
  }

  getTipoMaterialIcon(tipo: string): string {
    const iconos: { [key: string]: string } = {
      'video': '🎥',
      'pdf': '📄',
      'link': '🔗'
    };
    return iconos[tipo] || '📚';
  }

  getTipoMaterialColor(tipo: string): string {
    const colores: { [key: string]: string } = {
      'video': '#f44336',
      'pdf': '#2196f3',
      'link': '#4caf50'
    };
    return colores[tipo] || '#757575';
  }

  getTotalLecciones(): number {
    return this.unidades.reduce((total, unidad) => {
      return total + (unidad.lecciones?.length || 0);
    }, 0);
  }

  getDuracionEstimada(): string {
    const totalLecciones = this.getTotalLecciones();
    const duracionPorLeccion = 15;
    const duracionTotal = totalLecciones * duracionPorLeccion;
    
    if (duracionTotal < 60) {
      return `${duracionTotal} minutos`;
    } else {
      const horas = Math.floor(duracionTotal / 60);
      const minutos = duracionTotal % 60;
      return `${horas}h ${minutos}m`;
    }
  }

  tieneArchivosAdjuntos(): boolean {
    if (!this.unidades) return false;
    
    return this.unidades.some(unidad => 
      unidad.lecciones && unidad.lecciones.some(leccion => leccion.materialId || leccion.archivoId)
    );
  }

  descargarTodosLosArchivos(): void {
    if (!this.unidades) return;
    
    let archivosDescargados = 0;
    const totalArchivos = this.getTotalArchivos();
    
    this.unidades.forEach(unidad => {
      if (unidad.lecciones) {
        unidad.lecciones.forEach(leccion => {
          const materialId = leccion.materialId || leccion.archivoId;
          if (materialId) {
            this.descargarArchivo(materialId, leccion.titulo);
            archivosDescargados++;
          }
        });
      }
    });
    
    if (archivosDescargados > 0) {
      this.snackBar.open(`Iniciando descarga de ${archivosDescargados} archivo(s)`, 'Cerrar', {
        duration: 3000,
        panelClass: ['info-snackbar']
      });
    } else {
      this.snackBar.open('No hay archivos para descargar', 'Cerrar', {
        duration: 3000,
        panelClass: ['warning-snackbar']
      });
    }
  }

  private getTotalArchivos(): number {
    if (!this.unidades) return 0;
    
    return this.unidades.reduce((total, unidad) => {
      if (unidad.lecciones) {
        return total + unidad.lecciones.filter(leccion => leccion.materialId || leccion.archivoId).length;
      }
      return total;
    }, 0);
  }

  tieneArchivoAdjunto(leccion: LeccionDTO): boolean {
    if (leccion.materialId || leccion.archivoId) {
      return true;
    }
    
    const tiposConArchivo = ['documento', 'pdf', 'presentacion', 'imagen'];
    if (tiposConArchivo.includes(leccion.tipoMaterial?.toLowerCase())) {
      return true;
    }
    
    return false;
  }
}


