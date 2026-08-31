# Forzar la ruta al directorio donde está el script
Set-Location $PSScriptRoot

$7zipPath = "C:\Program Files\7-Zip\7z.exe"

# Lista para acumular los usuarios creados
$usuarios = @()

# Buscamos ambos tipos de archivo combinando los resultados
Get-ChildItem -Path . -File | Where-Object { $_.Extension -match '\.(zip|7z)' } | ForEach-Object {

    $fileName = $_.BaseName
    $parts = $fileName -split '_'

    # Buscamos la parte que contiene el '@' para extraer el usuario
    $emailPart = $parts | Where-Object { $_ -like "*@*" }
    if ($emailPart) {
        $usuario = ($emailPart -split '@')[0]

        Write-Host "Procesando usuario: $usuario" -ForegroundColor Cyan

        # 1. Extraer a carpeta temporal (usando el nombre de usuario)
        & $7zipPath x $_.FullName "-o$usuario" -y | Out-Null

        # 1b. Desbloquear todos los archivos extraidos (Windows los marca como
        #     "zona de internet" y Tomcat puede fallar al desplegarlos)
        Get-ChildItem -Path ".\$usuario" -Recurse -File | Unblock-File

        # 2. Mover el contenido de la subcarpeta interna al nivel del usuario
        $subCarpeta = Get-ChildItem -Path ".\$usuario" -Directory | Select-Object -First 1

        if ($subCarpeta) {
            Get-ChildItem -Path $subCarpeta.FullName | Move-Item -Destination ".\$usuario\" -Force
            Remove-Item $subCarpeta.FullName -Recurse -Force
        }

        # 3. Añadir el usuario a la lista
        $usuarios += $usuario
    }
}

# Generar el fichero index.html en la carpeta ROOT (donde está el script)
$fecha = Get-Date -Format "dd/MM/yyyy HH:mm:ss"

$linksHtml = $usuarios | Sort-Object | ForEach-Object {
    "        <li><a target=_blank href=`"http://localhost:8082/$_`">$_</a></li>"
}

$htmlContent = @"
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Indice de usuarios</title>
</head>
<body>
    <h1>Indice de usuarios</h1>
    <h2>$fecha</h2>
    <ul>
$($linksHtml -join "`n")
    </ul>
</body>
</html>
"@

$htmlContent | Out-File -FilePath ".\ROOT\index.html" -Encoding UTF8

Write-Host "Fichero index.html generado con $($usuarios.Count) usuario(s)." -ForegroundColor Yellow
Write-Host "--- Proceso finalizado ---" -ForegroundColor Green
Pause