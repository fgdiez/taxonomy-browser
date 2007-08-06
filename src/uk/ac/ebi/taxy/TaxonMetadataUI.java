package uk.ac.ebi.taxy;

import uk.ac.ebi.util.*;

import javax.swing.*;
import java.util.*;
import java.awt.*;


/**
 * This class defines an UI for the visualization of the properties of
 * a <code>TaxonProxy</code>.
 */
@SuppressWarnings("serial")
public class TaxonMetadataUI
    extends JPanel
{
    private static final int FIELD_HIGHT = 20;

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
        Box box = createTaxonBox( taxon );

        _mainBox.removeAll();
        _mainBox.add( box );
        _mainBox.add(Box.createHorizontalStrut( 20));
        _mainBox.revalidate();

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

    ////////////////////////////
    // Private Operations
    ////////////////////////////

    /**
     * Creates a Box that contains the visual information of the given
     * taxon.
     *
     * @param taxon
     * @return
     */
    private Box createTaxonBox( TaxonProxy taxon )
    {
        Box box = Box.createVerticalBox();
        
        box.add( Box.createVerticalStrut( 20 ) );

        box.add( newScalarField( "Identifier", taxon.getID()) );
        box.add( newScalarField( "Name", taxon.getName()) );

        box.add( Box.createVerticalStrut( 10 ) );

        ArrayList<String> propertyNames = taxon.getPropertyNames();

        for( int i = 0; i < propertyNames.size(); ++i )
        {
            String name = (String) propertyNames.get( i );

            TaxonProperty p = taxon.getProperty( name );

            String scalar = p.getScalar();

            if( scalar != null )
            {            	
            	box.add( newScalarField(name, scalar));
            }
            else
            {
                Table tableProperty = p.getTable();

//                Debug.ASSERT( tableProperty != null, "Unknown property type= " + name );
                if(tableProperty == null ) continue;

                box.add( Box.createVerticalStrut( 10 ) );

                box.add( new JLabel( name + ": " ) );

                JTable table = tableProperty.getJTable();

                table.setRowSelectionAllowed( false );

                JScrollPane tablePane = new JScrollPane( table );

                box.add( tablePane );

                box.add( Box.createVerticalStrut( 10 ) );
            }
        }

        return box;
    }

	private JComponent newScalarField(String name, String value) {
		
		return new JLabel("<html><b>" + name + "</b>: " + value + "</html>");
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


