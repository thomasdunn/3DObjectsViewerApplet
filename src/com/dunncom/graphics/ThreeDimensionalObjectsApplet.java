package com.dunncom.graphics;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Applet for showing off the capabilities of my Matrix3D, Point3D, Polygon, and Polyhedron classes
 * Allows user to pick different objects (such as dodecahedrons and icosahedrons) for viewing.
 * Allows user to rotate the objects with mouse or to auto-rotate.  Also allows wireframe
 * viewing or filled polygons with shading
 *
 * @author Thomas Dunn
 */
public class ThreeDimensionalObjectsApplet extends Applet implements Runnable, ItemListener, MouseMotionListener {

  /**
   * Thread for continuously repainting after a delay
   */
  Thread paintThread;

  /**
   * Rotation matrix for when object is rotating
   */
  Matrix3D rotateAroundPoint;

  /**
   * The object
   */
  Polyhedron polyhedron;

  /**
   * The "center" of the object
   */
  Point3D center;

  /**
   * For reading object files
   */
  BufferedReader fileReader;

  /**
   * Offscreen image for double buffering
   */
  Image offScreenImage;

  /**
   * Offscreen graphics for double buffering
   */
  Graphics offScreenGraphics;

  /**
   * Checkbox component for whether object is filled or not
   */
  Checkbox filledCheckbox;

  /**
   * For whether object is filled or not
   */
  boolean filled = true;

  /**
   * Checkbox component for whether object is rotating or not
   */
  Checkbox rotateCheckbox;

  /**
   * For whether object is rotating or not
   */
  boolean rotate = true;

  /**
   * List of models
   */
  Choice modelList = new Choice();

  /**
   * Delay in milliseconds between object repaints
   */
  final int DELAY = 50;

  /**
   * If a certain model can be filled or not
   */
  Hashtable canFill = new Hashtable();

  /**
   * whether we are painting or not
   */
  boolean painting = false;

  // for mouse dragging rotation of object
  Matrix3D yRotatePos;
  Matrix3D yRotateNeg;
  Matrix3D xRotatePos;
  Matrix3D xRotateNeg;
  int lastX;
  int lastY;

  /**
   * Sets up GUI and creates inital 3d object
   */
  public void init() {

    try {
      this.showStatus("Reading object manifest file...");
      URL manifestURL = new URL(this.getCodeBase() + "models/manifest");
      URLConnection manifestConnection = manifestURL.openConnection();
      fileReader = new BufferedReader(new InputStreamReader(manifestConnection.getInputStream()));
      parseManifest(fileReader);
      this.showStatus("Done reading object manifest.");
    }
    catch (MalformedURLException murle) {
      System.out.println("Caught MalformedURLException in ThreeDimensionalObjectsApplet, init()");
      System.out.println(murle);
      murle.printStackTrace();
    }
    catch (IOException ioe) {
      System.out.println("Caught IOException in ThreeDimensionalObjectsApplet, init()");
      System.out.println(ioe);
      ioe.printStackTrace();
    }

    setLayout(new BorderLayout());
    setBackground(new Color(240, 240, 240));

    offScreenImage = createImage(this.size().width,this.size().height);
    offScreenGraphics = offScreenImage.getGraphics();

    Panel controlPanel = new Panel();
    filledCheckbox = new Checkbox("Fill Object? ", filled);
    filledCheckbox.addItemListener(this);

    rotateCheckbox = new Checkbox("Rotate Object? ", rotate);
    rotateCheckbox.addItemListener(this);

    // for mouse dragging rotation
    double rotationAmount = 0.08;
    center = new Point3D(this.size().width / 2.0,
                         this.size().height / 2.0,
                         this.size().width / 2.0);
    yRotatePos = Matrix3D.createRotateAroundPointMatrix(center, 0.0, rotationAmount, 0.0);
    xRotatePos = Matrix3D.createRotateAroundPointMatrix(center, rotationAmount, 0.0, 0.0);
    yRotateNeg = Matrix3D.createRotateAroundPointMatrix(center, 0.0, -rotationAmount, 0.0);
    xRotateNeg = Matrix3D.createRotateAroundPointMatrix(center, -rotationAmount, 0.0, 0.0);
    lastX = this.size().width / 2;
    lastY = this.size().height / 2;
    this.addMouseMotionListener(this);

    modelList.addItemListener(this);

    controlPanel.add(filledCheckbox);
    controlPanel.add(rotateCheckbox);
    controlPanel.add(new Label("Select a model:"));
    controlPanel.add(modelList);
    this.add(BorderLayout.NORTH, controlPanel);
  }

