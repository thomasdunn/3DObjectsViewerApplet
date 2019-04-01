package com.dunncom.graphics;

import java.io.IOException;
import java.io.BufferedReader;
import java.util.Vector;
import java.util.StringTokenizer;

/**
 * For Parsing .obj 3D object files.  Generates Polyhedron instances from them.
 * Assumptions made, probably incorrect, about .obj files after quick examination:
 * <UL>
 * <LI>There can be blank lines at the beginning of the file
 * <LI>The first non-blank line is the first vertex line
 * <LI>There are no lines between the last vertex line and the first face line
 * <LI>vertices are a 'v' and three doubles on a line
 * <LI>faces are an 'f', then the point numbers
 * <LI>the last line of the file is the last face
 * </UL>
 *
 * @author Thomas Dunn
 */

public class OBJReader extends ThreeDimensionalObjectFileReader {

  public OBJReader(BufferedReader file) {
    super(file);
  }

  /**
   * Parses the file contents and returns the Polyhedron represented by the file
   */
  public Polyhedron parse() {
    polyhedron = new Polyhedron();
    String line = null;

    try {

      // parse vertices
      line = findVertices();
      parseVertexLine(line);
      while ((line = fileReader.readLine()) != null) {
        if (line.charAt(0) == 'f') {
          break;
        }
        parseVertexLine(line);
      }

      // parse faces
      parseFaceLine(line);
      while ((line = fileReader.readLine()) != null) {
        parseFaceLine(line);
      }
    }
    catch (IOException ioe) {
      System.out.println("Caught IOException in ThreeDimensionalObjectFileReader in parse()");
      System.out.println(ioe);
      ioe.printStackTrace();
    }

    return polyhedron;
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
      points[i] = (Point3D) vertices.elementAt(Integer.parseInt((String) pointsVector.elementAt(i)));
    }

    Polygon face = new Polygon(points);
    polyhedron.addFace(face);
  }

  /**
   * Parses a line, interpreting it as a vertex
   */
  public void parseVertexLine(String line) {
    double x = 0.0;
    double y = 0.0;
    double z = 0.0;

    st = new StringTokenizer(line);

    // discard one token.  This one tells us it is a face line,
    // but we already knew that
    st.nextToken();
    x = new Double(st.nextToken()).doubleValue();
    y = new Double(st.nextToken()).doubleValue();
    z = new Double(st.nextToken()).doubleValue();

    Point3D vertex = new Point3D(x, y, z);
    vertices.addElement(vertex);
  }
}