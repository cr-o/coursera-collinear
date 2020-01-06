/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
    // finds all line segments containing 4 points
    private Point[] points;

    public BruteCollinearPoints(Point[] points) {
        if (points.length == 0) {
            throw new IllegalArgumentException("illegal point argument");
        }
        this.points = points;
        for (int i = 0; i < this.points.length; i++) {
            for (int n = 0; n < i; n++) {
                if (points[n] == null) {
                    throw new IllegalArgumentException("null point argument");
                }
                if (n != i && points[n].compareTo(points[i]) == 0) {
                    throw new IllegalArgumentException("illegal duplicate point argument");
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments().length; // if this is one, then all four points are collinear
    }

    // the line segments
    public LineSegment[] segments() {
        ArrayList<Point> arrayList = new ArrayList<Point>();
        int pointsLength = this.points.length;
        double firstSlope = 0.0;
        double secondSlope = 0.0;
        double thirdSlope = 0.0;
        for (int i = 0; i < pointsLength; i++) {
            for (int j = 0; j < pointsLength; j++) {
                for (int k = 0; k < pointsLength; k++) {
                    for (int m = 0; m < pointsLength; m++) {
                        if (i != j && i != k && i != m && j != k && j != m && k != m) {
                            // three slopes between p and q, between p and r, and between p and s are all equal.
                            firstSlope = this.points[i].slopeTo(this.points[j]);
                            secondSlope = this.points[i].slopeTo(this.points[k]);
                            thirdSlope = this.points[i].slopeTo(this.points[m]);
                            if (firstSlope == secondSlope
                                    && secondSlope
                                    == thirdSlope) { // we only care only if all four are collinear
                                if (!arrayList.contains(this.points[i]) && !arrayList
                                        .contains(this.points[j]) && !arrayList
                                        .contains(this.points[k]) && !arrayList
                                        .contains(this.points[m])) {
                                    arrayList.add(this.points[i]);
                                    arrayList.add(this.points[j]);
                                    arrayList.add(this.points[k]);
                                    arrayList.add(this.points[m]);
                                }

                            }
                        }
                    }
                }
            }
        }
        if (arrayList.size() == 4) {
            Point[] collinearPts = arrayList.toArray(new Point[arrayList.size()]);
            Arrays.sort(collinearPts, pointComparator());
            LineSegment[] toReturn = new LineSegment[1];
            LineSegment finalSeg = new LineSegment(collinearPts[0], collinearPts[3]);
            toReturn[0] = finalSeg;
            return toReturn;
        }
        else {
            LineSegment[] toReturn = new LineSegment[0];
            return toReturn;
        }
    }

    private Comparator<Point> pointComparator() {
        return new Comparator<Point>() {
            public int compare(Point p1, Point p2) {
                return p1.compareTo(p2);
            }
        };
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
        int counter = 0;
        Point[] toUse = new Point[4];
        while (counter < points.length) {
            int it = 0;
            for (int m = counter; m < counter + 4; m++) {
                if (m < points.length) {
                    toUse[it] = points[m];
                    StdDraw.show();
                    it++;
                    if (it % 4 == 0) {
                        it = 0;
                        BruteCollinearPoints collinear = new BruteCollinearPoints(toUse);
                        for (LineSegment segment : collinear.segments()) {
                            StdOut.println(segment);
                            if (segment != null) {
                                segment.draw();
                            }
                        }
                        toUse = new Point[4];
                        StdDraw.show();
                    }
                }
            }
            counter += 4;
        }

    }
}
