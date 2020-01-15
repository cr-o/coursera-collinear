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

public class FastCollinearPoints {
    // finds all line segments containing 4 or more points
    private Point[] points;

    public FastCollinearPoints(Point[] points) {
        if (points.length == 0) {
            throw new IllegalArgumentException("illegal point argument");
        }
        this.points = points;
        for (int i = 0; i < this.points.length; i++) {
            for (int n = 0; n < i; n++) {
                if (points[n] == null) {
                    throw new IllegalArgumentException("null point argument");
                }
                if (points[n].compareTo(points[i]) == 0) {
                    throw new IllegalArgumentException("illegal duplicate point argument");
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments().length;
    }

    private class PointWithOriginSlope {
        private Point origin;
        private Point point;
        private double slopeToOrigin;

        public PointWithOriginSlope(Point origin, Point point, double slopeToOrigin) {
            this.origin = origin;
            this.point = point;
            this.slopeToOrigin = slopeToOrigin;
        }
    }

    // the line segments
    public LineSegment[] segments() {
        /*
         * Fix horizontal lines
         * Fix lines that have the same slope but different beginning and end points
         * */
        ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();
        Point origin;
        Point[] originSort;
        int counter = 0;
        for (int i = 0; i < this.points.length; i++) {
            Arrays.sort(this.points, this.points[i].slopeOrder());
            origin = this.points[i];
            counter = 0;
            ArrayList<Point> toOriginSort = new ArrayList<Point>();
            toOriginSort.add(origin);
            for (int n = 0; n < this.points.length - 1; n++) {
                if (n == i) {
                    counter++;
                    toOriginSort.add(this.points[n]);
                }
                else if (n == this.points.length - 2) {
                    // at last element
                    if (this.points[n].slopeTo(origin) == this.points[n + 1].slopeTo(origin)) {
                        if (!toOriginSort.contains(this.points[n])) {
                            toOriginSort.add(this.points[n]);
                            counter++;
                        }
                        if (!toOriginSort.contains(this.points[n + 1])) {
                            toOriginSort.add(this.points[n + 1]);
                            counter++;
                        }
                        if (counter >= 4) {
                            originSort = toOriginSort.toArray(new Point[toOriginSort.size()]);
                            Arrays.sort(originSort, pointComparator());
                            lineSegments.add(new LineSegment(originSort[0],
                                                             originSort[originSort.length - 1]));
                        }
                    }
                }
                else {
                    if (this.points[n].slopeTo(origin) == this.points[n + 1].slopeTo(origin)) {
                        if (!toOriginSort.contains(this.points[n])) {
                            toOriginSort.add(this.points[n]);
                            counter++;
                        }
                        if (!toOriginSort.contains(this.points[n + 1])) {
                            toOriginSort.add(this.points[n + 1]);
                            counter++;
                        }
                    }
                    else {
                        if (counter >= 4) {
                            System.out.println("FOUR");
                            originSort = toOriginSort.toArray(new Point[toOriginSort.size()]);
                            Arrays.sort(originSort, pointComparator());
                            lineSegments.add(new LineSegment(originSort[0],
                                                             originSort[originSort.length - 1]));
                        }
                        counter = 0;
                        toOriginSort = new ArrayList<Point>();
                    }
                }
                System.out.printf("counter: %d\n", counter);
                System.out.printf("origin: %s\n", origin.toString());
                System.out.printf("point: %s\n", this.points[n].toString());
            }
        }
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }

    private Comparator<Point> pointComparator() {
        return new Comparator<Point>() {
            public int compare(Point p1, Point p2) {
                return p1.compareTo(p2);
            }
        };
    }

    // private Comparator<PointWithOriginSlope> pointComparator() {
    //     return new Comparator<PointWithOriginSlope>() {
    //         public int compare(PointWithOriginSlope p1, PointWithOriginSlope p2) {
    //             return p1.point.compareTo(p2.point);
    //         }
    //     };
    // }

    private Comparator<PointWithOriginSlope> slopeComparator() {
        return new Comparator<PointWithOriginSlope>() {
            public int compare(PointWithOriginSlope p1, PointWithOriginSlope p2) {
                double slopeCompare = Double.compare(p1.slopeToOrigin, p2.slopeToOrigin);
                if (slopeCompare == 0.0) {
                    return p1.point.compareTo(p2.point);
                }
                return Double.compare(p1.slopeToOrigin, p2.slopeToOrigin);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
