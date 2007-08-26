package uk.ac.ebi.taxy;

//import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;

/**
 * This class defines the application's menus. This class creates the
 * application's menu bar with all the menu items.
 */
@SuppressWarnings("serial")
public class MenuBar extends javax.swing.JMenuBar {

   private Logger logger = Logger.getLogger("TaxyCore");

   /**
    * Contructs a new menu bar that will operate over the specified
    * <code>BrowserController</code>.
    */
   public MenuBar(TaxyController controller) {

      super();

      // _controller = controller;

      JMenu fileMenu = new JMenu("File");

      fileMenu.add(new ConnectionAction(controller));
      fileMenu.add(new DisconnectionAction(controller));
      fileMenu.add(new ExitAction(controller));

      JMenu helpMenu = new JMenu("Help");

      helpMenu.add(new AboutAction());

      super.add(fileMenu);
      super.add(helpMenu);
   }

   /**
    * This class implements the action to be done when clicking on the
    * <i>Connect</i> menu item.
    */
   public class ConnectionAction extends AbstractAction {

      private TaxyController _controller;

      public ConnectionAction(TaxyController controller) {

         super("Connect");

         _controller = controller;
      }

      /**
       * Implementation of ActionListener. Makes a new plug-in connection.
       * 
       * @param e
       *           event.
       */
      public void actionPerformed(ActionEvent e) {

         _controller.connectToPlugin();
      }
   };

   /**
    * This class implements the action to be done when clicking on the
    * <i>Disconnect</i> menu item.
    */
   public class DisconnectionAction extends AbstractAction {

      private TaxyController _controller;

      public DisconnectionAction(TaxyController controller) {

         super("Disconnect");

         _controller = controller;
      }

      /**
       * Implementation of ActionListener. Disconnects from the current plug-in.
       * 
       * @param e
       *           event.
       */
      public void actionPerformed(ActionEvent e) {

         _controller.disconnect();
      }
   };

   /**
    * This class implements the action to be done when clicking on the <i>Exit</i>
    * menu item.
    */
   public class ExitAction extends AbstractAction {

      private TaxyController _controller;

      public ExitAction(TaxyController controller) {

         super("Exit");

         _controller = controller;
      }

      /**
       * Exits from the application in a clean way.
       * 
       * @param e
       *           event.
       */
      public void actionPerformed(ActionEvent e) {

         _controller.exit();
      }
   };

   /**
    * This class implements the action to be done when clicking on the <i>About</i>
    * menu item.
    */
   public class AboutAction extends AbstractAction implements javax.swing.event.HyperlinkListener {

      private EasyDialog _dialog;

      private JButton _okButton;

      private JEditorPane _editorPane;

      private BrowserLauncher launcher = null;

      // private java.net.URL _mainURL;

      /** Contructs a new "about" action. */
      public AboutAction() {

         super("About");

         try {
            launcher = new BrowserLauncher();
         } catch (BrowserLaunchingInitializingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         } catch (UnsupportedOperatingSystemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }

         // Create an editor pane.
         _editorPane = new JEditorPane();
         _editorPane.setEditable(false);
         _editorPane.addHyperlinkListener(this);

         java.net.URL url = getAboutURL();
         if (url != null) {
            setPage(url);
         }

         JScrollPane editorScrollPane = new JScrollPane(_editorPane);

         editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

         editorScrollPane.setPreferredSize(new Dimension(600, 470));
         editorScrollPane.setMinimumSize(new Dimension(50, 50));

         _dialog = new EasyDialog(null, false, "About", "", "");
         _dialog.setCentralRegion(editorScrollPane);
         _okButton = _dialog.addButton("Ok", this);
         _dialog.pack();
      }

      /**
       * Sets the page to be shown as the "About" information.
       */
      public void setPage(java.net.URL url) {

         try {
            if (url != null) {
               _editorPane.setPage(url);
            }
         } catch (Exception ex) {
            logger.severe("Could not set the page " + url.toString());
         }
      }

      public void actionPerformed(ActionEvent e) {

         if (e.getSource() == _okButton) {
            _dialog.setVisible(false);
         } else // source is its menu item
         {
            // setPage( getMainURL() );
            _dialog.pack();
            _dialog.setVisible(true);
         }
      }

      /**
       * Gets the URL of the page containing the "about" information.
       */
      private java.net.URL getAboutURL() {

         String urlString = null;

         try {
            java.net.URL url = getClass().getClassLoader().getResource("uk/ac/ebi/taxy/about.html");

            return url;
         } catch (Exception ex) {
            logger.severe("Couldn't create URL: " + urlString);
            return null;
         }
      }

      public void hyperlinkUpdate(HyperlinkEvent e) {

         if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            logger.info("hyperlink pressed");
            launcher.openURLinBrowser(e.getURL().toString());
         }
      }
   };
}
