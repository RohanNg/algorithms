package selectedproblem;

public class OptimalAlignment {

    public static class Tuple<T> {
        private final T e1;
        private final T e2;
        public Tuple(T e1, T e2) {
            this.e1 = e1;
            this.e2 = e2;
        }
        public T getE1() {
            return e1;
        }
        public T getE2() {
            return e2;
        }

        @Override
        public String toString() {
            return "(" + e1.toString() + " ," + e2.toString() + " }";
        }
    }

    public static Tuple<String> getOptimalAlignment(final String Y, final String X, final int gapPen, final int misMatchPen) {
        int[][] pen = new int[Y.length()][];

        for(int i = 0; i < Y.length(); i++)
            pen[i] = new int[X.length()];

        for(int i = 0; i < X.length(); i++)
            pen[0][i] = i*gapPen;

        for(int i = 0; i < Y.length(); i++)
            pen[i][0] = i*gapPen;

        final char[] XX = X.toCharArray();
        final char[] YY = Y.toCharArray();
        for(int y = 1; y < Y.length(); y ++) {
            for(int x = 1; x < X.length(); x++) {
                final int case1 = pen[y-1][x-1] + (XX[x] == YY[y] ? 0 : misMatchPen);
                final int case2 = pen[y][x-1] + gapPen;
                final int case3 = pen[y-1][x] + gapPen;
                pen[y][x] = Math.min(Math.min(case1, case2), case3);
            }
        }

        // reconstruct optimal alignment
        StringBuilder XXX = new StringBuilder();
        StringBuilder YYY = new StringBuilder();
        int currY = Y.length() - 1;
        int currX = X.length() - 1;
        while(currX > 0 && currY > 0) {
            final int val = pen[currY][currX];
            if(val == pen[currY-1][currX-1] + (XX[currX] == YY[currY] ? 0 : misMatchPen)) {
                // case 1
                XXX.append(XX[currX]);
                YYY.append(YY[currY]);
                currX--;
                currY--;
            } else if (val == pen[currY][currX-1] + gapPen) {
                XXX.append(XX[currX--]);
                YYY.append('_');
            } else if (val == pen[currY-1][currX] + gapPen) {
                // case3
                YYY.append(XX[currY--]);
                XXX.append('_');
            } else throw new RuntimeException();
        }

        if(currX == 0) {
            char cx = XX[0];
            for(int i = currY; i >= 0; i--) {
                YYY.append(YY[i]);
                XXX.append(cx == YY[i] ? cx : '_');
            }
        } else {
            // currY = 0
            char cy = YY[0];
            for(int i = currX; i >= 0; i--) {
                XXX.append(XX[i]);
                YYY.append(cy == XX[i] ? cy : '_');
            }
        }
        return new Tuple<>(YYY.reverse().toString(), XXX.reverse().toString());
    }

    public static void main(String[] args) {
        OptimalAlignment.Tuple<String> tup = OptimalAlignment.getOptimalAlignment("123456789","abc123456789",1,1);
        System.out.println(tup);
    }
}
