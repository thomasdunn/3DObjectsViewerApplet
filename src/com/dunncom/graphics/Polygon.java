package com.dunncom.graphics;

import java.awt.*;

/**
 * A simple polygon representation.  Consists of collection of points
 * To create and define a polygon, just create a new Polygon object
 * passing as a parameter an array of Point objects, the vertices of the
 * polygon.
 *
 * @author Thomas Dunn
 */

public class Polygon {

  /**
   * Color of this face after illumination
   */
  public Color color;

  /**
   * color of this face before illumination
   */
  public Color baseColor;

  /**
   * The illumination level for light 1
   * Use formula I1 = 0.1 + 0.9 x L1�N
   */
  public double I1;

  /**
   * The illumination level for light 2
   * Use formula I2 = 0.1 + 0.9 x L2�N
   */
  public double I2;


  /**
   * The light source 1 unit vector, from right and in front of
   */
  public Point3D L1 = new Point3D(1, 0, 1);

  /**
   * The light source 1 unit vector, from slightly left and in front of
   */
  public Point3D L2 = new Point3D(-.3, 0, 1);

  /**
   * first point of unit vector in z direction
   */
  public Point3D u1 = new Point3D(0.0, 0.0, 0.0);
  /**
   * second point of unit vector in z direction
   */
  public Point3D u2 = new Point3D(0.0, 0.0, 1.0);

  public Point3D points[];

  /**
   * Plane Co-efficient
   */
  public double A;

  /**
   * Plane Co-efficient
   */
  public double B;

  /**
   * Plane Co-efficient
   */
  public double C;

  /**
   * Plane Co-efficient
   */
  public double D;

  /**
   * whether this is a shaded filled polygon or a wireframe
   */
  private boolean filled;


  public Polygon(Point3D points[]) {
    this.points = points;
    Point3D.normalizeVector(L1);
    Point3D.normalizeVector(L2);
    computeNormal();
  }

  public Color getColor() {
    return color;
  }
  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Computes the illumination of this face
   */
  public void computeIllumination() {
    Point3D N = new Point3D(A, B, C);
    Point3D.normalizeVector(N);
    I1 = Point3D.dotProduct(L1, N);
    I2 = Point3D.dotProduct(L2, N);
  }

  /**
   * polygon draws itself
   * @param g A graphics context object
   */
  public void draw(Graphics g) {
    int verticesX[] = new int[points.length];
    int verticesY[] = new int[points.length];
    for (int i = 0; i < points.length; i++) {
      verticesX[i] = (int) points[i].x;
      verticesY[i] = (int) points[i].y;
    }

    computeIllumination();

    double I = (I1 + I2) / 2;

    color = new Color((int)(Math.abs(I) * 255),
                      (int)(Math.abs(I) * 255),
                      (int)(Math.abs(I) * 255));

    // if polygon should be filled, fill it in
    if (filled) {
      g.setColor(color);
      g.fillPolygon(verticesX, verticesY, verticesX.length);
    }

    // draw black wire frames
    g.setColor(Color.black);
    g.drawPolygon(verticesX, verticesY, verticesX.length);
  }

  public void updateL1(Point3D p) {
    this.L1 = new Point3D(p.x, p.y, p.z);
    Point3D.normalizeVector(L1);
  }
  public void updateL2(Point3D p) {
    this.L2 = new Point3D(p.x, p.y, p.z);
    Point3D.normalizeVector(L2);
  }

  /**
   * Computes the plane co-efficients for this polygon.  The
   * sign of the z-component of the normal is the same as the
   * sign of C
   */
  public void computeNormal() {
    A = (points[0].y * (points[1].z - points[2].z)) +
        (points[1].y * (points[2].z - points[0].z)) +
        (points[2].y * (points[0].z - points[1].z));

    B = (points[0].z * (points[1].x - points[2].x)) +
        (points[1].z * (points[2].x - points[0].x)) +
        (points[2].z * (points[0].x - points[1].x));

    C = (points[0].x * (points[1].y - points[2].y)) +
        (points[1].x * (points[2].y - points[0].y)) +
        (points[2].x * (points[0].y - points[1].y));

    D = (-1*points[0].x * ((points[1].y * points[2].z) - (points[2].y * points[1].z))) -
        (points[1].x * ((points[2].y * points[0].z) - (points[0].y * points[2].z))) -
        (points[2].x * ((points[0].y * points[1].z) - (points[1].y * points[0].z)));
  }

  /**
   * If set to true, will fill and shade the polygon
   * If set to false, polygon will be wireframed
   */
  public void setFilled(boolean filled) {
    this.filled = filled;
  }

  public String toString() {
    String string = "Polygon:\n";
    for (int i = 0; i < points.length; i++) {
      string += points[i].toString() + "\n";
    }
    return string;
  }
}