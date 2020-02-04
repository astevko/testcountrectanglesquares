import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class Solution1 {

    class Square {
        /**
         * coordinates of a bounding box on an X, Y grid
         */
        final int x1;
        final int y1;
        final int x2;
        final int y2;

        /**
         * ctor
         *
         * @param x1 left
         * @param y1 bottom
         * @param x2 right
         * @param y2 top
         */
        public Square(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public int hashCode() {
            return Objects.hash(x1, y1, x2, y2);
        }
    }


    class Rectangle {
        /**
         * coordinates of a bounding box on an X, Y grid
         */
        final int x1;
        final int y1;
        final int x2;
        final int y2;
        final Map<Integer, Square> squares = new HashMap<>();

        Rectangle(int[] coords) {
            this.x1 = coords[0];
            this.y1 = coords[1];
            this.x2 = coords[2];
            this.y2 = coords[3];

            // create a list of squares within
            for (int x = x1; x < x2; x++) {
                for (int y = y1; y < y2; y++) {
                    Square sq = new Square(x, y, x + 1, y + 1);
                    squares.put(sq.hashCode(), sq);
                }
            }
        }

        boolean hasSquare(Square thatSquare) {
            return squares.containsKey(thatSquare.hashCode());
        }

        /**
         * return square area of rectangle
         *
         * @return int size
         */
        int area() {
            return squares.size();
        }

        public void add(Square thatSquare) {
            this.squares.put(thatSquare.hashCode(), thatSquare);
        }
    }

    /**
     * combine all the squares in the rectangles
     *
     * @param rects a bunch of rectangle coordinates in { { x1, y1, x2, y2}, { a, b, c, d} }
     * @return collection of squares
     */
    Collection<Square> combineSquares(int[][] rects) {
        Rectangle seedRect = new Rectangle(rects[0]);
        if (rects.length > 1) {
            // for every 2nd+ Rectangle
            for (int r = 1; r < rects.length; r++) {
                Rectangle thatRect = new Rectangle(rects[r]);
                // for every that that rect square
                for (Square thatSquare : thatRect.squares.values()) {
                    // compare with every seed squares
                    if (!seedRect.hasSquare(thatSquare)) {
                        seedRect.add(thatSquare);
                    }
                }
            }
        }
        return seedRect.squares.values();
    }

    /*
            +---+
            | 3 |
        +---+   +
        | 12! 23|
        + - +---+
        | 1 |
        +---+
     */
    static final int[] square1 = {0, 0, 1, 2};
    static final int[] square2 = {0, 1, 2, 2};
    static final int[] square3 = {1, 1, 2, 3};


    /*
        +---+---+
        | C | D |
        +---+---+
        | A | B |
        +---+---+
     */
    static final int[] sqa = {0, 0, 1, 1};
    static final int[] sqb = {1, 0, 2, 1};
    static final int[] sqc = {0, 1, 1, 2};
    static final int[] sqd = {1, 1, 2, 2};
    static final int[] sqab = {0, 0, 2, 1};

    @Test
    public void areaAthruD() {
        final Rectangle a = new Rectangle(sqa);
        final Rectangle b = new Rectangle(sqb);
        final Rectangle c = new Rectangle(sqc);
        final Rectangle d = new Rectangle(sqd);

        assertEquals(a.area(), 1);
        assertEquals(b.area(), 1);
        assertEquals(c.area(), 1);
        assertEquals(d.area(), 1);
    }

    @Test
    public void intersectingAA() {
        int[][] rects = new int[][]{sqa, sqa};
        Solution1 sol1 = new Solution1();
        assertEquals(sol1.combineSquares(rects).size(), 1);
    }

    @Test
    public void intersectingA_B() {
        int[][] rects = new int[][]{sqa, sqb};
        Solution1 sol1 = new Solution1();
        assertEquals(sol1.combineSquares(rects).size(), 2);
    }

    @Test
    public void intersectingA_AB() {
        int[][] rects = new int[][]{sqa, sqab};
        Solution1 sol1 = new Solution1();
        assertEquals(sol1.combineSquares(rects).size(), 2);
    }

    @Test
    public void areaSquare1() {
        Rectangle square = new Rectangle(square1);
        assertEquals(square.area(), 2);
    }

    @Test
    public void areaSquare2() {
        Rectangle square = new Rectangle(square2);
        assertEquals(square.area(), 2);
    }

    @Test
    public void areaSquare3() {
        Rectangle square = new Rectangle(square3);
        assertEquals(square.area(), 2);
    }


    @Test
    public void intersectingSQ1SQ2() {
        int[][] rects = new int[][]{square1, square2};
        Solution1 sol1 = new Solution1();
        assertEquals(sol1.combineSquares(rects).size(), 3);

    }

    @Test
    public void intersectingSQ2SQ3() {
        int[][] rects = new int[][]{square2, square3};
        Solution1 sol1 = new Solution1();
        assertEquals(sol1.combineSquares(rects).size(), 3);
    }

    @Test
    public void intersectingSQ1SQ3() {
        int[][] rects = new int[][]{square1, square3};
        Solution1 sol1 = new Solution1();
        assertEquals(sol1.combineSquares(rects).size(), 4);
    }

    @Test
    public void intersectingSQ1SQ2SQ3() {
        int[][] rects = new int[][]{square1, square2, square3};
        Solution1 sol1 = new Solution1();
        assertEquals(sol1.combineSquares(rects).size(), 4);
    }
    @Test
    public void intersectingAllSquares() {
        int[][] rects = new int[][]{square1, square2, square3, sqa, sqb, sqc, sqd, sqab};
        Solution1 sol1 = new Solution1();
        assertEquals(sol1.combineSquares(rects).size(), 5);
    }
}
