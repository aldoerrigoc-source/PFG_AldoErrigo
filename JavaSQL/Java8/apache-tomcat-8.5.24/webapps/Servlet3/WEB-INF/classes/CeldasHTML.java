public class CeldasHTML{
    public static String salidaHTML(String fila, String column){
     String out="";
     out +="<html>";
     out +="<body>";
     out+="<form action=CrearMatriz method=Get>";
     out +="<input TYPE=text name=filas value="+ fila+">";
     out +="<input TYPE=text name=columnas value="+ column+">";
     out +="</form>";
     out +="<TABLE BORDER=1>";
     for (int i=0; i < Integer.parseInt(fila); i++){
         out +="<TR>";
         for (int j=0; j<Integer.parseInt(column); j++){
             out += "<TD> <input TYPE=text name=celda"+i+"-"+j+"> </TD>";
         }
         out +="</TR>";
     }
     out +="</TABLE>";
     out +="<input type=submit value=Enviar>";
     out +="</body>";
     out +="</html>";
     return out;
    }
}