package com.dunncom.graphics;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Vector;
import java.util.StringTokenizer;

/**
 * For Parsing 3D object files.  Generates Polyhedron instances from them.
 *
 * @author Thomas Dunn
 */

public class ThreeDimensionalObjectFileReader {

  /**
   * Reader object for reading the file
   */
  protected BufferedReader fileReader;

  /**
   * Vector of the vertices of the polyhedron
   */
  protected Vector vertices;

  /**
   * To split the tokens on lines, delimited by whitespace
   */
  protected StringTokenizer st;

  /**
   * The Polyhedron we are building and will return to user
   */
  protected Polyhedron polyhedron;

  /**
   * Create a 3D object reader, based on a 3D object filename
   */
  public ThreeDimensionalObjectFileReader(BufferedReader file) {
    vertices = new Vector();

    // we dont want a vertex in the first spot because the object
    // files start numbering at 1
    vertices.addElement(null);

    fileReader = file;
  }

  public ThreeDimensionalObjectFileReader() {}

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
      while((line = fileReader.readLine()) != null) {
        parseVertexLine(line);
      }

      // parse faces
      line = findFaces();
      parseFaceLine(line);
      while((line = fileReader.readLine()) != null) {
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

        // skip the line if it is blank
        if (line.length() == 0) {
          continue;
        }
        // skip the line if it is a comment line
        else if (line.length() >= 2 &&
                 line.charAt(0) == '/' &&
                 line.charAt(1) == '/') {
          continue;
        }
        else {
          break;
        }
      }
    }
    catch (IOException ioe) {
      System.out.println("Caught IOException in ThreeDimensionalObjectFileReader in findVertices()");
      System.out.println(ioe);
      ioe.printStackTrace();
    }
    return line;
  }

  /**
   * Pass through text in between vertices and faces, possibly parsing, possibly skipping
   * until faces are encountered
   *
   * @return the first line representing a face
   */
  public String findFaces() {
    String line = null;

    try {
      while((line = fileReader.readLine()) != null) {
        line = line.trim();

        // skip the line if it is blank
        if (line.length() == 0) {
          continue;
        }
        // skip the line if it is a comment line
        else if (line.length() >= 2 &&
                 line.charAt(0) == '/' &&
                 line.charAt(1) == '/') {
          continue;
        }
        else {
          break;
        }
      }
    }
    catch (IOException ioe) {
      System.out.println("Caught IOException in ThreeDimensionalObjectFileReader in findVertices()");
      System.out.println(ioe);
      ioe.printStackTrace();
    }
    return line;
  }

  /**
   * Parses a line, interpreting it as a face
   */
  public void parseFaceLine(String line) {
    Vector pointsVector = new Vector();
    String token = "";

    st = new StringTokenizer(line);
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
    x = new Double(st.nextToken()).doubleValue();
    y = new Double(st.nextToken()).doubleValue();
    z = new Double(st.nextToken()).doubleValue();

    Point3D vertex = new Point3D(x, y, z);
    vertices.addElement(vertex);
  }
}