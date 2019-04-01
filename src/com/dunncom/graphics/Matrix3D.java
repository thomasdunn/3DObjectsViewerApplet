package com.dunncom.graphics;

/**
 * 3-dimensional Matrix class.  Contains methods for translation and rotation
 * transformations.  Also contains methods for translating different objects,
 * such as Point3Ds, Polygons, and Polyhedrons.
 *
 * @author Thomas Dunn
 */

public class Matrix3D {

  private double[][] matrix;

  /**
   * Create a new 4x4 zero matrix
   */
  public Matrix3D() {
    matrix = new double[4][];
    for (int i = 0; i < 4; i++) {
      matrix[i] = new double[4];
    }
  }

  /**
   * obtain a zero matrix
   * @return Matrix3D object representing a zero matrix
   */
  public static Matrix3D createZeroMatrix() {
    return new Matrix3D();
  }

  /**
   * obtain a unit matrix
   * @return Matrix3D object representing a unit matrix
   */
  public static Matrix3D createUnitMatrix() {
    Matrix3D matrix = new Matrix3D();
    matrix.matrix[0][0] = 1.0;
    matrix.matrix[0][1] = 1.0;
    matrix.matrix[0][2] = 1.0;
    matrix.matrix[0][3] = 1.0;

    matrix.matrix[1][0] = 1.0;
    matrix.matrix[1][1] = 1.0;
    matrix.matrix[1][2] = 1.0;
    matrix.matrix[1][3] = 1.0;

    matrix.matrix[2][0] = 1.0;
    matrix.matrix[2][1] = 1.0;
    matrix.matrix[2][2] = 1.0;
    matrix.matrix[2][3] = 1.0;

    matrix.matrix[3][0] = 1.0;
    matrix.matrix[3][1] = 1.0;
    matrix.matrix[3][2] = 1.0;
    matrix.matrix[3][3] = 1.0;

    return matrix;
  }

  /**
   * obtain a translation matrix
   * @param tx translation amount in x
   * @param ty translation amount in y
   * @param tz translation amount in z
   * @return Matrix3D object representing a translation matrix
   */
  public static Matrix3D createTranslationMatrix(double tx, double ty, double tz) {
    Matrix3D matrix = new Matrix3D();
    matrix.matrix[0][0] = 1.0;
    matrix.matrix[0][1] = 0.0;
    matrix.matrix[0][2] = 0.0;
    matrix.matrix[0][3] = tx;

    matrix.matrix[1][0] = 0.0;
    matrix.matrix[1][1] = 1.0;
    matrix.matrix[1][2] = 0.0;
    matrix.matrix[1][3] = ty;

    matrix.matrix[2][0] = 0.0;
    matrix.matrix[2][1] = 0.0;
    matrix.matrix[2][2] = 1.0;
    matrix.matrix[2][3] = tz;

    matrix.matrix[3][0] = 0.0;
    matrix.matrix[3][1] = 0.0;
    matrix.matrix[3][2] = 0.0;
    matrix.matrix[3][3] = 1.0;

    return matrix;
  }

  /**
   * obtain a scaling matrix
   * @param sx scale amount in x
   * @param sy scale amount in y
   * @param sz scale amount in z
   * @return Matrix3D object representing a scale matrix
   */
  public static Matrix3D createScalingMatrix(double sx, double sy, double sz) {
    Matrix3D matrix = new Matrix3D();
    matrix.matrix[0][0] = sx;
    matrix.matrix[0][1] = 0.0;
    matrix.matrix[0][2] = 0.0;
    matrix.matrix[0][3] = 0.0;

    matrix.matrix[1][0] = 0.0;
    matrix.matrix[1][1] = sy;
    matrix.matrix[1][2] = 0.0;
    matrix.matrix[1][3] = 0.0;

    matrix.matrix[2][0] = 0.0;
    matrix.matrix[2][1] = 0.0;
    matrix.matrix[2][2] = sz;
    matrix.matrix[2][3] = 0.0;

    matrix.matrix[3][0] = 0.0;
    matrix.matrix[3][1] = 0.0;
    matrix.matrix[3][2] = 0.0;
    matrix.matrix[3][3] = 1.0;

    return matrix;
  }

  /**
   * obtain a x-rotation matrix
   * @param theta radians to rotate around x
   * @return Matrix3D object representing x-rotation
   */
  public static Matrix3D createXRotationMatrix(double theta) {
    Matrix3D matrix = new Matrix3D();
    matrix.matrix[0][0] = 1.0;
    matrix.matrix[0][1] = 0.0;
    matrix.matrix[0][2] = 0.0;
    matrix.matrix[0][3] = 0.0;

    matrix.matrix[1][0] = 0.0;
    matrix.matrix[1][1] = Math.cos(theta);
    matrix.matrix[1][2] = -Math.sin(theta);
    matrix.matrix[1][3] = 0.0;

    matrix.matrix[2][0] = 0.0;
    matrix.matrix[2][1] = Math.sin(theta);
    matrix.matrix[2][2] = Math.cos(theta);
    matrix.matrix[2][3] = 0.0;

    matrix.matrix[3][0] = 0.0;
    matrix.matrix[3][1] = 0.0;
    matrix.matrix[3][2] = 0.0;
    matrix.matrix[3][3] = 1.0;

    return matrix;
  }

