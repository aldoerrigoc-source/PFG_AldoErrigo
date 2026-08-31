class C2 {
    static String salidaHTML(String[] palabras) {
        String out = "<html>";
        out += "<body>";
        out+="<head>Palabras</head>";
        out += "<form action=S3 method='Get'>";
        out+="<input type=text name=palabras value="+palabras.length+"></input>";
        out += "<table>";
       for (int i = 0; i < palabras.length; i++) {
            out += "<tr>";
            for (int j = 0; j <2 ; j++) {
                if(j==0){
                out += "<td><p>"+palabras[i]+"<input type=text name=palabra_"+i+" value="+palabras[i]+"></input></p></td>";
                }
                if(j==1){
                    int aux=palabras[i].length();
                    int num=aux*40;
                out+="<td><HR size=20px align=left color=blue width="+num+"px></td>";
                }
            }
            out += "</tr>";
        }
        out += "</table>";
		out +="<input type=submit>";
        out += "</form>";
        out += "</body>";
        out += "</html>";
        return out;
    }
}
