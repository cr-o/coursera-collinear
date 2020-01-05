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
                if (points[i] == null) {
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
        ArrayList<PointWithOriginSlope> arrayList = new ArrayList<PointWithOriginSlope>();
        ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();

        int pointsLength = this.points.length;
        for (int i = 0; i < pointsLength; i++) {
            for (int j = 0; j < pointsLength; j++) {
                for (int k = 0; k < pointsLength; k++) {
                    for (int m = 0; m < pointsLength; m++) {
                        if (i != j && i != k && i != m && j != k && j != m && k != m) {
                            double firstSlope = this.points[i].slopeTo(this.points[j]);
                            // if (firstSlope == this.points[i].slopeTo(this.points[k])
                            //         && firstSlope == this.points[k].slopeTo(this.points[m])) {
                            arrayList.add(new PointWithOriginSlope(this.points[i],
                                                                   this.points[m], firstSlope));
                            lineSegments.add(new LineSegment(this.points[i], this.points[m]));

                            //}
                        }
                    }
                }
            }
        }
        PointWithOriginSlope[] origins = arrayList
                .toArray(new PointWithOriginSlope[arrayList.size()]);

        // sort the values by origin point
        Arrays.sort(origins, slopeComparator());

        // for (int p = 0; p < origins.length; p++) {
        //
        // }

        return lineSegments.toArray(new LineSegment[lineSegments.size()]);

    }

    private Comparator<PointWithOriginSlope> originComparator() {
        return new Comparator<PointWithOriginSlope>() {
            public int compare(PointWithOriginSlope p1, PointWithOriginSlope p2) {
                return p1.origin.compareTo(p2.origin);
            }
        };
    }

    private Comparator<PointWithOriginSlope> slopeComparator() {
        return new Comparator<PointWithOriginSlope>() {
            public int compare(PointWithOriginSlope p1, PointWithOriginSlope p2) {
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
        int counter = 0;
        Point[] toUse = new Point[4];
        while (counter != points.length) {
            int it = 0;
            for (int m = counter; m < counter + 4; m++) {
                toUse[it] = points[m];

                StdDraw.show();
                it++;
                if (it % 4 == 0) {
                    it = 0;
                    BruteCollinearPoints collinear = new BruteCollinearPoints(toUse);
                    for (LineSegment segment : collinear.segments()) {
                        StdOut.println(segment);
                        segment.draw();
                    }
                    toUse = new Point[4];
                }
            }
            counter += 4;
        }

    }
}
