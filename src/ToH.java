
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// TowersOfHanoi
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

import data.Timer;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

public class ToH {

    static void towersofhanoi(int slices) {

        try (Timer timer = new Timer("towersofhanoi", 3)) {
            // Code, der gemessen werden soll

            int[][] towers = new int[3][slices];

            for (int i = 0; i < slices; i++) {
                towers[0][i] = i + 1;
            }
            int[] tops = {0, slices, slices};

            //System.out.println("Size:    " + towers[0].length + " " + towers[1].length + " " + towers[2].length);
            tohShow(towers);
            tohType3(towers, tops, 0, 1, 2);
            int level = 4;
            int from = 1;
            int to = 2;
            while (tops[0] < slices) {
                tohMove(towers, tops, 0, to);
                tohTypeX(level, towers, tops, from, to, 0);
                from = 3 - from;
                to = 3 - to;
                level++;
            }
        }
    }

    static void tohShow(int[][] towers) {
        for (int i = 0; i < towers[0].length; i++) {
            System.out.println("Slices:  " + (towers[0][i] == 0 ? " " : towers[0][i]) + "       " + (towers[1][i] == 0 ? " " : towers[1][i]) + "       " + (towers[2][i] == 0 ? " " : towers[2][i]));
        }
        System.out.println();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    static void tohMove(int[][] towers, int[] tops, int i, int j) {
        tops[j]--;
        towers[j][tops[j]] = towers[i][tops[i]];
        towers[i][tops[i]] = 0;
        tops[i]++;
        tohShow(towers);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    static void tohType3(int[][] towers, int[] tops, int i, int j, int k) {
        tohMove(towers, tops, i, j);
        tohMove(towers, tops, i, k);
        tohMove(towers, tops, j, k);
        tohMove(towers, tops, i, j);
        tohMove(towers, tops, k, i);
        tohMove(towers, tops, k, j);
        tohMove(towers, tops, i, j);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    static void tohTypeX(int level, int[][] towers, int[] tops, int i, int j, int k) {
        level = level - 1;
        if (level == 3) {
            tohType3(towers, tops, i, j, k);
        } else {
            tohTypeX(level, towers, tops, i, k, j);
            tohMove(towers, tops, i, j);
            tohTypeX(level, towers, tops, k, j, i);
        }
    }

    public static void main(String[] args) {
        for (int s = 3; s <= 20; s++) {
            // für 16 Scheiben braucht es schon mehr als 4 Minuten, bei 20 mehr als 85 Minuten
            // häh?!, Java ist so viel schneller, kann nicht sein, in 18 sec 19 Scheiben? - für 20 38 sec!
            System.out.println("Towers Of Hanoi with " + s + " Slices:\n\n");
            towersofhanoi(s);
        }
    }
}
