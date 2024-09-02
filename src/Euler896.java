import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
//import java.util.stream.Collectors;

public class Euler896 {

    // 02.09.2024 Ingo Werner

    static boolean showShow_ = true;
    static int dummy_ = 0;

    public static void show(ArrayList<Long>[] teiler) {
        for (int i = 0; i < teiler.length; ++i) {
            System.out.println((i + 1) + ":" + teiler[i]);
        }
        System.out.println();
    }

    public static void show(ArrayList<Long>[] teiler, ArrayList<Integer> multipleMissing) {
        for (int i = 0; i < teiler.length; ++i) {
            if (multipleMissing.contains((Integer) (i + 1))) {
                System.out.println((i + 1) + ":" + teiler[i]);
            }
        }
        System.out.println("Break für " + multipleMissing);
        System.out.println();
    }

    public static void show(ArrayList<Long>[] teiler, ArrayList<Integer> iteratePerm, ArrayList<Integer> sizesPerm) {
        for (int i = 0; i < teiler.length; ++i) {
            if ( ! teiler[i].isEmpty()) {
                System.out.println((i + 1) + ":" + teiler[i]);
            }
        }
        List<Integer> filtered = iteratePerm.stream()
                .filter(num -> num != 0)
                .toList();
        System.out.println("Iterate " + filtered);
        filtered = sizesPerm.stream()
                .filter(num -> num != 0)
                .toList();
        System.out.println("To      " + filtered);
        System.out.println();
    }

    public static void refreshTeiler(ArrayList<Long>[] teiler, long endstart, boolean addNew) {
        if (addNew) {
            for (int i = 0; i < teiler.length; ++i) {
                if (endstart % (i + 1) == 0) {
                    teiler[i].add(endstart);
                }
            }
        } else {
            for (int i = 0; i < teiler.length; ++i) {
                int index = teiler[i].indexOf(endstart);
                if (index != -1) {
                    teiler[i].remove(index);
                }
            }
        }
    }

    public static void removeMultiple(ArrayList<Long>[] teiler, long multiple) {
        for (int i = 0; i < teiler.length; ++i) {
            int index = teiler[i].indexOf(multiple);
            if (index != -1) {
                teiler[i].remove(index);
            }
        }
    }

    public static void blockMultiple(ArrayList<Long>[] teiler, long multiple) {
        for (int i = 0; i <teiler.length; ++i) {
            int index = teiler[i].indexOf(multiple);
            if (index != -1) {
                teiler[i].set(index, -multiple);
            }
        }
    }

    public static void makePositiveAgain(ArrayList<Long>[] teiler) {
        // die Negierungen wieder aufheben
        for (int i = 0; i < teiler.length; ++i) {
            teiler[i].replaceAll(Math::abs);
        }

    }

