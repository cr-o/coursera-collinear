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
        if (points.length == 0) {
            throw new IllegalArgumentException("illegal point argument");
        }
        this.points = points;
        for (int i = 0; i < 4; i++) {
            for (int n = 0; n < i; n++) {
                if (n != i && points[n].compareTo(points[i]) == 0) {
                    throw new IllegalArgumentException("illegal duplicate point argument");
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return (segments().length); // if this is one, then all four points are collinear
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment ls[] = new LineSegment[6];

        Comparator<Point> comparator = points[0].slopeOrder();
        int result = comparator.compare(this.points[1], this.points[2]);
        if (result != 0) {
            ls[0] = new LineSegment(this.points[0], this.points[1]);
            ls[1] = new LineSegment(this.points[0], this.points[2]);
        }
        else {
            Comparator<Point> secComparator = points[3].slopeOrder();
            int resultTwo = secComparator.compare(this.points[0], this.points[1]);
            if (resultTwo != 0) {
                ls[2] = new LineSegment(this.points[3], this.points[0]);
                ls[3] = new LineSegment(this.points[3], this.points[1]);
            }
            else {
                Comparator<Point> thirdComparator = points[2].slopeOrder();
                int resultThree = thirdComparator.compare(this.points[1], this.points[3]);
                if (resultThree != 0) {
                    ls[4] = new LineSegment(this.points[2], this.points[1]);
                    ls[5] = new LineSegment(this.points[2], this.points[3]);
                }
                else {
                    ls[0] = new LineSegment(this.points[0], this.points[3]);
                }
            }
        }
        return ls;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
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