  void generatePolyhedron(String modelFilename) {

    this.showStatus("Reading " + modelFilename + ".  This may take a while...");

    // in case the paint thread is running, shut it down temporarily while
    // the polyhedron is reconstructed
    stop();
    try {
      Thread.sleep(DELAY * 2);
    }
    catch (InterruptedException ie) {}

    if (modelFilename != null) {
      try {
        URL objectURL = new URL(this.getCodeBase() + "models/" + modelFilename);
        URLConnection objectConnection = objectURL.openConnection();
        fileReader = new BufferedReader(new InputStreamReader(objectConnection.getInputStream()));
      }
      catch (MalformedURLException murle) {
        System.out.println("Caught MalformedURLException in ThreeDimensionalObjectsApplet, init()");
        System.out.println(murle);
        murle.printStackTrace();
      }
      catch (IOException ioe) {
        System.out.println("Caught IOException in ThreeDimensionalObjectsApplet, init()");
        System.out.println(ioe);
        ioe.printStackTrace();
      }
      ThreeDimensionalObjectFileReader objectReader;

      // read the object file with the correct file format reader
      String extension = modelFilename.substring(modelFilename.indexOf("."));
      if (extension.equals(".nff")) {
        objectReader = new NFFReader(fileReader);
      }
      else if (extension.equals(".nfz")) {
        objectReader = new NFZReader(fileReader);
      }
      else if (extension.equals(".off")) {
        objectReader = new OFFReader(fileReader);
      }
      else if (extension.equals(".obj")) {
        objectReader = new OBJReader(fileReader);
      }
      else if (extension.equals(".ofo")) {
        objectReader = new OFOReader(fileReader);
      }
      else {
        showStatus("File format " + extension + " not recognized.");
        return;
      }

      polyhedron = objectReader.parse();
    }
    else {
      polyhedron = Polyhedron.createPyramid5Polyhedron();
    }

    this.showStatus("Done reading " + modelFilename + ".  Scaling object.  This may take a while...");

    // figure out how much to scale the polyhedron by
    Point3D rangePoint = polyhedron.range();
    double range = 0.0;
    double scaleAmount = 1.0;
    range = Math.max(rangePoint.x, rangePoint.y);
    range = Math.max(rangePoint.z, range);
    int width = ((Dimension)this.getSize()).width;
    scaleAmount = (width * 0.66) / range;

    //System.out.println(polyhedron);

    // scale the polyhedron
    Matrix3D scale = Matrix3D.createScalingMatrix(scaleAmount, scaleAmount, scaleAmount);
    scale.transformPolyhedron(polyhedron);

    this.showStatus("Done scaling.  Now centering object.  This may take a while...");

    // move the polyhedron into the center of the viewing area
    center = polyhedron.averagePoint();
    Matrix3D translate = Matrix3D.createTranslationMatrix(-center.x + (width / 2.0), -center.y + (width / 2.0), -center.z + (width / 2.0));
    translate.transformPolyhedron(polyhedron);

    //System.out.println(polyhedron);

    // create auto-rotation matrix
    center = polyhedron.averagePoint();
    rotateAroundPoint = Matrix3D.createRotateAroundPointMatrix(center,
                                                       0.03,
                                                       0.015,
                                                       0.005);
    if (modelFilename != null) {
      if(! ((Boolean) canFill.get(modelFilename)).booleanValue()) {
        filled = false;
      }
      else {
        filled = true;
      }
      filledCheckbox.setState(filled);
    }
    polyhedron.setFilled(filled);

    // start the paintThread up painting again
    startThread();

    this.showStatus("Object construction complete.");
  }

