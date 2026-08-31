public class CeldasParametrosHTML {
    public static String salidaHTML (String fila,String diag) {
     String out = "";
     out +="<html>";
     out +="<body>";
     out+="<form action=CrearMatriz method=Get>";
     out +="<input TYPE=text name=filas value="+ fila+">";
     out +="<input TYPE=text name=columnas value="+ fila+">";
     out +="</form>";
     out +="<TABLE BORDER=1>";
     for (int i=0; i < Integer.parseInt(fila); i++){
         out +="<TR>";
         for (int j=0; j<Integer.parseInt(fila); j++){
             if(i==j){
                 out += "<TD> <input TYPE=text name=celda"+i+"-"+j+" value ="+diag+"> </TD>";
             }
             else{
                 out += "<TD> <input TYPE=text name=celda"+i+"-"+j+" value =0> </TD>";
             }
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