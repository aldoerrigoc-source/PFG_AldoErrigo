public class FormHTML {
    public static String salidaHTML(int opciones){
        String out="";
        out += "<html>";
        out += "<body>";
        out+="<form action=MostrarDatos method=Get>";
        out +="<input TYPE=text name=user>";
        for (int i=1; i<=opciones; i++){
        out +="<input TYPE=radio NAME=calif VALUE=" + i + ">Valor "+ i + "<br>";
        //out +="<input TYPE=radio NAME=calif VALUE=2>Valor 2 <br>";
        //out +="<input TYPE=radio NAME=calif VALUE=3>Valor 3 <br>";
        }
        out +="<input TYPE=SUBMIT VALUE=Enviar>";
        out +="</form>";
        out += "</body>";
        out += "</html>";
        return out;
    }
}