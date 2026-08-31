   class C1{
     static String formMachines(String x){
     String out="";
     int num=Integer.parseInt(x);
     out +="<html>";
     out +="<body>";
     out+="<form action=S2 method=Get>";
     for (int i=0; i <num; i++){
         out +="<p>Machines: <input type=text name=machine_ value=1></p>";
         }
	 out+="<input type=text name=machines value="+num+">" ;
     out +="<input type=submit>";
	 out+="</form>";
     out +="</body>";
     out +="</html>";
     return out;
     }
   }