    public static boolean checkAufDivisiblePerm(ArrayList<Long>[] teilerMultiples, ArrayList<Integer> sizesPerm, ArrayList<Integer> iteratePerm, long allPerm, int lastPerm) {
        long countPerm = 0;
        boolean isDivisible = false;
        while (true) {
            ++countPerm;
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCheck Permutationen " + allPerm + " " + countPerm);
            isDivisible = true;
            for (int i = 1; i < teilerMultiples.length; ++i) {
                if (teilerMultiples[i].size() > 1) {
                    // gäbe es nur einen Wert, dann hätte ich ihn bei den Solitärbetrachtungen schon bearbeitet
                    long multiple = teilerMultiples[i].get(iteratePerm.get(i) - 1);
                    if (multiple > 0) {
                        // diese Kombination geht, also alle anderen Auftreten negativ setzen
                        blockMultiple(teilerMultiples, multiple);
                    } else {
                        // diese Kombination geht nicht
                        // also kann ich einen break machen, die Negierungen wieder aufheben, danach die Abbruchbedingung durch Inkrementierung berechnen und überprüfen
                        isDivisible = false;
                        break;
                    }
                }
            }
            if (isDivisible) {
                // eine Lösung gefunden
                System.out.println("Lösende Permutation: ");
                show(teilerMultiples, iteratePerm, sizesPerm);
                return true;
            }
            // die Negierungen wieder aufheben
            makePositiveAgain(teilerMultiples);
            // Inkrementierung berechnen und überprüfen
            for (int i = 1; i < iteratePerm.size(); ++i) {
                int inc = iteratePerm.get(i);
                if (inc > 0) {
                    // nur gültiges Fach nehmen
                    ++inc;
                    if (inc <= sizesPerm.get(i)) {
                        // es kann noch hochgezählt werden
                        iteratePerm.set(i, inc);
                        // und die Schleife ist beendet
                        break;
                    } else {
                        // weiter geht es nicht, also wieder zurücksetzen und nächstes Fach hochzählen (im nächsten Schleifendurchlauf), wenn es eines gibt
                        iteratePerm.set(i, 1);
                        if (i == lastPerm) {
                            // letztes Fach erreicht, Ende der Fahnenstange
                            // ja, letztes Fach, aber nicht das letzte zu betrachtende
                            return false;
                        }
                    }
                }
            }
            if (false) {
                System.out.println("Permutationen: ");
                show(teilerMultiples, iteratePerm, sizesPerm);
            }
            System.out.println("Nur Permutationen: ");
            show(teilerMultiples, iteratePerm, sizesPerm);
        }
    }

