import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

export interface ArchivoDTO {
  id?: number;
  nombreOriginal: string;
  nombreArchivo: string;
  tipoMime: string;
  tamano: number;
  descripcion?: string;
  fechaSubida?: string;
  url?: string;
}

export interface ArchivoUploadResponse {
  id: number;
  nombreOriginal: string;
  nombreArchivo: string;
  tipoMime: string;
  tamano: number;
  descripcion?: string;
  fechaSubida: string;
  url: string;
}

@Injectable({
  providedIn: 'root'
})
export class ArchivoService {
  private readonly API_URL = 'http://localhost:8081/courses-service/api/archivos';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  /**
   * Subir un archivo
   */
  subirArchivo(archivo: File, descripcion?: string): Observable<ArchivoUploadResponse> {
    const formData = new FormData();
    formData.append('archivo', archivo);
    if (descripcion) {
      formData.append('descripcion', descripcion);
    }

    return this.http.post<ArchivoUploadResponse>(
      `${this.API_URL}/subir`,
      formData,
      { headers: this.getAuthHeaders() }
    );
  }

  /**
   * Descargar un archivo por ID desde S3
   */
  descargarArchivo(id: number): Observable<Blob> {
    return this.http.get(
      `http://localhost:8082/api/materials/${id}/download`,
      {
        responseType: 'blob'
      }
    );
  }

  /**
   * Obtener información de un archivo por ID
   */
  obtenerArchivoPorId(id: number): Observable<ArchivoDTO> {
    return this.http.get<ArchivoDTO>(
      `${this.API_URL}/${id}`,
      { headers: this.getAuthHeaders() }
    );
  }

  /**
   * Obtener todos los archivos
   */
  obtenerTodosLosArchivos(): Observable<ArchivoDTO[]> {
    return this.http.get<ArchivoDTO[]>(
      this.API_URL,
      { headers: this.getAuthHeaders() }
    );
  }

  /**
   * Eliminar un archivo por ID
   */
  eliminarArchivo(id: number): Observable<any> {
    return this.http.delete<any>(
      `${this.API_URL}/${id}`,
      { headers: this.getAuthHeaders() }
    );
  }

  /**
   * Buscar archivos por descripción
   */
  buscarArchivosPorDescripcion(descripcion: string): Observable<ArchivoDTO[]> {
    const params = new HttpParams().set('descripcion', descripcion);
    return this.http.get<ArchivoDTO[]>(
      `${this.API_URL}/buscar`,
      {
        headers: this.getAuthHeaders(),
        params: params
      }
    );
  }

  /**
   * Obtener archivos recientes
   */
  obtenerArchivosRecientes(limite: number = 10): Observable<ArchivoDTO[]> {
    const params = new HttpParams().set('limite', limite.toString());
    return this.http.get<ArchivoDTO[]>(
      `${this.API_URL}/recientes`,
      {
        headers: this.getAuthHeaders(),
        params: params
      }
    );
  }

  /**
   * Helper method para descargar archivo y abrirlo
   */
  descargarYMostrarArchivo(id: number, nombreArchivo: string): void {
    this.descargarArchivo(id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = nombreArchivo;
        link.style.display = 'none';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
      },
      error: (error) => {
        throw error;
      }
    });
  }

  /**
   * Helper method para obtener la extensión del archivo
   */
  obtenerExtensionArchivo(nombreArchivo: string): string {
    return nombreArchivo.split('.').pop()?.toLowerCase() || '';
  }

  /**
   * Helper method para obtener el icono del tipo de archivo
   */
  obtenerIconoTipoArchivo(tipoMime: string): string {
    if (tipoMime.includes('pdf')) return '📄';
    if (tipoMime.includes('word') || tipoMime.includes('document')) return '📝';
    if (tipoMime.includes('excel') || tipoMime.includes('spreadsheet')) return '📊';
    if (tipoMime.includes('powerpoint') || tipoMime.includes('presentation')) return '📽️';
    if (tipoMime.includes('image')) return '🖼️';
    if (tipoMime.includes('video')) return '🎥';
    if (tipoMime.includes('audio')) return '🎵';
    if (tipoMime.includes('zip') || tipoMime.includes('rar')) return '📦';
    return '📎';
  }

  /**
   * Helper method para formatear el tamaño del archivo
   */
  formatearTamanoArchivo(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }
}
