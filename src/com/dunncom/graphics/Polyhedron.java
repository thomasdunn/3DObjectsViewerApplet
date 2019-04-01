package com.dunncom.graphics;

import java.awt.Graphics;
import java.awt.Color;
import java.util.Vector;

/**
 * A simple polyhedron representation.  Consists of collection of surfaces
 * or faces, and a collection of points, the vertices of the faces.
 * To create and define a polyhedron, just create a new Polyhedron object
 * and then add its faces with the addFace(Polygon) method.  The faces are
 * just Polygon objects
 *
 * @author Thomas Dunn
 */
public class Polyhedron {

  /**
   * Collection of points that are the vertices of this Polyhedron
   */
  public Vector points;

  /**
   * Collection of Polygons that are the faces of this Polyhedron
   */
  public Vector faces;

  /**
   * whether this is a shaded filled polyhedron or a wireframe
   */
  private boolean filled;

  public Polyhedron() {
    points = new Vector();
    faces = new Vector();
  }

  /**
   * Adds a face to this Polyhedron.  The points of the polygon are in turn
   * added here too, and duplication of points is not an issue.
   * @param poly the polygon face you're adding to this Polyhedron
   */
  public void addFace(Polygon poly) {
    faces.addElement(poly);
    for (int i = 0; i < poly.points.length; i++) {
      if (! points.contains(poly.points[i])) {
        points.addElement(poly.points[i]);
      }
    }
  }

  public static Polyhedron createPyramid5Polyhedron() {
    Point3D point1 = new Point3D(0, 0, 1);
    Point3D point2 = new Point3D(-1.5, -1, 0);
    Point3D point3 = new Point3D(1.5, -1, 0);
    Point3D point4 = new Point3D(1.5, 1, 0);
    Point3D point5 = new Point3D(-1.5, 1, 0);

    Point3D face1Points[] = new Point3D[3];
    face1Points[0] = point1;
    face1Points[1] = point2;
    face1Points[2] = point5;

    Point3D face2Points[] = new Point3D[3];
    face2Points[0] = point1;
    face2Points[1] = point3;
    face2Points[2] = point2;

    Point3D face3Points[] = new Point3D[3];
    face3Points[0] = point1;
    face3Points[1] = point4;
    face3Points[2] = point3;

    Point3D face4Points[] = new Point3D[3];
    face4Points[0] = point1;
    face4Points[1] = point5;
    face4Points[2] = point4;

    Point3D face5Points[] = new Point3D[4];
    face5Points[0] = point2;
    face5Points[1] = point3;
    face5Points[2] = point4;
    face5Points[3] = point5;

    Polygon face1 = new Polygon(face1Points);
    Polygon face2 = new Polygon(face2Points);
    Polygon face3 = new Polygon(face3Points);
    Polygon face4 = new Polygon(face4Points);
    Polygon face5 = new Polygon(face5Points);

    Polyhedron pyramid = new Polyhedron();
    pyramid.addFace(face1);
    pyramid.addFace(face2);
    pyramid.addFace(face3);
    pyramid.addFace(face4);
    pyramid.addFace(face5);

    return pyramid;
  }

  public void updateL1(Point3D p) {
    for (int i = 0; i < faces.size(); i++) {
      ((Polygon)faces.elementAt(i)).updateL1(p);
    }
  }
  public void updateL2(Point3D p) {
    for (int i = 0; i < faces.size(); i++) {
      ((Polygon)faces.elementAt(i)).updateL2(p);
    }
  }

  public void draw(Graphics g) {
    for (int i = 0; i < this.faces.size(); i++) {
      // only draw it if it is facing us, or it is a wireframe (filled = false)
      Polygon face = (Polygon)faces.elementAt(i);
      if (! filled || face.C < 0) {
        face.draw(g);
      }
    }
  }

  /**
   * Yields the average point, or average x, y, and z values for all the
   * points in this polyhedron
   */
  public Point3D averagePoint() {
    double totalX = 0.0;
    double totalY = 0.0;
    double totalZ = 0.0;
    int numPoints = points.size();

    for (int i = 0; i < numPoints; i++) {
      totalX += ((Point3D) points.elementAt(i)).x;
      totalY += ((Point3D) points.elementAt(i)).y;
      totalZ += ((Point3D) points.elementAt(i)).z;
    }

    return new Point3D(totalX / numPoints, totalY / numPoints, totalZ / numPoints);
  }

  /**
   * yields the maximum x, y, and z values of all points in this polyhedron,
   * encoded in a Point3D object
   */
  public Point3D maximums() {
    double x = ((Point3D) points.elementAt(0)).x;
    double y = ((Point3D) points.elementAt(0)).y;
    double z = ((Point3D) points.elementAt(0)).z;

    for (int i = 0; i < points.size(); i++) {
      if (((Point3D) points.elementAt(i)).x > x) {
        x = ((Point3D) points.elementAt(i)).x;
      }
      if (((Point3D) points.elementAt(i)).y > y) {
        y = ((Point3D) points.elementAt(i)).y;
      }
      if (((Point3D) points.elementAt(i)).z > z) {
        z = ((Point3D) points.elementAt(i)).z;
      }
    }

    return new Point3D(x, y, z);
  }

  /**
   * yields the minimum x, y, and z values of all points in this polyhedron,
   * encoded in a Point3D object
   */
  public Point3D minimums() {
    double x = ((Point3D) points.elementAt(0)).x;
    double y = ((Point3D) points.elementAt(0)).y;
    double z = ((Point3D) points.elementAt(0)).z;

    for (int i = 0; i < points.size(); i++) {
      if (((Point3D) points.elementAt(i)).x < x) {
        x = ((Point3D) points.elementAt(i)).x;
      }
      if (((Point3D) points.elementAt(i)).y < y) {
        y = ((Point3D) points.elementAt(i)).y;
      }
      if (((Point3D) points.elementAt(i)).z < z) {
        z = ((Point3D) points.elementAt(i)).z;
      }
    }

    return new Point3D(x, y, z);
  }

  /**
   * yields the range for x, y, and z values between the maximums and the minimums
   * of all points in this polyhedron, encoded in a Point3D object
   */
  public Point3D range() {
    Point3D max = maximums();
    Point3D min = minimums();
    return new Point3D(max.x - min.x, max.y - min.y, max.z - min.z);
  }

  public void setFilled(boolean filled) {
    this.filled = filled;
    for (int i = 0; i < faces.size(); i++) {
      ((Polygon) faces.elementAt(i)).setFilled(filled);
    }
  }

  public String toString() {
    String string = "Polyhedron:\n";
    for (int i = 0; i < faces.size(); i++) {
      string += faces.elementAt(i).toString() + "\n";
    }
    return string;
  }
}
