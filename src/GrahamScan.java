import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

// Point class to represent 2D points
class Point {
    int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class GrahamScan {

    // Function to find the orientation of triplet (p, q, r)
    private static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0;  // Collinear
        return (val > 0) ? 1 : 2; // Clockwise or Counterclockwise
    }

    // Function to compute the convex hull of a set of points using Graham Scan
    public static List<Point> convexHull(Point[] points) {
        int n = points.length;
        if (n < 3) return null; // Convex hull not possible with less than 3 points

        // Find the point with the lowest y-coordinate (and the leftmost one in case of a tie)
        int minIdx = 0;
        for (int i = 1; i < n; i++) {
            if (points[i].y < points[minIdx].y || (points[i].y == points[minIdx].y && points[i].x < points[minIdx].x))
                minIdx = i;
        }

        // Swap the lowest point with the first point
        Point temp = points[0];
        points[0] = points[minIdx];
        points[minIdx] = temp;

        // Sort the remaining points by polar angle with respect to the first point
        Point anchor = points[0];
        Arrays.sort(points, 1, n, new Comparator<Point>() {
            public int compare(Point p1, Point p2) {
                int orient = orientation(anchor, p1, p2);
                if (orient == 0) {
                    // If collinear, choose the one closer to anchor
                    return Integer.compare(distSq(anchor, p1), distSq(anchor, p2));
                }
                return (orient == 2) ? -1 : 1; // Sort in counterclockwise order
            }
        });

        // Initialize stack for storing convex hull vertices
        Stack<Point> stack = new Stack<>();
        stack.push(points[0]);
        stack.push(points[1]);

        // Process remaining points
        for (int i = 2; i < n; i++) {
            while (stack.size() > 1 && orientation(stack.get(stack.size() - 2), stack.peek(), points[i]) != 2) {
                stack.pop(); // Remove top of stack if not counterclockwise
            }
            stack.push(points[i]); // Add current point to stack
        }

        // Convert stack to list
        List<Point> convexHull = new ArrayList<>();
        while (!stack.isEmpty()) {
            convexHull.add(stack.pop());
        }
        return convexHull; // Return the convex hull
    }

    // Function to compute the square of distance between two points
    private static int distSq(Point p1, Point p2) {
        return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
    }

    public static void main(String[] args) {
        // Example usage
        Point[] points = {new Point(0, 3), new Point(1, 1), new Point(2, 2), new Point(4, 4),
                new Point(0, 0), new Point(1, 2), new Point(3, 1), new Point(3, 3)};

        // Compute convex hull
        List<Point> convexHull = convexHull(points);

        // Print the convex hull
        System.out.println("Convex Hull (Graham Scan): ");
        for (Point p : convexHull) {
            System.out.println("(" + p.x + ", " + p.y + ")");
        }
    }
}