    public static boolean checkAufDivisible(ArrayList<Long>[] teiler) {
        int dim = teiler.length;
        // zuerst mache ich meine Analysekopie der Liste
        // dann durchlaufe ich alle Fächer und überprüfe die Vielfachen der Teiler von 36 bis 2
        // und zwar solange durchlaufe ich die Schleife, bis ich nichts mehr eindeutig bestimmen kann ...
        //   ich überprüfe natürlich nur die Fächer, für das Vielfache noch nicht gefunden bzw. eindeutig bestimmt wurde
        //   wenn es keine Vielfachen gibt, also die ArrayList leer ist, dann ist die ganze 'range' nicht 'divisible'
        //   wenn es genau ein Vielfaches gibt, dann wird dieses genommen, und in allen Fächern gelöscht, für dieses Fach wird dann die Vielfach-Suche natürlich deaktiviert
        //     wenn dadurch ein anderes Fach geleert wird, so wird dies später registriert und die gesamte Analyse natürlich abgebrochen
        //   wenn es jetzt 2, 3, ... n Vielfache gibt, dann werden diese alle, also 2, 3, ... n Zahlen auf einmal mit Fächern der gleichen Größe Zahl für Zahl verglichen
        //     das Vergleichen geht 1:1 über den Index, weil alle Vielfachen aufsteigend in die Fächer gelegt werden
        //     wenn ich nun bei 2 Vielfachen ein zweites Fach der gleichen Länge mit den gleichen Vielfachen finde
        //     dann müssen diese natürlich für diese beiden Fächer genommen werden
        //     beide Zahlen werden nun auch in allen Fächern gelöscht und für diese beiden Fächer wird dann die Vielfach-Suche natürlich deaktiviert
        //     wenn dadurch noch ein drittes Fach komplett geleert wird, so wird dies später registriert und die gesamte Analyse natürlich auch abgebrochen
        //     und analog geht das dann mit Fächern der Länge 3, 4, ... n
        //       ich suche zu 3 Vielfachen 3 Fächer der Länge 3, wo die Vielfache gleich sind usw.
        //    beim Löschen der Vielfachen in allen Fächern werden dann natürlich neue Fächer kürzerer Länge erzeugt und diese müssen natürlich alle wieder überprüft werden
        //    und das geht solange bis es nichts mehr zu ermitteln gibt ...
        //  für das 1er-Fach muss natürlich außer dem Löschen nichts gemacht werden, wenn alles aufgeht, dann steht da zum Schluß das letzte Viefache mit dem letzten Teiler 1
        //  alle neuen Primzahlen stehen ja nur im 1er-Fach, d.h. es wird dann eine Primzahl dort stehen bis zum Ende der Analyse, wenn im 'range' eine neue Primzahl ist
        //  (die alten Primzahlen bis 31 stehen ja auch in "ihren" Primzahl-Fächern bis sie durchs 'range'-Wandern verschwinden
        //  zwei oder mehr neue Primzahlen dürfen nicht in einem 'range' sein, deswegen kläre ich das auch in main, weil diese nur im 1er-Fach stehen würden
        //  und dann kann das 'range' garantiert nicht 'divisible' sein
        // ... und wenn dann noch was übrig bleibt, noch Vielfache unbestimmt sind, dann muss ich eine mögliche Lösung über Permutationen ermitteln
        
        // Arbeitskopie erstellen, wo ich beliebig löschen kann, aber nur wenn das Löschen eindeutig erlaubt ist
        ArrayList<Long>[] teilerMultiples = new ArrayList[dim];
        for (int i = 0; i < dim; ++i) {
            teilerMultiples[i] = new ArrayList<>(teiler[i]);
        }
        // ich muss mir natürlich merken, was ich schon gefunden habe, welches Vielfaches unausweichlich zu einem Fach gehört
        ArrayList<Integer> multipleMissing = new ArrayList<>();
        for (int i = 1; i < dim; ++i) {
            multipleMissing.add(i + 1);
        }
        // jetzt die Analyse
        boolean didSomethingElse = true;
        while (didSomethingElse) {
            if (showShow_) {
                show(teilerMultiples, multipleMissing);
                dummy_ = 0;
            }
            didSomethingElse = false;
            for (int i = dim - 1; i > 0; --i) {
                // hier laufe ich jetzt rückwärts durch, weil die einzelnen, eindeutigen Treffer gibt es ja eher bei den großen Teilern
                if (multipleMissing.contains((Integer) (i + 1))) {
                    if (teilerMultiples[i].isEmpty()) {
                        // Check beendet, weil es zu diesem Teiler kein Vielfaches gab oder nicht mehr gibt
                        return false;
                    }
                    int size = teilerMultiples[i].size();
                    if (size == 1) {
                        // ich muss dieses Vielfache nehmen
                        // statt über Index über den Wert entfernen
                        multipleMissing.remove((Integer) (i + 1));
                        removeMultiple(teilerMultiples, teilerMultiples[i].getFirst());
                        didSomethingElse = true;
                        if (showShow_) {
                            show(teilerMultiples, multipleMissing);
                            dummy_ = 0;
                        }
                    } else {
                        boolean match = false;
                        int[] fach = new int[size];
                        int countMatch = 0;
                        for (int j = 1; j < dim; ++j) {
                            match = false;
                            if (multipleMissing.contains((Integer) (j + 1))) {
                                if (teilerMultiples[j].size() == size) {
                                    match = true;
                                    for (int k = 0; k < size; ++k) {
                                        // ich kann direkt vergleichen, weil die Vielfachen aufsteigend gesammelt wurden
                                        if ( ! teilerMultiples[i].get(k).equals(teilerMultiples[j].get(k))) {
                                            match = false;
                                        }
                                    }
                                }
                            }
                            if (match) {
                                if (countMatch < size) {
                                    // ich merke mir nur die maximal sinnvollen Fächer
                                    // erstens brauche ich nur so viele für eine gültige Auflösung
                                    // und zweitens ich das Fach-Array auch nur so dimensioniert
                                    fach[countMatch] = j;
                                }
                                ++countMatch;
                            }
                        }
                        if (countMatch > size) {
                            // größerer Bedarf als Verfügbarkeit
                            // also Abbruch für diesen 'range'
                            return false;
                        } else if (countMatch == size) {
                            // Bedarf wie Verfügbarkeit
                            // also die Multiples alle löschen
                            for (int k = 0; k < size; ++k) {
                                multipleMissing.remove((Integer) fach[k]);
                                // das hier ist jetzt etwas "gefährlich"
                                // da ich ja die Vielfachen lösche, reduzieren sich in den matchenden Fächer die Einträge
                                // d.h. ich darf eigentlich nur immer auf Index 0 zugreifen
                                // hier ist wieder absolut hilfreich, dass die Vielfachen aufsteigend gesetzt werden
                                removeMultiple(teilerMultiples, teilerMultiples[i].getFirst());
                                didSomethingElse = true;
                                if (showShow_) {
                                    show(teilerMultiples, multipleMissing);
                                    dummy_ = 0;
                                }
                            }
                        } // hier gibt es kein auswertbares else mehr, mehr kann ich nämlich nicht ermitteln, wenn diese Vielfachen auch noch woanders so auftauchen
                    }
                }
            }
            show(teilerMultiples, multipleMissing);
            if (multipleMissing.isEmpty()) {
                return true;
            }
        }
        // ab hier wirklich die Permutationsbetrachtungen
        // ich iteriere jetzt durch die "restlichen" Fächer, d.h. die, die noch eine Länge haben
        // aber die ohne Länge schleppe ich mit, damit das mit den Indizes nicht zu verwirrend wird
        ArrayList<Integer> sizesPerm = new ArrayList<>();
        ArrayList<Integer> iteratePerm = new ArrayList<>();
        long allPerm = 1;
        int lastPerm = -1;
        for (int i = 0; i < dim; ++i) {
            int size = teilerMultiples[i].size();
            if ((i > 0) && (size > 1)) {
                allPerm *= size;
                // ich muss mir das letzte zu betrachtende Fach merken
                lastPerm = i + 1;
            }
            sizesPerm.add(size);
            iteratePerm.add(size > 1 ? 1 : 0);
        }
        return checkAufDivisiblePerm(teilerMultiples, sizesPerm, iteratePerm, allPerm, lastPerm);
    }

