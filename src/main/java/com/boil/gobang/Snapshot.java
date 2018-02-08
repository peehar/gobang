package com.boil.gobang;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Snapshot {

    private static class Zi {
        /**
         * 旗子几枚
         */
        public int c = 0;
        /**
         * 其左空子几个
         */
        public int lr = 0;
        /**
         * 其右空子几个
         */
        public int rr = 0;
        /**
         *
         */
    }

    private static class Dir {
        public int x;
        public int y;

        public Dir(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final Dir[] dir = new Dir[]{
            new Dir(1, 0),
            new Dir(0, 1),
            new Dir(1, 1),
            new Dir(1, -1)
    };

    private Color[][] borad = new Color[15][15];
    private int level;

    public Snapshot(Color[][] color, int level) {
        this.level = level;
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                this.borad[x][y] = color[x][y];
            }
        }
    }

    public Snapshot() {
        this.level = 0;
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                this.borad[x][y] = Color.Nul;
            }
        }

        borad[7][3] = borad[7][3] = borad[7][3] = Color.Black;
//        borad[7][5] = borad[7][7] = Color.White;

    }

    public Snapshot clone() {
        return new Snapshot(this.borad, this.level + 1);
    }

    public Color[][] getcolor() {
        return borad;
    }


    public void play(int x, int y, Color color) {
        this.borad[x][y] = color;
    }

    private int evalDeep(int x, int y, Color color) {
        for (Dir d : dir) {
            for (int i = -1; i <=1; i += 2) {
                for (int k = 1; k < 5; k++) {
                    int tx = x + d.x * k * i;
                    int ty = y + d.y * k * i;

                    int s;
                    Snapshot ss = this.clone();
                    if (level == 4) {
                        int ns = evalComplex(tx, ty);
                        int as = ss.evalDeep(tx, ty, color == Color.Black ? Color.White : Color.Black);
                        s = ns > as ? ns : as;
                    } else if (level == 5) {
                        s = evalComplex(tx, ty);
                    } else {
                        ss.play(tx, ty, color == Color.Black ? Color.White : Color.Black);
                    }
                }
            }
        }
        return 0;
    }

    private int evalComplex(int x, int y) {
        // 如果一次着子形成了n个棋形，那么分开着子的话，这次着子就相当于n次着子，
        // 也就是说，去掉最大分支的方向（因为这个方向会被堵），其他方向上的各挣了一次着子的机会，所以其他方向加1，
        // 然后算出所有方向中的最大值，就是这次着子的分数

        List<LinkedList<Zi>> zis = new ArrayList<LinkedList<Zi>>();
        for (Dir d : dir) {
            LinkedList<Zi> zi = fetch(x, y, d);
            zis.add(zi);
        }

        int[] cs = new int[4];
        int index = 0;
        int maxIndex = 0;
        int max = -5;
        for (LinkedList<Zi> zi : zis) {
            int c = evalLine(zi);
            cs[index] = c;
            if (c > max) {
                max = c;
                maxIndex = index;
            }
            index++;
        }

        for (int i = 0; i < index; i++) {
            if (i != maxIndex && cs[i] > 1) {
                cs[i]++;
            }
        }

        max = -5;
        for (int i = 0; i < index; i++) {
            int c = cs[i];
            if (c > max) {
                max = c;
            }
        }

        return max;
    }

    private int evalLine(LinkedList<Zi> zi) {
        // 首先计算生存空间，如果小于5返回最低分
        int count = 0;
        int room = 0;
        Iterator<Zi> iter = zi.iterator();
        Zi z = iter.next();
        count += z.c;
        room += z.lr + z.c;
        while (iter.hasNext()) {
            z = iter.next();
            count += z.c;
            room += z.lr + z.c;
        }
        room += z.rr;

        if (room < 5) {
            return 0;
        }

        //如果有阻塞，减棋子
        Zi f = zi.getFirst();
        if (f.lr == 0) {
            count--;
        }
        Zi l = zi.getLast();
        if (l.rr == 0) {
            count--;
        }

        return count;
    }

    private double next(int x, int y, final Color color) {
        double max = 0;

//        for (int[] ints : dir) {
//            for (int k = 0; k < 8; k++) {
//                final int tx = x - ints[0] * 4 + k * ints[0];
//                final int ty = y - ints[1] * 4 + k * ints[1];
//                if (tx >= 0 && tx < 15 && ty >= 0 && ty < 15 && this.color[tx][ty] == null) {
//                    Runnable r = new Runnable() {
//                        public void run() {
//                            Snapshot snapshot = new Snapshot(Snapshot.this.color, level + 1);
//                            Color z = color == Color.White ? Color.Black : Color.White;
//                            double v = snapshot.calculate(tx, ty, z);
////                            if (max < v) {
////                                max = v;
////                            }
////                            System.out.println(Thread.currentThread().getName() + ": " + v);
//                        }
//                    };
////                    r.run();
//                    try {
//                        put(r);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }

//        for (int x = 0; x < 15; x++) {
//            for (int y = 0; y < 15; y++) {
//                if (this.color[x][y] == null) {
//                    Snapshot snapshot = new Snapshot(this.color, level + 1);
//                    Color z = color == Color.White ? Color.Black : Color.White;
//                    double v = snapshot.calculate(x, y, z);
//                    if (max < v) {
//                        max = v;
//                    }
//                }
//            }
//        }
        return max;
    }

    private boolean isWin(int x, int y, Color color) {
//        int count = 0;
//        for (int[] ints : dir) {
//            for (int k = 0; k < 8; k++) {
//                int tx = x - ints[0] * 4 + k * ints[0];
//                int ty = y - ints[1] * 4 + k * ints[1];
//                if (tx >= 0 && tx < 15 && ty >= 0 && ty < 15 && this.color[tx][ty] == color) {
//                    count++;
//                    if (count == 5) {
//                        return true;
//                    }
//                } else {
//                    count = 0;
//                }
//            }
//        }
        return false;
    }

    /**
     * 获取x,y为中心点两侧4个空位的旗子，加上x,y总共9个旗子
     * @param x
     * @param y
     * @param dir
     * @return
     */
    private LinkedList<Zi> fetch(int x, int y, Dir dir) {
        Color color = borad[x][y];
        Color negative = color == Color.White ? Color.Black : Color.White;
        LinkedList<Zi> zis = new LinkedList<Zi>();

        Zi zi = new Zi();
        zi.c = 1;
        zis.addFirst(zi);

        for (int i = 1; i <= 4; i++) {
            Zi z = zis.getFirst();
            int tx = x - dir.x * i;
            int ty = y - dir.y * i;
            Color c = tx < 0 || ty < 0 || tx > 14 || ty > 14 ? negative : borad[tx][ty];
            if (c == Color.Nul) {
                z.lr++;
            } else if (c == color) {
                if (z.lr == 0) {
                    z.c++;
                } else {
                    zi = new Zi();
                    zi.c = 1;
                    zi.rr = z.lr;
                    zis.addFirst(zi);
                }
            } else {
                break;
            }
        }

        for (int i = 1; i <= 4; i++) {
            Zi z = zis.getLast();
            int tx = x + dir.x * i;
            int ty = y + dir.y * i;
            Color c = tx < 0 || ty < 0 || tx > 14 || ty > 14 ? negative : borad[tx][ty];
            if (c == Color.Nul) {
                z.rr++;
            } else if (c == color  ) {
                if (z.rr == 0) {
                    z.c++;
                } else {
                    zi = new Zi();
                    zi.c = 1;
                    zi.lr = z.rr;
                    zis.addLast(zi);
                }
            } else {
                break;
            }
        }

        return zis;
    }

    private void print() {
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                System.out.print(this.borad[x][y] == null ? ". " : this.borad[x][y] == Color.White ? "O " : "X ");
            }
            System.out.println();
        }
        System.out.println();
    }
