class C2 {
    static String salidaHTML(String dimensiones,String escala) {
        int dim = Integer.parseInt(dimensiones);
        int esc = Integer.parseInt(escala);
        String out = "<html>";
        out += "<head><title>S2 Encuestas</title></head>";
        out += "<body>";
        out += "<form action=S3 method='Get'>";
        out += "<table>";
        for (int i = 1; i <= dim; i++) {
            out += "<tr>";
            for (int j = 1; j <=esc+1 ; j++) {
                int count=j-1;
                if(j==1){
                out += "<td>Dim "+i+"</td>";
                }
                else{
                    out+="<td><input TYPE=radio NAME=v_"+i+" VALUE="+count+"></td>";
                }
            }
            out += "</tr>";
        }
        out += "</table>";
        out +="<input TYPE=SUBMIT VALUE=Enviar>";
        out += "</form>";
        out += "</body>";
        out += "</html>";
        return out;
    }
}
