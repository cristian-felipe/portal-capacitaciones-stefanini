import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProgramaCompletoService, ProgramaCompletoDTO, UnidadDTO, LeccionDTO } from '../../services/programa-completo.service';
import { ArchivoService, ArchivoDTO } from '../../services/archivo.service';

@Component({
  selector: 'app-programa-completo-form',
  templateUrl: './programa-completo-form.component.html',
  styleUrls: ['./programa-completo-form.component.scss']
})
export class ProgramaCompletoFormComponent implements OnInit {
  programaForm: FormGroup;
  isEditMode = false;
  loading = false;
  uploadingFiles: { [key: string]: boolean } = {};

  // Opciones para tipos de material
  tiposMaterial = [
    { value: 'video', label: 'Video' },
    { value: 'documento', label: 'Documento PDF' },
    { value: 'presentacion', label: 'Presentación' },
    { value: 'enlace', label: 'Enlace Web' },
    { value: 'quiz', label: 'Cuestionario' },
    { value: 'imagen', label: 'Imagen' }
  ];

  // Áreas de conocimiento comunes
  areasConocimiento = [
    'Desarrollo de Software',
    'Base de Datos',
    'Redes y Seguridad',
    'Inteligencia Artificial',
    'Machine Learning',
    'DevOps',
    'Cloud Computing',
    'Frontend Development',
    'Backend Development',
    'Mobile Development',
    'Testing y QA',
    'Project Management',
    'UX/UI Design',
    'Data Science',
    'Cybersecurity',
    'Blockchain',
    'IoT',
    'Otro'
  ];

  constructor(
    private fb: FormBuilder,
    private programaCompletoService: ProgramaCompletoService,
    private archivoService: ArchivoService,
    private dialogRef: MatDialogRef<ProgramaCompletoFormComponent>,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit', programa?: ProgramaCompletoDTO }
  ) {
    this.isEditMode = data.mode === 'edit';
    this.initializeForm();
  }

  ngOnInit(): void {
    if (this.isEditMode && this.data.programa) {
      this.populateForm(this.data.programa);
    }
  }

