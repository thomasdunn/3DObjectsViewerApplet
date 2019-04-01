package com.dunncom.graphics;

/**
 * A simple 3-dimensional point.  Contains convenience methods for finding
 * dot-product of two vectors and for normalizing vectors.
 *
 * @author Thomas Dunn
 */

public class Point3D {

  public double x;
  public double y;
  public double z;
  public double a;

  public Point3D() {
    this.x = 0.0;
    this.y = 0.0;
    this.z = 0.0;
    this.a = 0.0;
  }
  public Point3D(double x, double y, double z, double a) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.a = a;
  }
  public Point3D(double x, double y, double z) {
    this(x, y, z, 1.0);
  }

  /**
   * Convenience method, computes the dot product of two vectors, both
   * encoded in point objects.
   *
   * @param p1 first vector
   * @param p2 second vector
   * @return the dot product of the two vectors
   */
  public static double dotProduct(Point3D p1, Point3D p2) {
    return (p1.x * p2.x) + (p1.y * p2.y) +(p1.z * p2.z);
  }

  /**
   * Convenience method.  Normalizes a vector, encoded as a point object
   * @param v the vector to normalize
   */
  public static void normalizeVector(Point3D v) {
    double length = Math.sqrt((v.x * v.x) + (v.y * v.y) + (v.z * v.z));
    v.x = v.x / length;
    v.y = v.y / length;
    v.z = v.z / length;
  }

  /**
   * returns string to look like:
   * <pre>
   * (1, 2, 3, 4)
   * </pre>
   */
  public String toString() {
    return "(" + x + ", " + y + ", " + z + ", " + a + ")";
  }
}