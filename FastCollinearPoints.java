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

        ArrayList<PointWithOriginSlope> arrayList = new ArrayList<PointWithOriginSlope>();
        /*
A faster, sorting-based solution. Remarkably, it is possible to solve the problem much faster than the brute-force solution described above. Given a point p, the following method determines whether p participates in a set of 4 or more collinear points.

Think of p as the origin.

For each other point q, determine the slope it makes with p.

Sort the points according to the slopes they makes with p.

Check if any 3 (or more) adjacent points in the sorted order have equal slopes with respect to p. If so, these points, together with p, are collinear.

Applying this method for each of the n points in turn yields an efficient algorithm to the problem. The algorithm solves the problem because points that have equal slopes with respect to p are collinear, and sorting brings such points together. The algorithm is fast because the bottleneck operation is sorting.
         */
        int pointsLength = this.points.length;
        for (int i = 0; i < pointsLength; i++) { // origin
            for (int j = i; j < pointsLength; j++) { // other point
                if (j != 0) {
                    PointWithOriginSlope ptOrigin = new PointWithOriginSlope(this.points[i],
                                                                             this.points[j],
                                                                             this.points[i].slopeTo(
                                                                                     this.points[j]));
                    arrayList.add(ptOrigin);
                }
            }
        }
        PointWithOriginSlope[] slopes = arrayList
                .toArray(new PointWithOriginSlope[arrayList.size()]);


        Arrays.sort(slopes, new Comparator<PointWithOriginSlope>() {
            public int compare(PointWithOriginSlope p1, PointWithOriginSlope p2) {
                return Double.compare(p1.slopeToOrigin, p2.slopeToOrigin);
            }
        });
        int currCount = 0;
        double prevSlope = 0.0;
        ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();
        for (int n = 0; n < slopes.length; n++) {
            if (n == 0) {
                prevSlope = slopes[0].slopeToOrigin;
            }
            else {
                if (slopes[n].slopeToOrigin == prevSlope) {
                    currCount += 1;
                    if (currCount >= 3 && !lineSegments
                            .contains(new LineSegment(slopes[n].origin, slopes[n].point))) {
                        lineSegments.add(new LineSegment(slopes[n].origin, slopes[n].point));
                    }
                }
                else {
                    currCount = 0;
                }
            }
            prevSlope = slopes[n].slopeToOrigin;
        }
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
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
