package uk.ac.ebi.taxy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import uk.ac.ebi.util.Debug;

/**
 * Main UI for searching and navigating taxonomy information.
 * 
 * @see uk.ac.ebi.taxy.TaxyController
 */
@SuppressWarnings("serial")
public class TaxyUI extends JFrame {

   private static final String WINDOW_TITLE = "Taxy v1.1.0-alpha3";

   private static Logger logger = Logger.getLogger("TaxyCore");
   
   /**
    * % of screen size which the application will cover initilialy.
    */
   private final double SCREEN_FACTOR = 0.9;

   /**
    * This UI controller
    */
   private TaxyController _controller;

   /**
    * UI component for searching taxonomy elements.
    */
   private SearchUI _searchUI;

   /**
    * UI component for displaying search results.
    */
   private SearchResultUI _searchResultUI;

   /**
    * UI component for displaying the taxonomy tree.
    */
   private TaxonomyTreeUI _taxaTreeUI;

   /**
    * UI component for displaying the complete information of a selected taxon.
    */
   private TaxonMetadataUI _taxonUI;

   /**
    * Application's status bar.
    */
   private StatusBarUI _statusBar;

   /**
    * UI components container.
    */
   private JSplitPane _leftPane;

   /**
    * UI components container.
    */
   private JSplitPane _rightPane;

   /**
    * UI components container.
    */
   private JSplitPane _topPane;

   /**
    * UI components container.
    */
//   private JSplitPane _bottomPane;

   static private final String _helpMessage = "usage: taxy [--help][--debug]";

   // /////////////////////////////
   // Public Static Operations
   // /////////////////////////////

   /** Application's <code>main</code> function */
   public static void main( String[] args) {

      ArgumentParser argParser = new ArgumentParser(args);

      if (argParser.hasOption("--help")) {
         System.out.println(_helpMessage);
         System.exit(1);
      }

      // setMotifLookAndFeel();

      // Debug.setEnabled( argParser.hasOption( "--debug" ) );
      Debug.setEnabled(true);
      Debug.TRACE("Debug is on");
      logger.info("initiating browserui");

      TaxyUI application = new TaxyUI();

      Debug.registerExitCallback(application.getController());

      application.setVisible(true);

      application.getController().connectToPlugin();
   }

   // /////////////////////////////
   // Public Operations
   // /////////////////////////////

   /**
    * Constructs a new taxonomy browser.
    */
   public TaxyUI() {

      _statusBar = new StatusBarUI("");

      _taxonUI = new TaxonMetadataUI();

      _taxaTreeUI = new TaxonomyTreeUI(_taxonUI.getController(), _statusBar.getController());

      _searchResultUI = new SearchResultUI(_taxaTreeUI.getController(), _taxonUI.getController(), _statusBar.getController());

      _controller = new TaxyController(this, _taxaTreeUI.getController());

      addWindowListener(new MyWindowListener(_controller));

      setJMenuBar(new MenuBar(_controller));

      _searchUI = new SearchUI(_searchResultUI.getController(), _controller);

      compose();

      // disable all the UI components
      setEnabled(false);
   }

   /**
    * Gets this UI's controller.
    */
   public TaxyController getController() {

      return _controller;
   }

   /**
    * Overrides the setEnabled operation. It is required for disabling properly
    * the UI components contained whinin this class.
    */
   public void setEnabled( boolean enabled) {

      _searchUI.setEnabled(enabled);
      _searchResultUI.setEnabled(enabled);
      _taxaTreeUI.setEnabled(enabled);
      _taxonUI.setEnabled(enabled);
   }

   /**
    * Shows a warning in a dialog
    */
   public void showWarningDialog( String title, String message) {

      JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
   }

   /**
    * Shows an informative dialog
    */
   public void showInformativeDialog( String title, String message) {

      JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
   }

   /** Clears the content of this UI */
   public void clearViews() {

      _statusBar.clearView();

      _taxonUI.clearView();

      _taxaTreeUI.clearView();

      _searchResultUI.clearView();

      _searchUI.clearView();

      Runtime.getRuntime().gc();
   }

   // ///////////////////////////////////////
   // Private static operations
   // ///////////////////////////////////////

//   private static void setMotifLookAndFeel() {
//
//      try {
//         // set look & feel of the application
//         javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
//      }
//      catch (Exception e) {
//         System.out.println("Warning: could not set the Motif Look & Feel."
//                            + " Using default Look & Feel");
//      }
//   }

   // ////////////////////////////////////////
   // Private operations
   // ////////////////////////////////////////

   /**
    * Calculates a dimension which is a factor of the screen size
    * 
    * @param screenFactor
    *           screen factor from 0(invisible) to 1(full screen).
    * 
    * @return the calculated dimension.
    */
   private Dimension getScreenFactorDimension( double screenFactor) {

      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

      double x = dim.width * screenFactor;
      double y = dim.height * screenFactor;

      Dimension result = new Dimension((int) x, (int) y);

      return result;
   }

   /**
    * Calculates a location for the upper left corner of this frame so that the
    * window is centered.
    * 
    * @return the calculated location.
    */
   private Point getCentreLocation() {

      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

      Dimension size = getSize();

      double x = (screenSize.width - size.width) / 2;
      double y = (screenSize.height - size.height) / 2;

      Point result = new Point((int) x, (int) y);

      return result;
   }

   /**
    * Composes the TaxonBrowser's elements layout. These elements are a
    * SearchUI, a SearchResultUI and a TaxonUI.
    */
   private void compose() {

      // panes' composition
      _leftPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, _searchResultUI, _taxaTreeUI);

      _rightPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, _leftPane, new JScrollPane(_taxonUI));

      _topPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, _searchUI, _rightPane);

      /*
       * _bottomPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, _topPane,
       * _statusBar.getContainer() );
       */

      getContentPane().add(_topPane);

      // setMotifLookAndFeel();

      setTitle(WINDOW_TITLE);

      // dimension setting
      Dimension dim = getScreenFactorDimension(SCREEN_FACTOR);
      setSize(dim);

      // location setting
      Point upperLeft = getCentreLocation();
      setLocation(upperLeft);

      // lay out the dividers
      int leftDivider = (int) (dim.width * 0.2);

      int rightDivider = (int) (dim.width * 0.5);

      // int bottomDivider = (int)(dim.width * 0.6);

      _leftPane.setDividerLocation(leftDivider);

      _rightPane.setDividerLocation(rightDivider);

      // _bottomPane.setDividerLocation( bottomDivider );

      getContentPane().add(_statusBar, BorderLayout.SOUTH);
   }

} // class BrowserUI

/**
 * This class implements a listener for window closing events. It calls back the
 * proper functions for exiting cleanly when a window closing event happen.
 */
class MyWindowListener extends java.awt.event.WindowAdapter {

   private TaxyController _controller;

   /** Constructs a new window events listener */
   public MyWindowListener( TaxyController controller) {

      _controller = controller;
   }

   public void windowClosing( java.awt.event.WindowEvent A) {

      _controller.exit();
   }
}
