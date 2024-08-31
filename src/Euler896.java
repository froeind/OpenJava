import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
//import java.util.stream.Collectors;

public class Euler896 {

    // 28.08.2024 Ingo Werner

    public static void show(ArrayList<Long>[] teiler) {
        for (int i = 1; i < teiler.length; ++i) {
            System.out.println(teiler[i]);
        }
        System.out.println();
    }

    public static void show(ArrayList<Long>[] teiler, ArrayList<Integer> nochNichtGefunden) {
        for (int i = 1; i < teiler.length; ++i) {
            if (nochNichtGefunden.contains((Integer) i)) {
                System.out.println(teiler[i]);
            }
        }
        System.out.println("Break für " + nochNichtGefunden);
        System.out.println();
    }

    public static void show(ArrayList<Long>[] teilerPerm, ArrayList<Integer> iteratePerm, ArrayList<Integer> sizesPerm) {
        for (int i = 1; i < teilerPerm.length; ++i) {
            if (teilerPerm[i].size() > 0) {
                System.out.println(teilerPerm[i]);
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

    public static void blockZahl(ArrayList<Long>[] teiler, long zahl) {
        for (int k = teiler.length - 1; k > 0; --k) {
            int index = teiler[k].indexOf(zahl);
            if (index != -1) {
                teiler[k].set(index, -zahl);
            }
        }
    }

    public static boolean checkAufDivisiblePerm(ArrayList<Long>[] teilerPerm, ArrayList<Integer> sizesPerm, ArrayList<Integer> iteratePerm) {
        boolean isDivisible = false;
        while (true) {
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
            for (int i = 1; i < teilerPerm.length - 1; ++i) {
                teilerPerm[i].replaceAll(Math::abs);
            }
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
                        if (i == iteratePerm.size() - 1) {
                            // letztes Fach erreicht, Ende der Fahnenstange
                            return false;
                        }
                    }
                }
            }
            System.out.println("Permutationen: ");
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
        show(teiler);
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
        for (int i = teiler.length - 1; i > 1; --i) {
            nochNichtGefunden.add(i);
            if (teiler[i].size() == 1) {
                // statt über Index über den Wert entfernen
                nochNichtGefunden.remove((Integer) i);
                blockZahl(teiler, teiler[i].getFirst());
            }
        }
        System.out.println("Nach Entfernung aller Fächer für die es von Anfang an nur eine Möglichkeit gab: ");
        show(teiler, nochNichtGefunden);
        // tja und dann eigentlich für alle anderen mit Mindestlänge 2 auch dort Solitäre "zweiter Wahl" suchen, solange bis es keine mehr gibt
        // das steht hier an zweiter Stelle, weil diese Solitäre auch eindeutig ermittelbar sind,
        // weil es ja keine Alternative zu ihnen gibt, sie sind ja hier das einzig verfügbare Vielfache
        // weil alle anderen Vielfachen im Teilerfach schon geblockt sind
        // auch hier "i > 1"
        // aber was ich vergessen hatte:
        // ich darf nur unerledigte Fächer durchlaufen, ansonsten würde das "doppelt" erledigt und Zahlen fallen weg
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
                        }
                    }
                }
            }
        }
        System.out.println("Nach Entfernung aller Fächer für die es sukzessive dann nur noch eine Möglichkeit gab: ");
        show(teiler, nochNichtGefunden);
        // und dann als drittes nochmal für alle anderen mit Mindestlänge 2 auch indirekte Solitäre suchen, da reicht ein Durchlauf, weil sich da dabei nichts ändert
        // indirekte Solitäre sind Zahlen, die nur in diesem einen Fach auftauchen (außer dem 1er-Fach natürlich), also teilerfremd zu den anderen Fächern sind
        // wenn ich das weiter verfeinere, dann lande ich bei den Ausnahmefächern 2 und 3, da sind ja immer ganz viele dabei, und auch 5 oder 7
        // aber letztendlich käme ich vielleicht nicht drumrum, dann doch die Permutationen für die Auswahlen anzugehen, aber da habe ich keine Lust
        // auch hier "i > 1" und auch hier betrachte ich nur unerledigte Fächer
        // wenn ich auch erledigte betrachte und dort einen indirekten Solitär hätte, dann kann ich nämlich nicht einfach den Test abbrechen,
        // weil ich das 1er-Fach ja nicht betrachte und dort alle Zahlen drin sind, d.h. es gibt ja eigentlich gar keine "richtigen" Solitäre
        // ausgenommen den Primzahlen größer der größten Zahl, die sitzen nur im 1er-Fach und bei mehr als zwei wird das ja schon außerhalb geregelt
        // ABER ich habe dabei einen Denkfehler gemacht:
        // wer sagt denn, dass dieser indirekte Solitär nicht im 1er-Fach benötigt wird, d.h. diese Auwahl darf ich eben nicht treffen
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
        System.out.println("Nach Entfernung aller Fächer für die es aßer dem 1er-FAch nur noch eine Möglichkeit gab: ");
        show(teiler, nochNichtGefunden);
        */
        // damit bin ich wieder bei den falschen 40301
        boolean isDivisible = false;
        if (nochNichtGefunden.size() > 0) {
            // jetzt kopiere ich meine ArrayList, damit ich in der Methode das permutativ durchlaufen kann
            // ArrayList<Long>[] teiler
            ArrayList<Long>[] teilerPerm = new ArrayList[teiler.length];
            for (int i = 1; i < teiler.length; ++i) {
                teilerPerm[i] = new ArrayList<>(teiler[i]);
            }
            // und ich entferne alle negativen Werte, weil die interessieren nicht mehr
            // die Erledigten erledige ich dann wieder mit dem "Flagtest" nochNichtGefunden.contains((Integer) i) in jedem Aufruf
            for (int i = teilerPerm.length - 1; i > 0; --i) {
                for (int j = teilerPerm[i].size() - 1; j >= 0; --j) {
                    if (teilerPerm[i].get(j) < 0) {
                        teilerPerm[i].remove(j);
                    }
                }
            }
            // ich iteriere jetzt durch die "restlichen" Fächer, d.h. die, die noch eine Länge haben
            // aber die ohne LÄnge schleppe ich mit, damit das mit den Indizes nicht zu verwirrend wird
            ArrayList<Integer> sizesPerm = new ArrayList<>();
            ArrayList<Integer> iteratePerm = new ArrayList<>();
            // Zwangsinitialisierung, damit die Indexe passen sind  mit den Fächern
            sizesPerm.add(0);
            iteratePerm.add(0);
            for (int i = 1; i < teilerPerm.length - 1; ++i) {
                sizesPerm.add(teilerPerm[i].size());
                iteratePerm.add(teilerPerm[i].size() > 1 ? 1 : 0);
            }
            isDivisible = checkAufDivisiblePerm(teilerPerm, sizesPerm, iteratePerm);
            // 7258 ist es auch nicht
        }
        // die Negierungen wieder aufheben
        for (int j = teiler.length - 1; j > 0; --j) {
            teiler[j].replaceAll(Math::abs);
        }
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
        System.out.println();
        do {
            if (checkAufDivisible(teiler)) {
                ++gefunden;
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
                    int rr=1;
                } while (primsCount >= 2);
                //show(teiler);
            }
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAktuelle Position vor Ziel ist im Moment " + gefunden);
            System.out.println();
        } while (gefunden < ziel);
        // das sollte dann die Lösung sein
        System.out.println();
        System.out.println("Startwert des " + ziel + "ten Range ist: " + start);
    }
}