package uk.ac.ebi.taxy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.logging.Level;
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

   public static final String VERSION = "v1.1.0-beta1";
   private static Logger logger = Logger.getLogger("TaxyCore");
   private static final String WINDOW_TITLE = "Taxy " + VERSION;
   
   /**
    * % of screen size which the application will cover initilialy.
    */
   private final double SCREEN_FACTOR = 0.9;

   /**
    * This UI controller
    */
   private TaxyController controller;

   /**
    * UI component for searching taxonomy elements.
    */
   private SearchUI searchUI;

   /**
    * UI component for displaying search results.
    */
   private SearchResultUI searchResultUI;

   /**
    * UI component for displaying the taxonomy tree.
    */
   private TaxonomyTreeUI taxaTreeUI;

   /**
    * UI component for displaying the complete information of a selected taxon.
    */
   private TaxonMetadataUI taxonUI;

   /**
    * Application's status bar.
    */
   private StatusBarUI statusBar;

   /**
    * UI components container.
    */
   private JSplitPane leftPane;

   /**
    * UI components container.
    */
   private JSplitPane rightPane;

   /**
    * UI components container.
    */
//   private JSplitPane _bottomPane;

   static private final String HELP_MESSAGE = "usage: taxy [--help][--debug]";

   // /////////////////////////////
   // Public Static Operations
   // /////////////////////////////

   /** Application's <code>main</code> function */
   public static void main( String[] args) {
      ArgumentParser argParser = new ArgumentParser(args);

      if (argParser.hasOption("--help")) {
         System.out.println(HELP_MESSAGE);
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

      statusBar = new StatusBarUI("");

      taxonUI = new TaxonMetadataUI();

      taxaTreeUI = new TaxonomyTreeUI(taxonUI.getController(), statusBar.getController());

      searchResultUI = new SearchResultUI(taxaTreeUI.getController(), taxonUI.getController(), statusBar.getController());

      controller = new TaxyController(this, taxaTreeUI.getController());

      addWindowListener(new MyWindowListener(controller));

      setJMenuBar(new MenuBar(controller));

      searchUI = new SearchUI(searchResultUI.getController(), controller);

      compose();

      // disable all the UI components
      setEnabled(false);
   }

   /**
    * Gets this UI's controller.
    */
   public TaxyController getController() {

      return controller;
   }

   /**
    * Overrides the setEnabled operation. It is required for disabling properly
    * the UI components contained whinin this class.
    */
   public void setEnabled( boolean enabled) {

      searchUI.setEnabled(enabled);
      searchResultUI.setEnabled(enabled);
      taxaTreeUI.setEnabled(enabled);
      taxonUI.setEnabled(enabled);
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

      statusBar.clearView();

      taxonUI.clearView();

      taxaTreeUI.clearView();

      searchResultUI.clearView();

      searchUI.clearView();

      Runtime.getRuntime().gc();
   }


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

      setTitle(WINDOW_TITLE);

      // dimension setting
      Dimension dim = getScreenFactorDimension(SCREEN_FACTOR);
      setSize(dim);

      // location setting
      Point upperLeft = getCentreLocation();
      setLocation(upperLeft);

      // panes' composition
      leftPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, searchResultUI, taxaTreeUI);
      rightPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane, new JScrollPane(taxonUI));

      // lay out the dividers
      int leftDivider = (int) (dim.width * 0.2);
      int rightDivider = (int) (dim.width * 0.5);

      leftPane.setDividerLocation(leftDivider);
      rightPane.setDividerLocation(rightDivider);

      getContentPane().add(rightPane, BorderLayout.CENTER);
      getContentPane().add(searchUI, BorderLayout.NORTH);
      getContentPane().add(statusBar, BorderLayout.SOUTH);
   }

} 

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
