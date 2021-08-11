/*
       A component that acts as a simple stop-watch.  When the user clicks
       on it, this component starts timing.  When the user clicks again,
       it displays the time between the two clicks.  Clicking a third time
       starts another timer, etc.  While it is timing, the label just
       displays the whole number of seconds since the timer was started.
    */

    import java.awt.*;
    import java.awt.event.*;

    public class StopWatchRunner extends Label
                           implements MouseListener, Runnable {

       private Thread runner; // A thread that runs as long as the timer
                              // is running.  It sets the text in the label
                              // to show the elapsed time.  This variable is
                              // non-null when the timer is running and is
                              // null when it is not running.  (This is
                              // tested in mousePressed().)


       private static final int        // Constants for use with status variable.
                   GO = 0,
                   TERMINATE = 1;

       private int status;  // This variable is set by mousePressed()
                            // to control the thread.  When it's time
                            // for the thread to end, the value is
                            // set to TERMINATE.

       private long startTime;   // Start time of timer.
                                 //   (Time is measured in milliseconds.)


       public StopWatchRunner() {
             // Constructor.  Call the constructor from the superclass
             // and listen for mouse clicks.
          super("  Click to start timer.  ", Label.CENTER);
          addMouseListener(this);
       }


       synchronized public void destroy() {
              // Before an applet that uses this component is
              // destroyed, it can call this method to make sure
              // that, if a timer thread is still running, it
              // will stop.  (This method is NOT called by the system!)
           status = TERMINATE;
       }


       public void run() {
              // Run method executes while the timer is going.
              // Several times a second, it computes the number of
              // seconds the thread has been running and displays the
              // whole number of seconds on the label.  It ends
              // when status is set to TERMINATE.
          long start;  // Time when thread starts.
          start = System.currentTimeMillis();
          while (true) {
             synchronized(this) {
                if (status == TERMINATE)
                   break;
                long time = (System.currentTimeMillis() - start) / 1000;
                setText( "Running:  " + time + " seconds");
             }
             waitDelay(100);
          }
       }


       synchronized void waitDelay(int milliseconds) {
             // Pause for the specified number of milliseconds OR
             // until the notify() method is called by some other thread.
             // (From Section 7.5 of the text.)
          try {
             wait(milliseconds);
          }
          catch (InterruptedException e) {
          }
       }


       synchronized public void mousePressed(MouseEvent evt) {
              // React when user presses the mouse by
              // starting or stopping the timer.
          if (runner == null) {
                // Since runner is null, the timer is not running.
                // Record the time and start the timer by creating a thread.
             startTime = evt.getWhen();  // Time when mouse was clicked.
             status = GO;
             runner = new Thread(this);
             runner.start();
          }
          else {
                // Stop the thread.  Compute the elapsed time since the
                // timer was started and display it.
             status = TERMINATE;
             notify();  // Wake up thread so it can terminate quickly.
             long endTime = evt.getWhen();
             double seconds = (endTime - startTime) / 1000.0;
             setText("Time:  " + seconds + " seconds");
             runner = null;
          }
       }

       public void mouseReleased(MouseEvent evt) { }
       public void mouseClicked(MouseEvent evt) { }
       public void mouseEntered(MouseEvent evt) { }
       public void mouseExited(MouseEvent evt) { }

    }  // end StopWatch
