
////////// Hunt the Wumpus - Uraltspiel aus den 70ern - in den 80ern von mir auch auf HP41CV programmiert
/*
Jage den Wumpus, bevor er dich frisst!
Erkunde ein Labyrinth aus Höhlenräumen.
Achte auf Gerüche und Wind, die den Wumpus und Fledermäuse verraten.
Bewege dich geschickt und schieße den Wumpus mit deinen Pfeilen ab.
Erlege den Wumpus mit deinem Pfeil oder sterbe durch
gefressen vom Wumpus oder zu oft in eine Grube gefallen oder vom eigenen Pfeil getroffen
*/

import java.util.*;

public class Wumpus {

    public static void main(String[] args) {

        Random random = new Random();

        boolean autotest = false;

        Input input = Input.getInstance();

        int countCaves;
        int countConnections;
        if (autotest) {
            countCaves = random.nextInt(30, 101);
            countConnections = random.nextInt(countCaves / 2, countCaves);
        } else {
            countCaves = input.importInt(1, "Anzahl der Höhlen")[0];
            countConnections = input.importInt(1, "Anzahl der Verbindungen")[0];
        }
        countCaves = Math.max(countCaves, 10);
        countConnections = Math.min(countConnections, countCaves - 1);
        System.out.println("Du spielst in " + countCaves + " Höhlen mit maximal " + countConnections + " Verbindungen zwischen den Höhlen.");
        System.out.println();

        // auf dem HP41 ging das meiner Erinnerung nach so, dass ich das Höhlensystem über AH.N1N2N3N4 (AktuelleHöhle als Vorkommateil und Nebenhöhlen als Nachkommastellen) darstellte
        Integer[][] caves;
        boolean notconsistent;
        do {
            caves = new Integer[countCaves][countConnections];
            notconsistent = true;
            for (int i = 0; i < countCaves; i++) {
                // ich durchlaufe alle Höhlen
                for (int j = 1; j < countConnections; j++) {
                    // und suche noch freie Plätze für eine Verbindung
                    if (caves[i][j] == null) {
                        // und versuche für jede maximal viele Verbindungen aufzubauen
                        int neighbor = random.nextInt(1, countCaves + 1);
                        // dazu ermittle ich eine zufällige Höhle, die Nachbar werden könnte
                        if (neighbor != i + 1) {
                            // diese muss natürlich eine andere Höhle als die aktuelle sein
                            boolean alien = true;
                            for (int k = 0; k < j; k++) {
                                // und diese darf keine der schon vorhandenen Nachbarhöhlen sein
                                //alien = alien && (caves[i][k] != neighbor);
                                alien = alien && (!Objects.equals(caves[i][k], neighbor));
                            }
                            if (alien) {
                                // jetzt habe ich eine Höhle gefunden, die Nachbar werden kann
                                // diese muss aber jetzt auch noch Platz für eine Verbindung haben und diese Verbindung darf noch nicht existieren
                                // den Platz muss ich überprüfen
                                // dass die Verbindung noch nicht existiert habe ich schon ermittelt, weil andernfalls diese Höhle ja hier auftaucht
                                for (int l = 0; l < countConnections; l++) {
                                    if (caves[neighbor - 1][l] == null) {
                                        // Verbindung möglich, also setzen
                                        caves[i][j] = neighbor;
                                        caves[neighbor - 1][l] = i + 1;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // konsistent ist das System, wenn ich ausgehend von Höhle 1 alle anderen Höhlen erreichen kann
            // die Grundlogik habe ich von Gemini und angepasst (Klasse brauche ich keine)
            notconsistent = cavesAreNotConsistent(caves);
        } while (notconsistent);

        // mit null-Sortieren schon etwas gesucht, aber dann gefunden, Danke an
        // https://ioflood.com/blog/sort-array-java/
        for (int i = 0; i < countCaves; i++) {
            Arrays.sort(caves[i], Comparator.nullsLast(Comparator.naturalOrder()));
        }

        // Starthöhle, Wumpus, Grube(n), Fledermäuse setzen
        // Pfeile bereitstellen
        int countBats = 0;
        int countPits = 0;
        int countFocus = 0;
        do {
            countBats = Math.max(4, (random.nextInt(4, countCaves + 1) / 10));
            countPits = Math.max(1, (random.nextInt(1, countCaves + 1) / 10));
            countFocus = 1 + 1 + countBats + countPits;
        } while (countFocus > countCaves);
        Integer[] focus = new Integer[countFocus];
        int nextcave;
        boolean free;
        for (int i = 0; i < countFocus; i++) {
            do {
                nextcave = random.nextInt(1, countCaves + 1);
                free = true;
                for (int j = 0; j < i; j++) {
                    free = free && (focus[j] != nextcave);
                }
            } while (!free);
            focus[i] = nextcave;
        }
        int countLives = Math.max(5, (random.nextInt(1, countCaves+1) / 100));
        int countArrows = Math.max(5, (random.nextInt(1, countCaves+1) / 100));

        boolean debug = false;
        boolean showcavesindebug = true;
        String action;
        do {
            // Ausgabe Position und Umgebung (Nachbarhöhlen und eventuell Wumpus) und Frage nach Aktion
            String pewms = "Du bist in Höhle " + focus[0] + ", Deine Nachbarhöhle(n): " + printWithoutNull(caves[focus[0] - 1]).trim() + ", Leben: " + countLives + ", Pfeile: " + countArrows + ", ";
            boolean wumpusIsNear = wumpusIsNear(caves, focus[0], focus[1]);
            boolean batsAreNear = batsOrPitsAreNear(caves, focus[0], Arrays.copyOfRange(focus, 2, 2 + countBats));
            boolean pitsAreNear = batsOrPitsAreNear(caves, focus[0], Arrays.copyOfRange(focus, 2 + countBats, countFocus));
            pewms = pewms + ((wumpusIsNear) ? "Wumpus ist nahe! " : "") + ((batsAreNear) ? "Fledermäuse sind nahe! " : "") + ((pitsAreNear) ? "Löcher sind nahe! " : "");
            System.out.println(pewms);
            if (autotest) {
                debug = true;
                int actionI = random.nextInt(1, countCaves + 1);
                int sign = random.nextInt(1, 8);
                actionI = (sign == 1) ? -actionI : actionI;
                action = Integer.toString(actionI);
                System.out.println("Deine Wahl ist " + action);
            } else {
                pewms = "Was willst Du machen? (Bewegen" + (countArrows > 0 ? ", -Schießen" : "") + ")";
                action = input.importString(1, pewms)[0];
            }
            boolean shoot = false;
            if (action.charAt(0) == '-') {
                action = action.substring(1);
                shoot = true;
            }
            // falsche Eingaben muss ich abfangen, da ich - für das Schießen verwende, wird nur noch ein + akzeptiert (sollte so sein) und das + tut nicht weh
            try { nextcave = Integer.parseInt(action);
            } catch (Exception e) { nextcave = focus[0]; }
            if (nextcave == focus[0]) {
                System.out.println("Du bist schon hier!");
            } else if (nextcave == 0) {
                debug = ! debug;
                System.out.println("Debugging " + (debug ? "" : "de") + "aktiviert!");
                if (showcavesindebug) {
                    System.out.println();
                    for (int i = 0; i < countCaves; i++) {
                        System.out.println((i + 1) + " = " + Arrays.toString(caves[i]));
                    }
                    System.out.println();
                    System.out.println(Arrays.toString(focus));
                    showcavesindebug = false;
                }
            } else if (nextcave > countCaves) {
                System.out.println("Diese Höhle gibt es nicht!");
            } else {
                if (shoot) {
                    // Ziel kann "irgendwo" liegen,
                    // entweder auf dem Weg ist der Wumpus oder nicht,
                    // oder Ziel ist gar nicht erreichbar, dann geht es zufällig weiter, dann kann ich auch selber getroffen werden
                    // ein Pfeil fliegt maximal 5 Höhlen
                    // aber es müssen natürlich auch Pfeile noch da sein
                    if (countArrows == 0) {
                        System.out.println("Du hast keine Pfeile mehr, Du musst also selber dorthin/irgendwohin gehen!");
                        shoot = false;
                        nextcave = focus[0];
                    } else {
                        countArrows--;
                        // Check auf Existenz des Ziels
                        // wenn es existiert wird wieder nextcave zurückgegeben, ansonsten eine zufällige Höhle
                        Integer[] target = trueTarget(autotest, debug, focus[0], nextcave, caves);
                        if (autotest) {
                            System.out.println("trueTarget Check");
                        }
                        if (target[0] == nextcave) {
                            // Schuss in erreichbare Höhle, jetzt alle Höhlen bis dahin auf Wumpus testen
                            for (Integer c : target) {
                                if (c == focus[1]) {
                                    System.out.println("Du hast den Wumpus erschossen!");
                                    System.exit(0);
                                }
                            }
                        } else if (target[0] == focus[0]) {
                                    System.out.println("Du hast Dich selber erschossen!");
                                    System.exit(0);
                        }
                        // Wumpus bewegt sich und "betroffene" Fledermäuse auch
                        wumpusChangeCave(autotest, focus, caves, countBats, random, 1);
                    }
                }
                if (!shoot) {
                    // Höhle wechseln, Test auf Wumpus, Fledermäuse, Loch
                    // in der Reihenfolge, weil zuerst packt der Wumpus zu, dann könnten die Fledermäuse noch vor einem Loch retten und dann wird reingefallen
                    // danach eventuelle Bewegung von Wumpus und sein tödlicher Angriff wenn er zu nahe gekommen ist
                    focus[0] = nextcave;
                    if (focus[1] == nextcave) {
                        System.out.println("Wumpus hat Dich getötet!");
                        System.exit(0);
                    } else {
                        // Fledermäuse
                        Integer[] allbats = Arrays.copyOfRange(focus, 2, 2 + countBats);
                        if (Arrays.asList(allbats).contains(nextcave)) {
                            System.out.println("Hui, eine Fledermaus packt Dich und flattert mit Dir in eine andere Höhle!");
                            int mybatfly = 0;
                            int whichbat = 0;
                            while (whichbat >= 0) {
                                whichbat = Arrays.asList(allbats).indexOf(nextcave);
                                if (whichbat >= 0) {
                                    // die erste Fledermaus trägt weiter
                                    // bei wenig Leben weiter, bei viel Leben kürzer
                                    // wenn ich noch viele Pfeile habe kürzer, bei wenigen Pfeilen weiter
                                    // mögliche weitere Fledermäuse flattern auch woandershin, aber alle nicht zum Wumpus
                                    // und die erste flattert auch noch eine Höhle weiter als die Mitnehmhöhle
                                    int batfly = batChangeCave(autotest, mybatfly, nextcave, countLives, countArrows, focus, whichbat, caves, random);
                                    if (mybatfly == 0) { mybatfly = batfly; focus[0] = mybatfly;}
                                    allbats[whichbat] = 0;
                                }
                            }
                        }
                        // und das sind die restlichen Fledermäuse, die auch dann eine Flatter machen
                        for(int b = 0; b < countBats; b++) {
                            if (allbats[b] != 0) {
                                batChangeCave(autotest, -1, allbats[b], -1, -1, focus, b, caves, random);
                            }
                        }
                        // Löcher
                        //List xx = Arrays.asList(Arrays.copyOfRange(focus, countBats + 2, countFocus));
                        if (Arrays.asList(Arrays.copyOfRange(focus, countBats + 2, countFocus)).contains(focus[0])) {
                            countLives--;
                            if (countLives == 0) {
                                System.out.println("Du bist zum letzten Mal in ein Loch gefallen. Du bist tot!");
                                //running = false;
                                System.exit(0);
                            } else {
                                System.out.println("Du bist in ein Loch gefallen!");
                            }
                        }
                        // wenn Wumpus in Nachbarhöhle, dann kann er sich bewegen und dann auch zu Dir mit etwas höherer Wahrscheinlichkeit
                        if (Arrays.asList(caves[focus[0] - 1]).contains(focus[1])) {
                            wumpusChangeCave(autotest, focus, caves, countBats, random, 5);
                            if (focus[1] == focus[0]) {
                                System.out.println("Wumpus hat Dich getötet!");
                                System.exit(0);
                            }
                        }
                        if (debug) {
                            System.out.println(Arrays.toString(focus));
                        }
                        // gnädigerweise kannst Du jetzt auch noch ab und zu einen Pfeil finden
                        if (countArrows <= 1) {
                            if (random.nextInt(1, caves[0].length) == 1) {
                                System.out.println("Oh, Du findest einen Pfeil!");
                                countArrows++;
                            }
                        }
                    }
                }
            }
        } while (true);
    }

    private static void wumpusChangeCave(boolean autotest, Integer[] focus, Integer[][] caves, int countBats, Random random, int weight) {
        if (autotest) {
            System.out.println("wumpusChangeCave weight=" + weight);
        }
        // Wumpus bewegt sich einfach eine oder mehrere Höhlen weiter
        // Löcher machen ihm nichts aus
        // Fledermäuse verteibt er
        Integer newcave = null;
        int start = focus[1];
        while (newcave == null) {
            while ((weight > 0) && (newcave != focus[0])) {
                newcave = caves[start - 1][random.nextInt(0, caves[0].length)];
                if (newcave != null) {
                    start = newcave;
                    weight--;
                }
            }
        }
        focus[1] = newcave;
        // werden Fledermäuse vertrieben?
        for (int b = 0; b < countBats; b++) {
            if (focus[2 + b] == focus[1]) {
                batChangeCave(autotest, -1, focus[1], -1, -1, focus, b, caves, random);
            }
        }
    }

    private static int batChangeCave(boolean autotest, int mybatfly, int cave, int countLives, int countArrows, Integer[] focus, int whichbat, Integer[][] caves, Random random) {
        if (autotest) {
            System.out.println("batChangeCave " + ((mybatfly == 0) ? "Me" : ""));
        }
        // die erste Fledermaus trägt weiter
        // bei wenig Leben weiter, bei viel Leben kürzer
        // wenn ich noch viele Pfeile habe kürzer, bei wenigen Pfeilen weiter
        // mögliche weitere Fledermäuse flattern auch woandershin, aber alle nicht zum Wumpus
        // und die erste flattert auch noch eine Höhle weiter als die Mitnehmhöhle
        Integer newcave = null;
        int howfar = (mybatfly == 0) ? (12-(countLives+countArrows)) : (2);
        for(int b = 0; b < howfar; b++) {
            newcave = null;
            while (newcave == null) {
                newcave = caves[cave - 1][random.nextInt(0, caves[0].length)];
            }
            if (mybatfly == 0) {
                if (b == howfar - 2) {
                    // dahin geht der Transport, da kann auch der Wumpus sein
                    mybatfly = newcave;
                }
            }
        }
        while (focus[1] == newcave) {
            // die Fledermaus fliegt nicht zum Wumpus
            newcave = batChangeCave(autotest, -1, newcave, -1, -1, focus, whichbat, caves, random);
        }
        focus[2 + whichbat] = newcave;
        return (mybatfly == -1) ? newcave : mybatfly;
    }

    private static boolean wumpusIsNear(Integer[][] caves, int me, int wumpus) {
        boolean wumpusIsNear = Arrays.asList(caves[me - 1]).contains(wumpus);
        if (wumpusIsNear) { return wumpusIsNear; };
        for (int w = 0; w < caves[me - 1].length; w++) {
            if (caves[me - 1][w] != null) {
                wumpusIsNear = wumpusIsNear || Arrays.asList(caves[caves[me - 1][w] - 1]).contains(wumpus);
                if (wumpusIsNear) {
                    return wumpusIsNear;
                }
            }
        }
        return wumpusIsNear;
    }

    private static boolean batsOrPitsAreNear(Integer[][] caves, int me, Integer[] subfocus) {
        boolean batsOrPitsAreNear = false;
        for (int s : subfocus) {
            batsOrPitsAreNear = batsOrPitsAreNear || Arrays.asList(caves[me - 1]).contains(s);
            if (batsOrPitsAreNear) { return batsOrPitsAreNear; };
        }
        return batsOrPitsAreNear;
    }

    private static Integer[] trueTarget(boolean autotest, boolean debug, int me, Integer target, Integer[][] caves) {
        // ausgehend von me durchlaufe ich alle Nebenhöhlen und deren Nebenhöhlen bis ich nach maximal 5 Schritten cave erreicht habe oder nicht
        // um doppeltes Betreten eine Höhle zu vermeiden mache ich eine Breitensuche, also werden von jeder ersten Nebenhöhle erst alle zweiten Nebenhöhlen aufgesucht
        // und ich merke mir natürlich alle betretenen Höhlen, dh. was ich schon kenne, verfolge ich woanders nicht weiter
        // und das baue ich jetzt als Liste auf
        // was ich auch noch überwachen muss, und da wäre ein Baum geschickter, wenn der Pfeil auf seinem Weg den Wunmpus trifft ist er auch hinüber
        // dazu darf ich aber nicht einfach alle Höhlen auf Wumpus überprüfen, sondern nur die auf dem konkreten Weg
        // also muss ich mir zusätzlich auch noch die Struktur merken, scheint aber trotzdem einfacher als ein Baum
        if (autotest) {
            System.out.println("trueTarget");
        }
        ArrayList<Integer> searched = new ArrayList();
        ArrayList<Integer> path = new ArrayList();
        searched.add(me);
        path.add(0);
        int start = 0;
        // ? int ende = searched.size();
        int ende = 1;
        int nextende = ende;
        int actualpath = 0;
        int fullpath = 0;
        Integer[] trueTarget = new Integer[5];
        breakpoint:
        for (int l = 1; l <= 5; l++) {
            for (int s = start; s < ende; s++) {
                actualpath = path.get(s) * 10;
                for (int c = 0; c < caves[0].length; c++) {
                    Integer cave = caves[searched.get(s) - 1][c];
                    if (cave != null) {
                        searched.add(cave);
                        path.add(actualpath + c + 1);
                        if (cave == target) {
                            trueTarget[0] = cave;
                            fullpath = path.getLast();
                            break breakpoint;
                        }
                        nextende++;
                    }
                }
                start = ende;
                ende = nextende;
            }
        }
        if (fullpath > 0) {
            // Zwischenstationen ermitteln, die brauche ich für den Wumpus-Schuß, den er kann auch schon vor dem Ziel getroffen werden
            start = 0;
            for (int r = searched.size() - 1; r > 0; r--) {
                if (path.get(r) == fullpath) {
                    trueTarget[start] = searched.get(r);
                    fullpath = fullpath / 10;
                    start++;
                };
            }
        } else {
            trueTarget[0] = searched.getLast();
        }
        if (debug) {
            System.out.println(Arrays.toString(searched.toArray()));
            System.out.println(Arrays.toString(path.toArray()));
            System.out.println(Arrays.toString(trueTarget));
        }
        return trueTarget;
    }

    private static String printWithoutNull(Integer[] caves) {
        String printWithoutNull = " ";
        for (Integer cave : caves) {
            if (cave != null) {
                printWithoutNull = printWithoutNull + cave + " ";
            }
    }
        return printWithoutNull;
    }

    private static boolean cavesAreNotConsistent(Integer[][] caves) {
        boolean[] isVisited = new boolean[caves.length];
        int countNotVisited = caves.length;
        countNotVisited = walkThrough(1, caves, isVisited, countNotVisited);
        return (countNotVisited > 0);
    }
    private static int walkThrough(int cave, Integer[][] caves, boolean[] isVisited, int countNotVisited) {
        if (isVisited[cave - 1]) { return countNotVisited; }
        isVisited[cave - 1] = true;
        countNotVisited--;
        if (countNotVisited == 0) { return countNotVisited; }
        for (int i = 0; i < caves[cave - 1].length; i++) {
            if (caves[cave - 1][i] != null) {
                // Danke Gemini, ich muss das Minimum nehmen
                countNotVisited = Math.min(countNotVisited, walkThrough(caves[cave - 1][i], caves, isVisited, countNotVisited));
            }
        }
        return countNotVisited;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // nicht mehr benötigt dank
    // Arrays.sort(caves[i], Comparator.nullsLast(Comparator.naturalOrder()));

    // Gemini, eine unnötige Vertauschung aber rausgenommen
    // braucht es jetzt nicht mehr, aber die folgende Variation mit null - brauche ich auch nicht mehr
    private static void moveZerosToTheEnd(int[] arr) {
        int lastNonZeroIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                if (i != lastNonZeroIndex) {
                    int temp = arr[i];
                    arr[i] = arr[lastNonZeroIndex];
                    arr[lastNonZeroIndex] = temp;
                }
                lastNonZeroIndex++;
            }
        }
    }
    private static void moveNullToTheEnd(Integer[] arr) {
        int lastNonZeroIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                if (i != lastNonZeroIndex) {
                    int temp = arr[i];
                    arr[i] = arr[lastNonZeroIndex];
                    arr[lastNonZeroIndex] = temp;
                }
                lastNonZeroIndex++;
            }
        }
    }
}
