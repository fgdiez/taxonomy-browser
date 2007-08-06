package uk.ac.ebi.taxy;

import java.util.Vector;




/** This class defines a data model for <code>TaxonProxyTable</code>.
 * @see uk.ac.ebi.taxy.TaxonProxyTable
 */
public class TaxonProxyTableModel
    extends javax.swing.table.AbstractTableModel
{
    ///////////////////////
    // Private Attributes
    ///////////////////////

    /** The names for the table's columns.
     */
    private final String[] _columnNames = { "Scientific Name", 
                                            "ID" };

    /** The collection of <code>TaxonProxy</code> that are the 
     * content of the table.
     */
    private Vector _taxa;


    ///////////////////////
    // Public Operations
    ///////////////////////
    
    /**
     * Constructs a new empty table model.
     */
    public TaxonProxyTableModel()
    {
        _taxa = new Vector();
    }


    /**
     * Sets the content of the table model.
     * It sorts taxa alphabetically.
     * 
     * @param content A collection of <code>TaxonProxy</code>.
     */
    public void setContent( Vector content )
    {
        _taxa = content;
        
        this.fireTableChanged( new javax.swing.event.TableModelEvent( this  ) );
    }


    /**
     * Gets the taxon at the specified row. 
     */
    public TaxonProxy getTaxon( int rowIndex )
    {
        return (TaxonProxy)_taxa.get( rowIndex );
    }


    /**
     * Gets the number of columns in the table model.
     * @return The number of columns.
     */
    public int getColumnCount() 
    {
        return _columnNames.length;
    }
        

    /**
     * Gets the number of rows in the table model, 
     * which is the number of contained taxa.
     * @return The number of rows.
     */
    public int getRowCount() 
    {
        return _taxa.size();
    }


    /**
     * Gets a column name. Which can be "Scientific name" or "ID".
     * @param index the column index.
     * @return The column name.
     */
    public String getColumnName(int index ) 
    {
        return _columnNames[ index ];
    }


    /**
     * Get a cell from the table model.
     * @param rowIndex The cell's row index.
     * @param columnIndex The cell's column index.
     * @return The cell.
     */
    public Object getValueAt( int rowIndex, int columnIndex ) 
    {
        TaxonProxy taxon = (TaxonProxy)_taxa.get( rowIndex );

        switch( columnIndex )
        {
        case 0:
            return taxon.getName();
        case 1:
            return taxon.getID();
        default:
            return taxon.getName();
        }
    }


    /**
     * Get the Class of any cell in a given column.
     * @param columnIndex The column's index.
     * @return The Class for that column.
     */
    public Class getColumnClass( int columnIndex ) 
    {
        return this.getValueAt( 0, columnIndex ).getClass();
    }

}

