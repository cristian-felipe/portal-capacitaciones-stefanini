# 🔧 Troubleshooting - Portal de Capacitaciones

## 🚨 Problema: Siempre redirige al dashboard sin mostrar el login

### **Causa del Problema:**
El problema ocurre cuando hay datos de autenticación almacenados en el `localStorage` del navegador que hacen que la aplicación piense que el usuario ya está autenticado.

### **Soluciones:**

#### **1. Solución Rápida - Limpiar desde el Navegador:**
1. Abre las **Herramientas de Desarrollador** (F12)
2. Ve a la pestaña **Application** (Chrome) o **Storage** (Firefox)
3. En el panel izquierdo, expande **Local Storage**
4. Selecciona tu dominio (ej: `http://localhost:4200`)
5. Haz clic derecho y selecciona **Clear All** o elimina manualmente:
   - `auth_token`
   - `user_data`
   - `authState`
6. Recarga la página (F5)

#### **2. Solución desde la Consola del Navegador:**
1. Abre las **Herramientas de Desarrollador** (F12)
2. Ve a la pestaña **Console**
3. Ejecuta el siguiente comando:
```javascript
localStorage.clear();
sessionStorage.clear();
location.reload();
```

#### **3. Solución desde la Aplicación:**
1. Si logras acceder al dashboard:
   - Haz clic en el **menú de usuario** (ícono de perfil)
   - Selecciona **"Limpiar Todo"**
   - Esto te redirigirá al login

#### **4. Solución Manual - Modo Incógnito:**
- Abre una ventana de navegación privada/incógnito
- Navega a `http://localhost:4200`
- Esto evitará cualquier dato almacenado

### **Prevención del Problema:**

#### **Para Desarrollo:**
El código ahora incluye limpieza automática al cargar el login:
- Se limpia el localStorage al inicializar el componente de login
- Se incluye un método de simulación de login para desarrollo
- Cualquier email/contraseña funcionará para desarrollo

#### **Para Producción:**
- Implementar logout automático por tiempo de sesión
- Validar tokens en el servidor
- Limpiar datos al cerrar el navegador

### **Comandos Útiles para Debugging:**

```javascript
// Verificar datos de autenticación
console.log('Token:', localStorage.getItem('auth_token'));
console.log('User:', localStorage.getItem('user_data'));

// Limpiar todo
localStorage.clear();
sessionStorage.clear();

// Función global de limpieza (disponible cuando estás en login)
clearAuthData();
```

### **Verificación de la Solución:**
1. Abre las herramientas de desarrollador
2. Ve a **Application** > **Local Storage**
3. Verifica que no haya datos de autenticación
4. Recarga la página
5. Deberías ver la pantalla de login

### **Notas Importantes:**
- **Para desarrollo:** Usa cualquier email y contraseña (ej: `test@test.com` / `123456`)
- **El backend no necesita estar corriendo** para el login de desarrollo
- **Los datos se limpian automáticamente** al cargar el login
- **Si el problema persiste**, usa el modo incógnito del navegador

### **Estado Actual del Sistema:**
✅ **Login funcional** con datos simulados  
✅ **Limpieza automática** de datos de autenticación  
✅ **Navegación completa** entre páginas  
✅ **Dashboard** con enlaces a cursos y perfil  
✅ **Catálogo de cursos** con datos de prueba  
✅ **Perfil de usuario** con estadísticas  

### **Próximos Pasos:**
1. Conectar con el backend real cuando esté disponible
2. Implementar autenticación JWT real
3. Agregar validación de tokens
4. Implementar refresh tokens

---

**¿Necesitas ayuda?** Revisa la consola del navegador para mensajes de error o contacta al equipo de desarrollo.


