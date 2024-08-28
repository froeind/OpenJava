import java.util.ArrayList;
//import java.util.List;

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
            System.out.println(teiler[i]);
        }
        System.out.println("Break für " + nochNichtGefunden);
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

    public static boolean checkAufDivisible(ArrayList<Long>[] teiler, boolean aufwaertsAuswaehlen) {
        // zuerst teste ich, ob es eine leere Vielfachenkette gibt
        // dann bin ich schon negativ durch
        // diesen Test könnte ich auch innerhalb der nächsten Schleifenkonstruktion machen
        // aber auf Zeitgewinn bin ich jetzt mal nicht aus
        for (int i = teiler.length - 1; i > 0; --i) {
            if (teiler[i].isEmpty()) {
                return false;
            }
        }
        // dann alles von hinten kommend wegstreichen, was eindeutig zu machen ist und gemacht werden muss
        // dazu negiere ich die Zahlen absteigend, so dass sie als Vielfache der Teiler für weitere Betrachtungen ausgeschlossen werden
        // und ich muss mir natürlich merken, was ich gefunden habe
        ArrayList<Integer> nochNichtGefunden = new ArrayList<>();
        // als allererstes - hatte ich wieder vergessen - muss ich die Solitäre abarbeiten, also die Plätze, wo es nur ein einziges Vielfaches des Teilers gibt
        // aber ob das die Stagnation auf Position 5 verbessert? Ja, aber meine Start-Zahl-Lösung ist immer noch falsch
        // immerhin reduziert es die Auswahlmöglichkeiten an den anderen Plätzen
        // und hier muss ich auch schon die Breaks initialisieren und für die Solitäre gleich wieder freigeben
        for (int i = teiler.length - 1; i > 0; --i) {
            nochNichtGefunden.add(i);
            if (teiler[i].size() == 1) {
                // statt über Index über den Wert entfernen
                nochNichtGefunden.remove((Integer) i);
                blockZahl(teiler, teiler[i].getFirst());
            }
        }
        System.out.print("Nach Solitärentfernung in eindimensionalem Fach: ");
        show(teiler, nochNichtGefunden);
        // tja und dann eigentlich für alle anderen mit Mindestlänge 2 auch dort Solitäre suchen, solange bis es keine mehr gibt
        // das steht hier an zweiter Stelle, weil diese Solitäre auch eindeutig ermittelbar sind,
        // weil es ja keine Alternative zu ihnen gibt, sie sind ja hier das einzig verfügbare Vielfache
        boolean nochNichtFertig = true;
        while (nochNichtFertig) {
            nochNichtFertig = false;
            for (int i = teiler.length - 1; i > 0; --i) {
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
        System.out.print("Nach Solitärentfernung in mehrdimensionalem Fach: ");
        show(teiler, nochNichtGefunden);
        // und dann als drittes nochmal für alle anderen mit Mindestlänge 2 auch indirekte Solitäre suchen, da reicht ein Durchlauf, weil da nichts sich ändert
        // indirekte Solitäre sind Zahlen, die nur in diesem einen Fach auftauchen (außer dem 1er-Fach natürlich), also teilerfremd zu den anderen Fächern sind
        // wenn ich das weiter verfeinere, dann lande ich bei den Ausnahmefächern 2 und 3, da sind ja immer ganz viele dabei, und auch 5 oder 7
        // aber letztendlich käme ich vielleicht nicht drumrum, dann doch die Permutationen für die Auswahlen anzugehen, aber da habe ich keine Lust
        for (int i = teiler.length - 1; i > 1; --i) {
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
        // und jetzt höre ich auf, nachdem 50603, 40301 und zuletzt 3948 falsch waren, verfeinere ich nicht mehr mit Permutationen und sowas
        // 3948 war sowieso falsch, ich durfte ja nicht Fach 1 betrachten, damit ist es nun 3953
        // und jetzt plötzlich 40306??
        System.out.print("Nach Solitärentfernung für solitäre Vielfache: ");
        show(teiler, nochNichtGefunden);
        for (int i = teiler.length - 1; i > 0; --i) {
            // und hier muss ich auf jeden Fall wieder abwärts suchen
            // weil die größeren Zahlen zuerst abgearbeitet werden müssen, weil die kleineren brauche ich für die kleineren Teiler
            /*
            [3, -4, -5, -6, -7]
            [-4, -6]
            [3, -6]
            [-4]
            [-5]

            [2]
             */
            // nein ich teste aufwärts, ob das besser ist
            // weil die kleineren vielleicht unflexibler sind und die größeren auch weiter unten d.h. optisch noch vielleicht besser gehen
            /*
            [-5, -6, -7, -8, 9]
            [-6, -8]
            [-6, 9]
            [-8]
            [-5]

            [2]
            */
            // beides ist falsch, ich muss eigentlich wählen, welche Zahl ich nehmen kann, die ich später nicht mehr verwenden kann
            // ich muss also noch weiter das analysieren
            // wenn ich wüßte, dass entweder oder funktioniert ... ich probiere es, aber dann höre ich auf mit dem Programm
            // abwärts
            //for (int j = teiler[i].size() - 1; j >= 0 ; --j)
            // aufwärts
            for (int j = 0; j < teiler[i].size(); ++j) {
                int jj = aufwaertsAuswaehlen ? j : teiler[i].size() - 1 - j;
                if (teiler[i].size() > 1) {
                    // hier muss ich natürlich die Solitäre überspringen, die ich oben behandelt habe
                    long zahl = teiler[i].get(jj);
                    if (zahl > 0) {
                        // statt über Index über den Wert entfernen
                        nochNichtGefunden.remove((Integer) i);
                        blockZahl(teiler, zahl);
                        break;
                    }
                }
            }
        }
        if ( ! nochNichtGefunden.isEmpty()) {
            //System.out.print(aufwaertsAuswaehlen ? "1 up: " : "2 down: ");
            // nur Ausgabe, wenn Korrektur nichts brachte
            if ( ! aufwaertsAuswaehlen) {
                System.out.print("2 down: ");
                show(teiler, nochNichtGefunden);
            }
        }
        // die Negierungen wieder aufheben
        for (int j = teiler.length - 1; j > 0; --j) {
            teiler[j].replaceAll(Math::abs);
        }
        return nochNichtGefunden.isEmpty();
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
            if (checkAufDivisible(teiler, true)) {
                ++gefunden;
            } else if (checkAufDivisible(teiler, false)) {
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