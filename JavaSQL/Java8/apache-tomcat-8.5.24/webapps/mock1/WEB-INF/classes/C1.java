   class C1{
     static String salidaHTML (String args){
     String out="";
     int valoraciones=Integer.parseInt(args);
     out +="<html>";
     out +="<body>";
     out+="<form action=S2 method=Get>";
     out +="<select name=valoraciones size="+valoraciones+" multiple>";
     for (int i=1; i <=valoraciones; i++){
         out +="<option VALUE="+i+">"+i+" valoraciones </option>";
         }
     out+="</form>";
     out +="<input type=submit>";
     out +="</body>";
     out +="</html>";
     return out;
     }
   }