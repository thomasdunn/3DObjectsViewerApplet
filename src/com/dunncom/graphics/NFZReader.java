package com.dunncom.graphics;

import java.io.IOException;
import java.io.BufferedReader;
import java.util.Vector;
import java.util.StringTokenizer;

/**
 * For Parsing .nff 3D object files where the vertices are numbered based on zero (teapot.nff).
 * Generates Polyhedron instances from them.
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

public class NFZReader extends NFFReader {

  public NFZReader(BufferedReader file) {
    super(file);
    vertices.removeAllElements();
  }
}