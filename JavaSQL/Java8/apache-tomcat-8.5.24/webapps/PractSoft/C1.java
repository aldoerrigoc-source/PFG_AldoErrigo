class C1 {
    static String form(String[] values) {
        String out = "";
        out += "<html><form>";
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                out += "<input type='radio' name='op' value='" + values[i] + "'> " + values[i] + " <br>";
            }
        }
        out += "</form></html>";
        return out;
    }
}