# Corrector Item 1 - VBA (Practica 3, Objetos en VBA)

## Que hace

Abre el archivo .xlsm del estudiante con Excel (automatizado, sin ventanas
visibles), pone una formula conocida en la celda E6, la selecciona como celda
activa, ejecuta la macro llamada "Item1" que debio escribir el estudiante, y
lee lo que quedo en A1, B1 y C1 para compararlo con lo esperado.

## Antes de ejecutar

```
pip install pywin32
```

Necesitas Microsoft Excel instalado en la maquina donde corres esto (se
automatiza el programa real, no hay forma de evitarlo para VBA).

## Formato de entrega del estudiante

Un archivo `.xlsm` (Excel con macros habilitadas) que contenga, en el
Editor de VBA (Alt+F11), un modulo con una macro llamada exactamente:

```vb
Sub Item1()
    ActiveCell.Offset(1 - ActiveCell.Row, 1 - ActiveCell.Column).Value = ActiveCell.Row
    ' etc. Esto lo escribe el estudiante, no nosotros.
    Range("A1").Value = ActiveCell.Row
    Range("B1").Value = ActiveCell.Column
    Range("C1").Value = ActiveCell.Formula
End Sub
```

(El contenido de arriba es solo un ejemplo de referencia de como se vería
una solución correcta, no se le da al estudiante.)

## Estructura de carpetas

```
corrector-vba/
├── corrector_item1.py
├── README.md
└── entregas/
    └── alu001.xlsm   <- uno por estudiante
```

## Ejecutar

```
python corrector_item1.py ./entregas --salida resultados_vba.csv
```

## Advertencias importantes 

- **La primera vez que se ejecuta, Windows/Office puede pedir permisos**
  de seguridad o mostrar un aviso de "vista protegida" si el archivo viene
  de una carpeta no confiable (por ejemplo, si vino de un email o de
  descarga de internet marcado con la marca de "bloqueado"). 
- **AutomationSecurity = 1 evita el dialogo de "Habilitar macros"**, pero
  no evita otros avisos de seguridad de Windows sobre el origen del archivo.

