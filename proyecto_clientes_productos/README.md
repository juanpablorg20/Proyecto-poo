# Sistema de Gestión de Pedidos y Entregas — Módulo Clientes y Productos

Parte del proyecto final de Programación Orientada a Objetos (UCAB).
Incluye: `service/` (ClienteService, ProductoService), `Main`, repositorios
en memoria, excepciones propias y pruebas funcionales.

## Requisitos
- JDK 17 o superior (`javac -version`).

## Estructura
```
src/edu/ucab/app/
├── model/        Identificable, Validaciones, Cliente, Producto
├── repository/   IRepository<T>, InMemoryRepository<T>
├── service/      ServicioBase<T>, ClienteService, ProductoService
├── exception/    ReglaNegocioException (base), IdDuplicadoException,
│                 EntidadNoEncontradaException
└── console/      Main, Consola, MenuClientes, MenuProductos, Lector
test/edu/ucab/app/PruebasFuncionales.java
```

## Manejo de errores
- Dato inválido (id o nombre vacío, precio <= 0): `IllegalArgumentException`.
- Id repetido: `IdDuplicadoException`. Id inexistente: `EntidadNoEncontradaException`.
  Ambas heredan de `ReglaNegocioException` (verificada).

## Compilar y ejecutar (Linux / macOS)
```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out edu.ucab.app.console.Main
```

## Compilar y ejecutar (Windows PowerShell)
```powershell
mkdir out -Force
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out edu.ucab.app.console.Main
```

## Pruebas funcionales
```bash
javac -encoding UTF-8 -d out $(find src test -name "*.java")
java -cp out edu.ucab.app.PruebasFuncionales
```
Debe terminar con `0 falladas`. Si ves símbolos raros en los acentos en
Windows, ejecuta antes `chcp 65001`.
