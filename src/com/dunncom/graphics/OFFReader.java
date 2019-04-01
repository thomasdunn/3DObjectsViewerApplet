package com.dunncom.graphics;

import java.io.IOException;
import java.io.BufferedReader;
import java.util.Vector;
import java.util.StringTokenizer;

/**
 * For Parsing .off 3D object files.  Generates Polyhedron instances from them.
 * Assumptions made, probably incorrect, about .off files after quick examination:
 * &lt;UL&gt;
 * &lt;LI&gt;The first line is a non-blank header line
 * &lt;LI&gt;One blank line separates the header lines and the vertices
 * &lt;LI&gt;One blank line separates the vertices and the faces
 * &lt;LI&gt;vertices are just three doubles on a line
 * &lt;LI&gt;faces are the number of points, then the point numbers
 * &lt;LI&gt;the last line of the file is the last face
 * &lt;/UL&gt;
 *
 * @author Thomas Dunn
 */

public class OFFReader extends ThreeDimensionalObjectFileReader {

  public OFFReader(BufferedReader file) {
    vertices = new Vector();

    fileReader = file;
  }

  /**
   * Pass through text at beginning of file, possibly parsing, possibly skipping
   * until vertices are encountered
   *
   * @return the first line representing a vertex
   */
  public String findVertices() {
    String line = null;

    try {
      while((line = fileReader.readLine()) != null) {
        line = line.trim();

        // keep looking until we find a blank line
        if (line.length() != 0) {
          continue;
        }
        // skip the line if it is a comment line
        else {
          break;
        }
      }

      // we have reached a blank line, so read one more and return it
      // as the first vertex line
      line = fileReader.readLine();
    }
    catch (IOException ioe) {
      System.out.println("Caught IOException in ThreeDimensionalObjectFileReader in findVertices()");
      System.out.println(ioe);
      ioe.printStackTrace();
    }
    return line;
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
        // if it is a blank line, we have reached end of vertices
        line = line.trim();
        if (line.length() == 0) {
          break;
        }

        parseVertexLine(line);
      }

      // parse faces
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
}