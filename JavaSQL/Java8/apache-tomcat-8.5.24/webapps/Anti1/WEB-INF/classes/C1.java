   class C1{
     static String salidaHTML (String x, String y){
     String out="";
     int inty=Integer.parseInt(y);
     int intx=Integer.parseInt(x);
     out +="<html>";
     out +="<body>";
     out+="<form action=S2 method=Get>";
     out+="<textarea name=texto rows="+intx+" cols="+inty+">Texto de prueba de la caja de texto</textarea>";
     out +="<input type=submit>";
	 out+="</form>";
     out +="</body>";
     out +="</html>";
     return out;
     }
   }