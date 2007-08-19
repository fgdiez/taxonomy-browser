package uk.ac.ebi.taxy;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;

import uk.ac.ebi.util.Debug;

/**
 * This class defines a UI for the visualization of the taxonomy tree.
 */
@SuppressWarnings("serial")
public class TaxonomyTreeUI extends JPanel

implements TreeSelectionListener, TreeWillExpandListener {

   // //////////////////////////////////
   // Private Attributes
   // //////////////////////////////////

   private TaxonomyTreeController _controller;

   /**
    * Tree for visualizing lineage info.
    */
   private TaxonProxyTree _tree;

   // //////////////////////////////////
   // Public Operations
   // //////////////////////////////////

   /**
    * Constructs a new UI for displaying a taxonomy tree.
    * 
    * @param selectedTaxonController
    *           The controller to which to notify of a selection make on the
    *           tree.
    * @param statusBarController
    *           The status bar controller where to write the lineage of the
    *           currently selected taxon.
    */
   public TaxonomyTreeUI( TaxonMetadataController selectedTaxonController, StatusBarController statusBarController) {

      compose();

      // Controller creation
      _controller = new TaxonomyTreeController(this, selectedTaxonController, statusBarController);
   }

   /**
    * Returns the UI's controller.
    */
   public TaxonomyTreeController getController() {

      return _controller;
   }

   /**
    * Shows the specified taxon on the taxonomy tree.
    */
   public void showTaxon( TaxonProxy taxon) {

      _tree.removeTreeWillExpandListener(this);
      _tree.removeTreeSelectionListener(this);

      _tree.showTaxon(taxon);

      _tree.addTreeWillExpandListener(this);
      _tree.addTreeSelectionListener(this);
   }

   /**
    * Clears the currently selected taxon.
    */
   public void clearSelection() {

      _tree.removeTreeSelectionListener(this);
      _tree.clearSelection();
      _tree.addTreeSelectionListener(this);
   }

   /**
    * Clear the content of this UI.
    */
   public void clearView() {

      removeAll();
      compose();
      revalidate();
   }

   /**
    * Overrides the setEnabled operation. It is required for disabling properly
    * the UI components contained whinin this class.
    */
   public void setEnabled( boolean enabled) {

      super.setEnabled(enabled);

      java.awt.Component[] contentArray = getComponents();

      for (int i = 0; i < contentArray.length; i++) {
         contentArray[i].setEnabled(enabled);
      }

      _tree.setEnabled(enabled);
   }

   /**
    * Reacts to the selection of a taxon. Tells the controller to notify the
    * selection made by the user.
    */
   public void valueChanged( TreeSelectionEvent event) {

      Object selection = event.getPath().getLastPathComponent();

      // discard null selections
      if (selection == null) {
         Debug.TRACE("Null tree selection!!!");
         return;
      }

      TaxonProxyTreeNode node = (TaxonProxyTreeNode) selection;

//      Debug.ASSERT(node != null, "Null tree node");

      TaxonProxy selectedTaxon = (TaxonProxy) node.getUserObject();

      _controller.notifySelection(selectedTaxon);
   }

   /**
    * Expands the selected node.
    * 
    * @param event
    *           Contains the node to be expanded.
    */
   public void treeWillExpand( TreeExpansionEvent event) {

      TaxonProxyTreeNode node = (TaxonProxyTreeNode) event.getPath().getLastPathComponent();

      Debug.ASSERT(node != null, "Null node will expand!!!");

      // children are incarnated (in case they have not yet been added to
      // the taxnomy tree) and shown
      _tree.expandNode(node);

   }

   public void treeWillCollapse( TreeExpansionEvent event) {

   }

   // ///////////////////////////
   // Private Operations
   // ///////////////////////////

   /**
    * Lays out the graphical elements of the form
    */
   private void compose() {

      // UI components creation
      _tree = new TaxonProxyTree();

      _tree.addTreeSelectionListener(this);

      _tree.addTreeWillExpandListener(this);

      // addition of elements
      setLayout(new java.awt.BorderLayout(0, 1));

      add(new JLabel("Taxonomy Tree"), java.awt.BorderLayout.NORTH);

      add(_tree.getContainer());
   }
}
