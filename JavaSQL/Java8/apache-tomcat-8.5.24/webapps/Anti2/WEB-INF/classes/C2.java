class C2 {
    static String printSimulaciones(String[] values) {
        String out = "<html>";
        out += "<body>";
        out += "<form action=S3 method='Get'>";
        out += "<table>";
       for (int i = 0; i < values.length; i++) {
		   int x=Integer.parseInt(values[i]);
            out += "<tr>";
            for (int j = 0; j <3 ; j++) {
                if(j==0){
                out += "<td>Simulations: "+i+"</td>";
                }
                if(j==1){
                out+="<td><input type=text NAME=S_"+i+" value="+values[i]+"></td>";
                }
                if(j==2){
                    out+="<td>";
					for(int k=0;k<x;k++){
					out+="<img src=https://www.nicolasserrano.com/CS/webapps/auxiliary/images/euro.png>";
					}
					out+="</td>";
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
