   class C1{
     static String createForm (String x, String y){
     String out="";
     int num=Integer.parseInt(y);
     int res=Integer.parseInt(x);
     out +="<html>";
     out +="<body>";
     out+="<form action=S2 method=Get>";
     out +="<p>Selects:<input type=text name=simulaciones value="+y+"></p>";
     for (int i=0; i <num; i++){
		 int aux=i+1;
         out +="<p><input type=text name=S"+i+" value="+S1.resultado(res)+">Simulation "+i+":</p>";
         }
     out +="<input type=submit>";
	 out+="</form>";
     out +="</body>";
     out +="</html>";
     return out;
     }
   }