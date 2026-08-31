class C2 {
    static String printMachines(String[] values) {
        String out = "<html>";
        out += "<body>";
        out += "<form action=S3 method='Get'>";
        out += "<table>";
       for (int i = 0; i < values.length; i++) {
		   int aux=1+i;
            out += "<tr>";
            for (int j = 0; j <4 ; j++) {
                if(j==0){
                out += "<td>Machines "+values[i]+"</td>";
                }
                if(j==1){
                out+="<td><img src=http://nicolasserrano.github.io/CS/webapps/auxiliary/letters/"+values[i]+".png></td>";
                }
                if(j==2){
                    out+="<td><input type=text name="+values[i]+"ix value=1></td>";
                }
            }
            out += "</tr>";
        }
        out += "</table>";
		out += "<input type=text name=simulaciones value="+values.length+">";
		out +="<input type=submit>";
        out += "</form>";
        out += "</body>";
        out += "</html>";
        return out;
    }
}
