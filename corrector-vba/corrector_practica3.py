"""
Corrector de la Practica 3 (Objetos en VBA) - Items 1, 2 y 3

Requiere que el archivo .xlsm del estudiante tenga tres macros con estos
nombres exactos: Item1, Item2, Item3.

Requisitos:
  pip install pywin32
  Microsoft Excel instalado

Uso:
  python corrector_practica3.py ./entregas --salida resultados_vba.csv
"""

import argparse
import csv
import os
import subprocess
import time
from concurrent.futures import ThreadPoolExecutor, TimeoutError as FutureTimeoutError

import win32com.client
import pywintypes
import pythoncom

TIMEOUT_SEGUNDOS = 15


def matar_excel_forzado():
    subprocess.run(["taskkill", "/f", "/im", "excel.exe"],
                    stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)


# ---------------------------------------------------------------------------
# Setup y verificacion por item
# ---------------------------------------------------------------------------

def _limpiar_area_trabajo(hoja):
    """Borra un area generosa para evitar contaminacion entre items."""
    hoja.Range("A1:J20").ClearContents()


def _setup_item1(hoja):
    hoja.Range("E6").Formula = "=B2*2"
    hoja.Range("E6").Select()


def _verificar_item1(hoja):
    a1 = hoja.Range("A1").Value
    b1 = hoja.Range("B1").Value
    c1 = hoja.Range("C1").Formula
    if a1 != 6:
        return "FALLO", f"A1 esperado=6, obtenido={a1}"
    if b1 != 5:
        return "FALLO", f"B1 esperado=5, obtenido={b1}"
    if c1 != "=B2*2":
        return "FALLO", f"C1 esperado==B2*2, obtenido={c1}"
    return "OK", ""


def _setup_item2(hoja):
    hoja.Range("E2").Formula = "=1+1"
    hoja.Range("F2").Formula = "=3+3"
    hoja.Range("E3").Formula = "=2+2"
    hoja.Range("F3").Formula = "=4+4"
    hoja.Range("E2:F3").Select()


def _verificar_item2(hoja):
    esperado = {
        (2, 5, "=1+1"),
        (2, 6, "=3+3"),
        (3, 5, "=2+2"),
        (3, 6, "=4+4"),
    }
    obtenido = set()
    for fila in range(1, 6):  # margen generoso, se esperan 4 filas
        a = hoja.Range(f"A{fila}").Value
        b = hoja.Range(f"B{fila}").Value
        c = hoja.Range(f"C{fila}").Formula
        if a is None and b is None:
            continue
        obtenido.add((a, b, c))

    if obtenido != esperado:
        faltan = esperado - obtenido
        sobran = obtenido - esperado
        return "FALLO", f"faltan={faltan} sobran={sobran}"
    return "OK", ""


def _setup_item3(hoja):
    hoja.Range("A1").Value = 8
    hoja.Range("B1").Value = 3
    hoja.Range("C1").Formula = "=SUM(A1:A5)"


def _verificar_item3(hoja):
    destino = hoja.Range("C8").Formula  # fila=8 -> C, columna=3 -> C  =>  C8
    esperado = "=SUM(A1:A5)"
    if destino != esperado:
        return "FALLO", f"C8 esperado={esperado}, obtenido={destino}"
    return "OK", ""


ITEMS = {
    1: {"macro": "Item1", "setup": _setup_item1, "verificar": _verificar_item1},
    2: {"macro": "Item2", "setup": _setup_item2, "verificar": _verificar_item2},
    3: {"macro": "Item3", "setup": _setup_item3, "verificar": _verificar_item3},
}


# ---------------------------------------------------------------------------
# Ejecucion contra Excel real
# ---------------------------------------------------------------------------

def _corregir_todos_los_items(ruta_xlsm):
    """
    Abre el archivo una sola vez y corre los 3 items en orden.
    Devuelve {num_item: (veredicto, detalle)}.
    """
    pythoncom.CoInitialize()
    resultados = {}
    try:
        excel = win32com.client.DispatchEx("Excel.Application")
        excel.Visible = False
        excel.DisplayAlerts = False
        excel.AutomationSecurity = 1

        libro = excel.Workbooks.Open(os.path.abspath(ruta_xlsm))
        hoja = libro.Worksheets(1)
        hoja.Activate()

        for num_item, cfg in ITEMS.items():
            try:
                _limpiar_area_trabajo(hoja)
                cfg["setup"](hoja)
                excel.Run(cfg["macro"])
                veredicto, detalle = cfg["verificar"](hoja)
                resultados[num_item] = (veredicto, detalle)
            except pywintypes.com_error as e:
                resultados[num_item] = ("ERROR", f"error COM/macro: {e}")
            except Exception as e:
                resultados[num_item] = ("ERROR", f"error inesperado: {e}")

        libro.Close(SaveChanges=False)
        excel.Quit()
        return resultados

    finally:
        pythoncom.CoUninitialize()


def corregir_archivo(ruta_xlsm):
    with ThreadPoolExecutor(max_workers=1) as executor:
        future = executor.submit(_corregir_todos_los_items, ruta_xlsm)
        try:
            return future.result(timeout=TIMEOUT_SEGUNDOS * len(ITEMS))
        except FutureTimeoutError:
            matar_excel_forzado()
            return {n: ("TIMEOUT", "no termino a tiempo (posible bucle infinito)") for n in ITEMS}


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

def main():
    parser = argparse.ArgumentParser(description="Corrector Practica 3 VBA (items 1-3)")
    parser.add_argument("carpeta", help="Carpeta con los .xlsm de los estudiantes")
    parser.add_argument("--salida", default="resultados_vba.csv")
    args = parser.parse_args()

    archivos = sorted(f for f in os.listdir(args.carpeta) if f.lower().endswith(".xlsm"))
    if not archivos:
        print(f"No se encontraron .xlsm en {args.carpeta}")
        return

    with open(args.salida, "w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f, delimiter=";")
        writer.writerow(["alumno", "item", "resultado", "detalle"])

        for archivo in archivos:
            alumno = os.path.splitext(archivo)[0]
            ruta = os.path.join(args.carpeta, archivo)
            print(f"Corrigiendo {alumno}...")

            resultados = corregir_archivo(ruta)
            for num_item in sorted(resultados):
                veredicto, detalle = resultados[num_item]
                writer.writerow([alumno, num_item, veredicto, detalle])
                print(f"  item {num_item}: {veredicto} {detalle}")

            time.sleep(1)

    print(f"\nListo. Resultados en {args.salida}")


if __name__ == "__main__":
    main()