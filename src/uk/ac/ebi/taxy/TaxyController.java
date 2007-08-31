package uk.ac.ebi.taxy;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import uk.ac.ebi.util.Debug;
import uk.ac.ebi.util.SwingWorker;

/**
 * This class is the controller for <code>BrowserUI</code>.
 * 
 * @see uk.ac.ebi.taxy.TaxyUI
 */
public class TaxyController {

   final class ConnectionWorker extends SwingWorker {

      public Object construct() {
         try {
            logger.info("Worker construct entered");
            setWaitCursor();
            connectToSelectedPlugin();
            return null;
         } 
         catch(Throwable e) {
            logger.throwing("", "", e);
            e.printStackTrace();
            return null;
         } 
         finally {
            setDefaultCursor();
         }
      }
   }

   private Logger logger = Logger.getLogger("TaxyCore");

   private TaxyUI _ui;

   private TaxonomyPlugin selectedPlugin;

   private TaxonomyTreeController _treeController;

   private final ConnectionWorker connectionWorker;

   /**
    * Constructs a new controller that will operate over the specified
    * <code>BrowserUI</code> and <code>TaxonomyTreeController</code>.
    */
   public TaxyController(TaxyUI ui, TaxonomyTreeController treeController) {

      _ui = ui;
      _treeController = treeController;
      connectionWorker = new ConnectionWorker();
   }

   /**
    * Gets the implementation of <code>TaxonomyPlugin</code> that is currently
    * in use.
    */
   public TaxonomyPlugin getTaxonProvider() {

      return selectedPlugin;
   }

   /**
    * It resolves which plug-ins are available and the one chosen by the user.
    * 
    * @return The plug-in chosen by the user or <code>null</code> if no
    *         selecting was made.
    */
   public TaxonomyPlugin resolvePlugins() {

      try {
         ArrayList<Class<TaxonomyPlugin>> pluginClasses = new ArrayList<Class<TaxonomyPlugin>>();
         ArrayList<Class<PluginDescription>> pluginDescriptionClasses = new ArrayList<Class<PluginDescription>>();

         java.io.File[] jarFiles = getJarFiles();

         if (jarFiles == null) {
            Debug.TRACE("No jar files found");

            return null;
         }

         for (int i = 0; i < jarFiles.length; ++i) {
            Debug.TRACE("Looking for plugins in file : " + jarFiles[i].getPath());
            PluginLoader classLoader = new PluginLoader(jarFiles[i]);

            Class<TaxonomyPlugin> pluginClass = classLoader.getPluginClass();

            if (pluginClass != null) {
               pluginClasses.add(pluginClass);
            } else {
               Debug.TRACE("Plugin not found");
            }

            Class<PluginDescription> pluginDescriptionClass = classLoader.getPluginDescriptionClass();

            if (pluginDescriptionClass != null) {
               pluginDescriptionClasses.add(pluginDescriptionClass);
            } else {
               Debug.TRACE("Plugin description not found");
            }
         }

         Class<TaxonomyPlugin> selectedPluginClass = selectPlugin(pluginClasses, pluginDescriptionClasses);

         if (selectedPluginClass == null) {
            Debug.ERROR("error selecting Plugin class");
            return null;
         }

         TaxonomyPlugin plugin = selectedPluginClass.newInstance();

         plugin.setParentFrame(_ui);

         return plugin;
      } catch (Throwable ex) {
         ex.printStackTrace();
         Debug.ERROR(ex.getMessage());
         return null;
      }
   }

   /**
    * Connects to any of the available sources of taxonomic data. It resolves
    * which plug-ins are available and allows the user to select one of them.
    */
   public void connectToPlugin() {

      _ui.setEnabled(false);
      selectedPlugin = resolvePlugins();

      if (selectedPlugin != null) {
         connectToPluginInNewThread();
      } else {
         logger.severe("Cannot resolve pluggins");
      }
   }

   void connectToSelectedPlugin() {
      Cursor currentCursor = _ui.getCursor();
      _ui.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

      if (selectedPlugin.connect()) {
         initializeViews(selectedPlugin);
         _ui.setEnabled(true);
      }
      _ui.setCursor(currentCursor);
   }

   void connectToPluginInNewThread() {
      connectionWorker.interrupt();
      connectionWorker.start();
   }

   public void disconnect() {

      connectionWorker.interrupt();

      _ui.clearViews();
      if (selectedPlugin != null) {
         selectedPlugin.disconnect();
         selectedPlugin = null;
      }
      _ui.setEnabled(false);
      System.gc();
      System.gc();
      System.runFinalization();
   }

   /**
    * Exists cleanly from the application.
    */
   public void exit() {

      if (selectedPlugin != null) {
         if (!selectedPlugin.disconnect()) {
            String title = "Connection Error";
            String message = "Could not disconnect from database";

            _ui.showWarningDialog(title, message);

            return;
         }
      }

      System.exit(0);
   }

   /** Shows a warning message centered in the applications window. */
   public void showWarningDialog(String title, String message) {

      _ui.showWarningDialog(title, message);
   }

   /** Shows an informative message centered in the applications window. */
   public void showInformativeDialog(String title, String message) {

      _ui.showInformativeDialog(title, message);
   }

   public void setWaitCursor() {
      _ui.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
   }

   public void setDefaultCursor() {
      _ui.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
   }

   // ////////////////////////
   // Private operations
   // ////////////////////////

   private void initializeViews(TaxonomyPlugin plugin) {

      _ui.clearViews();
      selectedPlugin = plugin;

      List<TaxonProxy> rootChildren = plugin.getRoot().getChildren();

      TaxonProxy firstChild = rootChildren.get(0);

      _treeController.showTaxon(firstChild);
      _treeController.clearSelection();

      // System.gc();
   }

   /**
    * Selects one of the available implementations of
    * <code>TaxonomyPlugin</code>.
    */
   private Class<TaxonomyPlugin> selectPlugin(ArrayList<Class<TaxonomyPlugin>> plugins,
         ArrayList<Class<PluginDescription>> pluginDescriptions) {

      PluginDialog dialog = new PluginDialog(_ui, plugins, pluginDescriptions);
      dialog.setVisible(true);
      return dialog.getSelectedPlugin();
   }

   /**
    * Gets all the jar files in the <i>plugins</id> directory.
    */
   private java.io.File[] getJarFiles() {

      java.io.File pluginsDir = new java.io.File("plugins");

      java.io.FileFilter filter = new java.io.FileFilter() {

         public boolean accept(java.io.File f) {

            return f.getName().toLowerCase().endsWith(".jar");
         }
      };

      java.io.File[] jarFiles = pluginsDir.listFiles(filter);

      return jarFiles;
   }
}
