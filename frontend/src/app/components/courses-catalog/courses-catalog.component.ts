import { Component, OnInit } from '@angular/core';
import { ProgramaCompletoService, ProgramaCompletoDTO } from '../../services/programa-completo.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-courses-catalog',
  templateUrl: './courses-catalog.component.html',
  styleUrls: ['./courses-catalog.component.scss']
})
export class CoursesCatalogComponent implements OnInit {
  programas: ProgramaCompletoDTO[] = [];
  programasFiltrados: ProgramaCompletoDTO[] = [];
  isLoading = false;
  searchTerm = '';
  selectedArea = '';
  
  areasConocimiento: string[] = [];

  constructor(
    private programaCompletoService: ProgramaCompletoService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.cargarProgramas();
  }

  cargarProgramas(): void {
    this.isLoading = true;
    this.programaCompletoService.obtenerTodosLosProgramasCompletos().subscribe({
      next: (programas) => {
        this.programas = programas;
        this.programasFiltrados = programas;
        
        // Extraer áreas de conocimiento únicas de los datos reales
        this.areasConocimiento = [...new Set(programas.map(p => p.areaConocimiento))];
        
        this.isLoading = false;
      },
      error: (error) => {
        this.snackBar.open('Error al cargar los cursos', 'Cerrar', {
          duration: 5000,
          panelClass: ['error-snackbar']
        });
        this.isLoading = false;
      }
    });
  }

  filtrarCursos(): void {
    let resultados = [...this.programas];

    // Filtrar por término de búsqueda
    if (this.searchTerm.trim()) {
      const termino = this.searchTerm.toLowerCase();
      resultados = resultados.filter(programa =>
        programa.titulo.toLowerCase().includes(termino) ||
        programa.descripcion.toLowerCase().includes(termino) ||
        programa.areaConocimiento.toLowerCase().includes(termino)
      );
    }

    // Filtrar por área de conocimiento
    if (this.selectedArea) {
      resultados = resultados.filter(programa =>
        programa.areaConocimiento === this.selectedArea
      );
    }

    this.programasFiltrados = resultados;
  }

  limpiarFiltros(): void {
    this.searchTerm = '';
    this.selectedArea = '';
    this.programasFiltrados = [...this.programas];
  }


  getAreaIcon(area: string): string {
    const iconos: { [key: string]: string } = {
      'Fullstack': '💻',
      'APIs e Integraciones': '🔗',
      'Cloud': '☁️',
      'Data Engineer': '📊'
    };
    return iconos[area] || '📚';
  }

  getAreaColor(area: string): string {
    const colores: { [key: string]: string } = {
      'Fullstack': '#2196F3',
      'APIs e Integraciones': '#FF9800',
      'Cloud': '#4CAF50',
      'Data Engineer': '#9C27B0'
    };
    return colores[area] || '#757575';
  }
}

