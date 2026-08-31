public class HR{
    public static String salidaHR(int casillas){
     String out="";
     out +="<html>";
     out +="<body>";
     for (int i=1; i <= casillas; i++){
         out +="<HR SIZE=" + i + ">";
     }
     out +="</body>";
     out +="</html>";
     return out;
    }
}