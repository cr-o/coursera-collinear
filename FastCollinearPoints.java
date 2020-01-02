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
        int pointsLength = this.points.length;
        for (int i = 0; i < pointsLength; i++) { // origin
            for (int j = i; j < pointsLength; j++) { // other point
                if (i != j) {
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


        Arrays.sort(slopes, slopeComparator());
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
