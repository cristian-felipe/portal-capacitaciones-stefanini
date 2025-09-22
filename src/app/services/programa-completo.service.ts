import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

export interface LeccionDTO {
  id?: number;
  titulo: string;
  descripcion?: string;
  orden: number;
  tipoMaterial: string;
  urlMaterial: string;
  archivoId?: number;
  materialId?: number; // Campo para la descarga desde S3
}

export interface UnidadDTO {
  id?: number;
  titulo: string;
  descripcion?: string;
  orden: number;
  lecciones: LeccionDTO[];
}

export interface ProgramaCompletoDTO {
  id?: number;
  titulo: string;
  descripcion: string;
  areaConocimiento: string;
  fechaCreacion?: string;
  unidades: UnidadDTO[];
}

@Injectable({
  providedIn: 'root'
})
export class ProgramaCompletoService {
  private readonly API_URL = 'http://localhost:8081/courses-service/api/programas-completos';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  private getAuthHeaders(): HttpHeaders {
    return this.authService.getAuthHeaders();
  }

  /**
   * Crear programa completo con todas sus unidades y lecciones
   */
  crearProgramaCompleto(programaCompletoDTO: ProgramaCompletoDTO): Observable<ProgramaCompletoDTO> {
    return this.http.post<ProgramaCompletoDTO>(
      this.API_URL, 
      programaCompletoDTO,
      { headers: this.getAuthHeaders() }
    );
  }

  /**
   * Obtener todos los programas completos con sus unidades y lecciones
   */
  obtenerTodosLosProgramasCompletos(): Observable<ProgramaCompletoDTO[]> {
    return this.http.get<ProgramaCompletoDTO[]>(
      this.API_URL,
      { headers: this.getAuthHeaders() }
    );
  }

  /**
   * Obtener programa completo por ID con todas sus unidades y lecciones
   */
  obtenerProgramaCompletoPorId(id: number): Observable<ProgramaCompletoDTO> {
    return this.http.get<ProgramaCompletoDTO>(
      `${this.API_URL}/${id}`,
      { headers: this.getAuthHeaders() }
    );
  }

  /**
   * Actualizar programa completo (reemplaza todas las unidades y lecciones)
   */
  actualizarProgramaCompleto(id: number, programaCompletoDTO: ProgramaCompletoDTO): Observable<ProgramaCompletoDTO> {
    return this.http.put<ProgramaCompletoDTO>(
      `${this.API_URL}/${id}`,
      programaCompletoDTO,
      { headers: this.getAuthHeaders() }
    );
  }

  /**
   * Eliminar programa completo (elimina en cascada todas las unidades y lecciones)
   */
  eliminarProgramaCompleto(id: number): Observable<any> {
    return this.http.delete<any>(
      `${this.API_URL}/${id}`,
      { headers: this.getAuthHeaders() }
    );
  }

  /**
   * Buscar programas completos por título
   */
  buscarProgramasCompletosPorTitulo(titulo: string): Observable<ProgramaCompletoDTO[]> {
    return this.http.get<ProgramaCompletoDTO[]>(
      `${this.API_URL}/buscar/titulo?titulo=${encodeURIComponent(titulo)}`,
      { headers: this.getAuthHeaders() }
    );
  }

  /**
   * Buscar programas completos por área de conocimiento
   */
  buscarProgramasCompletosPorArea(area: string): Observable<ProgramaCompletoDTO[]> {
    return this.http.get<ProgramaCompletoDTO[]>(
      `${this.API_URL}/buscar/area?area=${encodeURIComponent(area)}`,
      { headers: this.getAuthHeaders() }
    );
  }

  /**
   * Obtener programas completos recientes
   */
  obtenerProgramasCompletosRecientes(): Observable<ProgramaCompletoDTO[]> {
    return this.http.get<ProgramaCompletoDTO[]>(
      `${this.API_URL}/recientes`,
      { headers: this.getAuthHeaders() }
    );
  }
}