  /**
   * Paints the object
   */
  public void paint(Graphics g) {
    // clear background
    offScreenGraphics.setColor(this.getBackground());
    offScreenGraphics.fillRect(0, 0, this.size().width, this.size().height);

    // draw the object
    polyhedron.draw(offScreenGraphics);

    // display credits
    showInfo(offScreenGraphics, "3D Objects Viewer Applet, Copyright (C) 2001 Thomas J. Dunn");

    // show it on the screen
    g.drawImage(offScreenImage, 0, 0, this);
  }

  public void update(Graphics g) {
    paint(g);
  }

  /**
   * Overriding applet's start method
   */
  public void start() {
    String startModel = getParameter("startModel");
    modelList.select(startModel);
    generatePolyhedron(startModel);
    startThread();
  }

  public void startThread() {
    painting = true;
    paintThread = new Thread(this);
    paintThread.start();
  }

  /**
   * Overriding applet's stop method
   */
  public void stop() {
    painting = false;
  }

  /**
   * For running the paint thread
   */
  public void run() {
    while (painting) {
      try {
        Thread.sleep(DELAY);
      }
      catch (InterruptedException e) {}

      // transform the object if we are rotating
      if (rotate) {
        rotateAroundPoint.transformPolyhedron(polyhedron);
      }

      // and draw its updated position
      repaint();
    }
  }

  /**
   * For acting on checkbox clicks
   */
  public void itemStateChanged(ItemEvent ie) {
    if (ie.getSource() == rotateCheckbox) {
      rotate = ! rotate;
    }
    else if (ie.getSource() == filledCheckbox) {
      filled = ! filled;
      polyhedron.setFilled(filled);
    }
    else if (ie.getSource() == modelList) {
      String objectFilename = modelList.getSelectedItem();
      generatePolyhedron(objectFilename);
    }
  }

  /**
   * Respond to rotating the object via mouse drags
   */
  public void mouseDragged(MouseEvent me) {

    if (rotate) {
      rotate = false;
      rotateCheckbox.setState(rotate);
    }

    if (me.getX() < lastX) {
      yRotatePos.transformPolyhedron(polyhedron);
    }
    else if (me.getX() > lastX) {
      yRotateNeg.transformPolyhedron(polyhedron);
    }

    if (me.getY() < lastY) {
      xRotateNeg.transformPolyhedron(polyhedron);
    }
    else if (me.getY() > lastY) {
      xRotatePos.transformPolyhedron(polyhedron);
    }
    lastX = me.getX();
    lastY = me.getY();
    repaint();
  }
  public void mouseMoved(MouseEvent me) {}

  /**
   * display info to the user at the bottom of the applet
   * @param g graphics context
   * @param info information to display
   */
  void showInfo(Graphics g, String info) {
    FontMetrics fm = g.getFontMetrics();
    int typeSize = fm.getAscent() + fm.getDescent();
    g.drawString(info, 5, this.getSize().height - typeSize);
  }

  void parseManifest(BufferedReader fileReader) {
    String line = "";
    StringTokenizer st;

    try {
      while ((line = fileReader.readLine()) != null) {
        st = new StringTokenizer(line);
        String filename = "";

        // if there are more than one token, that means we have marked the file as
        // not to be filled, drop the marking token
        if (st.countTokens() > 1) {
          st.nextToken();
          filename = st.nextToken();
          canFill.put(filename, new Boolean(false));
        }
        else {
          filename = st.nextToken();
          canFill.put(filename, new Boolean(true));
        }
        modelList.add(filename);
      }
    }
    catch (IOException ioe) {
      System.out.println("Caught IOException in ThreeDimensionalObjectsApplet, parseManifest()");
      System.out.println(ioe);
      ioe.printStackTrace();
    }
  }
}