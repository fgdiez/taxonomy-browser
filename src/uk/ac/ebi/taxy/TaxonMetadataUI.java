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

    /**
     * Container that lays out the taxon's properties.
     */
    private Box _mainBox;

    /** Constructs a new empty UI.
     */
    public TaxonMetadataUI()
    {
        compose();

        _controller = new TaxonMetadataController( this );
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
        Component box = createTaxonBox( taxon );

        removeAll();
        add(box);

        revalidate();
        this.repaint();
    }


    /** Clears the content of the UI.
     */
    public void clearView()
    {
        removeAll();
        compose();
        revalidate();
    }


    /**
     * Creates a Box that contains the visual information of the given
     * taxon.
     *
     * @param taxon
     * @return
     */
    private Component createTaxonBox( TaxonProxy taxon )
    {
        Box box = Box.createVerticalBox();
        
        box.add( Box.createVerticalStrut( 20 ) );

        box.add( createScalarView( "Identifier", taxon.getID()) );
        box.add( createScalarView( "Name", taxon.getName()) );

        box.add( Box.createVerticalStrut( 10 ) );

        ArrayList<String> propertyNames = taxon.getPropertyNames();

        for( int i = 0; i < propertyNames.size(); ++i )
        {
            String name = (String) propertyNames.get( i );

            TaxonProperty p = taxon.getProperty( name );

            String scalar = p.getScalar();

            if( scalar != null )
            {     
            	Box scalarContainer = createScalarView(name, scalar);
            	box.add(scalarContainer);
            }
            else
            {
                Table tableProperty = p.getTable();

//                Debug.ASSERT( tableProperty != null, "Unknown property type= " + name );
                if(tableProperty == null ) continue;

                box.add( Box.createVerticalStrut( 10 ) );

                JTable table = tableProperty.getJTable();
                table.setCellSelectionEnabled(true);
                
                int cellHeight = table.getCellRect(0, 0, true).height;
                logger.info("Cell heigth: " + cellHeight);
				table.setPreferredScrollableViewportSize(new Dimension(table.getMaximumSize().width, cellHeight));

				JScrollPane tablePane = new JScrollPane(table); 
                tablePane.setBorder(BorderFactory.createTitledBorder(p.getName()));
                tablePane.setMaximumSize(new Dimension(tablePane.getMaximumSize().width, cellHeight + 50));
                
                box.add( tablePane );
                box.add( Box.createVerticalStrut( 10 ) );
            }
        }

        return box;
    }

	private Box createScalarView(String name, String scalar) {
		Box scalarContainer = Box.createHorizontalBox();
		
		JTextField textField = new JTextField(scalar);
		textField.setEditable(false);
		textField.setBackground(Color.WHITE);
		textField.setMaximumSize(new Dimension(textField.getMaximumSize().width, 25));
		
		JLabel textLabel = new JLabel(name, JLabel.TRAILING);
		textLabel.setLabelFor(textField);
				
		scalarContainer.add( textLabel);
		scalarContainer.add(new Box.Filler(new Dimension(5,5), new Dimension(10,5), new Dimension(30,5)));
		scalarContainer.add(textField);
		return scalarContainer;
	}



    /**
     * Composes UI components.
     */
    private void compose( )
    {
        // Box
        _mainBox = Box.createVerticalBox();

        // this container initialization
        setLayout( new BorderLayout( 0, 1 ) );
        add( new JLabel( "Element Details" ), BorderLayout.NORTH );
        add( _mainBox );
    }
}