  /**
   * obtain a y-rotation matrix
   * @param theta radians to rotate around y
   * @return Matrix3D object representing y-rotation
   */
  public static Matrix3D createYRotationMatrix(double theta) {
    Matrix3D matrix = new Matrix3D();
    matrix.matrix[0][0] = Math.cos(theta);
    matrix.matrix[0][1] = 0.0;
    matrix.matrix[0][2] = Math.sin(theta);
    matrix.matrix[0][3] = 0.0;

    matrix.matrix[1][0] = 0.0;
    matrix.matrix[1][1] = 1.0;
    matrix.matrix[1][2] = 0.0;
    matrix.matrix[1][3] = 0.0;

    matrix.matrix[2][0] = -Math.sin(theta);
    matrix.matrix[2][1] = 0.0;
    matrix.matrix[2][2] = Math.cos(theta);
    matrix.matrix[2][3] = 0.0;

    matrix.matrix[3][0] = 0.0;
    matrix.matrix[3][1] = 0.0;
    matrix.matrix[3][2] = 0.0;
    matrix.matrix[3][3] = 1.0;

    return matrix;
  }

  /**
   * obtain a z-rotation matrix
   * @param theta radians to rotate around z
   * @return Matrix3D object representing z-rotation
   */
  public static Matrix3D createZRotationMatrix(double theta) {
    Matrix3D matrix = new Matrix3D();
    matrix.matrix[0][0] = Math.cos(theta);
    matrix.matrix[0][1] = -Math.sin(theta);
    matrix.matrix[0][2] = 0.0;
    matrix.matrix[0][3] = 0.0;
    matrix.matrix[1][0] = Math.sin(theta);
    matrix.matrix[1][1] = Math.cos(theta);
    matrix.matrix[1][2] = 0.0;
    matrix.matrix[1][3] = 0.0;

    matrix.matrix[2][0] = 0.0;
    matrix.matrix[2][1] = 0.0;
    matrix.matrix[2][2] = 1.0;
    matrix.matrix[2][3] = 0.0;

    matrix.matrix[3][0] = 0.0;
    matrix.matrix[3][1] = 0.0;
    matrix.matrix[3][2] = 0.0;
    matrix.matrix[3][3] = 1.0;

    return matrix;
  }

  /**
   * creates a "Rotation Around Point p" matrix
   * @param theta the radians to rotate
   * @param p a Point object to rotate around
   * @return a new composite matrix
   */
  public static Matrix3D createRotateAroundPointMatrix(Point3D p,
                                                       double thetaX,
                                                       double thetaY,
                                                       double thetaZ) {
    Matrix3D transNeg = Matrix3D.createTranslationMatrix(-p.x, -p.y, -p.z);
    Matrix3D rotateX = Matrix3D.createXRotationMatrix(thetaX);
    Matrix3D rotateY = Matrix3D.createYRotationMatrix(thetaY);
    Matrix3D rotateZ = Matrix3D.createZRotationMatrix(thetaZ);
    Matrix3D transPos = Matrix3D.createTranslationMatrix(p.x, p.y, p.z);

    Matrix3D composite = Matrix3D.mult(rotateZ, transNeg);
    composite = Matrix3D.mult(rotateY, composite);
    composite = Matrix3D.mult(rotateX, composite);
    composite = Matrix3D.mult(transPos, composite);

    return composite;
  }

  public void transformPolygon(Polygon poly) {
    for (int i = 0; i < poly.points.length; i++) {
      this.transformPoint(poly.points[i]);
    }
    poly.computeNormal();
  }

  public void transformPolyhedron(Polyhedron poly) {
    // transform all the points of the polyhedron
    for (int i = 0; i < poly.points.size(); i++) {
      this.transformPoint((Point3D)poly.points.elementAt(i));
    }
    // recompute the normals of all the faces in the polyhedron
    for (int i = 0; i < poly.faces.size(); i++) {
      ((Polygon)poly.faces.elementAt(i)).computeNormal();
    }
  }

  /**
   * Transforms a point by this matrix
   * @param point the point to transform
   */
  public void transformPoint(Point3D point) {
    double oldPoint[] = {point.x, point.y, point.z, point.a};
    double newPoint[] = new double[4];
    for (int i = 0; i < 4; i++) {
      newPoint[i] = 0.0;
    }

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        newPoint[i] += this.matrix[i][j] * oldPoint[j];
      }
    }
    point.x = newPoint[0];
    point.y = newPoint[1];
    point.z = newPoint[2];
    point.a = newPoint[3];
  }

  /**
   * Multiplies the two matrices and returns the result
   * @param m2 the first matrix
   * @param m2 the second matrix
   * @return the new composite matrix
   */
  public static Matrix3D mult(Matrix3D m1, Matrix3D m2) {
    Matrix3D m3 = new Matrix3D();

    for (int i = 0; i <  4; i++) {
      for (int j = 0; j <  4; j++) {
        for (int k = 0; k <  4; k++) {
          m3.matrix[j][i] += m1.matrix[j][k] * m2.matrix[k][i];
        }
      }
    }

    return m3;
  }

  /**
   * Prints the matrix out to look like:
   * <pre>
   * [1, 0, 0, 0]
   * [0, 1, 0, 0]
   * [0, 0, 1, 0]
   * [0, 0, 0, 1]
   * </pre>
   */
  public String toString() {
    String string = "";
    for (int i = 0; i < 4; i++) {
      string += "[" + matrix[i][0] + ", " + matrix[i][1] + ", " + matrix[i][2] + ", " + matrix[i][3] + "]\n";
    }
    return string;
  }

}