package com.boil.gobang;

public class DirIter {

    public static final Point[] dir = new Point[]{
            new Point(1, 0),
            new Point(0, 1),
            new Point(1, 1),
            new Point(1, -1)
    };

    private int d = 0;
    private int i = -1;
    private int k = 1;
    private int x, y;
    private boolean more;

    public DirIter(int x, int y) {
        this.x = x;
        this.y = y;
        this.more = true;
    }

    public boolean hasNext() {
        return this.more;
    }

    public Point next() {
        Point dd = dir[d];
        int tx = x + dd.x * k * i;
        int ty = y + dd.y * k * i;
        Point point = new Point(tx, ty);

        k ++;
        if (k == 5) {
            k = 1;
            i += 2;
            if (i > 1) {
                i = -1;
                d++;
                if (d == 5) {
                    this.more = false;
                }
            }
        }
        this.more = true;

        return point;
    }
}
