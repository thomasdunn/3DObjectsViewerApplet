package com.dunncom.graphics;

import java.io.IOException;
import java.io.BufferedReader;
import java.util.Vector;
import java.util.StringTokenizer;

/**
 * For Parsing .off 3D object files where vertices are 1 based rather than
 * the usual 0 based.  Generates Polyhedron instances from them.
 * Assumptions made, probably incorrect, about .off files after quick examination:
 * <UL>
 * <LI>The first line is a non-blank header line
 * <LI>One blank line separates the header lines and the vertices
 * <LI>One blank line separates the vertices and the faces
 * <LI>vertices are just three doubles on a line
 * <LI>faces are the number of points, then the point numbers
 * <LI>the last line of the file is the last face
 * </UL>
 *
 * @author Thomas Dunn
 */

public class OFOReader extends OFFReader {

  public OFOReader(BufferedReader reader) {
    super(reader);
  }

  /**
   * Parses a line, interpreting it as a face
   */
  public void parseFaceLine(String line) {
    Vector pointsVector = new Vector();
    String token = "";

    st = new StringTokenizer(line);

    // discard one token.  This one tells us how many vertices there are in this face
    // but we'll know that just from the vector's size
    st.nextToken();

    while (st.hasMoreTokens()) {
      token = st.nextToken();
      pointsVector.addElement(token);
    }

    Point3D points[] = new Point3D[pointsVector.size()];
    for (int i = 0; i < pointsVector.size(); i++) {
      points[i] = (Point3D) vertices.elementAt(Integer.parseInt((String) pointsVector.elementAt(i)) - 1);
    }

    Polygon face = new Polygon(points);
    polyhedron.addFace(face);
  }
}