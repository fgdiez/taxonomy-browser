package uk.ac.ebi.taxy;

import java.awt.Component;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 * This class defines a table for displaying a collection of
 * <code>TaxonProxy</code>.
 */
@SuppressWarnings("serial")
public class TaxonProxyTable extends JTable {

   /**
    * Table's data model.
    */
   private TaxonProxyTableModel _model;

   /**
    * Table's container.
    */
   private JScrollPane _pane;

   /**
    * Contructs a new empty table.
    */
   public TaxonProxyTable() {

      super();

      _model = new TaxonProxyTableModel();

      setModel(_model);

      _pane = new JScrollPane(this);

      compose();
   }

   /**
    * Overrides the setEnabled operation. It is required for disabling properly
    * the UI components contained whinin this class.
    */
   public void setEnabled( boolean enabled) {

      super.setEnabled(enabled);

      Component[] components = _pane.getComponents();

      for (int i = 0; i < components.length; i++) {
         components[i].setEnabled(enabled);
      }

      components = getComponents();

      for (int i = 0; i < components.length; i++) {
         components[i].setEnabled(enabled);
      }

      if (_pane.getColumnHeader() != null) {
         _pane.getColumnHeader().setEnabled(enabled);
      }

      getTableHeader().setEnabled(enabled);
   }

   /**
    * Returns the container of the graphical table.
    * 
    * @return The table container (which is a JScrollPane).
    */
   public JComponent getContainer() {

      return _pane;
   }

   /**
    * Sets the taxa to be displayed.
    * 
    * @param content
    *           A collection of <code>TaxonProxy</code>.
    */
   public void setContent( List<TaxonProxy> content) {

      _model.setContent(content);
   }

   /**
    * Compose the graphical elements of this class.
    */
   private void compose() {

      setTableColumnsWidth();

//      Dimension preferredSize = getPreferredSize();
//      preferredSize.height += 35;
//      _pane.setPreferredSize(preferredSize);
//
//      Dimension maxSize = _pane.getMaximumSize();
//      maxSize.height = preferredSize.height;
//      _pane.setMaximumSize(maxSize);

   }

   /**
    * Sets the displayed width of the table columns.
    */
   private void setTableColumnsWidth() {

      TableColumn column = null;

      column = getColumnModel().getColumn(0);
      column.setPreferredWidth(100);

      column = getColumnModel().getColumn(1);
      column.setPreferredWidth(30);
   }

}
