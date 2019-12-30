/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class BruteCollinearPoints {
    // finds all line segments containing 4 points
    private BruteCollinearPoints bcf;
    private Point points[];

    public BruteCollinearPoints(Point[] points) {
        if (points == null && points.length != 4) {
            throw new IllegalArgumentException("illegal point argument");
        }
        this.points = points;
        for (int i = 0; i < 4; i++) {
            for (int n = 0; n < i; n++) {
                if (n != i && points[n].equals(points[i])) {
                    throw new IllegalArgumentException("illegal duplicate point argument");
                }
            }
        }
        bcf = new BruteCollinearPoints(points);
    }

    // the number of line segments
    public int numberOfSegments() {
        return (segments().length); // if this is one, then all four points are collinear
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment ls[];
        Comparator<Point> comparator = points[0].slopeOrder();
        int result = comparator.compare(this.points[2], this.points[3]);
        // compare bcf's 1-2 and 2-3
        // if they are different, add to line segment array
        // if they are the same, don't add. compare 3-4 and 1-4
        ls = new LineSegment[2]; // placeholder. only add to array if conditions met.

        return ls;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        Point[] points = new Point[n];
        for (int i = 0; i < 4; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
