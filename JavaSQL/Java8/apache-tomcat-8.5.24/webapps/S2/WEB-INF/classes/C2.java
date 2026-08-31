	class C2 {
    static String printChecks (String[] vector){
        String out="";
        out += "<html>";
        out += "<body>";
		out+="<form action=http://www.nicolasserrano.com/CS/HTML/query.html method=Get>";
		out +="<input type=text name=rows value="+vector.length+">";
		out +="<input type=submit value=Enviar>";
		for (int i=1; i<vector.length; i++){
        }
     out +="</form>";
		
        out += "</body>";
        out += "</html>";
        return out;
    }
}