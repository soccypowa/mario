package dev.soccan.jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import dev.soccan.util.Time;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL11.*;

public class Window {

  private int width, height;
  private String title;
  private long glfwWindow;

  public float r, g, b, a;
  private boolean fadeToBlack = false;

  private static Window window = null;

  private static Scene currentScene;

  private Window() {
    this.width = 1920;
    this.height = 1080;
    this.title = "Mario";
    r = 1;
    g = 1;
    b = 1;
    a = 1;
  }

  public static void changeScene(int newScene) {
    switch (newScene) {
      case 0:
        currentScene = new LevelEditorScene();
        // currentScene.init();
        break;
      case 1:
        currentScene = new LevelScene();
        // currentScene.int();
        break;
      default:
        assert false : "Unknown Scene: '" + newScene + "'";
        break;
    }
  }

  public static Window get() {
    if (window == null) {
      Window.window = new Window();
    }
    return Window.window;
  }

  public void run() {
    System.out.println("Hello LWJGL " + Version.getVersion() + "!");

    init();
    loop();

    // Free the memory (OS will do this, but this is nicer)
    glfwFreeCallbacks(glfwWindow);
    glfwDestroyWindow(glfwWindow);

    // Terminate GLFW and free the error callback (OS will do this, but this is
    // nicer)
    glfwTerminate();
    glfwSetErrorCallback(null).free();
  }

  public void init() {
    // Setup error callback
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW
    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW.");
    }

    // Configure GLFW
    glfwDefaultWindowHints();
    ;
    glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

    // Create the window
    glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
    if (glfwWindow == NULL) {
      throw new IllegalStateException("Failed to create the GLFW window.");
    }

    // Setting our callbacks for the mouse and keyboard
    glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
    glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
    glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
    glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

    // Make OpenGL the context current
    glfwMakeContextCurrent(glfwWindow);
    // Enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(glfwWindow);

    // This row is very important because a very long text on their site!
    GL.createCapabilities();

    Window.changeScene(0);
  }

  public void loop() {
    float beginTime = Time.getTime();
    float endTime;
    float dt = -1.0f;

    while (!glfwWindowShouldClose(glfwWindow)) {
      // Poll events
      glfwPollEvents();

      glClearColor(r, g, b, a);
      glClear(GL_COLOR_BUFFER_BIT);

      if (dt >= 0) {
        currentScene.update(dt);
      }

      glfwSwapBuffers(glfwWindow);

      endTime = Time.getTime();
      dt = endTime - beginTime;
      beginTime = endTime;
    }
  }

}
