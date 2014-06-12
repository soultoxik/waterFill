package com.company;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Contains OpenGL2.0 based 3D-visualisation
 */
class VisualModel implements GLEventListener {
    private final float CUBE_SIZE = 2.0f;
    private final GLU glu = new GLU();

    private float rAngle = 45.0f;
    private float xScale = 0.0f;
    private float yScale = 1.0f;
    private float zScale = 0.0f;
    private float distance = -20.0f;
    private GLCanvas canvas;
    private VisualModel visualModelContext;
    private List<List<Character>> map;
    private MouseController mouseController = new MouseController();


    /**
     *
     * Shows frame with 3D model of water filling
     * @param map two-dimension map of earth/water, extracted from WaterFill
     *            @see WaterFill#getData()
     */
    public void show(List<List<Character>> map) {
        final Frame frame = initFrame(map);
        addFrameListeners(frame);
    }

    /**
     * Adds close and resizing listeners
     * @param frame target frame
     */
    private void addFrameListeners(final Frame frame) {
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if(e.getNewState() == Frame.NORMAL){
                    frame.setSize(640,640);
                }
            }
        });
    }

    /**
     *
     * @param map two-dimension map of earth/water, extracted from WaterFill
     *            @see WaterFill#getData()
     * @return initialized frame
     */
    private Frame initFrame(List<List<Character>> map) {
        GLProfile defaultGLProfile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(defaultGLProfile);
        canvas = new GLCanvas(capabilities);

        final Frame frame = new Frame("Waterfill Model");
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.add(canvas);
        frame.setVisible(true);

        visualModelContext = new VisualModel();
        visualModelContext.map = map;
        canvas.addGLEventListener(visualModelContext);
        canvas.addMouseListener(mouseController);
        canvas.addMouseMotionListener(mouseController);
        canvas.addMouseWheelListener(mouseController);
        return frame;
    }

    /**
     * Overrode method of GLEventListener
     * Runs only once
     * Contains first-start initializations
     * @param glAutoDrawable
     */
    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {}

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        render(glAutoDrawable);
    }

    /**
     * Contains reshaping logic and init of GLU view
     * @param glAutoDrawable
     * @param x x-position of window
     * @param y y-position of window
     * @param width window's width
     * @param height window's height
     */
    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        if (height == 0) height = 1;
        float aspect = (float) width / height;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45, aspect, 0.1, 100.0);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    /**
     * Render's logic
     * @param glAutoDrawable
     */
    private void render(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, -3.0f, distance);

        gl.glRotatef(rAngle, xScale, yScale, zScale);

        int offset = map.size() / 2;

        for (int i = 0; i < map.size(); ++i) {
            for (int j = 0; j < map.get(i).size(); ++j) {
                renderCube(gl, (i - offset) * CUBE_SIZE, j * CUBE_SIZE, map.get(i).get(j));
            }
        }
    }

    /**
     * Method renders one cube with x and y positions given with color of given type
     * @param gl inited GL2
     * @param x x-position of left-bottom-front point of cube
     * @param y y-position of left-bottom-front point of cube
     * @param type type of cube
     *             @see com.company.WaterFill#BLOCK or
     *             @see com.company.WaterFill#EMPTY
     */
    private void renderCube(GL2 gl, float x, float y, Character type) {
        if (type == WaterFill.BLOCK) {
            gl.glColor3f(0.62f, 0.32f, 0.18f); //brown color
        } else if (type == WaterFill.WATER) {
            gl.glColor3f(0.0f, 0.0f, 1.0f); // blue color
        } else return;

        gl.glBegin(GL2.GL_QUADS);

        // Bottom-face
        gl.glVertex3f(x + CUBE_SIZE, y, -1.0f);
        gl.glVertex3f(x, y, -1.0f);
        gl.glVertex3f(x, y, 1.0f);
        gl.glVertex3f(x + CUBE_SIZE, y, 1.0f);

        // Bottom-face
        gl.glVertex3f(x + CUBE_SIZE, y + CUBE_SIZE, 1.0f);
        gl.glVertex3f(x, y + CUBE_SIZE, 1.0f);
        gl.glVertex3f(x, y + CUBE_SIZE, -1.0f);
        gl.glVertex3f(x + CUBE_SIZE, y + CUBE_SIZE, -1.0f);

        // Front-face
        gl.glVertex3f(x + CUBE_SIZE, y + CUBE_SIZE, 1.0f);
        gl.glVertex3f(x, y + CUBE_SIZE, 1.0f);
        gl.glVertex3f(x, y, 1.0f);
        gl.glVertex3f(x + CUBE_SIZE, y, 1.0f);

        // Back-face
        gl.glVertex3f(x + CUBE_SIZE, y + CUBE_SIZE, -1.0f);
        gl.glVertex3f(x, y + CUBE_SIZE, -1.0f);
        gl.glVertex3f(x, y, -1.0f);
        gl.glVertex3f(x + CUBE_SIZE, y, -1.0f);


        // Left-face
        gl.glVertex3f(x, y, 1.0f);
        gl.glVertex3f(x, y, -1.0f);
        gl.glVertex3f(x, y + CUBE_SIZE, -1.0f);
        gl.glVertex3f(x, y + CUBE_SIZE, 1.0f);

        // Right-face
        gl.glVertex3f(x + CUBE_SIZE, y, -1.0f);
        gl.glVertex3f(x + CUBE_SIZE, y, 1.0f);
        gl.glVertex3f(x + CUBE_SIZE, y + CUBE_SIZE, 1.0f);
        gl.glVertex3f(x + CUBE_SIZE, y + CUBE_SIZE, -1.0f);

        gl.glEnd();

        gl.glColor3f(1.0f, 1.0f, 1.0f); // white color
        gl.glBegin(GL2.GL_LINES);

        //Front lines
        gl.glVertex3f(x + CUBE_SIZE, y + CUBE_SIZE, 1.0f);
        gl.glVertex3f(x, y + CUBE_SIZE, 1.0f);

        gl.glVertex3f(x, y, 1.0f);
        gl.glVertex3f(x + CUBE_SIZE, y, 1.0f);

        gl.glVertex3f(x + CUBE_SIZE, y + CUBE_SIZE, 1.0f);
        gl.glVertex3f(x + CUBE_SIZE, y, 1.0f);

        gl.glVertex3f(x, y + CUBE_SIZE, 1.0f);
        gl.glVertex3f(x, y, 1.0f);

        //Back lines
        gl.glVertex3f(x + CUBE_SIZE, y + CUBE_SIZE, -1.0f);
        gl.glVertex3f(x, y + CUBE_SIZE, -1.0f);

        gl.glVertex3f(x, y, -1.0f);
        gl.glVertex3f(x + CUBE_SIZE, y, -1.0f);

        gl.glVertex3f(x + CUBE_SIZE, y + CUBE_SIZE, -1.0f);
        gl.glVertex3f(x + CUBE_SIZE, y, -1.0f);

        gl.glVertex3f(x, y + CUBE_SIZE, -1.0f);
        gl.glVertex3f(x, y, -1.0f);

        //Left-side lines
        gl.glVertex3f(x, y + CUBE_SIZE, 1.0f);
        gl.glVertex3f(x, y + CUBE_SIZE, -1.0f);

        gl.glVertex3f(x, y, 1.0f);
        gl.glVertex3f(x, y, -1.0f);

        //Right-side lines
        gl.glVertex3f(x + CUBE_SIZE, y, 1.0f);
        gl.glVertex3f(x + CUBE_SIZE, y, -1.0f);

        gl.glVertex3f(x + CUBE_SIZE, y + CUBE_SIZE, 1.0f);
        gl.glVertex3f(x + CUBE_SIZE, y + CUBE_SIZE, -1.0f);
        gl.glEnd();
    }

    private class MouseController extends MouseAdapter {
        private float lastXvalue;

        /**
         * Saves x-value on mouse pressed
         * @param e
         */
        @Override
        public void mousePressed(MouseEvent e) {
            lastXvalue = e.getX();
        }

        /**
         * Re-renders scene with new angle of view
         * @param e
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            visualModelContext.rAngle = -(lastXvalue - e.getX());
            canvas.display();
        }

        /**
         * Change the distance by mouse wheel
         * @param e
         */
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            visualModelContext.distance += e.getWheelRotation();
            canvas.display();
        }
    }
}
