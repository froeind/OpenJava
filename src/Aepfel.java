
//import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Aepfel {

    Graphics G;

    Choice chx;
    Label[] label;
    TextField[] textfield;
    Button[] button;
    KnopfAbfrage[] knopfabfrage;
    final int anzkomponenten = 18;
    int[] vonkomponente,biskomponente;
    Random rand = new Random();

    // Vorauswahl
    int auswahl = 5;
    int second;

    // Dimensionen für Rechteck + Kreis + Dreieck + Kreisring
    int[] dim;

    // speziell für Kreisring
    final int KrAradius = 240;
    int KrIradius = 100;

    // speziell für Apfelmann
    final int apfelbreite = 575;
    final int apfelhoehe = 425;
    int iend = 2*apfelbreite;
    int jend = 2*apfelhoehe;
    double rzf = 0;
    double izf = 0;
    double qrzf;
    double qizf;
    double crstep;
    double cistep;
    double crmgrenze;
    double crpgrenze;
    double cimgrenze;
    double cipgrenze;
    double crstepAlt;
    double cistepAlt;
    double crmgrenzeAlt;
    double crpgrenzeAlt;
    double cimgrenzeAlt;
    double cipgrenzeAlt;
    int farbe = 0;
    int rausbei = 0;
    final int maxfarbe = 4;
    Color[] acolor,arcolor,agcolor,abcolor;
    int ineu = -1;
    int jneu = -1;
    // 'neuoderalt' regelt was gerade gemalt werden soll
    // 0=altes nochmal - 1=nochmal das gleiche - 2=neue werte nehmen
    // 3=original-apfelmann zeichnen
    int neuoderalt = 3;
    int schwarzgrenze = 200;
    int zoom = 6;
    String modus = "R";
    final int apfelxabstand = 200;
    final int apfelyabstand = 60;
    // 'grenzqradius' ist der Radius des Kreises,
    // den die Punkte verlassen oder auch nicht,
    // zum Quadrat genommen
    double grenzqradius=100;
    int[][] punktfarbe;
    int lupe;
    int exponent = 2;

    // speziell für Schneeflocke
    int schneetiefe = 6;
    int schneekante = 150;

    // speziell für Dreieckchen
    final int dxmitte = 425;
    final int dymitte = 250;
    double faktor;
    int dfarbe = 11;
    int dreitiefe = 8;
    int dreikante = 140;

    // Rechentiefe wird bei 'second=1' halbiert
    // also Hilfsvariable benutzen
    int realetiefe = 0;

    public void paint(Graphics g)
    {
        if (auswahl == 1)
        {
            // ausgefüllter Polygonzug, der Rechteck ausfüllt
            int[] x = new int[dim[auswahl]];
            int[] y = new int[dim[auswahl]];
            int seite = 200;
            int xabstand = 200;
            int yabstand = 40;
            for(int i=0;i<dim[auswahl];++i)
            {
                x[i] = (int)(rand.nextDouble()*2*seite+xabstand);
                y[i] = (int)(rand.nextDouble()*2*seite+yabstand);
            }
            farbeSetzen(g,2);
            g.fillPolygon(x,y,dim[auswahl]);
        }
        else if (auswahl == 2)
        {
            // ausgefüllter Polygonzug, der Kreis ausfüllt
            int[] x = new int[dim[auswahl]];
            int[] y = new int[dim[auswahl]];
            int radius = 240;
            int xabstand = 180;
            int yabstand = 20;
            for(int i=0;i<dim[auswahl];++i)
            {
                x[i] = (int)(rand.nextDouble()*2*radius+xabstand);
                int h = x[i]-xabstand-radius;
                y[i] = (int)(rand.nextDouble()*(Math.sqrt(radius*radius-h*h)));
                y[i] = rand.nextDouble() < 0.5 ? yabstand+radius+y[i] : yabstand+radius-y[i];
            }
            farbeSetzen(g,2);
            g.fillPolygon(x,y,dim[auswahl]);
        }
        else if (auswahl == 3)
        {
            // ausgefüllter Polygonzug, der Dreieck ausfüllt
            int[] x = new int[dim[auswahl]];
            int[] y = new int[dim[auswahl]];
            int seite = 500;
            int hoehe = 400;
            int xabstand = 150;
            int yabstand = 40;
            for(int i=0;i<dim[auswahl];++i)
            {
                x[i] = (int)(rand.nextDouble()*seite+xabstand);
                int h1 = (int)(seite/2);
                int h2 = x[i]-xabstand-h1;
                if (h2 == 0) y[i] = yabstand;
                else if (h2 > 0) y[i] = yabstand+hoehe-(int)(rand.nextDouble()*(hoehe-hoehe*h2/h1));
                else y[i] = yabstand+hoehe-(int)(rand.nextDouble()*(hoehe+hoehe*h2/h1));
            }
            farbeSetzen(g,2);
            g.fillPolygon(x,y,dim[auswahl]);
        }
        else if (auswahl == 4)
        {
            // ausgefüllter oder gezogener Polygonzug, der Kreisring ausfüllt
            int[] x = new int[dim[auswahl]];
            int[] y = new int[dim[auswahl]];
            int xabstand = 180;
            int yabstand = 20;

            x[0] = xabstand;
            y[0] = KrAradius+yabstand;
            for(int i=1;i<dim[auswahl]-1;++i)
            {
                boolean nochnichtfertig = true;
                // Endlosschleifenabbruch
                int sofortraus = 0;
                do
                {
                    x[i] = (int)(rand.nextDouble()*2*KrAradius+xabstand);
                    int h = x[i]-xabstand-KrAradius;
                    int hh = (int)(rand.nextDouble()*(Math.sqrt(KrAradius*KrAradius-h*h)));
                    hh = rand.nextDouble() < 0.5 ? hh : -hh;
                    y[i] = hh+yabstand+KrAradius;
                    // der Richtungsvektor
                    double f1 = x[i]-x[i-1];
                    double f2 = y[i]-y[i-1];
                    // der zukünftige Normalenvektor
                    double n1;
                    double n2;
                    if((f1 != 0) | (f2 != 0))
                    {
                        // aufeinanderfolgende Punkte sind nicht gleich
                        // also Normalenvektor zur Verbindungsgeraden aufstellen
                        if(f1 == 0)
                        {
                            n1 = 1;
                            n2 = -1/f2;
                        }
                        else if(f2 == 0)
                        {
                            n1 = -1/f1;
                            n2 = 1;
                        }
                        else
                        {
                            n1 = 1;
                            n2 = (-1-f1)/f2;
                        }
                        // Normalenvektor auf Länge 1 bringen
                        double n3 = Math.sqrt(n1*n1+n2*n2);
                        n1 = n1/n3;
                        n2 = n2/n3;
                        // Abstand von Nullpunkt, d.h. Kreisringmitte, zu Gerade berechnen
                        // und mit Innenradius vergleichen
                        if((int)Math.abs(n1*h+n2*hh) > KrIradius) nochnichtfertig = false;
                        ++sofortraus;
                    }
                }
                while((nochnichtfertig) && (sofortraus <= 20));
                if(sofortraus > 20)
                {
                    // nächster Punkt wird 'einfach' als vorhergehender gesetzt
                    // wenn dieser Punkt schon vorhanden ist
                    if(i > 2)
                    {
                        x[i] = x[i-2];
                        y[i] = y[i-2];
                    }
                    else
                    {
                        x[i] = x[i-1];
                        y[i] = y[i-1];
                    }
                }
            }

            // der letzte Punkt muß eine Brücke zum ersten schlagen
            // wenn aber der letzte Weg sozusagen um den Innenkreis herum verlief
            // wird dieser eventuell umgefärbt (nicht bei gezogenem Polygon)
            x[dim[auswahl]-1] = KrAradius+xabstand;
            y[dim[auswahl]-1] = y[dim[auswahl]-2] < KrAradius+yabstand ? yabstand : 2*KrAradius+yabstand;

            farbeSetzen(g,2);
            // Außen- und Innenkreis
            //g.drawOval(xabstand,yabstand,2*KrAradius,2*KrAradius);
            //g.drawOval(xabstand+KrAradius-KrIradius,yabstand+KrAradius-KrIradius,2*KrIradius,2*KrIradius);
            if (second == 0) g.fillPolygon(x,y,dim[auswahl]);
            else g.drawPolygon(x,y,dim[auswahl]);

            // Innenkreis in Hintergrundfarbe setzen
            //g.setColor(getBackground());
            g.fillOval(xabstand+KrAradius-KrIradius,yabstand+KrAradius-KrIradius,2*KrIradius,2*KrIradius);
        }
        else if (auswahl == 5)
        {
            // Apfelmännchen

            // die Funktion lautet 'z^2+c'
            // wo c den Bereich durchläuft
            // und z iteriert wird
            qrzf = rzf*rzf;
            qizf = izf*izf;

            // eigentlich nur im Randmodus nötig
            // Compiler meckert aber sonst
            punktfarbe = new int[iend+1][jend+1];

            if(neuoderalt == 3)
            {
                // das Original-Apfelmännchen
                crmgrenzeAlt = crmgrenze;
                crpgrenzeAlt = crpgrenze;
                cimgrenzeAlt = cimgrenze;
                cipgrenzeAlt = cipgrenze;
                crstepAlt = crstep;
                cistepAlt = cistep;

                if(exponent == 2)
                {
                    crmgrenze = -2.1;
                    crpgrenze = 0.7;
                    cimgrenze = -1.2375;
                    cipgrenze = 1.2375;
                }
                else if(exponent == 3)
                {
                    crmgrenze = -1.35;
                    crpgrenze = 1.25;
                    cimgrenze = -1.4;
                    cipgrenze = 1.4;
                }
                else
                {
                    crmgrenze = -1.45;
                    crpgrenze = 1.15;
                    cimgrenze = -1.1;
                    cipgrenze = 1.1;
                }
                if((exponent == 4) || (exponent == 6) || (exponent == 7))
                {
                    cimgrenze = -1.2;
                    cipgrenze = 1.2;
                }
                crstep = (crpgrenze-crmgrenze)/(iend+1);
                cistep = (cipgrenze-cimgrenze)/(jend+1);
                schwarzgrenze = 200;

                // Vorgabefarbe im 'Mittelpunkt'
                //punktfarbe[250][50] = schwarzgrenze;
            }
            else if(neuoderalt == 2)
            {
                // ganz neuen Bereich festlegen
                crmgrenzeAlt = crmgrenze;
                crpgrenzeAlt = crpgrenze;
                cimgrenzeAlt = cimgrenze;
                cipgrenzeAlt = cipgrenze;
                crstepAlt = crstep;
                cistepAlt = cistep;

                double innercr = crmgrenze+ineu*crstep;
                double innerci = cimgrenze+jneu*cistep;
                double intervall = (crpgrenze-crmgrenze)/zoom;
                crmgrenze = innercr - intervall;
                crpgrenze = innercr + intervall;
                intervall = (cipgrenze-cimgrenze)/zoom;
                cimgrenze = innerci - intervall;
                cipgrenze = innerci + intervall;
                crstep = (crpgrenze-crmgrenze)/(iend+1);
                cistep = (cipgrenze-cimgrenze)/(jend+1);

                schwarzgrenze += 2*zoom;
            }
            else if(neuoderalt == 0)
            {
                // alten Bereich wieder festlegen
                crmgrenze = crmgrenzeAlt;
                crpgrenze = crpgrenzeAlt;
                cimgrenze = cimgrenzeAlt;
                cipgrenze = cipgrenzeAlt;
                crstep = crstepAlt;
                cistep = cistepAlt;

                schwarzgrenze -= 2*zoom;
            }
            // in allen anderen Fällen
            // letzten Bereich nochmals zeichnen

            textfield[vonkomponente[5]].setText(""+schwarzgrenze);

            farbe = ((int)(rand.nextDouble()*12345)) % maxfarbe;
            acolor = new Color[schwarzgrenze];
            arcolor = new Color[schwarzgrenze];
            agcolor = new Color[schwarzgrenze];
            abcolor = new Color[schwarzgrenze];
            for(int c=0;c<schwarzgrenze;++c) acolor[c] = new Color((int)(rand.nextDouble()*256),(int)(rand.nextDouble()*256),(int)(rand.nextDouble()*256));
            for(int c=0;c<schwarzgrenze;++c) arcolor[c] = new Color((int)(rand.nextDouble()*206+50),(int)(rand.nextDouble()*206+50),0);
            for(int c=0;c<schwarzgrenze;++c) agcolor[c] = new Color((int)(rand.nextDouble()*256),0,(int)(rand.nextDouble()*256));
            for(int c=0;c<schwarzgrenze;++c) abcolor[c] = new Color(0,(int)(rand.nextDouble()*206+50),(int)(rand.nextDouble()*206+50));

            g.setColor(Color.black);
            Font ffont = new Font("Tahoma",Font.PLAIN,11);
            g.setFont(ffont);
            // die Koordinaten setzen
            g.drawString("i-Wert",201,40);
            for(int i=0;i<=(iend+1)/50;++i)
            {
                String s = "   "+(i*50);
                s = s.substring(s.length()-4);
                g.drawString(s,194+i*49,55);
                g.drawString(s,194+i*49,925);
            }
            g.drawString("j-Wert",149,69);
            for(int i=0;i<=(jend+1)/50;++i)
            {
                String s = "  "+(i*50);
                s = s.substring(s.length()-3);
                g.drawString(s,178,69+i*49);
                g.drawString(s,1356,69+i*49);
            }

            if(modus.equals("P"))
            {
                // Pixelmalerei
                lupe = 0;
                for(int j=0;j<=jend;++j)
                {
                    for(int i=0;i<=iend;++i)
                    {
                        FarbeBestimmen(i,j,g,true);
                    }
                }
            }
            else
            {
                // Randabsuchen
                lupe = 0;
                int janf = 0;
                int ianf = 0;
                int i = ianf;
                int j = janf;
                int aktuellefarbe;

                if(neuoderalt == 3)
                {
                    // den inneren schwarzen Bereich vorbestimmen
                    // Voraussetzung ist hierfür,
                    // daß der Punkt (575,425) für jeden Exponenten im Schwarzbereich liegt
                    int ii = 575;
                    int jj = 425;
                    FarbeBestimmen(ii,jj,g,false);
                    while(punktfarbe[ii][jj] == schwarzgrenze)
                    {
                        // Punkte weder anzeigen noch Farbe wirklich setzen
                        punktfarbe[ii][jj] = 0;
                        --ii;
                        FarbeBestimmen(ii,jj,g,false);
                    }
                    // linker Randpunkt gefunden
                    // jetzt Rand 'abklappern'
                    SuchePunkte(ii+1,jj,schwarzgrenze,g,0);
                }

                // zur Verdeutlichung der möglichen Konstellationen
                // Grafiken mit folgenden Symbolen
                // + = gewünschte Farbe
                // - = unerwünschte Farbe
                // · = keine Farbe

                while(j <= jend)
                {
                    while(i <= iend)
                    {
                        aktuellefarbe = punktfarbe[i][j];
                        if(aktuellefarbe == 0)
                        {
                            // hier wird nichts gemalt

                            // in diesem Punkt war man noch nicht
                            // ·
                            // also zuerst Farbe des Punktes bestimmen
                            FarbeBestimmen(i,j,g,true);
                            aktuellefarbe = punktfarbe[i][j];

                            // dann das zugehörige Gebiet umkreisen

                            // Startrichtung ist Ost,
                            // d.h. von Osten wird jeder Startpunkt anfänglich erreicht
                            // Richtungsabfolge innerhalb Suche ist Ost=0 Süd=1 West=2 Nord=3 Süd...,

                            // kommt man von Westen (Richtung Ost)
                            // in einen gültigen Punkt (Punkt gleicher Farbe) wird versucht
                            // in südlicher Richtung von ihm aus weiterzusuchen
                            // kommt man von Norden (Richtung Süd) wird versucht
                            // in westlicher Richtung weiterzusuchen
                            // d.h. die Richtungssuche in gültigen Punkten läuft im Uhrzeigersinn

                            // aber kommt man von irgendeiner Richtung X
                            // in einen ungültigen Punkt (Punkt anderer Farbe oder außerhalb Bereichs)
                            // wird vom gültigen Vorgängerpunkt aus in der Richtung weitergesucht,
                            // die gegen den Uhrzeigersinn nach X kommt

                            SuchePunkte(i,j,aktuellefarbe,g,j);
                        }
                        else if(aktuellefarbe < 0)
                        {
                            // hier wird nichts gemalt

                            // in diesem Punkt war man
                            // +
                            // aber man hat noch nicht von hier aus gesucht
                            // Farbe richtigstellen
                            punktfarbe[i][j] = -aktuellefarbe;

                            // nun Suche wie oben
                            SuchePunkte(i,j,-aktuellefarbe,g,j);
                        }
                        else
                        {
                            // hier wird eventuell gemalt

                            // dieser Punkt hat schon eine Farbe
                            // also nachfolgende Punkte testen
                            if(i == iend)
                            {
                                // Rand erreicht
                                // Wechsel in neue Zeile vorbereiten
                                ++i;
                            }
                            else if(punktfarbe[i+1][j] == 0)
                            {
                                // nächster Punkt hat keine Farbe
                                // +·
                                // also rechten Randpunkt in dieser Farbe suchen
                                int ii=i;
                                do ++ii;
                                while((ii < iend) && (punktfarbe[ii][j] == 0));
                                if(punktfarbe[ii][j] == aktuellefarbe)
                                {
                                    // Farbrandpunkt gefunden
                                    // +··········+
                                    // nun testen,
                                    // ob Punkt rechts vom linken Randpunkt aber auch diese Farbe hat
                                    FarbeBestimmen(i+1,j,g,true);
                                    if(punktfarbe[i+1][j] == aktuellefarbe)
                                    {
                                        // ++·········+
                                        // diese Linie kann also auf jeden Fall gezogen werden

                                        // nun noch testen, ob rechts vom rechten Randpunkt
                                        // auch ein gleichfarbiger Punkt sitzt
                                        // das wäre dann die Sonderkonstellation
                                        // ++·········++······
                                        // in dem Fall wird der rechte Randpunkt wieder Startpunkt
                                        // im nächsten Durchlauf

                                        // Test auf Sonderkonstellation
                                        if(ii < iend)
                                        {
                                            // der rechte Randpunkt ist nicht genereller Randpunkt

                                            boolean neuefarbe = false;
                                            if(punktfarbe[ii+1][j] == 0)
                                            {
                                                FarbeBestimmen(ii+1,j,g,true);
                                                neuefarbe = true;
                                            }
                                            // Sonderkonstellation
                                            // ++·········++······
                                            // eingetreten oder
                                            if(punktfarbe[ii+1][j] != aktuellefarbe)
                                            {
                                                // rechter Randpunkt ist echter Farbrandpunkt
                                                // ++·········+-······
                                                // Farbe rechts negieren, falls sie noch nicht vorlag
                                                if(neuefarbe) punktfarbe[ii+1][j] = -punktfarbe[ii+1][j];
                                            }
                                        }

                                        LinieZiehen(aktuellefarbe,i,ii,j,g);
                                        i = ii+1;
                                    }
                                    else
                                    {
                                        // aber nächster Punkt hat eine andere Farbe
                                        // +-········+
                                        // Farbe rechts negieren
                                        punktfarbe[i+1][j] = -punktfarbe[i+1][j];
                                        ++i;
                                    }
                                }
                                else
                                {
                                    // Gebiet rechts davon noch nicht erforscht
                                    // +·········-
                                    // oder
                                    // +··········Rand
                                    ++i;
                                }
                            }
                            else if(punktfarbe[i+1][j] != aktuellefarbe)
                            {
                                // nächster Punkt hat eine andere Farbe
                                // +-
                                ++i;
                            }
                            else
                            {
                                // nächster Punkt hat gleiche Farbe
                                // ++
                                // also weitermachen
                                // bis zum letzten nächsten Punkt mit gleicher Farbe
                                ++i;
                            }
                        }
                    }
                    i = ianf;
                    ++j;
                }
            }

            textfield[vonkomponente[5]+3].setText(""+ineu);
            textfield[vonkomponente[5]+4].setText(""+jneu);

            modus = "R";
            textfield[vonkomponente[5]+6].setText(modus);

        }
        else if (auswahl == 6)
        {
            // Schneeflocke
            int xmitte = 400;
            int ymitte = 270;
            int tiefenzaehler = 0;

            if(second == 0) realetiefe = schneetiefe;
            else realetiefe = schneetiefe / 2;

            // Zufallsauswahl: Teilung zwischen 3 und 5
            int teilung = (int)(rand.nextDouble()*3+3);
            int halbkante = schneekante / 2;
            halbkante = (int)(3 * halbkante / teilung);

            // Parameter sind
            // Startpunkt x und y
            // die Richtung (0,1,2 oder 3 für ost, süd, west oder nord)
            // die Länge der Seite
            // die Rechentiefe
            // das Grafikobjekt
            // die Teilung
            // Richtung
            SeiteBauen(xmitte-halbkante,ymitte-halbkante,0,2*halbkante,tiefenzaehler,g,teilung);
            SeiteBauen(xmitte+halbkante,ymitte-halbkante,1,2*halbkante,tiefenzaehler,g,teilung);
            SeiteBauen(xmitte+halbkante,ymitte+halbkante,2,2*halbkante,tiefenzaehler,g,teilung);
            SeiteBauen(xmitte-halbkante,ymitte+halbkante,3,2*halbkante,tiefenzaehler,g,teilung);
            // Gegenrichtung
            SeiteBauen(xmitte+halbkante,ymitte-halbkante,2,2*halbkante,tiefenzaehler,g,teilung);
            SeiteBauen(xmitte+halbkante,ymitte+halbkante,3,2*halbkante,tiefenzaehler,g,teilung);
            SeiteBauen(xmitte-halbkante,ymitte+halbkante,0,2*halbkante,tiefenzaehler,g,teilung);
            SeiteBauen(xmitte-halbkante,ymitte-halbkante,1,2*halbkante,tiefenzaehler,g,teilung);
        }
        else if (auswahl == 7)
        {
            if(second == 0) realetiefe = dreitiefe;
            else realetiefe = dreitiefe / 2;

            // Schneeflocke mit Dreiecken
            int tiefenzaehler = 0;

            // Zufallsauswahl: Teilung zwischen 3 und 8
            int teilung = (int)(rand.nextDouble()*6+3);

            // und die Malgrundfarbe
            double rr = rand.nextDouble();
            dfarbe = rr > (1.0/3.0) ? 12 : 11 ;
            dfarbe = rr > (2.0/3.0) ? 13 : dfarbe ;

            // die Seitenlänge der aufgesetzten Dreiecke
            do faktor = rand.nextDouble(); while((faktor < 0.4) || (faktor > 0.8));

            // Parameter sind
            // Startpunkt x und y
            // die Richtung als trigonometrischer Winkel
            // die Länge der Kante
            // die Rechentiefe
            // die Teilung
            // die Färbung
            double idkante = dreikante / Math.sqrt(3);
            DreiSeiteBauen(Math.cos(7*Math.PI/6.0)*idkante,Math.sin(7*Math.PI/6.0)*idkante,Math.PI/3.0,dreikante,tiefenzaehler,g,teilung);
            DreiSeiteBauen(Math.cos(Math.PI/2.0)*idkante,Math.sin(Math.PI/2.0)*idkante,5*Math.PI/3.0,dreikante,tiefenzaehler,g,teilung);
            DreiSeiteBauen(Math.cos(11*Math.PI/6.0)*idkante,Math.sin(11*Math.PI/6.0)*idkante,Math.PI,dreikante,tiefenzaehler,g,teilung);
            if(second >= 2)
            {
                // Gegenrichtung
                DreiSeiteBauen(Math.cos(7*Math.PI/6.0)*idkante,Math.sin(7*Math.PI/6.0)*idkante,0,dreikante,tiefenzaehler,g,teilung);
                DreiSeiteBauen(Math.cos(Math.PI/2.0)*idkante,Math.sin(Math.PI/2.0)*idkante,4*Math.PI/3.0,dreikante,tiefenzaehler,g,teilung);
                DreiSeiteBauen(Math.cos(11*Math.PI/6.0)*idkante,Math.sin(11*Math.PI/6.0)*idkante,2*Math.PI/3.0,dreikante,tiefenzaehler,g,teilung);
            }
        }
    }


    public void FarbeBestimmen(int i,int j,Graphics G,boolean malen)
    {
        // Imaginärteil von c erstellen
        double ic = cimgrenze+j*cistep;
        double rc = crmgrenze+i*crstep;
        rausbei = 0;
        double rz = rzf;
        double iz = izf;
        double qrz = qrzf;
        double qiz = qizf;
        double rzh;
        double izh;
        do
        {
            // neuen Punkt erstellen
            // und 'rausbei' hochzählen
            ++rausbei;
            // quadratrischer Exponent wird vorberechnet
            rzh = qrz-qiz;
            izh = 2*rz*iz;
            // kubischer Exponent explizit
            //rz = rz*(qrz-3*qiz)+rc;
            //iz = iz*(3*qrz-qiz)+ic;
            for(int ii=3;ii<=exponent;++ii)
            {
                double rzhh = rzh*rz-izh*iz;
                izh = rzh*iz+izh*rz;
                rzh = rzhh;
            }
            rz = rzh+rc;
            iz = izh+ic;
            qrz = rz*rz;
            qiz = iz*iz;
        }
        while((rausbei < schwarzgrenze) && (qrz+qiz < grenzqradius));

        if((rausbei > schwarzgrenze-5) && (rausbei < schwarzgrenze))
        {
            if((rand.nextDouble() > 0.9) && (lupe < 10))
            {
                ineu = i;
                jneu = j;
                ++lupe;
            }
        }

        // im Punkt- und Randmodus werden die Pixel gesetzt
        // aber nicht beim Suchen im Randmodus
        if(malen) LinieZiehen(rausbei,i,i,j,G);

        // für den Randmodus zusätzlich der Arraywert
        punktfarbe[i][j] = rausbei;
    }

    public void SuchePunkte(int akti,int aktj,int aktf,Graphics G,int jrand)
    {
        int pfn;

        int spi = akti;
        int spj = aktj;
        int spr = 0;
        boolean abbruch = false;

        while((!abbruch) || (akti != spi) || (aktj != spj))
        {
            // kein Abbruch am Anfang, da ja der Startpunkt der Ausgangspunkt ist
            // und ein Abbruch erst beim Eintreffen in Startpunkt stattfindet
            abbruch = true;

            // nächste Richtung, ausgehend von gültigem Punkt,
            // in der neuer Punkt gesucht wird
            int sprn = (spr+1) % 4;

            // nächster Punkt in dieser Richtung
            int spin = spi - ((sprn-1) % 2);
            int spjn = spj - ((sprn-2) % 2);

            // neue Richtung für Weitersuche in gültigem Punkt
            // aber ausgehend von einem ungültigen Punkt
            // aus dieser wird dann die neue Richtung gegen Uhrzeigersinn
            int spru = (sprn+2) % 4;

            if((spin < 0) | (spin > iend) | (spjn < jrand) | (spjn > jend))
            {
                // neuer Punkt ist außerhalb des Suchbereichs
                // also zum Vorgänger zurück
                // und dort in Richtung gegen Uhrzeigersinn weitersuchen
                spr = spru;
            }
            else
            {
                // Punkt ist innerhalb des Suchbereichs

                if((akti != spin) || (aktj != spjn))
                {
                    // Ausgangspunkt wurde noch nicht erreicht
                    // also weitersuchen
                    pfn = Math.abs(punktfarbe[spin][spjn]);
                    if(pfn == 0)
                    {
                        // Punkt ist unbekannt
                        FarbeBestimmen(spin,spjn,G,true);
                        pfn = punktfarbe[spin][spjn];
                        if(pfn == aktf)
                        {
                            // neuer Punkt hat gewünschte Farbe
                            if((spin < akti) && (spjn == aktj))
                            {
                                // aber liegt vor aktuellem Startpunkt
                                // man ist auf einen schon durchlaufenen Punktsuchpfad gestoßen
                                // also Abbruch
                                spi = akti;
                                spj = aktj;
                            }
                            else
                            {
                                // sonst weiter
                                spi = spin;
                                spj = spjn;
                                spr = sprn;
                            }
                        }
                        else
                        {
                            // neuer Punkt mit anderer Farbe, also ungültiger Punkt
                            // diese Farbe negieren, da durch Punkt noch kein Suchpfad lief
                            punktfarbe[spin][spjn] = -pfn;

                            // also zum Vorgänger zurück und dort in nächster Richtung weitersuchen
                            spr = spru;
                        }
                    }
                    else if(pfn == aktf)
                    {
                        // Punkt ist bekannt und hat gleiche Farbe
                        // man ist irgendwie auf dem Rückweg
                        // bzw. vorhergehende Suche hatte diesen Punkt schon negativ markiert
                        // auf jeden Fall Farbwert positiv setzen
                        punktfarbe[spin][spjn] = pfn;

                        if((spin < akti) && (spjn == aktj))
                        {
                            // aber liegt vor aktuellem Startpunkt
                            // man ist auf einen schon durchlaufenen Punktsuchpfad gestoßen
                            // also Abbruch
                            spi = akti;
                            spj = aktj;
                        }
                        else
                        {
                            // sonst weiter
                            spi = spin;
                            spj = spjn;
                            spr = sprn;
                        }

                        // also von hier weiter
                        //spi = spin;
                        //spj = spjn;
                        //spr = sprn;
                    }
                    else
                    {
                        // Punkt bekannt mit anderer Farbe, also ungültiger Punkt
                        // also zum Vorgänger zurück und dort in nächster Richtung weitersuchen
                        spr = spru;
                    }
                }
                else
                {
                    // Abbruch
                    spi = akti;
                    spj = aktj;
                }
            }
        }
    }

    public void LinieZiehen(int aktf,int anf,int end,int zeile,Graphics G)
    {
        if(aktf == schwarzgrenze) G.setColor(new Color(0,0,0));
        else
        {
            if(farbe == 0) G.setColor(arcolor[aktf]);
            else if(farbe == 1) G.setColor(agcolor[aktf]);
            else if(farbe == 2) G.setColor(abcolor[aktf]);
            else G.setColor(acolor[aktf]);
        }
        G.drawLine(anf+apfelxabstand,zeile+apfelyabstand,end+apfelxabstand,zeile+apfelyabstand);
    }

    public void SeiteBauen(int sx,int sy,int sr,int sl,int stz,Graphics G,int teilung)
    {
        int sxa = 0;
        int sya = 0;
        int sxag = 0;
        int syag = 0;
        int sxe = 0;
        int sye = 0;
        int sl3 = 0;
        int sl3m = 0;

        if(stz == realetiefe)
        {
            // Maximaltiefe erreicht, also Kante setzen
            G.setColor(Color.white);
            sxe = sx-((sr-1)%2)*sl;
            sye = sy-((sr-2)%2)*sl;
            G.drawLine(sx,sy,sxe,sye);
        }
        else
        {
            // Maximaltiefe noch nicht erreicht, also rekursiv weiter

            // Seitenkanten sind hier Drittelstrecken oder beliebige Strecken
            // die zwei Außenstücke sind gleichlang
            // das Mittelstück immer das längere
            sl3 = (int)(sl / teilung);
            sl3m = sl - 2 * sl3;
            if(sl3m > sl3)
            {
                sl3 = ++sl3;
                sl3m -= 2;
            }

            if(second == 0)
            {
                // die zwei Kantenstücke, die gezeichnet werden
                G.setColor(Color.white);
                sxe = sx-((sr-1)%2)*sl3;
                sye = sy-((sr-2)%2)*sl3;
                G.drawLine(sx,sy,sxe,sye);
                G.setColor(Color.white);
                sxa = sx-((sr-1)%2)*(sl3+sl3m);
                sya = sy-((sr-2)%2)*(sl3+sl3m);
                sxe = sxa-((sr-1)%2)*sl3;
                sye = sya-((sr-2)%2)*sl3;
                G.drawLine(sxa,sya,sxe,sye);
            }
            else
            {
                // die zwei Kantenstücke werden auch gedrittelt
                SeiteBauen(sx,sy,sr,sl3,stz+1,G,teilung);
                // Gegenrichtung
                sxag = sx-((sr-1)%2)*sl3;
                syag = sy-((sr-2)%2)*sl3;
                SeiteBauen(sxag,syag,(sr+2)%4,sl3,stz+1,G,teilung);

                sxa = sx-((sr-1)%2)*(sl3+sl3m);
                sya = sy-((sr-2)%2)*(sl3+sl3m);
                SeiteBauen(sxa,sya,sr,sl3,stz+1,G,teilung);
                // Gegenrichtung
                sxag = sxa-((sr-1)%2)*sl3;
                syag = sya-((sr-2)%2)*sl3;
                SeiteBauen(sxag,syag,(sr+2)%4,sl3,stz+1,G,teilung);
            }
            // die Mittelkante, die zu drei neuen Kanten wird
            sxa = sx-((sr-1)%2)*sl3;
            sya = sy-((sr-2)%2)*sl3;
            SeiteBauen(sxa,sya,(sr+3)%4,sl3m,stz+1,G,teilung);
            // Gegenrichtung
            sxag = sxa-((((sr+3)%4)-1)%2)*sl3m;
            syag = sya-((((sr+3)%4)-2)%2)*sl3m;
            SeiteBauen(sxag,syag,(sr+1)%4,sl3m,stz+1,G,teilung);

            sxa = sxa-((((sr+3)%4)-1)%2)*sl3m;
            sya = sya-((((sr+3)%4)-2)%2)*sl3m;
            SeiteBauen(sxa,sya,sr,sl3m,stz+1,G,teilung);
            // Gegenrichtung
            sxag = sxa-((sr-1)%2)*sl3m;
            syag = sya-((sr-2)%2)*sl3m;
            SeiteBauen(sxag,syag,(sr+2)%4,sl3m,stz+1,G,teilung);

            sxa = sxa-((sr-1)%2)*sl3m;
            sya = sya-((sr-2)%2)*sl3m;
            SeiteBauen(sxa,sya,(sr+1)%4,sl3m,stz+1,G,teilung);
            // Gegenrichtung
            sxag = sxa-((((sr+1)%4)-1)%2)*sl3m;
            syag = sya-((((sr+1)%4)-2)%2)*sl3m;
            SeiteBauen(sxag,syag,(sr+3)%4,sl3m,stz+1,G,teilung);
        }
    }

    public void DreiSeiteBauen(double sx,double sy,double alpha,double sl,int stz,Graphics G,int teilung)
    {
        double sxa = 0;
        double sya = 0;
        double sxe = 0;
        double sye = 0;
        double sl3 = 0;
        double sl3m = 0;

        if(stz == realetiefe)
        {
            // Maximaltiefe erreicht, also Kante setzen
            farbeSetzen(G,dfarbe);
            sxe = sx+Math.cos(alpha)*sl;
            sye = sy+Math.sin(alpha)*sl;
            G.drawLine(dxmitte+(int)sx,dymitte-(int)sy,dxmitte+(int)sxe,dymitte-(int)sye);
        }
        else
        {
            // Maximaltiefe noch nicht erreicht, also rekursiv weiter

            // Seitenkanten sind hier Drittelstrecken oder beliebige Strecken
            // die zwei Außenstücke sind gleichlang
            // das Mittelstück immer das längere
            sl3 = sl / teilung;
            sl3m = sl - 2 * sl3;
            if(sl3m > sl3)
            {
                sl3 = ++sl3;
                sl3m -= 2;
            }

            if((second % 2) == 0)
            {
                // zwei Kantenstücke, die gezeichnet werden
                farbeSetzen(G,dfarbe);
                sxe = sx+Math.cos(alpha)*sl3;
                sye = sy+Math.sin(alpha)*sl3;
                G.drawLine(dxmitte+(int)sx,dymitte-(int)sy,dxmitte+(int)sxe,dymitte-(int)sye);
                farbeSetzen(G,dfarbe);
                sxa = sx + Math.cos(alpha)*(sl3+sl3m);
                sya = sy + Math.sin(alpha)*(sl3+sl3m);
                sxe = sx + Math.cos(alpha)*sl;
                sye = sy + Math.sin(alpha)*sl;
                G.drawLine(dxmitte+(int)sxa,dymitte-(int)sya,dxmitte+(int)sxe,dymitte-(int)sye);
            }
            else
            {
                // auch mit Dreieck besetzen
                DreiSeiteBauen(sx,sy,alpha,sl3,stz+1,G,teilung);
                sxa = sx + Math.cos(alpha)*(sl3+sl3m);
                sya = sy + Math.sin(alpha)*(sl3+sl3m);
                DreiSeiteBauen(sxa,sya,alpha,sl3,stz+1,G,teilung);
            }
            // zwei neue Dreieckseiten
            sxa = sx + Math.cos(alpha)*sl3;
            sya = sy + Math.sin(alpha)*sl3;

            // 'acos'-Argument muß im Definitionsbereich bleiben
            double argument = sl3m/(sl*2*faktor);
            argument = argument < -1 ? -1 : argument;
            argument = argument > 1 ? 1 : argument;
            double alphax = Math.acos(argument);
            double alphaneu = (alpha+alphax) % (2*Math.PI);
            DreiSeiteBauen(sxa,sya,alphaneu,faktor*sl,stz+1,G,teilung);
            sxa += Math.cos(alphaneu)*faktor*sl;
            sya += Math.sin(alphaneu)*faktor*sl;
            alphaneu = (alpha+2*Math.PI-alphax) % (2*Math.PI);
            DreiSeiteBauen(sxa,sya,alphaneu,faktor*sl,stz+1,G,teilung);
        }
    }

    public void farbeSetzen(Graphics G,int i)
    {
        // Hintergrund-, Vordergrund- und Farbe an sich setzen
        int rAnteil = (int)(rand.nextDouble()*200+55);
        int gAnteil = (int)(rand.nextDouble()*200+55);
        int bAnteil = (int)(rand.nextDouble()*200+55);
        /*if(i == 0) setBackground(new Color(rAnteil,gAnteil,bAnteil));
        else if(i == 1) setForeground(new Color((int)(rAnteil/10),(int)(gAnteil/10),(int)(bAnteil/10)));
        else if(i == 2) G.setColor(new Color(rAnteil,gAnteil,bAnteil));
        else if(i == 11) G.setColor(new Color((int)(rand.nextDouble()*150+105),(int)(rand.nextDouble()*150+105),0));
        else if(i == 12) G.setColor(new Color((int)(rand.nextDouble()*150+105),0,(int)(rand.nextDouble()*150+105)));
        else if(i == 13) G.setColor(new Color(0,(int)(rand.nextDouble()*150+105),(int)(rand.nextDouble()*150+105)));*/
    }

    class ItemAbfrager implements ItemListener
    {
        public void itemStateChanged(ItemEvent ie)
        {
            if(ie.getSource() == chx)
            {
                int alteauswahl = auswahl;

                if(chx.getSelectedItem().equals("rechteck")) auswahl = 1;
                else if(chx.getSelectedItem().equals("kreis")) auswahl = 2;
                else if(chx.getSelectedItem().equals("dreieck")) auswahl = 3;
                else if(chx.getSelectedItem().equals("kreisring"))
                {
                    auswahl = 4;
                    second = 0;
                }
                else if(chx.getSelectedItem().equals("kreisring 2"))
                {
                    auswahl = 4;
                    second = 1;
                }
                else if(chx.getSelectedItem().equals("apfelmann"))
                {
                    auswahl = 5;
                    neuoderalt = 3;
                    textfield[vonkomponente[5]+3].setText("original");
                    textfield[vonkomponente[5]+4].setText("original");
                }
                else if(chx.getSelectedItem().equals("schneeflocke"))
                {
                    auswahl = 6;
                    second = 0;
                }
                else if(chx.getSelectedItem().equals("schneedeckchen"))
                {
                    auswahl = 6;
                    second = 1;
                }
                else if(chx.getSelectedItem().equals("dreieckchen"))
                {
                    auswahl = 7;
                    second = 0;
                }
                else if(chx.getSelectedItem().equals("dreieckchen +"))
                {
                    auswahl = 7;
                    second = 1;
                }
                else if(chx.getSelectedItem().equals("dreieckchen i"))
                {
                    auswahl = 7;
                    second = 2;
                }
                else if(chx.getSelectedItem().equals("dreieckchen +i"))
                {
                    auswahl = 7;
                    second = 3;
                }

                if(auswahl != alteauswahl)
                {
                    // alte Komponenten ausblenden
                    for(int i=vonkomponente[alteauswahl];i<=biskomponente[alteauswahl];++i)
                    {
                        label[i].setVisible(false);
                        textfield[i].setVisible(false);
                    }
                    // es existiert immer nur ein Button
                    button[vonkomponente[alteauswahl]].setVisible(false);

                    // und neue Komponenten einblenden
                    for(int i=vonkomponente[auswahl];i<=biskomponente[auswahl];++i)
                    {
                        label[i].setVisible(true);
                        textfield[i].setVisible(true);
                    }
                    // es existiert immer nur ein Button
                    button[vonkomponente[auswahl]].setVisible(true);
                }

                farbeSetzen(G,1);
                /*if(auswahl == 7) setBackground(Color.white);
                else farbeSetzen(G,0);*/
            }
        }
    }

    public class KnopfAbfrage implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            for(int i=1;i<=4;++i)
            {
                if(ae.getSource() == button[i])
                {
                    dim[i] = (Double.valueOf(textfield[i].getText())).intValue();
                    dim[i] = dim[i] < 4 ? 3 : dim[i];
                    dim[i] = dim[i] > 3000 ? 3000 : dim[i];
                    textfield[i].setText(""+dim[i]);
                }
            }
            if(ae.getSource() == button[4])
            {
                // Extraabfrage für Kreisring-Innenradius
                KrIradius = (Double.valueOf(textfield[vonkomponente[4]+1].getText())).intValue();
                KrIradius = KrIradius < 10 ? 10 : KrIradius;
                KrIradius = KrIradius > KrAradius-4 ? KrAradius-4 : KrIradius;
                textfield[vonkomponente[4]+1].setText(""+KrIradius);
            }
            else if(ae.getSource() == button[vonkomponente[5]])
            {
                schwarzgrenze = (Double.valueOf(textfield[vonkomponente[5]].getText())).intValue();
                schwarzgrenze = schwarzgrenze < 30 ? 30 : schwarzgrenze;
                schwarzgrenze = schwarzgrenze > 2000 ? 2000 : schwarzgrenze;
                textfield[vonkomponente[5]].setText(""+schwarzgrenze);

                rzf = (Double.valueOf(textfield[vonkomponente[5]+1].getText())).doubleValue();
                rzf = rzf < -0.9 ? -0.9 : rzf;
                rzf = rzf > 0.9 ? 0.9 : rzf;
                textfield[vonkomponente[5]+1].setText(""+rzf);

                izf = (Double.valueOf(textfield[vonkomponente[5]+2].getText())).doubleValue();
                izf = izf < -0.9 ? -0.9 : izf;
                izf = izf > 0.9 ? 0.9 : izf;
                textfield[vonkomponente[5]+2].setText(""+izf);

                ineu = (Double.valueOf(textfield[vonkomponente[5]+3].getText())).intValue();
                jneu = (Double.valueOf(textfield[vonkomponente[5]+4].getText())).intValue();
                if((ineu < 0) && (jneu < 0))
                {
                    // zurück zu alten Werten
                    neuoderalt = 0;
                }
                else if((ineu >= 0) && (jneu >= 0))
                {
                    ineu = ineu < 0 ? 0 : ineu;
                    ineu = ineu > iend ? iend : ineu;
                    jneu = jneu < 0 ? 0 : jneu;
                    jneu = jneu > jend ? jend : jneu;
                    neuoderalt = 2;
                    textfield[vonkomponente[5]+3].setText(""+ineu);
                    textfield[vonkomponente[5]+4].setText(""+jneu);
                }
                else
                {
                    neuoderalt = 1;
                }

                zoom = (Double.valueOf(textfield[vonkomponente[5]+5].getText())).intValue();
                zoom = zoom < 2 ? 2 : zoom;
                zoom = zoom > 20 ? 20 : zoom;
                textfield[vonkomponente[5]+5].setText(""+zoom);

                modus = textfield[vonkomponente[5]+6].getText().toUpperCase();
                modus = modus.equals("P") ? modus : "R";
                textfield[vonkomponente[5]+6].setText(modus);

                int p = exponent;
                exponent = (Double.valueOf(textfield[vonkomponente[5]+7].getText())).intValue();
                exponent = exponent < 2 ? 2 : exponent;
                exponent = exponent > 20 ? 20 : exponent;
                textfield[vonkomponente[5]+7].setText(""+exponent);
                // Neuaufbau bei Exponentenwechsel
                if(p != exponent) neuoderalt = 3;
            }
            else if(ae.getSource() == button[vonkomponente[6]])
            {
                schneetiefe = (Double.valueOf(textfield[vonkomponente[6]].getText())).intValue();
                schneetiefe = schneetiefe < 0 ? 0 : schneetiefe;
                schneetiefe = schneetiefe > 12 ? 12 : schneetiefe;
                textfield[vonkomponente[6]].setText(""+schneetiefe);

                schneekante = (Double.valueOf(textfield[vonkomponente[6]+1].getText())).intValue();
                schneekante = schneekante < 10 ? 10 : schneekante;
                schneekante = schneekante > 240 ? 240 : schneekante;
                // die Kante ist immer eine 2er-Zahl
                schneekante = 2 * (schneekante / 2);
                textfield[vonkomponente[6]+1].setText(""+schneekante);
            }
            else if(ae.getSource() == button[vonkomponente[7]])
            {
                dreitiefe = (Double.valueOf(textfield[vonkomponente[7]].getText())).intValue();
                dreitiefe = dreitiefe < 0 ? 0 : dreitiefe;
                dreitiefe = dreitiefe > 14 ? 14 : dreitiefe;
                textfield[vonkomponente[7]].setText(""+dreitiefe);

                dreikante = (Double.valueOf(textfield[vonkomponente[7]+1].getText())).intValue();
                dreikante = dreikante < 10 ? 10 : dreikante;
                dreikante = dreikante > 150 ? 150 : dreikante;
                // die Kante ist immer eine 2er-Zahl
                dreikante = 2 * (dreikante / 2);
                textfield[vonkomponente[7]+1].setText(""+dreikante);
            }

            farbeSetzen(G,1);
            /*if(auswahl == 7) setBackground(Color.white);
            else farbeSetzen(G,0);*/
        }
    }

    public void main(String[] args) {

        farbeSetzen(G,1);
        /*if(auswahl == 7) setBackground(Color.white);
        else farbeSetzen(G,0);*/
        //setLayout(null);

        label = new Label[anzkomponenten];
        textfield = new TextField[anzkomponenten];
        button = new Button[anzkomponenten];
        knopfabfrage = new KnopfAbfrage[anzkomponenten];
        vonkomponente = new int[anzkomponenten];
        biskomponente = new int[anzkomponenten];
        dim = new int[5];

        label[0] = new Label("Auswahl");
        label[0].setBounds(10,10,100,20);
        //add(label[0]);

        chx = new Choice();
        chx.add("apfelmann");
        chx.add("dreieckchen");
        chx.add("dreieckchen +");
        chx.add("dreieckchen i");
        chx.add("dreieckchen +i");
        chx.add("schneeflocke");
        chx.add("schneedeckchen");
        chx.add("kreisring");
        chx.add("kreisring 2");
        chx.add("dreieck");
        chx.add("kreis");
        chx.add("rechteck");
        ItemAbfrager ihy = new ItemAbfrager();
        chx.addItemListener(ihy);
        chx.setBounds(10,35,120,20);
        //add(chx);

        // Rechteck + Kreis + Dreieck
        for(int i=1;i<=3;++i)
        {
            vonkomponente[i] = i;
            biskomponente[i] = i;
            dim[i] = 1000;
            label[i] = new Label("Kantenanzahl");
            label[i].setBounds(10,70,100,20);
            //add(label[i]);
            label[i].setVisible(false);
            textfield[i] = new TextField();
            textfield[i].setText(""+dim[i]);
            textfield[i].setBounds(10,95,120,20);
            //add(textfield[i]);
            textfield[i].setVisible(false);
            button[i] = new Button("Datenübergabe");
            knopfabfrage[i] = new KnopfAbfrage();
            button[i].addActionListener(knopfabfrage[i]);
            button[i].setBounds(10,120,120,20);
            //add(button[i]);
            button[i].setVisible(false);
        }

        // Kreisring hat Extraparameter Innenkreisradius
        vonkomponente[4] = 4;
        biskomponente[4] = 5;
        dim[4] = 1000;
        int loc = 4;

        label[vonkomponente[loc]] = new Label("Kantenanzahl");
        label[vonkomponente[loc]].setBounds(10,70,100,20);
        //add(label[vonkomponente[loc]]);
        label[vonkomponente[loc]].setVisible(false);
        textfield[vonkomponente[loc]] = new TextField();
        textfield[vonkomponente[loc]].setText(""+dim[vonkomponente[loc]]);
        textfield[vonkomponente[loc]].setBounds(10,95,120,20);
        //add(textfield[vonkomponente[loc]]);
        textfield[vonkomponente[loc]].setVisible(false);

        label[vonkomponente[loc]+1] = new Label("Innenradius");
        label[vonkomponente[loc]+1].setBounds(10,120,100,20);
        //add(label[vonkomponente[loc]+1]);
        label[vonkomponente[loc]+1].setVisible(false);
        textfield[vonkomponente[loc]+1] = new TextField();
        textfield[vonkomponente[loc]+1].setText(""+KrIradius);
        textfield[vonkomponente[loc]+1].setBounds(10,145,120,20);
        //add(textfield[vonkomponente[loc]+1]);
        textfield[vonkomponente[loc]+1].setVisible(false);

        button[vonkomponente[loc]] = new Button("Datenübergabe");
        knopfabfrage[vonkomponente[loc]] = new KnopfAbfrage();
        button[vonkomponente[loc]].addActionListener(knopfabfrage[vonkomponente[loc]]);
        button[vonkomponente[loc]].setBounds(10,170,120,20);
        //add(button[vonkomponente[loc]]);
        button[vonkomponente[loc]].setVisible(false);

        // Apfelmännchen
        vonkomponente[5] = 6;
        biskomponente[5] = 13;
        loc = 5;

        label[vonkomponente[loc]] = new Label("Schwarzgrenze");
        label[vonkomponente[loc]].setBounds(10,190,100,20);
        //add(label[vonkomponente[loc]]);
        label[vonkomponente[loc]].setVisible(false);
        textfield[vonkomponente[loc]] = new TextField();
        textfield[vonkomponente[loc]].setText(""+schwarzgrenze);
        textfield[vonkomponente[loc]].setBounds(10,215,120,20);
        //add(textfield[vonkomponente[loc]]);
        textfield[vonkomponente[loc]].setVisible(false);

        label[vonkomponente[loc]+1] = new Label("x-Wert");
        label[vonkomponente[loc]+1].setBounds(10,80,100,20);
        //add(label[vonkomponente[loc]+1]);
        label[vonkomponente[loc]+1].setVisible(false);
        textfield[vonkomponente[loc]+1] = new TextField();
        textfield[vonkomponente[loc]+1].setText(""+rzf);
        textfield[vonkomponente[loc]+1].setBounds(10,105,120,20);
        //add(textfield[vonkomponente[loc]+1]);
        textfield[vonkomponente[loc]+1].setVisible(false);

        label[vonkomponente[loc]+2] = new Label("y-Wert");
        label[vonkomponente[loc]+2].setBounds(10,130,100,20);
        //add(label[vonkomponente[loc]+2]);
        label[vonkomponente[loc]+2].setVisible(false);
        textfield[vonkomponente[loc]+2] = new TextField();
        textfield[vonkomponente[loc]+2].setText(""+izf);
        textfield[vonkomponente[loc]+2].setBounds(10,155,120,20);
        //add(textfield[vonkomponente[loc]+2]);
        textfield[vonkomponente[loc]+2].setVisible(false);

        label[vonkomponente[loc]+3] = new Label("i-Wert");
        label[vonkomponente[loc]+3].setBounds(10,310,100,20);
        //add(label[vonkomponente[loc]+3]);
        label[vonkomponente[loc]+3].setVisible(false);
        textfield[vonkomponente[loc]+3] = new TextField();
        textfield[vonkomponente[loc]+3].setText("original");
        textfield[vonkomponente[loc]+3].setBounds(10,335,120,20);
        //add(textfield[vonkomponente[loc]+3]);
        textfield[vonkomponente[loc]+3].setVisible(false);

        label[vonkomponente[loc]+4] = new Label("j-Wert");
        label[vonkomponente[loc]+4].setBounds(10,360,100,20);
        //add(label[vonkomponente[loc]+4]);
        label[vonkomponente[loc]+4].setVisible(false);
        textfield[vonkomponente[loc]+4] = new TextField();
        textfield[vonkomponente[loc]+4].setText("original");
        textfield[vonkomponente[loc]+4].setBounds(10,385,120,20);
        //add(textfield[vonkomponente[loc]+4]);
        textfield[vonkomponente[loc]+4].setVisible(false);

        label[vonkomponente[loc]+5] = new Label("Zoom");
        label[vonkomponente[loc]+5].setBounds(10,250,100,20);
        //add(label[vonkomponente[loc]+5]);
        label[vonkomponente[loc]+5].setVisible(false);
        textfield[vonkomponente[loc]+5] = new TextField();
        textfield[vonkomponente[loc]+5].setText(""+zoom);
        textfield[vonkomponente[loc]+5].setBounds(10,275,120,20);
        //add(textfield[vonkomponente[loc]+5]);
        textfield[vonkomponente[loc]+5].setVisible(false);

        button[vonkomponente[loc]] = new Button("Datenübergabe");
        knopfabfrage[vonkomponente[loc]] = new KnopfAbfrage();
        button[vonkomponente[loc]].addActionListener(knopfabfrage[vonkomponente[loc]]);
        button[vonkomponente[loc]].setBounds(10,420,120,20);
        //add(button[vonkomponente[loc]]);
        button[vonkomponente[loc]].setVisible(false);

        label[vonkomponente[loc]+6] = new Label("Rand oder Pixel");
        label[vonkomponente[loc]+6].setBounds(10,455,96,20);
        //add(label[vonkomponente[loc]+6]);
        label[vonkomponente[loc]+6].setVisible(false);
        textfield[vonkomponente[loc]+6] = new TextField();
        textfield[vonkomponente[loc]+6].setText(modus);
        textfield[vonkomponente[loc]+6].setBounds(110,455,18,20);
        //add(textfield[vonkomponente[loc]+6]);
        textfield[vonkomponente[loc]+6].setVisible(false);

        label[vonkomponente[loc]+7] = new Label("Exponent");
        label[vonkomponente[loc]+7].setBounds(10,490,91,20);
        //add(label[vonkomponente[loc]+7]);
        label[vonkomponente[loc]+7].setVisible(false);
        textfield[vonkomponente[loc]+7] = new TextField();
        textfield[vonkomponente[loc]+7].setText(""+exponent);
        textfield[vonkomponente[loc]+7].setBounds(105,490,23,20);
        //add(textfield[vonkomponente[loc]+7]);
        textfield[vonkomponente[loc]+7].setVisible(false);

        // Schneeflocke
        vonkomponente[6] = 14;
        biskomponente[6] = 15;
        loc = 6;

        label[vonkomponente[loc]] = new Label("Rechentiefe");
        label[vonkomponente[loc]].setBounds(10,70,100,20);
        //add(label[vonkomponente[loc]]);
        label[vonkomponente[loc]].setVisible(false);
        textfield[vonkomponente[loc]] = new TextField();
        textfield[vonkomponente[loc]].setText(""+schneetiefe);
        textfield[vonkomponente[loc]].setBounds(10,95,120,20);
        //add(textfield[vonkomponente[loc]]);
        textfield[vonkomponente[loc]].setVisible(false);

        label[vonkomponente[loc]+1] = new Label("Kantenlänge");
        label[vonkomponente[loc]+1].setBounds(10,120,100,20);
        //add(label[vonkomponente[loc]+1]);
        label[vonkomponente[loc]+1].setVisible(false);
        textfield[vonkomponente[loc]+1] = new TextField();
        textfield[vonkomponente[loc]+1].setText(""+schneekante);
        textfield[vonkomponente[loc]+1].setBounds(10,145,120,20);
        //add(textfield[vonkomponente[loc]+1]);
        textfield[vonkomponente[loc]+1].setVisible(false);

        button[vonkomponente[loc]] = new Button("Datenübergabe");
        knopfabfrage[vonkomponente[loc]] = new KnopfAbfrage();
        button[vonkomponente[loc]].addActionListener(knopfabfrage[vonkomponente[loc]]);
        button[vonkomponente[loc]].setBounds(10,170,120,20);
        //add(button[vonkomponente[loc]]);
        button[vonkomponente[loc]].setVisible(false);

        // Dreieckchen
        vonkomponente[7] = 16;
        biskomponente[7] = 17;
        loc = 7;

        label[vonkomponente[loc]] = new Label("Rechentiefe");
        label[vonkomponente[loc]].setBounds(10,70,100,20);
        //add(label[vonkomponente[loc]]);
        label[vonkomponente[loc]].setVisible(false);
        textfield[vonkomponente[loc]] = new TextField();
        textfield[vonkomponente[loc]].setText(""+dreitiefe);
        textfield[vonkomponente[loc]].setBounds(10,95,120,20);
        //add(textfield[vonkomponente[loc]]);
        textfield[vonkomponente[loc]].setVisible(false);

        label[vonkomponente[loc]+1] = new Label("Kantenlänge");
        label[vonkomponente[loc]+1].setBounds(10,120,100,20);
        //add(label[vonkomponente[loc]+1]);
        label[vonkomponente[loc]+1].setVisible(false);
        textfield[vonkomponente[loc]+1] = new TextField();
        textfield[vonkomponente[loc]+1].setText(""+dreikante);
        textfield[vonkomponente[loc]+1].setBounds(10,145,120,20);
        //add(textfield[vonkomponente[loc]+1]);
        textfield[vonkomponente[loc]+1].setVisible(false);

        button[vonkomponente[loc]] = new Button("Datenübergabe");
        knopfabfrage[vonkomponente[loc]] = new KnopfAbfrage();
        button[vonkomponente[loc]].addActionListener(knopfabfrage[vonkomponente[loc]]);
        button[vonkomponente[loc]].setBounds(10,170,120,20);
        //add(button[vonkomponente[loc]]);
        button[vonkomponente[loc]].setVisible(false);

        // vorausgewählte Komponenten einblenden
        for(int i=vonkomponente[auswahl];i<=biskomponente[auswahl];++i)
        {
            label[i].setVisible(true);
            textfield[i].setVisible(true);
        }
        // es existiert immer nur ein Button
        button[vonkomponente[auswahl]].setVisible(true);
    }
}