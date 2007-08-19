package uk.ac.ebi.taxy;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import uk.ac.ebi.util.Debug;

/**
 * UI component for the visualization of found taxa from a requested search.
 */
@SuppressWarnings("serial")
public class SearchResultUI extends JPanel implements javax.swing.event.ListSelectionListener {

   /**
    * Controller of this UI.
    */
   private SearchResultController _controller;

   /**
    * The displayed search result.
    */
   private TaxonProxyTable _resultTable;

   // /////////////////////////////
   // Private Operations
   // /////////////////////////////

   /**
    * Constructs a new search result UI that will operate over the specified
    * <code>TaxonomyTreeController</code>, <code>TaxonController</code> and
    * <code>StatusBarController</code>.
    */
   public SearchResultUI( TaxonomyTreeController lineageController, TaxonMetadataController taxonController, StatusBarController statusBarController) {

      _controller = new SearchResultController(this, lineageController, taxonController, statusBarController);

      // composition of graphycal elements
      compose();
   }

   /**
    * Shows a new search result. It sets the content of the UI.
    */
   public void setContent( List<TaxonProxy> taxa) {

      _resultTable.setContent(taxa);
   }

   /**
    * Returns the controller of this UI.
    */
   public SearchResultController getController() {

      return _controller;
   }

   /**
    * Overrides JComponent.setEnabled operation for proper disability of the
    * content of this UI component.
    */
   public void setEnabled( boolean enabled) {

      super.setEnabled(enabled);

      Component[] components = getComponents();

      for (int i = 0; i < components.length; i++) {
         components[i].setEnabled(enabled);
      }

      _resultTable.setEnabled(enabled);
   }

   /**
    * Reacts to user selections on _resultTable rows.
    */
   public void valueChanged( ListSelectionEvent e)
   // Implements javax.swing.event.ListSelectionListener
   {

      // ignore extra messages
      if (e.getValueIsAdjusting()) return;

      ListSelectionModel selectionModel = (ListSelectionModel) e.getSource();

      if (!selectionModel.isSelectionEmpty()) {

         int row = selectionModel.getMinSelectionIndex();

         selectionModel.clearSelection();

         TaxonProxyTableModel model = (TaxonProxyTableModel) _resultTable.getModel();

         TaxonProxy taxon = model.getTaxon(row);

         Debug.ASSERT(taxon != null, "Illegal Result Table index = " + row);

         _controller.notifySelection(taxon);
      }
   }

   /**
    * Clear the content of the UI.
    */
   public void clearView() {

      removeAll();
      compose();
      revalidate();
   }

   // ///////////////////////
   // Private operations
   // ///////////////////////

   /**
    * Lays out the graphical elements of the UI.
    */
   private void compose() {

      // creation of graphycal elements
      _resultTable = new TaxonProxyTable();

      _resultTable.getSelectionModel().addListSelectionListener(this);

      // addition of elements
      setLayout(new BorderLayout(0, 1));

      add(new JLabel("Query Result"), BorderLayout.NORTH);

      add(_resultTable.getContainer());
   }
}
