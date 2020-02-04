import org.junit.Test;

import static org.junit.Assert.*;

/**
 * failed attempt at a solution
 * excess complexity with assessing intersecting areas from raw coordinates
 * 
 */
public class Solution2 {
    class Rectangle {
        /**
         * coordinates of a bounding box on an X, Y grid
         */
        int x1, y1, x2, y2;

        /**
         * ctor
         * @param coords coordinates for a rectangle
         */
        Rectangle( int[] coords) {
            this.x1 = coords[0];
            this.y1 = coords[1];
            this.x2 = coords[2];
            this.y2 = coords[3];
        }

        boolean isIntersecting( final Rectangle that ) {
            // need to intersect on both dimensions
            return (
                    (isIntersectingThatX1( that) && isIntersectingThatY1(that))
                    ||
                    (isIntersectingThatX2( that) && isIntersectingThatY2(that))
            );
        }

        /**
         * return square are of rectangle
         * @return int size
         */
        int area() {
            return ( x2 - x1 ) * ( y2 - y1 );
        }

        // would be much easier to code between operator that janky java.math syntax
        // ( this.x1 >= that.x1 >= this.x2 )    that.x1 is between this.x
        // ( this.x1 >= that.x2 >= this.x2 )    that.x2 is between this.x
        // ( this.y1 >= that.y1 >= this.y2 )    that.y1 is between this.y
        // ( this.y1 >= that.y2 >= this.y2 )    that.y2 is between this.y

        boolean isIntersectingThatX1(final Rectangle that ) { return ( that.x1 >= this.x1 && that.x1 <= this.x2 ); }
        boolean isIntersectingThatX2(final Rectangle that ) { return  ( that.x2 >= this.x1 && that.x2 <= this.x2 );}
        boolean isEncasedThatX(final Rectangle that ) { return  ( isIntersectingThatX1(that) && isIntersectingThatX2(that) );}
        boolean isIntersectingThatY1(final Rectangle that ) { return  ( that.y1 >= this.y1 && that.y1 <= this.y2 );}
        boolean isIntersectingThatY2(final Rectangle that ) { return  ( that.y2 >= this.y1 && that.y2 <= this.y2 );}
        boolean isEncasedThatY(final Rectangle that ) { return ( isIntersectingThatY1(that)  && isIntersectingThatY2(that) ); }
        boolean isEncasedThatXY(final Rectangle that ) { return isEncasedThatX(that) && isEncasedThatY(that) ; }

        /**
         * reduce intersection between that and this rectangles
         * @param that intersecting that
         * @return non-interesecting that
         */
        Rectangle reduceIntersection( Rectangle that ) {
            // encased both X and Y cords --> eliminate that size
            if (isEncasedThatXY(that)) {
                // remove from consideration by going to [0,0] [0,0] AND DONE
                that.x1 = that.x2 = that.y1 = that.y2 = 0;
                return that;
            }
            // Encased X coords  --> Move Y coords
            if (isEncasedThatX(that)) {
                if (isIntersectingThatY1(that)) {
                    // move Y1 upwards this
                    that.y1 = this.y2;
                }
                if (isIntersectingThatY2(that)) {
                    // move y2 below this
                    that.y2 = this.y1;
                }
            }
            // Encased Y coords -> Move X coords
            else if (isEncasedThatY(that)) {
                if (isIntersectingThatX1(that)) {
                    // move X1 to the right this
                    that.x1 = this.x2;
                }
                if (isIntersectingThatX2(that)) {
                    // move X2 to the left this
                    that.x2 = this.x1;
                }
            }
            // that below this   --> Move X2 cord to this.x1
            else if (isIntersectingThatX1(that)) {
                // move to the left
                that.x2 = this.x1;
            }
            // that Above this   --> Move X1 coord to this.x2
            else if (isIntersectingThatX2(that)) {
                // move to the right
                that.x1 = this.x2;
            }
            // that left of this -> Move Y2 coord to this.y1
            else if (isIntersectingThatY2(that)) {
                // move downwards
                that.y2 = this.y1;
            }
            // that right of this -> Move Y1 coord to this.y2
            else if (isIntersectingThatY2(that)) {
                // move upwards
                that.y1 = this.y2;
            }
            return that;
        }
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
    final int[] square1 = { 0, 0, 1, 2 };
    final int[] square2 = { 0, 1, 2, 2 };
    final int[] square3 = { 1, 1, 2, 3 };