    public static boolean isPrime(long n) {
        if (n <= 1) {
            return false;
        }
        if (n <= 3) {
            return true;
        }
        // Alle Primzahlen größer als 3 sind entweder von der Form 6k + 1 oder 6k - 1
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }
        for (long i = 5; i * i <= n; i = i + 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) {
        // alle meine Überlegungen werden jetzt konzentriert fusioniert
        int dim = 36;
        ArrayList<Long>[] teiler = new ArrayList[dim];
        // ich kann aber nicht gleich befüllen, weil ich alle Zahlen mit allen Teilern verwurschteln muss
        for (int i = 0; i < dim; ++i) {
            teiler[i] = new ArrayList<>();
        }
        // mit Vielfachen befüllen
        for (int i = 0; i < dim; ++i) {
            for (long j = 1; j <= dim; ++j) {
                if (j % (i + 1) == 0) {
                    teiler[i].add(j);
                }
            }
        }
        long start = 1;
        long end = dim;
        int found = 0;
        ArrayList<Long> newPrimes = new ArrayList<>();
        int countPrimes = 0;
        int target = dim;
        System.out.println();
        do {
            show(teiler);
            if (start == 4160) {
                ////////////////////////////////////////////////////////////////////////// breakpoint
                dummy_ = 1;
                ////////////////////////////////////////////////////////////////////////// breakpoint
            }
            if (checkAufDivisible(teiler)) {
                ++found;
            }
            if (found < target) {
                do {
                    // ich verschiebe den 'range' so lange, bis nur noch maximal eine neue Primzahl dabei ist
                    refreshTeiler(teiler, start, false);
                    int index = newPrimes.indexOf(start);
                    if (index != -1) {
                        newPrimes.remove(index);
                        --countPrimes;
                    }
                    ++start;
                    ++end;
                    if (isPrime(end)) {
                        newPrimes.add(end);
                        ++countPrimes;
                    }
                    refreshTeiler(teiler, end, true);
                    System.out.println("Primzahlkontrolle: " + teiler[1]);
                } while (countPrimes >= 2);
                //show(teiler);
            }
            for (int i = 0; i < dim; ++i) {
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAktuelle Position vor Ziel ist im Moment " + found);
            }
            System.out.println();
        } while (found < target);
        // das sollte dann die Lösung sein
        System.out.println();
        System.out.println("Startwert des " + target + "ten Range ist: " + start);
    }
}