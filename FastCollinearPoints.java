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
        ArrayList<PointWithOriginSlope> collinearPoints = new ArrayList<PointWithOriginSlope>();
        ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();
        for (int n = 0; n < slopes.length; n++) {
            PointWithOriginSlope newPoint = new PointWithOriginSlope(slopes[n].origin,
                                                                     slopes[n].point,
                                                                     slopes[n].slopeToOrigin);
            if (n == 0) {
                prevSlope = slopes[0].slopeToOrigin;
            }
            else {
                if (slopes[n].slopeToOrigin == prevSlope) {
                    currCount += 1;
                    // LineSegment newLine = new LineSegment(slopes[n].origin, slopes[n].point);
                    if (currCount >= 3 && !collinearPoints
                            .contains(newPoint)) {
                        // lineSegments.add(newLine);
                        collinearPoints.add(newPoint);
                        if (slopes[n].slopeToOrigin == slopes[0].slopeToOrigin) {
                            collinearPoints
                                    .add(new PointWithOriginSlope(slopes[0].origin, slopes[0].point,
                                                                  slopes[0].slopeToOrigin));
                        }
                    }
                }
                else {
                    currCount = 0;
                }
            }
            prevSlope = slopes[n].slopeToOrigin;
        }
        double previousSlope = 0.0;
        ArrayList<Point> singleGroup = new ArrayList<Point>();
        Point[] allInGroup;
        for (int n = 0; n < collinearPoints.size(); n++) {
            // if a group ended
            // iterate through previous group
            if (n == 0) {
                previousSlope = collinearPoints.get(n).slopeToOrigin;
                // add to singlegroup
                singleGroup.add(collinearPoints.get(n).origin);
                singleGroup.add(collinearPoints.get(n).point);
            }
            else {
                if (previousSlope != collinearPoints.get(n).slopeToOrigin
                        || n == collinearPoints.size() - 1) {
                    if (!(n == collinearPoints.size() - 1)) {

                        previousSlope = collinearPoints.get(n).slopeToOrigin;
                        allInGroup = singleGroup.toArray(new Point[singleGroup.size()]);
                        // sort single group
                        Arrays.sort(allInGroup, pointComparator());
                        // make and add line segment using min and max
                        lineSegments
                                .add(new LineSegment(allInGroup[0],
                                                     allInGroup[allInGroup.length - 1]));
                        // reset vector
                        singleGroup = new ArrayList<Point>();
                        singleGroup.add(collinearPoints.get(n).origin);
                        singleGroup.add(collinearPoints.get(n).point);
                    }
                    else {
                        singleGroup.add(collinearPoints.get(n).origin);
                        singleGroup.add(collinearPoints.get(n).point);
                        allInGroup = singleGroup.toArray(new Point[singleGroup.size()]);
                        // sort single group
                        Arrays.sort(allInGroup, pointComparator());
                        // make and add line segment using min and max
                        lineSegments
                                .add(new LineSegment(allInGroup[0],
                                                     allInGroup[allInGroup.length - 1]));
                    }
                    // add to new single group

                }
                else {
                    // add to single group
                    singleGroup.add(collinearPoints.get(n).origin);
                    singleGroup.add(collinearPoints.get(n).point);
                }
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