  private initializeForm(): void {
    this.programaForm = this.fb.group({
      titulo: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(200)]],
      descripcion: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(1000)]],
      areaConocimiento: ['', Validators.required],
      unidades: this.fb.array([])
    });

    // Agregar una unidad inicial si es modo creación
    if (!this.isEditMode) {
      this.addUnidad();
    }
  }

  private populateForm(programa: ProgramaCompletoDTO): void {
    this.programaForm.patchValue({
      titulo: programa.titulo,
      descripcion: programa.descripcion,
      areaConocimiento: programa.areaConocimiento
    });

    // Limpiar unidades existentes
    const unidadesArray = this.programaForm.get('unidades') as FormArray;
    unidadesArray.clear();

    // Agregar unidades del programa
    if (programa.unidades && programa.unidades.length > 0) {
      programa.unidades.forEach(unidad => {
        this.addUnidad(unidad);
      });
    } else {
      this.addUnidad();
    }
  }

  get unidadesArray(): FormArray {
    return this.programaForm.get('unidades') as FormArray;
  }

  addUnidad(unidad?: UnidadDTO): void {
    const unidadForm = this.fb.group({
      titulo: [unidad?.titulo || '', [Validators.required, Validators.minLength(3), Validators.maxLength(200)]],
      descripcion: [unidad?.descripcion || '', [Validators.maxLength(500)]],
      orden: [unidad?.orden || this.unidadesArray.length + 1, [Validators.required, Validators.min(1)]],
      lecciones: this.fb.array([])
    });

    this.unidadesArray.push(unidadForm);

    // Agregar lecciones si la unidad las tiene
    if (unidad?.lecciones && unidad.lecciones.length > 0) {
      const leccionesArray = unidadForm.get('lecciones') as FormArray;
      unidad.lecciones.forEach(leccion => {
        this.addLeccion(leccionesArray, leccion);
      });
    } else {
      // Agregar una lección inicial
      this.addLeccion(unidadForm.get('lecciones') as FormArray);
    }
  }

  removeUnidad(index: number): void {
    if (this.unidadesArray.length > 1) {
      this.unidadesArray.removeAt(index);
      this.updateUnidadOrders();
    }
  }

  addLeccion(leccionesArray: FormArray, leccion?: LeccionDTO): void {
    const leccionForm = this.fb.group({
      titulo: [leccion?.titulo || '', [Validators.required, Validators.minLength(3), Validators.maxLength(200)]],
      descripcion: [leccion?.descripcion || '', [Validators.maxLength(500)]],
      orden: [leccion?.orden || leccionesArray.length + 1, [Validators.required, Validators.min(1)]],
      tipoMaterial: [leccion?.tipoMaterial || 'video', Validators.required],
      urlMaterial: [leccion?.urlMaterial || '', [Validators.maxLength(500)]],
      archivoId: [leccion?.archivoId || null],
      materialId: [leccion?.materialId || null]
    });

    leccionForm.get('urlMaterial')?.setValidators([
      this.urlOrFileValidator.bind(this),
      Validators.maxLength(500)
    ]);

    leccionesArray.push(leccionForm);
  }

  private urlOrFileValidator(control: any): {[key: string]: any} | null {
    const archivoId = control.parent?.get('archivoId')?.value;
    const materialId = control.parent?.get('materialId')?.value;
    const urlMaterial = control.value;
    
    if (!archivoId && !materialId && !urlMaterial) {
      return { 'urlOrFileRequired': true };
    }
    
    return null;
  }

  removeLeccion(unidadIndex: number, leccionIndex: number): void {
    const unidadForm = this.unidadesArray.at(unidadIndex);
    const leccionesArray = unidadForm.get('lecciones') as FormArray;
    
    if (leccionesArray.length > 1) {
      leccionesArray.removeAt(leccionIndex);
      this.updateLeccionOrders(unidadIndex);
    }
  }

  private updateUnidadOrders(): void {
    this.unidadesArray.controls.forEach((control, index) => {
      control.get('orden')?.setValue(index + 1);
    });
  }

  private updateLeccionOrders(unidadIndex: number): void {
    const unidadForm = this.unidadesArray.at(unidadIndex);
    const leccionesArray = unidadForm.get('lecciones') as FormArray;
    
    leccionesArray.controls.forEach((control, index) => {
      control.get('orden')?.setValue(index + 1);
    });
  }

  moveUnidadUp(index: number): void {
    if (index > 0) {
      const unidades = this.unidadesArray;
      const unidad = unidades.at(index);
      unidades.removeAt(index);
      unidades.insert(index - 1, unidad);
      this.updateUnidadOrders();
    }
  }

  moveUnidadDown(index: number): void {
    if (index < this.unidadesArray.length - 1) {
      const unidades = this.unidadesArray;
      const unidad = unidades.at(index);
      unidades.removeAt(index);
      unidades.insert(index + 1, unidad);
      this.updateUnidadOrders();
    }
  }

  moveLeccionUp(unidadIndex: number, leccionIndex: number): void {
    if (leccionIndex > 0) {
      const unidadForm = this.unidadesArray.at(unidadIndex);
      const leccionesArray = unidadForm.get('lecciones') as FormArray;
      const leccion = leccionesArray.at(leccionIndex);
      leccionesArray.removeAt(leccionIndex);
      leccionesArray.insert(leccionIndex - 1, leccion);
      this.updateLeccionOrders(unidadIndex);
    }
  }

  moveLeccionDown(unidadIndex: number, leccionIndex: number): void {
    const unidadForm = this.unidadesArray.at(unidadIndex);
    const leccionesArray = unidadForm.get('lecciones') as FormArray;
    
    if (leccionIndex < leccionesArray.length - 1) {
      const leccion = leccionesArray.at(leccionIndex);
      leccionesArray.removeAt(leccionIndex);
      leccionesArray.insert(leccionIndex + 1, leccion);
      this.updateLeccionOrders(unidadIndex);
    }
  }

  onSubmit(): void {
    if (this.programaForm.valid) {
      this.loading = true;
      const programaData = this.programaForm.value;
      
      this.cleanupEmptyArchivoIds(programaData);

      const operation = this.isEditMode && this.data.programa?.id
        ? this.programaCompletoService.actualizarProgramaCompleto(this.data.programa.id, programaData)
        : this.programaCompletoService.crearProgramaCompleto(programaData);

      operation.subscribe({
        next: (result) => {
          this.loading = false;
          this.snackBar.open('Programa guardado exitosamente', 'Cerrar', { duration: 3000 });
          this.dialogRef.close(result);
        },
        error: (error) => {
          this.loading = false;
          let errorMessage = 'Error al guardar el programa';
          
          if (error.status === 400) {
            errorMessage = 'Datos inválidos. Verifica que todos los campos estén completos.';
          } else if (error.status === 401) {
            errorMessage = 'No tienes permisos para realizar esta acción.';
          } else if (error.status === 500) {
            errorMessage = 'Error interno del servidor.';
          }
          
          this.snackBar.open(errorMessage, 'Cerrar', { duration: 5000 });
        }
      });
    } else {
      this.markFormGroupTouched();
      this.snackBar.open('Por favor, completa todos los campos requeridos', 'Cerrar', { duration: 3000 });
    }
  }

  private markFormGroupTouched(): void {
    Object.keys(this.programaForm.controls).forEach(key => {
      const control = this.programaForm.get(key);
      control?.markAsTouched();
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  getErrorMessage(controlName: string): string {
    const control = this.programaForm.get(controlName);
    if (control?.hasError('required')) {
      return 'Este campo es requerido';
    }
    if (control?.hasError('minlength')) {
      return `Mínimo ${control.errors?.['minlength'].requiredLength} caracteres`;
    }
    if (control?.hasError('maxlength')) {
      return `Máximo ${control.errors?.['maxlength'].requiredLength} caracteres`;
    }
    if (control?.hasError('min')) {
      return `Valor mínimo: ${control.errors?.['min'].min}`;
    }
    return '';
  }

  getUnidadErrorMessage(unidadIndex: number, controlName: string): string {
    const unidadForm = this.unidadesArray.at(unidadIndex);
    const control = unidadForm.get(controlName);
    if (control?.hasError('required')) {
      return 'Este campo es requerido';
    }
    if (control?.hasError('minlength')) {
      return `Mínimo ${control.errors?.['minlength'].requiredLength} caracteres`;
    }
    if (control?.hasError('maxlength')) {
      return `Máximo ${control.errors?.['maxlength'].requiredLength} caracteres`;
    }
    if (control?.hasError('min')) {
      return `Valor mínimo: ${control.errors?.['min'].min}`;
    }
    return '';
  }

  getLeccionErrorMessage(unidadIndex: number, leccionIndex: number, controlName: string): string {
    const unidadForm = this.unidadesArray.at(unidadIndex);
    const leccionesArray = unidadForm.get('lecciones') as FormArray;
    const leccionForm = leccionesArray.at(leccionIndex);
    const control = leccionForm.get(controlName);
    if (control?.hasError('required')) {
      return 'Este campo es requerido';
    }
    if (control?.hasError('minlength')) {
      return `Mínimo ${control.errors?.['minlength'].requiredLength} caracteres`;
    }
    if (control?.hasError('maxlength')) {
      return `Máximo ${control.errors?.['maxlength'].requiredLength} caracteres`;
    }
    if (control?.hasError('min')) {
      return `Valor mínimo: ${control.errors?.['min'].min}`;
    }
    return '';
  }

  getLeccionesCount(unidad: any): number {
    const leccionesArray = unidad.get('lecciones') as FormArray;
    return leccionesArray ? leccionesArray.length : 0;
  }

  getLeccionesArray(unidad: any): FormArray {
    return unidad.get('lecciones') as FormArray;
  }

  addLeccionToUnidad(unidadIndex: number): void {
    const unidadForm = this.unidadesArray.at(unidadIndex);
    const leccionesArray = unidadForm.get('lecciones') as FormArray;
    this.addLeccion(leccionesArray);
  }

  onFileSelected(event: any, unidadIndex: number, leccionIndex: number): void {
    const file = event.target.files[0];
    if (file) {
      const fileKey = `${unidadIndex}-${leccionIndex}`;
      this.uploadingFiles[fileKey] = true;
      
      const descripcion = `Archivo para lección: ${file.name}`;
      
      this.archivoService.subirArchivo(file, descripcion).subscribe({
        next: (archivoResponse) => {
          const unidadForm = this.unidadesArray.at(unidadIndex);
          const leccionesArray = unidadForm.get('lecciones') as FormArray;
          const leccionForm = leccionesArray.at(leccionIndex);
          
          leccionForm.patchValue({
            archivoId: archivoResponse.id,
            materialId: archivoResponse.id,
            urlMaterial: archivoResponse.url
          });
          
          leccionForm.get('urlMaterial')?.updateValueAndValidity();
          
          this.uploadingFiles[fileKey] = false;
          this.snackBar.open('Archivo subido exitosamente', 'Cerrar', { duration: 3000 });
        },
        error: (error) => {
          this.uploadingFiles[fileKey] = false;
          this.snackBar.open('Error al subir el archivo', 'Cerrar', { duration: 5000 });
        }
      });
    }
  }

  isUploadingFile(unidadIndex: number, leccionIndex: number): boolean {
    const fileKey = `${unidadIndex}-${leccionIndex}`;
    return this.uploadingFiles[fileKey] || false;
  }

  getFileName(unidadIndex: number, leccionIndex: number): string {
    const unidadForm = this.unidadesArray.at(unidadIndex);
    const leccionesArray = unidadForm.get('lecciones') as FormArray;
    const leccionForm = leccionesArray.at(leccionIndex);
    const archivoId = leccionForm.get('archivoId')?.value;
    
    if (archivoId) {
      return `Archivo ID: ${archivoId}`;
    }
    return 'Ningún archivo seleccionado';
  }

  removeFile(unidadIndex: number, leccionIndex: number): void {
    const unidadForm = this.unidadesArray.at(unidadIndex);
    const leccionesArray = unidadForm.get('lecciones') as FormArray;
    const leccionForm = leccionesArray.at(leccionIndex);
    
    leccionForm.patchValue({
      archivoId: null,
      materialId: null,
      urlMaterial: ''
    });
    
    leccionForm.get('urlMaterial')?.updateValueAndValidity();
    
    this.snackBar.open('Archivo removido', 'Cerrar', { duration: 2000 });
  }

  private cleanupEmptyArchivoIds(programaData: any): void {
    if (programaData.unidades) {
      programaData.unidades.forEach((unidad: any) => {
        if (unidad.lecciones) {
          unidad.lecciones.forEach((leccion: any) => {
            if (!leccion.archivoId || leccion.archivoId === null) {
              delete leccion.archivoId;
            }
            if (!leccion.materialId || leccion.materialId === null) {
              delete leccion.materialId;
            }
          });
        }
      });
    }
  }

  private getFormErrors(): any {
    const errors: any = {};
    
    Object.keys(this.programaForm.controls).forEach(key => {
      const control = this.programaForm.get(key);
      if (control && control.errors) {
        errors[key] = control.errors;
      }
    });
    
    // Verificar errores en unidades
    const unidadesArray = this.programaForm.get('unidades') as FormArray;
    unidadesArray.controls.forEach((unidadControl, unidadIndex) => {
      Object.keys(unidadControl.value).forEach(key => {
        const control = unidadControl.get(key);
        if (control && control.errors) {
          errors[`unidad_${unidadIndex}_${key}`] = control.errors;
        }
      });
      
      // Verificar errores en lecciones
      const leccionesArray = unidadControl.get('lecciones') as FormArray;
      leccionesArray.controls.forEach((leccionControl, leccionIndex) => {
        Object.keys(leccionControl.value).forEach(key => {
          const control = leccionControl.get(key);
          if (control && control.errors) {
            errors[`unidad_${unidadIndex}_leccion_${leccionIndex}_${key}`] = control.errors;
          }
        });
      });
    });
    
    return errors;
  }
}
