# Portal de Capacitaciones - Frontend

Frontend del Portal de Capacitaciones de Stefanini desarrollado en Angular 9 con Angular Material.

## 🚀 Características

- **Autenticación**: Login con email/password y OAuth2 (Google)
- **Dashboard**: Panel principal con acciones rápidas
- **Módulo de Administración**: Gestión completa de cursos, unidades y lecciones
- **Gestión de Archivos**: Subida y descarga de materiales desde S3
- **Responsive**: Diseño adaptativo para móviles y desktop
- **Material Design**: Interfaz moderna con Angular Material
- **Guards**: Protección de rutas basada en roles
- **Servicios**: Gestión de autenticación y estado

## 🛠️ Tecnologías

- **Angular 9.1.0**
- **Angular Material 9.2.4**
- **TypeScript 3.8.3**
- **SCSS** para estilos
- **RxJS** para programación reactiva

## 📦 Instalación

```bash
# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
ng serve

# Compilar para producción
ng build --prod
```

## 🏗️ Estructura del Proyecto

```
src/
├── app/
│   ├── components/
│   │   ├── login/           # Componente de login
│   │   └── dashboard/       # Componente de dashboard
│   ├── services/
│   │   └── auth.service.ts  # Servicio de autenticación
│   ├── guards/
│   │   └── auth.guard.ts    # Guards de autenticación
│   ├── app.module.ts        # Módulo principal
│   ├── app-routing.module.ts # Configuración de rutas
│   └── app.component.*      # Componente raíz
├── assets/
│   └── icons/               # Iconos personalizados
├── environments/            # Configuración de entornos
└── styles.scss             # Estilos globales
```

## 🔐 Autenticación

### Login con Email/Password
```typescript
this.authService.login(email, password).subscribe({
  next: (response) => {
    if (response.success) {
      this.router.navigate(['/dashboard']);
    }
  }
});
```

### Login con Google
```typescript
this.authService.loginWithGoogle().subscribe({
  next: (response) => {
    if (response.success) {
      this.router.navigate(['/dashboard']);
    }
  }
});
```

## 🛡️ Protección de Rutas

### AuthGuard
Protege rutas que requieren autenticación:
```typescript
{
  path: 'dashboard',
  component: DashboardComponent,
  canActivate: [AuthGuard]
}
```

### RoleGuard
Protege rutas basadas en roles:
```typescript
{
  path: 'admin',
  component: AdminComponent,
  canActivate: [RoleGuard],
  data: { roles: ['admin'] }
}
```

## 🎨 Componentes

### LoginComponent
- Formulario de login con validaciones
- Botón de login con Google
- Manejo de errores y loading states
- Diseño responsive y moderno

### DashboardComponent
- Panel principal con información del usuario
- Acciones rápidas basadas en roles
- Menú de usuario con opciones
- Diseño de tarjetas con Material Design

## 🔧 Configuración

### Variables de Entorno
```typescript
// environment.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  appName: 'Portal de Capacitaciones',
  companyName: 'Stefanini'
};
```

### Tema Personalizado
El proyecto incluye un tema personalizado de Angular Material con colores corporativos de Stefanini.

## 📱 Responsive Design

- **Mobile First**: Diseño optimizado para móviles
- **Breakpoints**: 768px, 480px
- **Flexible Grid**: Sistema de grid responsivo
- **Touch Friendly**: Botones y elementos táctiles

## 🚀 Despliegue

```bash
# Compilar para producción
ng build --prod

# Los archivos se generan en dist/portal-capacitaciones-frontend/
```

## 🔗 Integración con Backend

El frontend se conecta con el backend Spring Boot en:
- **Desarrollo**: `http://localhost:8080/api`
- **Producción**: `https://api.portal-capacitaciones.com/api`

### Endpoints Utilizados
- `POST /auth/basic/login` - Login con email/password
- `POST /auth/basic/register` - Registro de usuarios
- `GET /oauth2/authorization/google` - Login con Google

## 📋 Próximas Funcionalidades

- [ ] Módulo de cursos
- [ ] Sistema de insignias
- [ ] Notificaciones en tiempo real
- [ ] Panel de administración
- [ ] Perfil de usuario
- [ ] Reportes y métricas

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto es propiedad de Stefanini y está destinado para uso interno.