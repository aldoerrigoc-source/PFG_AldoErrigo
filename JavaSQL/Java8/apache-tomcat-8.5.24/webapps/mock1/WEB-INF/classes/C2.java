class C2 {
    static String salidaHTML(String args) {
        int num = Integer.parseInt(args);
        String out = "<html>";
        out += "<head><title>S2 valoraciones</title></head>";
        out += "<body>";
        out += "<form action='http://nicolasserrano.github.io/CS/HTML/query.html' method='Get'>";
        out += "<table>";
        for (int i = 1; i <= num; i++) {
            out += "<tr>";
            out += "<td>" ;
            for (int j = 1; j <= 5; j++) {
                out += "<input type='radio' name='v_" + i + "' value='" + j + "'>";
            }
            out += "</td>";
            out += "</tr>";
        }
        out += "</table>";
        out += "</form>";
        out += "</body>";
        out += "</html>";
        return out;
    }
}