//
//    private static List<ThreadQue> threads = new ArrayList<ThreadQue>();
//    private static int count = 0;
//    private static int tn = 1;
//
//    public static synchronized void put(Runnable r) throws InterruptedException {
//        threads.get(count).put(r);
//        count ++;
//        if (count == tn) {
//            count = 0;
//        }
//    }
//
    public static void main(String[] args) throws InterruptedException {



        Color[][] color = new Color[15][];
        for (int x = 0; x < 15; x++) {
            Color[] row = new Color[15];
            for (int y = 0; y < 15; y++) {
                row[y] = Color.Nul;
            }
            color[x] = row;
        }

        color[7][3] = color[7][3] = color[7][9] = Color.Black;
        color[7][5] = color[7][7] = Color.White;

        Snapshot ss = new Snapshot(color, 0);


        List<Zi> r = ss.fetch(7, 7, dir[1]);
        System.out.println(r);

//
//        PointIterator pi1 = new PointIterator(7, 7, -4, 0, dir[1], ss);
//        PointIterator pi2 = new PointIterator(7, 7, 1, 4, dir[1], ss);
//
//        Zi zi1 = ss.fetch(pi1, Color.White);
//        Zi zi2 = ss.fetch(pi2, Color.White);
//
//        zi1.connect(zi2);
//
//        System.out.println(zi1);

//        ss.calculate(8,8, Color.White);
    }

}
