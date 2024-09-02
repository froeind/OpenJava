import java.util.ArrayList;
import java.util.List;
//import java.util.stream.Collectors;

public class Euler896_dev {

    // 28.08.2024 Ingo Werner

    static boolean showShow_ = false;
    static int dummy_ = 0;

    public static void show(ArrayList<Long>[] teiler) {
        for (int i = 1; i < teiler.length; ++i) {
            System.out.println(i + ":" + teiler[i]);
        }
        System.out.println();
    }

    public static void show(ArrayList<Long>[] teiler, ArrayList<Integer> nochNichtGefunden) {
        for (int i = 1; i < teiler.length; ++i) {
            if (nochNichtGefunden.contains((Integer) i)) {
                System.out.println(i + ":" + teiler[i]);
            }
        }
        System.out.println("Break für " + nochNichtGefunden);
        System.out.println();
    }

    public static void show(ArrayList<Long>[] teilerPerm, ArrayList<Integer> iteratePerm, ArrayList<Integer> sizesPerm) {
        if (showShow_) {
            for (int i = 1; i < teilerPerm.length; ++i) {
                if ( ! teilerPerm[i].isEmpty()) {
                    System.out.println(i + ":" + teilerPerm[i]);
                }
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
            for (int i = 1; i < teiler.length; ++i) {
                if (endstart % i == 0) {
                    teiler[i].add(endstart);
                }
            }
        } else {
            for (int i = 1; i < teiler.length; ++i) {
                int index = teiler[i].indexOf(endstart);
                if (index != -1) {
                    teiler[i].remove(index);
                }
            }
        }
    }

    public static void blockZahlFinally(ArrayList<Long>[] teilerPerm) {
        for (int i = teilerPerm.length - 1; i > 0; --i) {
            for (int j = teilerPerm[i].size() - 1; j >= 0; --j) {
                if (teilerPerm[i].get(j) < 0) {
                    teilerPerm[i].remove(j);
                }
            }
        }
    }

    public static void blockZahl(ArrayList<Long>[] teiler, long zahl) {
        for (int k = teiler.length - 1; k > 0; --k) {
            int index = teiler[k].indexOf(zahl);
            if (index != -1) {
                teiler[k].set(index, -zahl);
            }
        }
    }

    public static void makePositiveAgain(ArrayList<Long>[] teiler) {
        // die Negierungen wieder aufheben
        for (int i = 1; i < teiler.length; ++i) {
            teiler[i].replaceAll(Math::abs);
        }

    }

    public static boolean checkAufDivisiblePerm(ArrayList<Long>[] teilerPerm, ArrayList<Integer> sizesPerm, ArrayList<Integer> iteratePerm, long allPerm, int lastPerm) {
        if (teilerPerm[1].getFirst() == 1325) {
            ////////////////////////////////////////////////////////////////////////// breakpoint
            dummy_ = 1;
            ////////////////////////////////////////////////////////////////////////// breakpoint
        }
        long countPerm = 0;
        boolean isDivisible = false;
        while (true) {
            ++countPerm;
            if (countPerm >= allPerm - 5) {
                ////////////////////////////////////////////////////////////////////////// breakpoint
                dummy_ = 1;
                ////////////////////////////////////////////////////////////////////////// breakpoint
            }
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCheck Permutationen " + allPerm + " " + countPerm);
            isDivisible = true;
            for (int i = 2; i < teilerPerm.length - 1; ++i) {
                if (teilerPerm[i].size() > 1) {
                    // gäbe es nur einen Wert, dann hätte ich ihn bei den Solitärbetrachtungen schon bearbeitet
                    long zahl = teilerPerm[i].get(iteratePerm.get(i) - 1);
                    if (zahl > 0) {
                        // diese Kombination geht, also alle Auftreten negativ setzen, wie gewohnt
                        blockZahl(teilerPerm, zahl);
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
                show(teilerPerm, iteratePerm, sizesPerm);
                return true;
            }
            // die Negierungen wieder aufheben
            makePositiveAgain(teilerPerm);
            // Inkrementierung berechnen und überprüfen
            for (int i = 2; i < iteratePerm.size(); ++i) {
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
            if (showShow_) {
                System.out.println("Permutationen: ");
                show(teilerPerm, iteratePerm, sizesPerm);
            }
            System.out.println("Nur Permutationen: ");
            show(teilerPerm, iteratePerm, sizesPerm);
        }
    }

    public static boolean checkAufDivisible(ArrayList<Long>[] teiler) {
        // zuerst teste ich, ob es eine leere Vielfachenkette gibt
        // dann bin ich schon negativ durch
        // diesen Test könnte ich auch innerhalb der nächsten Schleifenkonstruktion machen
        // aber auf Zeitgewinn bin ich jetzt mal nicht aus
        for (int i = teiler.length - 1; i > 0; --i) {
            if (teiler[i].isEmpty()) {
                return false;
            }
        }
        if (showShow_) {
            show(teiler);
        }
        // dann alles vom Ende kommend wegstreichen, was eindeutig zu machen ist und gemacht werden muss
        // dazu negiere ich die Zahlen absteigend, so dass sie als Vielfache der Teiler für weitere Betrachtungen ausgeschlossen werden
        // und ich muss mir natürlich merken, was ich gefunden habe
        ArrayList<Integer> nochNichtGefunden = new ArrayList<>();
        // als allererstes - hatte ich wieder vergessen - muss ich die Solitäre abarbeiten, also die Plätze, wo es nur ein einziges Vielfaches des Teilers gibt
        // aber ob das die Stagnation auf Position 5 verbessert? Ja, aber meine Start-Zahl-Lösung ist immer noch falsch
        // immerhin reduziert es die Auswahlmöglichkeiten an den anderen Plätzen
        // und hier muss ich auch schon die Breaks initialisieren und für die Solitäre gleich wieder freigeben
        // hier brauche ich das 1er-Fach nicht betrachten (das 2er und 3er aber schon, da kann es wie gesehen Konflikte geben, und alle weiteren Fächer auch)
        // also bis "i > 1"
        if (teiler[1].getFirst() == 1326) {
            ////////////////////////////////////////////////////////////////////////// breakpoint
            dummy_ = 1;
            ////////////////////////////////////////////////////////////////////////// breakpoint
        }
        for (int i = teiler.length - 1; i > 1; --i) {
            nochNichtGefunden.add(i);
            if (teiler[i].size() == 1) {
                // wie dumm=kurz gedacht war das, die negativen zuzulassen
                // und nun ja, der Sonderfall der einelementigen Fächer könnte ich auch mit der folgenden Analyse zusammenfassen, aber ich lasse das jetzt getrennt
                if (teiler[i].getFirst() < 0) {
                    makePositiveAgain(teiler);
                    return false;
                }
                // statt über Index über den Wert entfernen
                nochNichtGefunden.remove((Integer) i);
                blockZahl(teiler, teiler[i].getFirst());
            }
        }
        if (showShow_) {
            System.out.println("Nach Entfernung aller Fächer für die es von Anfang an nur eine Möglichkeit gab: ");
            show(teiler, nochNichtGefunden);
        }
        if (teiler[1].getFirst() == 1326) {
            ////////////////////////////////////////////////////////////////////////// breakpoint
            dummy_ = 1;
            ////////////////////////////////////////////////////////////////////////// breakpoint
        }
        // tja und dann eigentlich für alle anderen mit Mindestlänge 2 auch dort Solitäre "zweiter Wahl" suchen, solange bis es keine mehr gibt
        // das steht hier an zweiter Stelle, weil diese Solitäre auch eindeutig ermittelbar sind,
        // weil es ja keine Alternative zu ihnen gibt, sie sind ja hier das einzig verfügbare Vielfache
        // weil alle anderen Vielfachen im Teilerfach schon geblockt sind
        // auch hier "i > 1"
        // aber was ich vergessen hatte:
        // ich darf nur unerledigte Fächer durchlaufen, ansonsten würde das "doppelt" erledigt und Zahlen fallen weg
        // und wenn in einem Fach, das noch nicht geklärt ist, nur noch negative Zahlen auftauchen, dann muss ich ja auch abbrechen
        boolean nochNichtFertig = true;
        while (nochNichtFertig) {
            nochNichtFertig = false;
            for (int i = teiler.length - 1; i > 1; --i) {
                if (nochNichtGefunden.contains((Integer) i)) {
                    if (teiler[i].size() > 1) {
                        // Solitäre suchen
                        int countPositiv = 0;
                        long solitaer = -1;
                        for (int j = 0; j < teiler[i].size(); ++j) {
                            long zahl = teiler[i].get(j);
                            if (zahl > 0) {
                                ++countPositiv;
                                solitaer = zahl;
                            }
                        }
                        if (countPositiv == 1) {
                            // statt über Index über den Wert entfernen
                            nochNichtGefunden.remove((Integer) i);
                            blockZahl(teiler, solitaer);
                            nochNichtFertig = true;
                        } else if (countPositiv == 0) {
                            makePositiveAgain(teiler);
                            return false;
                        }
                    }
                }
            }
        }
        if (showShow_) {
            System.out.println("Nach Entfernung aller Fächer für die es sukzessive dann nur noch eine Möglichkeit gab: ");
            show(teiler, nochNichtGefunden);
        }
        if (teiler[1].getFirst() == -1326) {
            ////////////////////////////////////////////////////////////////////////// breakpoint
            dummy_ = 1;
            ////////////////////////////////////////////////////////////////////////// breakpoint
        }
        // und dann als drittes nochmal für alle anderen mit Mindestlänge 2 auch indirekte Solitäre suchen, da reicht ein Durchlauf, weil sich da dabei nichts ändert
        // indirekte Solitäre sind Zahlen, die nur in diesem einen Fach auftauchen (außer dem 1er-Fach natürlich), also teilerfremd zu den anderen Fächern sind
        // wenn ich das weiter verfeinere, dann lande ich bei den Ausnahmefächern 2 und 3, da sind ja immer ganz viele dabei, und auch 5 oder 7
        // aber letztendlich käme ich vielleicht nicht drumrum, dann doch die Permutationen für die Auswahlen anzugehen, aber da habe ich keine Lust
        // auch hier "i > 1" und auch hier betrachte ich nur unerledigte Fächer
        // wenn ich auch erledigte betrachte und dort einen indirekten Solitär hätte, dann kann ich nämlich nicht einfach den Test abbrechen,
        // weil ich das 1er-Fach ja nicht betrachte und dort alle Zahlen drin sind, d.h. es gibt ja eigentlich gar keine "richtigen" Solitäre
        // ausgenommen den Primzahlen größer der größten Zahl, die sitzen nur im 1er-Fach und bei mehr als zwei wird das ja schon außerhalb geregelt
        // ABER ich habe dabei einen Denkfehler gemacht:
        // -> wer sagt denn, dass dieser indirekte Solitär nicht im 1er-Fach benötigt wird, d.h. diese Auswahl darf ich eben nicht treffen
        /*
        for (int i = teiler.length - 1; i > 1; --i) {
            if (nochNichtGefunden.contains((Integer) i)) {
                if (teiler[i].size() > 1) {
                    // indirekten Solitär suchen
                    for (int j = 0; j < teiler[i].size(); ++j) {
                        boolean iSolitaer = false;
                        long zahl = teiler[i].get(j);
                        if (zahl > 0) {
                            iSolitaer = true;
                            for (int k = teiler.length - 1; k > 1; --k) {
                                if (i != k) {
                                    int index = teiler[k].indexOf(zahl);
                                    if (index != -1) {
                                        iSolitaer = false;
                                        break;
                                    }
                                }
                            }
                        }
                        if (iSolitaer) {
                            // statt über Index über den Wert entfernen
                            nochNichtGefunden.remove((Integer) i);
                            blockZahl(teiler, zahl);
                            break;
                        }
                    }
                }
            }
        }
        // und jetzt höre ich auf, nachdem 50603, 40301 und zuletzt 3948 falsch waren, verfeinere ich nicht mehr mit Permutationen und sowas
        // 3948 war sowieso falsch, ich durfte ja nicht Fach 1 betrachten, damit ist es nun 3953
        // und jetzt plötzlich 40306?? falsch, wird schon stimmen, muss jetzt doch über Permutationen den Rest machen
        System.out.println("Nach Entfernung aller Fächer für die es außer dem 1er-Fach nur noch eine Möglichkeit gab: ");
        show(teiler, nochNichtGefunden);
        */
        // damit bin ich wieder bei den falschen 40301
        // und damit dann doch bei Brut Force über Permutaionen
        // aber das ist doch nur letztes Mittel der Wahl
        // weil ich dort in riesengroßen Berechnungen/Schleifen lande
        // muss ich zuvor noch mehr Sonderfälle abhandeln
        // 1. es gibt noch den Fall, dass Vielfache an zuviel Stellen gebraucht werden
        // bei 53776813 .. 53776848 gibt es die Vielfache 53776820 und 53776840, die beide für die Teiler 5, 10 und 20 gebraucht werden
        // d.h. das kann ich schon vor den Permutationen ermitteln, dass dieses 'range' nicht funktioniert
        // dazu muss ich für Fach-Länge 2 die zwei Vielfachen betrachten und merken und nachsehen, ob diese in mindestens 3 Fächern der Länge 2 auftauchen
        // dann kann ich abbrechen
        // verallgemeinert ist das dann:
        // ich betrachte für Fach-Länge N die N Vielfachen und sehe nach, ob diese N in mindestens N+1 Fächern der Länge N auftauchen
        // dann kann ich abbrechen
        // für diese Betrachtungen verwende ich die vorbereitenden Berechnungen für die Permutationen
        boolean isDivisible = true;
        if ( ! nochNichtGefunden.isEmpty()) {
            // jetzt kopiere ich meine ArrayList, damit ich in der Methode das permutativ durchlaufen kann
            // ArrayList<Long>[] teiler
            ArrayList<Long>[] teilerPerm = new ArrayList[teiler.length];
            for (int i = 1; i < teiler.length; ++i) {
                teilerPerm[i] = new ArrayList<>(teiler[i]);
            }
            // und ich entferne alle negativen Werte, weil die interessieren nicht mehr
            // die Erledigten erledige ich dann wieder mit dem "Flagtest" nochNichtGefunden.contains((Integer) i) in jedem Aufruf
            if (teiler[1].getFirst() == -1326) {
                ////////////////////////////////////////////////////////////////////////// breakpoint
                dummy_ = 1;
                ////////////////////////////////////////////////////////////////////////// breakpoint
            }
            // negative Zahlen=Vielfache löschen
            blockZahlFinally(teilerPerm);
            // jetzt untersuche ich auf ein Zuviel an Bedarf
            // die Frage bleibt aber, wenn ich so keinen Abbruch finde, wie reduziere ich die Permutationen?
            // so:
            // ich betrachte also für Fach-Länge N die N Vielfachen
            // wenn ich für N weniger als N Fächer finde, wo die N drin sind, dann kann ich keine Aussage tätigen, weil in anderern Fächern auch ein Bedarf nach den N sein kann
            // wenn ich für N genau N Fächer finde, dann muss ich diese N auch nehmen und überall blocken
            // dann reduziere ich den Umfang der Permutationen, das muss aber ja nicht sein, aber mal schau'n
            // wenn diese N in mehr als N, also mindestens N+1 Fächern der Länge N auftauchen, dann kann es keine Lösung geben
            // ich durchlaufe aber jetzt nicht für N=2, 3, ...
            // sondern ich nehme die Fächer wie sie kommen und habe damit mein N vorgegeben und passe dann alles daran an
            // unterm Strich könnte ich jetzt alles da oben mit dem zusammen in einer gemeinsamen Logik vereinen, aber ich lasse das jetzt so
            // manche Sachen klären sich vielleicht so etwas schneller
            for (int i = 2; i < teilerPerm.length - 1; ++i) {
                if (nochNichtGefunden.contains((Integer) i)) {
                    int size = teilerPerm[i].size();
                    boolean match = false;
                    int[] fach = new int[size];
                    if (size > 1) {
                        int countMatch = 0;
                        for (int j = 2; j < teilerPerm.length - 1; ++j) {
                            match = false;
                            if (nochNichtGefunden.contains((Integer) j)) {
                                if (teilerPerm[j].size() == size) {
                                    match = true;
                                    for (int k = 0; k < size; ++k) {
                                        // ich kann direkt vergleichen, weil die Vielfachen aufsteigend gesammelt wurden
                                        if ( ! teilerPerm[i].get(k).equals(teilerPerm[j].get(k))) {
                                            match = false;
                                        }
                                    }
                                }
                            }
                            if (match) {
                                fach[countMatch] = j;
                                ++countMatch;
                            }
                        }
                        if (countMatch > size) {
                            // größerer Bedarf als Verfügbarkeit
                            // also Abbruch für diesen 'range'
                            makePositiveAgain(teiler);
                            return false;
                        } else if (countMatch == size) {
                            // Bedarf wie Verfügbarkeit
                            // also die Werte alle blocken, aber diesesmal gleich nach Negation auch löschen
                            // negative Zahlen=Vielfache löschen
                            for (int k = 0; k < size; ++k) {
                                blockZahl(teiler, teilerPerm[i].get(k));
                                blockZahlFinally(teilerPerm);
                                nochNichtGefunden.remove((Integer) fach[k]);
                            }
                        } // hier gibt es kein auswertbares else mehr, mehr kann ich nämlich nicht ermitteln
                    }
                }
            }
            if (nochNichtGefunden.isEmpty()) {
                return true;
            }
            // ab hier wirklich die Permutationsbetrachtungen
            // ich iteriere jetzt durch die "restlichen" Fächer, d.h. die, die noch eine Länge haben
            // aber die ohne Länge schleppe ich mit, damit das mit den Indizes nicht zu verwirrend wird
            ArrayList<Integer> sizesPerm = new ArrayList<>();
            ArrayList<Integer> iteratePerm = new ArrayList<>();
            // Zwangsinitialisierung, damit die Indexe passen sind mit den Fächern
            sizesPerm.add(0);
            iteratePerm.add(0);
            long allPerm = 1;
            int lastPerm = -1;
            for (int i = 1; i < teilerPerm.length - 1; ++i) {
                int size = teilerPerm[i].size();
                if ((i > 1) && (size > 1)) {
                    allPerm *= size;
                    // ich muss mir das letzte zu betrachtende Fach merken
                    lastPerm = i;
                }
                sizesPerm.add(size);
                iteratePerm.add(size > 1 ? 1 : 0);
            }
            isDivisible = checkAufDivisiblePerm(teilerPerm, sizesPerm, iteratePerm, allPerm, lastPerm);
            // 7258 ist es auch nicht
        }
        // die Negierungen wieder aufheben
        makePositiveAgain(teiler);
        //if (isDivisible) {
        //}
        return isDivisible;
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
        // Danke Gemini, so ging es am schnellsten
        // einfach gedacht:
        // bei einer Länge von 36 eines 'contiguous range of positive integers' gibt es "immer" Zahlen, die durch 1 bis 36 geteilt werden können
        // weil entweder es ist durch das Weiterwandern der ersten Zahl eine Zahl noch im Intervall oder eine mehrfache taucht auf
        // sobald 36 rausfällt, ist aber 72 hinten wieder drin, wenn 35 rausfällt, ist 71 und damit 70 drin
        // weitergedacht:
        // kritisch wird das ganze immer dann, wenn hinten durch das Wandern eine neue Primzahl dazukommt
        // und das passiertt ja andauernd
        // denn diese Primzahl muss dann immer an Position 1 als teilbar "stehen=verwendet werden", weil sie ja nicht sonstwie geteilt werden kann
        // und damit verschiebt sich dann alles in den "ist Mehrfaches von-Fächern", die ich über die ArrayList verwalten werde
        // um das Ganze zu verkleinern aber auch zu verkomplizieren, könnte ich jetzt die Kontrollen des 1- bis n?-Fachs weglassen,
        // also der Teiler, für die es immer genügend Zahlen geben müsste, wie 1, 2, 3, 4, ... n?
        // und noch weiter gedacht:
        // aber ich kann nicht einfach nur die Fächer füllen und kontrollieren auf Inhalt
        // eine Zahl kann ja in mehreren Fächern sein, aber auch dafür explizit benötigt werden, es gibt sie aber nur einmal
        // also fehlt eine Zahl für eine 'divisible range'
        // und noch ein Stückchen:
        // solange mehr als eine Primzahl größer 36 in der 'range' ist, ist es kein 'divisible range'
        // also muss ich "nur" alle anderen 'range' betrachten
        // also habe ich zwei Fälle:
        // ich habe keine Primzahl dabei oder genau eine
        // habe ich keine, habe ich automatisch ein 'divisible range'
        // habe ich eine, muss ich das untersuchen, denn das dann auch gehen kann, zeigt das Beispiel in der Aufgabenstellung
        // jetzt ist die Frage wie ich das kontrolliere
        // "optisch=auf dem Papier" ist das einfach:
        // ich fülle die 1- ... 36-Fächer mit den Zahlen, die durch 1 ... 36 geteilt werden können
        // wenn dann in mehr als einem Fach eine Zahl alleine auftaucht, dann gibt es kein 'divisible range'
        // ansonsten nehme ich das Fach mit der Zahl weg und die Zahl aus allen anderen Fächern auch
        // und mache so weiter
        // wenn natürlich eine Zahl in mehreren Fächern auftaucht, so dass gewählt werden müsste, für welches "ist Teiler von" ich mich entscheide
        // dann wird es komplizierter, aber händisch wohl noch machbar
        // klar dass ich von 36 absteigend die Fächer durchsuche, weil die großen Teiler sind eindeutiger zuzuordnen, da wird es ja am Anfang die leeren Fächer geben
        // weil klar ist, dass in der 'range' weniger Zahlen durch 36, 35, 34, ... m teilbar sind, als durch 1, 2, 3 ...
        // noch interessanter d.h. komplizierter wird es, wenn wir in Zahlenbereichen sind von 36*35*34*33*...
        // d.h. das Lösungsrezept kann wohl nicht durch die Sonderfälle am Anfang inspiriert, so "einfach=vereinfacht" durchgezogen werden
        int dim = 36;
        //int dim = 5;
        ArrayList<Long>[] teiler = new ArrayList[dim + 1];
        // ich laufe ab 1, damit ich nicht immer umdenken muss
        // ich kann aber nicht gleich befüllen, weil ich alle Zahlen mit allen Teilern verwurschteln muss
        for (int i = 1; i < teiler.length; ++i) {
            teiler[i] = new ArrayList<>();
        }
        // dann kann ich auch gleich befüllen
        // und auch gleich ab 2, weil durch 1 lässt sich alles teilen
        // und jetzt doch wieder ab 1, weil so das Fehlersuchen vereinfacht wird, und nur so sehe ich, wo die Primzahlen auftauchen
        // und ich sehe auch den kompletten aktuellen 'range'
        for (int i = 1; i < teiler.length; ++i) {
            for (long j = 1; j < teiler.length; ++j) {
                if (j % i == 0) {
                    teiler[i].add(j);
                }
            }
        }
        long start = 1;
        long end = dim;
        int gefunden = 0;
        ArrayList<Long> primsList = new ArrayList<>();
        int primsCount = 0;
        int ziel = dim;
        //int ziel = 27;
        if (showShow_) {
            System.out.println();
        }
        do {
            if (start == 1326) {
                ////////////////////////////////////////////////////////////////////////// breakpoint
                dummy_ = 1;
                ////////////////////////////////////////////////////////////////////////// breakpoint
            }
            showShow_ = false;
            if (checkAufDivisible(teiler)) {
                ++gefunden;
                showShow_ = true;
            }
            if (gefunden < ziel) {
                do {
                    // ich verschiebe den 'range' so lange, bis nur noch maximal eine neue Primzahl dabei ist
                    refreshTeiler(teiler, start, false);
                    int index = primsList.indexOf(start);
                    if (index != -1) {
                        primsList.remove(index);
                        --primsCount;
                    }
                    ++start;
                    ++end;
                    if (isPrime(end)) {
                        primsList.add(end);
                        ++primsCount;
                    }
                    refreshTeiler(teiler, end, true);
                    System.out.println("Primzahlkontrolle: " + teiler[1]);
                } while (primsCount >= 2);
                //show(teiler);
            }
            if (showShow_) {
                for (int i = 1; i < 36; ++i) {
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAktuelle Position vor Ziel ist im Moment " + gefunden);
                }
                System.out.println();
            }
            showShow_ = false;
        } while (gefunden < ziel);
        // das sollte dann die Lösung sein
        System.out.println();
        System.out.println("Startwert des " + ziel + "ten Range ist: " + start);
    }
}