    /*
        +---+---+
        | C | D |
        +---+---+
        | A | B |
        +---+---+
     */
    final int[] sqa = { 0, 0, 1, 1};
    final  int[] sqb = { 1, 0, 2, 1};
    final int[] sqc = { 0, 1, 1, 2};
    final int[] sqd = { 1, 1, 2, 2};
    final int[] sqab = { 0, 0, 2, 1 };

    @Test public void areaAthruD(){
        Rectangle a = new Rectangle(sqa);
        Rectangle b = new Rectangle(sqb);
        Rectangle c = new Rectangle(sqc);
        Rectangle d = new Rectangle(sqd);

        assertEquals(a.area(), 1);
        assertEquals(b.area(), 1);
        assertEquals(c.area(), 1);
        assertEquals(d.area(), 1);
    }

    @Test public void intersectingAA() {
        Rectangle a1 = new Rectangle(sqa);
        Rectangle a2 = new Rectangle(sqa);

        assertTrue(a1.isIntersectingThatX1(a2));
        assertTrue(a1.isIntersectingThatX2(a2));

        assertTrue(a1.isIntersectingThatY1(a2));
        assertTrue(a1.isIntersectingThatY2(a2));

        assertTrue(a2.isIntersectingThatX1(a1));
        assertTrue(a2.isIntersectingThatX2(a1));

        assertTrue(a2.isIntersectingThatY1(a1));
        assertTrue(a2.isIntersectingThatY2(a1));

        assertTrue(a1.isEncasedThatX(a2));
        assertTrue(a1.isEncasedThatY(a2));
        assertTrue(a1.isEncasedThatXY(a2));
    }

    @Test public void intersectingA_B() {
        Rectangle a = new Rectangle(sqa);
        Rectangle b = new Rectangle(sqb);

        assertTrue(a.isIntersectingThatX1(b));
        assertTrue(a.isIntersectingThatY1(b));

        assertFalse( a.isIntersectingThatX2(b));

        assertTrue( a.isIntersectingThatY2(b));

        assertFalse( a.isIntersecting(b));
    }

    @Test public void intersectingA_AB() {
        Rectangle a = new Rectangle(sqa);
        Rectangle ab = new Rectangle(sqab);

        assertTrue( a.isIntersectingThatX1(ab));
        assertFalse( a.isIntersectingThatX2(ab));

        assertTrue( a.isIntersectingThatY1(ab));
        assertTrue( a.isIntersectingThatY2(ab));

        assertTrue( a.isIntersecting(ab));
    }

    @Test public void intersectingAB_A() {
        Rectangle ab = new Rectangle(sqab);
        Rectangle a = new Rectangle(sqa);

        assertTrue( ab.isIntersectingThatX1(a));
        assertTrue( ab.isIntersectingThatX2(a));

        assertTrue( ab.isIntersectingThatY1(a));
        assertTrue( ab.isIntersectingThatY2(a));

        assertTrue( ab.isIntersecting(a));
    }

    @Test public void areaSquare1() {
        Rectangle square = new Rectangle(square1);
        assertEquals(2, square.area());
    }
    @Test public void areaSquare2() {
        Rectangle square = new Rectangle(square2);
        assertEquals(2, square.area());
    }
    @Test public void areaSquare3() {
        Rectangle square = new Rectangle(square3);
        assertEquals(2, square.area());
    }



    @Test public void isIntersectingSQ1SQ2() {
        Rectangle sq1 = new Rectangle(square1);
        Rectangle sq2 = new Rectangle(square2);
        assertTrue( sq1.isIntersecting(sq2));

        assertTrue( sq1.isIntersectingThatX1(sq2) );   // 0 <= 0 <= 1
        assertFalse( sq1.isIntersectingThatX2(sq2) );  // 0 <= 2 <= 1

        assertTrue( sq1.isIntersectingThatY1(sq2) );   // 0 <= 1 <= 2
        assertTrue( sq1.isIntersectingThatY2(sq2) );   // 0 <= 2 <= 2

        assertTrue( sq2.isIntersecting(sq1));
        assertTrue( sq2.isIntersectingThatX1(sq1) );   // 0 <= 0 <= 2
        assertTrue( sq2.isIntersectingThatX2(sq1) );   // 0 <= 1 <= 2

        assertFalse( sq2.isIntersectingThatY1(sq1) );   // 1 <= 0  <= 2
        assertTrue( sq2.isIntersectingThatY2(sq1) );    // 1 <= 2  <= 2
    }

