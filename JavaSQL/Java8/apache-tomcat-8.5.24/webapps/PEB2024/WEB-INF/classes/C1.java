   class C1{
     static String createSelects (String args){
     String out="";
     int num=Integer.parseInt(args);
     out +="<html>";
     out +="<body>";
     out+="<form action=S2 method=Get>";
     out +="<p>Selects:<input type=text name=selects value="+num+"></p>";
     for (int i=0; i <num; i++){
		 int aux=i+1;
         out +="<p><input type=text name=Select_"+i+" value="+aux+"></p>";
         }
     out +="<input type=submit>";
	 out+="</form>";
     out +="</body>";
     out +="</html>";
     return out;
     }
   }