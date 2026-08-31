   class C1{
     static String salidaHTML (String args){
     String out="";
     int valoraciones=Integer.parseInt(args);
     out +="<html>";
     out +="<body>";
     for (int i=1; i <=valoraciones; i++){
         out+="<P>";
         if(i<=3){
             out +="<a href=S2?dimensiones="+i+"&escala=5>Crear encuesta "+i+"</a>";
            }
         else{
             out +="<a href=S2?dimensiones="+i+"&escala=7>Crear encuesta "+i+"</a>";
         }
         out+="</P>";
     }
     out +="</body>";
     out +="</html>";
     return out;
     }
   }