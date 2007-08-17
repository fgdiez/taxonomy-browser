package uk.ac.ebi.taxy;

import uk.ac.ebi.util.*;

import javax.swing.*;
import java.util.*;
import java.util.logging.Logger;
import java.awt.*;


/**
 * This class defines an UI for the visualization of the properties of
 * a <code>TaxonProxy</code>.
 */
@SuppressWarnings("serial")
public class TaxonMetadataUI
    extends JPanel
{
	Logger logger = Logger.getLogger("TaxonMetadataUI");
	private TaxonMetadataController _controller;

    /** Constructs a new empty UI.
     */
    public TaxonMetadataUI()
    {
        _controller = new TaxonMetadataController( this );
        setLayout( new BorderLayout());
    }

    /** Gets the controller.
     */
    public TaxonMetadataController getController()
    {
        return _controller;
    }

    /**
     * Overrides the setEnabled operation. It is required for
     * disabling properly the UI components contained within
     * this class.
     */
    public void setEnabled( boolean enabled )
    {
        super.setEnabled( enabled );

        Component[] components = super.getComponents();

        for( int i = 0; i < components.length; i++ )
        {
            components[ i ].setEnabled( enabled );
        }
    }


    /** Shows the properties of the specified taxon.
     */
    public void showProperties( TaxonProxy taxon )
    {
        Component container = createTaxonBag( taxon );

        removeAll();
        add(container);

        revalidate();
        repaint();
    }


    /** Clears the content of the UI.
     */
    public void clearView()
    {
        removeAll();
        revalidate();
    }


    private Component createTaxonBag( TaxonProxy taxon )
    {
        ArrayList<String> propertyNames = taxon.getPropertyNames();

        JPanel bag = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridy = 0;
//        c.ipadx = 10;
        
        for( int i = 0; i < propertyNames.size(); ++i )
        {
            c.gridy = i;
            String name = (String) propertyNames.get( i );

            TaxonProperty p = taxon.getProperty( name );

            String scalar = p.getScalar();

            // last row must have more weight than the others so that
            // it pushes them up
    		if(i == propertyNames.size() - 1) {
    			c.weighty = 1;
    		}
    		else {
    			c.weighty = 0;
    		}

            if( scalar != null )
            {     
        		JTextField textField = new JTextField(scalar);
        		textField.setEditable(false);
        		textField.setBackground(Color.WHITE);
        		textField.setMaximumSize(new Dimension(textField.getMaximumSize().width, 25));
        		
        		JLabel textLabel = new JLabel(name, JLabel.TRAILING);
        		textLabel.setLabelFor(textField);
        		
        		c.gridx = 0;
        		c.weightx = 0;
        		c.insets = new Insets(2,5,2,5);
        		bag.add(textLabel,c);

        		c.gridx = 1;
        		c.weightx = 1;
        		bag.add(textField,c);
            }
            else
            {
                Table tableProperty = p.getTable();

                if(tableProperty == null ) continue;

                JTable table = tableProperty.getJTable();
                table.setCellSelectionEnabled(true);
                
                int cellHeight = table.getRowHeight()*(table.getRowCount()+1);
                int cellWidth= 530;

				table.setPreferredScrollableViewportSize(new Dimension(cellWidth, cellHeight));

				JScrollPane tablePane = new JScrollPane(table); 
                tablePane.setBorder(BorderFactory.createTitledBorder(p.getName()));
                
                c.anchor = GridBagConstraints.FIRST_LINE_START;
                c.gridwidth = 2;
        		c.gridx = 0;
                c.weightx = 1; // expand this component horizontally
        		bag.add(tablePane,c);
            }
        }

        return bag;
    }
}


