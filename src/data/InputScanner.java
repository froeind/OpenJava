
package data;

import java.util.Scanner;

public class InputScanner {

    // von Hendriks Singleton-Version InputHelper übernommen
    private static InputScanner instance = null;
    private final Scanner scanner;

    private InputScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public static InputScanner getInstance() {
        if (instance == null) {
            instance = new InputScanner(new Scanner(System.in));
        }
        return instance;
    }

    public String[] importString(int anzahl, String text) {
        String[] eingabeS = new String[anzahl];
        for (int i = 0; i < anzahl; i++) {
            //System.out.println((anzahl==1?"":(i + 1) + "ten ") + text + " bitte:");
            System.out.println((anzahl==1?"":(i + 1) + "ten ") + text);
            eingabeS[i] = this.scanner.next();
        }
        return eingabeS;
    }

    public String[] importString(String[] texte) {
        int anzahl = texte.length;
        String[] eingabeS = new String[anzahl];
        for (int i = 0; i < anzahl; i++) {
            System.out.println(texte[i]);
            eingabeS[i] = this.scanner.next();
        }
        return eingabeS;
    }

    public int[] importInt(int anzahl, String text) {
        int[] eingabeI = new int[anzahl];
        for (int i = 0; i < anzahl; i++) {
            System.out.println((anzahl==1?"":(i + 1) + "te ") + text + " bitte:");
            eingabeI[i] = parseInt(this.scanner.next());
        }
        return eingabeI;
    }

    public static int parseInt(String input) {
        // Ingo Werner 20.03.2024
        // Suche nach Ganzzahlteil ohne RegEx
        // zusammen mit Gemini entwickelt, weil im Web nichts anständiges schnell gefunden wurde
        // ich ignoriere Tausendermarkierungen, weil es hier um händische Eingabe geht
            int index = 0;
            boolean notsigned = true;
            StringBuilder buffer = new StringBuilder();
            while (index < input.length()) {
                char ch = input.charAt(index);
                if (Character.isDigit(ch)) {
                    // das erste gefundene digit wird erst angehängt, wenn das Vorzeichen geklärt ist
                    // alle weiteren digits hänge ich einfach an, solange digits folgen
                    if (notsigned) {
                        if (index > 0) {
                            char sign = input.charAt(index - 1);
                            if (sign == '-') {
                                buffer.append(sign);
                            }
                        }
                        notsigned = false;
                    }
                    buffer.append(ch);
                    index++;
                } else if (!buffer.isEmpty()) {
                    break;
                } else {
                    index++;
                }
            }
            if (buffer.isEmpty()) {
                return 0;
            } else {
                return Integer.parseInt(buffer.toString());
            }
    }

    public static float parseFloat(String input) {
        // Ingo Werner 20.03.2024 noch nichts weiter gemacht
        // Suche nach Fließkommateil ohne RegEx
        // zusammen mit Gemini und aus parseInt entwickelt
        // ich ignoriere Tausendermarkierungen, weil es hier um händische Eingabe geht,
        // akzeptiere aber . oder , als Dezimalpunkt
        int index = 0;
        boolean notsigned = true;
        boolean hasnotE = true;
        boolean notEsigned = true;
        StringBuilder buffer = new StringBuilder();
        while (index < input.length()) {
            char ch = input.charAt(index);
            if (Character.isDigit(ch)) {
                // das erste gefundene digit wird erst angehängt, wenn das Vorzeichen geklärt ist
                // alle weiteren digits hänge ich einfach an, solange digits folgen
                // ein möglicher Dezimalpunkt muss dann noch berücksichtigt werden und der mögliche Exponent
                if (notsigned) {
                    if (index > 0) {
                        char sign = input.charAt(index - 1);
                        if (sign == '-') {
                            buffer.append(sign);
                        }
                    }
                    notsigned = false;
                }
                buffer.append(ch);
                index++;
            } else if (!buffer.isEmpty() && (ch == '.' || ch == ',')) {
                buffer.append('.');
            } else if (!buffer.isEmpty()) {
                break;
            } else {
                index++;
            }
        }
        if (buffer.isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(buffer.toString());
        }
    }

    public static int parseIntRegEx(String input) {
        return 123;
    }

    public static float parseFloatRegEx(String input) {
        return 12.3f;
    }

}

/*





public static class ParseResult {
    public int integerValue;
    public float floatValue;

    public ParseResult(int integerValue, float floatValue) {
        this.integerValue = integerValue;
        this.floatValue = floatValue;
    }
}

public static ParseResult parseString(String input) {
    int integerValue = 0;
    float floatValue = 0.0f;

    // Suche nach Ganzzahlteil
    int index = 0;
    while (index < input.length() && Character.isDigit(input.charAt(index))) {
        integerValue = integerValue * 10 + (input.charAt(index) - '0');
        index++;
    }

    // Suche nach Dezimalteil
    if (index < input.length() && input.charAt(index) == '.') {
        index++;
        int decimalPlaces = 0;
        while (index < input.length() && Character.isDigit(input.charAt(index))) {
            floatValue += (input.charAt(index) - '0') / Math.pow(10, decimalPlaces++);
            index++;
        }
    }

    return new ParseResult(integerValue, floatValue);
}

public static void main(String[] args) {
    String input = "dfdf126dafadf12.8daf";
    ParseResult result = parseString(input);

    System.out.println("Integer-Wert: " + result.integerValue);
    System.out.println("Float-Wert: " + result.floatValue);
}















Scanner scanner = new Scanner(System.in);

// Regex-Muster für Ganzzahl definieren
Pattern intPattern = Pattern.compile("[\\-]?[0-9]+");

// Regex-Muster für Gleitkommazahl definieren
Pattern floatPattern = Pattern.compile("[\\-]?[0-9]+(\\.[0-9]+)?");

// Eingabe lesen
System.out.println("Geben Sie eine Zeichenkette mit einer Ganzzahl und einer Gleitkommazahl ein:");
String input = scanner.nextLine();

// Scanner an den Anfang der Eingabe setzen
scanner = new Scanner(input);

// Position der Ganzzahl finden
int intStart = scanner.findInLine(intPattern);

// Position der Gleitkommazahl finden
int floatStart = scanner.findInLine(floatPattern);

// Wenn beide Muster gefunden wurden
if (intStart != -1 && floatStart != -1) {

// Ganzzahl extrahieren
String intString = input.substring(intStart, scanner.match().end());
int number = Integer.parseInt(intString);

// Gleitkommazahl extrahieren
String floatString = input.substring(floatStart, scanner.match().end());
float decimal = Float.parseFloat(floatString);

// Extrahierte Werte ausgeben
    System.out.println("Ganzzahl: " + number);
    System.out.println("Gleitkommazahl: " + decimal);

} else {
        System.out.println("Ungültige Eingabe!");
}
 */
