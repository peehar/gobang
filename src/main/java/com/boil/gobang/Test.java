package com.boil.gobang;

public class Test {

    public static void main(String[] args) {
        int[][] a = new int[][] {
                new int[] {0,0,0},
                new int[] {0,0,0},
                new int[] {0,0,0},
        };
        int z = 1;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                a[x][y] = z;
                z = -z;

            }
        }
    }

    private static void next() {

    }

    private static void print(int[][] a) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                System.out.print(a[x][y]);
            }
            System.out.println();
        }
    }

}
