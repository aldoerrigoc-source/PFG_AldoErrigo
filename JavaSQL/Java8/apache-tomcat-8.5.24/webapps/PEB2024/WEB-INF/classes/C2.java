class C2 {
    static String printSelects(String[] values) {
        String out = "<html>";
        out += "<body>";
        out += "<form action='http://nicolasserrano.github.io/CS/HTML/query.html' method='Get'>";
        out += "<table>";
       for (int i = 0; i < values.length; i++) {
		   int x=Integer.parseInt(values[i]);
            out += "<tr>";
            for (int j = 0; j <2 ; j++) {
                int count=j-1;
                if(j==0){
                out += "<td>Selects "+i+"</td>";
                }
                else{
                    out+="<td><select NAME=S_"+i+" size="+x+" MULTIPLE>";
					for(int k=0;k<x;k++){
					out+="<option VALUE="+k+">Option "+k+"</option>";
					}
					out+="</select></td>";
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
