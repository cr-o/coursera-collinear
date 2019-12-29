/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null && points.length != 4) {
            throw new IllegalArgumentException("illegal point argument");
        }
        for (int i = 0; i < 4; i++) {
            for (int n = 0; n < i; n++) {
                if (points[n].equals(points[i])) {
                    throw new IllegalArgumentException("illegal duplicate point argument");
                }
            }
        }
        BruteCollinearPoints bcf = new BruteCollinearPoints(points);
    }

    // the number of line segments
    public int numberOfSegments() {
        return (segments().length);
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment ls[] = new LineSegment[4];
        return
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
