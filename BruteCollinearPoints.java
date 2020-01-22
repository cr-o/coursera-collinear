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
        ArrayList<LineSegment> arrayList = new ArrayList<LineSegment>();
        int pointsLength = this.points.length;
        double firstSlope = 0.0;
        double secondSlope = 0.0;
        double thirdSlope = 0.0;
        Point[] pointsCopy = this.points.clone();
        Arrays.sort(pointsCopy);
        for (int i = 0; i < pointsLength; i++) {
            System.out.printf(pointsCopy[i].toString());
            for (int j = i + 1; j < pointsLength; j++) {
                for (int k = j + 1; k < pointsLength; k++) {
                    for (int m = k + 1; m < pointsLength; m++) {
                        // three slopes between p and q, between p and r, and between p and s are all equal.
                        firstSlope = pointsCopy[i].slopeTo(pointsCopy[j]);
                        secondSlope = pointsCopy[i].slopeTo(pointsCopy[k]);
                        thirdSlope = pointsCopy[i].slopeTo(pointsCopy[m]);
                        if (firstSlope == secondSlope
                                && secondSlope
                                == thirdSlope) { // we only care only if all four are collinear

                            arrayList.add(new LineSegment(pointsCopy[i], pointsCopy[m]));

                        }
                    }
                }
            }
        }
        LineSegment[] toReturn = arrayList.toArray(new LineSegment[arrayList.size()]);
        return toReturn;
    }

    private Comparator<Point> pointComparator() {
        return new Comparator<Point>() {
            public int compare(Point p1, Point p2) {
                return p1.compareTo(p2);
            }
        };
    }

    // public static void main(String[] args) {
    //     // read the n points from a file
    //     In in = new In(args[0]);
    //     int n = in.readInt();
    //     Point[] points = new Point[n];
    //     for (int i = 0; i < n; i++) {
    //         int x = in.readInt();
    //         int y = in.readInt();
    //         points[i] = new Point(x, y);
    //     }
    //
    //     // draw the points
    //     StdDraw.enableDoubleBuffering();
    //     StdDraw.setXscale(0, 32768);
    //     StdDraw.setYscale(0, 32768);
    //     for (Point p : points) {
    //         p.draw();
    //     }
    //     StdDraw.show();
    //     // print and draw the line segments
    //     int counter = 0;
    //     Point[] toUse = new Point[4];
    //     while (counter < points.length) {
    //         int it = 0;
    //         for (int m = counter; m < counter + 4; m++) {
    //             if (m < points.length) {
    //                 toUse[it] = points[m];
    //                 StdDraw.show();
    //                 it++;
    //                 if (it % 4 == 0) {
    //                     it = 0;
    //                     BruteCollinearPoints collinear = new BruteCollinearPoints(toUse);
    //                     for (LineSegment segment : collinear.segments()) {
    //                         StdOut.println(segment);
    //                         if (segment != null) {
    //                             segment.draw();
    //                         }
    //                     }
    //                     toUse = new Point[4];
    //                     StdDraw.show();
    //                 }
    //             }
    //         }
    //         counter += 4;
    //     }
    //
    // }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            String s = in.readString();
            if ("null".equals(s)) {
                points[i] = null;
            }
            else {
                int x = Integer.parseInt(s);
                int y = in.readInt();
                points[i] = new Point(x, y);
            }
        }

        BruteCollinearPoints collinear = new BruteCollinearPoints(points);

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
