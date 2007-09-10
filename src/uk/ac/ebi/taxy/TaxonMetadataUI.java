package uk.ac.ebi.taxy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import uk.ac.ebi.util.Table;

/**
 * This class defines an UI for the visualization of the properties of a
 * <code>TaxonProxy</code>.
 */
@SuppressWarnings("serial")
public class TaxonMetadataUI extends JPanel {

   Logger logger = Logger.getLogger("TaxonMetadataUI");
   private TaxonMetadataController _controller;

   /**
    * Constructs a new empty UI.
    */
   public TaxonMetadataUI() {

      _controller = new TaxonMetadataController(this);
      setLayout(new BorderLayout());
   }

   /**
    * Gets the controller.
    */
   public TaxonMetadataController getController() {

      return _controller;
   }

   /**
    * Overrides the setEnabled operation. It is required for disabling properly
    * the UI components contained within this class.
    */
   public void setEnabled( boolean enabled) {

      super.setEnabled(enabled);

      Component[] components = super.getComponents();

      for (int i = 0; i < components.length; i++) {
         components[i].setEnabled(enabled);
      }
   }

   /**
    * Shows the properties of the specified taxon.
    */
   public void showProperties( TaxonProxy taxon) {

      Component container = createTaxonBag(taxon);

      removeAll();
      add(container);

      revalidate();
      repaint();
   }

   /**
    * Clears the content of the UI.
    */
   public void clearView() {

      removeAll();
      revalidate();
   }

   private Component createTaxonBag( TaxonProxy taxon) {

      List<String> propertyNames = taxon.getPropertyNames();

      JPanel bag = new JPanel(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();
      c.anchor = GridBagConstraints.FIRST_LINE_START;
      c.gridy = 0;
      // c.ipadx = 10;

      for (int i = 0; i < propertyNames.size(); ++i) {
         c.gridy = i;
         String name = propertyNames.get(i);

         TaxonProperty p = taxon.getProperty(name);
         logger.info( "property name: " + name);
         String scalar = p.getScalar();
         logger.info( "property value: " + scalar);

         // last row must have more weight than the others so that
         // it pushes them up
         if (i == propertyNames.size() - 1) {
            c.weighty = 1;
         }
         else {
            c.weighty = 0;
         }

         if (scalar != null) {
            JTextField textField = new JTextField(scalar);
            textField.setEditable(false);
            textField.setBackground(Color.WHITE);
            textField.setMaximumSize(new Dimension(textField.getMaximumSize().width, 25));

            JLabel textLabel = new JLabel(name, JLabel.TRAILING);
            textLabel.setLabelFor(textField);

            c.gridx = 0;
            c.weightx = 0;
            c.insets = new Insets(2, 5, 2, 5);
            bag.add(textLabel, c);

            c.gridx = 1;
            c.weightx = 1;
            bag.add(textField, c);
         }
         else {
            Table tableProperty = p.getTable();

            if (tableProperty == null) continue;

            JTable table = tableProperty.getJTable();
            table.setCellSelectionEnabled(true);

            int tableHeight = table.getRowHeight() * (table.getRowCount() + 1);
            int tableWidth = 530;
            tableProperty.setTableColumnsWidth(tableWidth);
            table.setPreferredScrollableViewportSize(new Dimension(tableWidth, tableHeight));
                        
            JScrollPane tablePane = new JScrollPane(table);
            tablePane.setBorder(BorderFactory.createTitledBorder(p.getName()));
            tablePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            
            logger.info("maximun pane width: " + tablePane.getMaximumSize().width);

            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.gridwidth = 2;
            c.gridx = 0;
            c.weightx = 1; // expand this component horizontally
            bag.add(tablePane, c);
         }
      }

      return bag;
   }
}
