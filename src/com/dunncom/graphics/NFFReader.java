package com.dunncom.graphics;

import java.io.IOException;
import java.io.BufferedReader;
import java.util.Vector;
import java.util.StringTokenizer;

/**
 * For Parsing .nff 3D object files.  Generates Polyhedron instances from them.
 * Assumptions made, probably incorrect, about .nff files after quick examination:
 * <UL>
 * <LI>There can be blank lines or comment lines '//' at the beginning of the file
 * <LI>The first non-blank, non-comment line is the number of following lines that are vertices
 * <LI>The line after the last vertex line is the number of following lines that are faces
 * <LI>the last line of the file is the last face
 * </UL>
 *
 * @author Thomas Dunn
 */

public class NFFReader extends ThreeDimensionalObjectFileReader {

  public NFFReader(BufferedReader file) {
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
      int numVertices = Integer.parseInt(line);
      for (int i = 0; i < numVertices; i++) {
        line = fileReader.readLine();
        parseVertexLine(line);
      }

      // parse faces
      line = findFaces();
      int numFaces = Integer.parseInt(line);
      for (int i = 0; i < numFaces; i++) {
        line = fileReader.readLine();
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
}