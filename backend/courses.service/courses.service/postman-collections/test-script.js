// Script de prueba automatizada para el servicio de cursos
// Este script puede ejecutarse en Postman o como script independiente

const baseUrl = 'http://localhost:8081/courses-service';

// Función para hacer peticiones HTTP
async function makeRequest(method, url, body = null) {
    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        }
    };
    
    if (body) {
        options.body = JSON.stringify(body);
    }
    
    try {
        const response = await fetch(url, options);
        const data = await response.json();
        return { status: response.status, data: data };
    } catch (error) {
        return { status: 500, error: error.message };
    }
}

// Función principal de pruebas
async function runTests() {
    console.log('🚀 Iniciando pruebas del servicio de cursos...\n');
    
    let testResults = {
        passed: 0,
        failed: 0,
        total: 0
    };
    
    // Test 1: Inicializar datos de prueba
    console.log('📊 Test 1: Inicializar datos de prueba');
    const initResult = await makeRequest('POST', `${baseUrl}/api/datos-prueba/inicializar`);
    testResults.total++;
    if (initResult.status === 200) {
        console.log('✅ Datos de prueba inicializados correctamente');
        testResults.passed++;
    } else {
        console.log('❌ Error al inicializar datos de prueba:', initResult.error);
        testResults.failed++;
    }
    
    // Test 2: Listar programas
    console.log('\n📚 Test 2: Listar todos los programas');
    const programasResult = await makeRequest('GET', `${baseUrl}/api/programas`);
    testResults.total++;
    if (programasResult.status === 200 && programasResult.data.length > 0) {
        console.log(`✅ Se encontraron ${programasResult.data.length} programas`);
        testResults.passed++;
    } else {
        console.log('❌ Error al listar programas:', programasResult.error);
        testResults.failed++;
    }
    
    // Test 3: Obtener programa por ID
    console.log('\n🔍 Test 3: Obtener programa por ID');
    const programaResult = await makeRequest('GET', `${baseUrl}/api/programas/1`);
    testResults.total++;
    if (programaResult.status === 200 && programaResult.data.id === 1) {
        console.log(`✅ Programa obtenido: ${programaResult.data.titulo}`);
        testResults.passed++;
    } else {
        console.log('❌ Error al obtener programa:', programaResult.error);
        testResults.failed++;
    }
    
    // Test 4: Listar unidades de un programa
    console.log('\n📖 Test 4: Listar unidades de un programa');
    const unidadesResult = await makeRequest('GET', `${baseUrl}/api/unidades/programa/1`);
    testResults.total++;
    if (unidadesResult.status === 200 && unidadesResult.data.length > 0) {
        console.log(`✅ Se encontraron ${unidadesResult.data.length} unidades`);
        testResults.passed++;
    } else {
        console.log('❌ Error al listar unidades:', unidadesResult.error);
        testResults.failed++;
    }
    
    // Test 5: Listar lecciones de una unidad
    console.log('\n📝 Test 5: Listar lecciones de una unidad');
    const leccionesResult = await makeRequest('GET', `${baseUrl}/api/lecciones/unidad/1`);
    testResults.total++;
    if (leccionesResult.status === 200 && leccionesResult.data.length > 0) {
        console.log(`✅ Se encontraron ${leccionesResult.data.length} lecciones`);
        testResults.passed++;
    } else {
        console.log('❌ Error al listar lecciones:', leccionesResult.error);
        testResults.failed++;
    }
    
    // Test 6: Crear nuevo programa
    console.log('\n➕ Test 6: Crear nuevo programa');
    const nuevoPrograma = {
        titulo: 'Programa de Prueba Automatizada',
        descripcion: 'Programa creado por script de prueba',
        areaConocimiento: 'Testing'
    };
    const crearProgramaResult = await makeRequest('POST', `${baseUrl}/api/programas`, nuevoPrograma);
    testResults.total++;
    if (crearProgramaResult.status === 201 && crearProgramaResult.data.titulo === nuevoPrograma.titulo) {
        console.log('✅ Programa creado correctamente');
        testResults.passed++;
        
        // Test 7: Actualizar programa
        console.log('\n✏️ Test 7: Actualizar programa');
        const programaActualizado = {
            titulo: 'Programa Actualizado',
            descripcion: 'Descripción actualizada',
            areaConocimiento: 'Testing'
        };
        const actualizarResult = await makeRequest('PUT', `${baseUrl}/api/programas/${crearProgramaResult.data.id}`, programaActualizado);
        testResults.total++;
        if (actualizarResult.status === 200 && actualizarResult.data.titulo === programaActualizado.titulo) {
            console.log('✅ Programa actualizado correctamente');
            testResults.passed++;
        } else {
            console.log('❌ Error al actualizar programa:', actualizarResult.error);
            testResults.failed++;
        }
        
        // Test 8: Eliminar programa
        console.log('\n🗑️ Test 8: Eliminar programa');
        const eliminarResult = await makeRequest('DELETE', `${baseUrl}/api/programas/${crearProgramaResult.data.id}`);
        testResults.total++;
        if (eliminarResult.status === 204) {
            console.log('✅ Programa eliminado correctamente');
            testResults.passed++;
        } else {
            console.log('❌ Error al eliminar programa:', eliminarResult.error);
            testResults.failed++;
        }
    } else {
        console.log('❌ Error al crear programa:', crearProgramaResult.error);
        testResults.failed++;
    }
    
    // Test 9: Buscar programas por área
    console.log('\n🔍 Test 9: Buscar programas por área');
    const buscarAreaResult = await makeRequest('GET', `${baseUrl}/api/programas/buscar/area?area=Fullstack`);
    testResults.total++;
    if (buscarAreaResult.status === 200) {
        console.log(`✅ Búsqueda por área completada: ${buscarAreaResult.data.length} resultados`);
        testResults.passed++;
    } else {
        console.log('❌ Error en búsqueda por área:', buscarAreaResult.error);
        testResults.failed++;
    }
    
    // Test 10: Estadísticas
    console.log('\n📊 Test 10: Obtener estadísticas');
    const estadisticasResult = await makeRequest('GET', `${baseUrl}/api/programas/estadisticas/areas`);
    testResults.total++;
    if (estadisticasResult.status === 200) {
        console.log('✅ Estadísticas obtenidas correctamente');
        testResults.passed++;
    } else {
        console.log('❌ Error al obtener estadísticas:', estadisticasResult.error);
        testResults.failed++;
    }
    
    // Resumen de resultados
    console.log('\n' + '='.repeat(50));
    console.log('📋 RESUMEN DE PRUEBAS');
    console.log('='.repeat(50));
    console.log(`✅ Pruebas exitosas: ${testResults.passed}`);
    console.log(`❌ Pruebas fallidas: ${testResults.failed}`);
    console.log(`📊 Total de pruebas: ${testResults.total}`);
    console.log(`🎯 Porcentaje de éxito: ${((testResults.passed / testResults.total) * 100).toFixed(1)}%`);
    
    if (testResults.failed === 0) {
        console.log('\n🎉 ¡Todas las pruebas pasaron exitosamente!');
    } else {
        console.log('\n⚠️ Algunas pruebas fallaron. Revisa los errores arriba.');
    }
    
    return testResults;
}

// Ejecutar pruebas si se ejecuta directamente
if (typeof window === 'undefined') {
    // Node.js environment
    const fetch = require('node-fetch');
    runTests().catch(console.error);
} else {
    // Browser environment
    window.runTests = runTests;
}

// Para Postman, exportar la función
if (typeof pm !== 'undefined') {
    pm.globals.set('runTests', runTests.toString());
}