    @Test public void isEncaseSQ1SQ2() {
        Rectangle sq1 = new Rectangle(square1);
        Rectangle sq2 = new Rectangle(square2);

        assertFalse( sq1.isEncasedThatX(sq2));  // sq2 overflows to the right of sq1
        assertTrue( sq1.isEncasedThatY(sq2));   // sq2 within height of sq1
        assertFalse( sq1.isEncasedThatXY(sq2)); // sq2 overflows s1

        assertTrue(  sq2.isEncasedThatX(sq1));   // sq1 within the x bounds of sq2
        assertFalse(  sq2.isEncasedThatY(sq1) ); // sq1 overflows below y bounds of sq2
        assertFalse( sq2.isEncasedThatXY(sq1));  // sq1 overflows s2
    }

   @Test public void isIntersectingSQ2SQ3() {
       Rectangle sq2 = new Rectangle(square2);
       Rectangle sq3 = new Rectangle(square3);

       assertTrue( sq2.isIntersectingThatX1(sq2) );   // 0 <= 1 <= 2
       assertTrue( sq2.isIntersectingThatX2(sq2) );  // 0 <= 2 <= 2

       assertTrue( sq2.isIntersectingThatY1(sq3) );   // 1 <= 1 <= 2
       assertFalse( sq2.isIntersectingThatY2(sq3) );   // 1 <= 3 <= 2

       assertFalse( sq3.isIntersectingThatX1(sq2) );   // 1 <= 0 <= 2
       assertTrue( sq3.isIntersectingThatX2(sq2) );   // 1 <= 2 <= 2

       assertTrue( sq3.isIntersectingThatY1(sq2) );   // 1 <= 1  <= 3
       assertTrue( sq3.isIntersectingThatY2(sq2) );    // 1 <= 2  <= 3

   }

    @Test public void isEncaseSQ2SQ3() {
        Rectangle sq2 = new Rectangle(square2);
        Rectangle sq3 = new Rectangle(square3);

        assertTrue( sq2.isEncasedThatX(sq3));  // sq3 within width of sq2
        assertFalse( sq2.isEncasedThatY(sq3));   // sq3 taller than sq2
        assertFalse( sq2.isEncasedThatXY(sq3)); // sq3 overflows s2

        assertFalse(  sq3.isEncasedThatX(sq2));   // sq2 overflows X bounds to the left of sq3
        assertTrue(  sq3.isEncasedThatY(sq2) ); // sq2 within height of sq3
        assertFalse( sq3.isEncasedThatXY(sq2));  // sq2 overflows sq3
    }

    @Test public void isIntersectingSQ1SQ3() {
        Rectangle sq1 = new Rectangle(square1);
        Rectangle sq3 = new Rectangle(square3);

        assertTrue( sq1.isIntersectingThatX1(sq3) );   // 0 <= 1 <= 1
        assertFalse( sq1.isIntersectingThatX2(sq3) );  // 0 <= 2 <= 1

        assertTrue( sq1.isIntersectingThatY1(sq3) );   // 0 <= 1 <= 2
        assertFalse( sq1.isIntersectingThatY2(sq3) );   // 0 <= 3 <= 2
        assertFalse( sq1.isIntersecting(sq3));

        assertTrue( sq3.isIntersectingThatX1(sq1) );   // 1 <= 1 <= 2
        assertTrue( sq3.isIntersectingThatX2(sq1) );   // 1 <= 2 <= 2

        assertFalse( sq3.isIntersectingThatY1(sq1) );   // 1 <= 0  <= 3
        assertTrue( sq3.isIntersectingThatY2(sq1) );    // 1 <= 2  <= 3
        assertFalse( sq3.isIntersecting(sq1));
    }
    @Test public void produceDiscreteRectsSQ1SQ2() {
        Rectangle sq1 = new Rectangle(square1);
        Rectangle sq2 = new Rectangle(square2);

        assertTrue(sq1.isIntersecting(sq2));
        sq1.reduceIntersection(sq2);
        assertFalse(sq1.isIntersecting(sq2));
    }
    @Test public void produceDiscreteRectsSQ2SQ1() {
        Rectangle sq1 = new Rectangle(square1);
        Rectangle sq2 = new Rectangle(square2);

        assertTrue(sq2.isIntersecting(sq1));
        sq2.reduceIntersection(sq1);
        assertFalse(sq2.isIntersecting(sq1));
    }


}
