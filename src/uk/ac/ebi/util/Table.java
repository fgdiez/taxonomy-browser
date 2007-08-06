package uk.ac.ebi.util;

import java.util.Vector;
import javax.swing.event.TableModelListener;

/**
 * This class defines a generic table data structure.
 * It can directly be represented graphically as it implements
 * a <i>swing</i> <code>TableModel</code> and provides a method
 * to return a <code>JTable</code>.
 */
public class Table implements javax.swing.table.TableModel
{
//    private java.util.Vector _rows;
	private String[][] rows;

    private java.util.Vector _columnNames;

    private javax.swing.JTable _jTable;

    private Class _columnClass;

    /** Constructs a new table with the specified column names and
     * rows.
     * @param columnNames The table's column names.
     * @param rows The table's rows. It is a collection of
     *             <code>TableRow</code>.
     */
    public Table( Vector columnNames, String[][] rows )
    {
        Debug.ASSERT( columnNames != null, "Null column names" );
        Debug.ASSERT( rows != null, "Null rows" );

        this.rows = rows;

        _columnNames = columnNames;

        _jTable = new javax.swing.JTable( this );

        _columnClass = String.class;
    }

    /**
     * Returns a graphical representation of the table.
     */
    public javax.swing.JTable getJTable()
    {
        return _jTable;
    }

    //////////////////////////////
    // TableModel implementation

    public Class getColumnClass( int columnIndex )
    {
        return _columnClass;
    }

    public int getColumnCount()
    {
        return _columnNames.size();
    }

    public String getColumnName( int columnIndex )
    {
        return (String)_columnNames.get( columnIndex );
    }

    public int getRowCount()
    {
        return rows.length;
    }

    public Object getValueAt( int rowIndex, int columnIndex )
    {
//        TableRow row = (TableRow) _rows.get( rowIndex );

//        return row.get( columnIndex );
    	return rows[rowIndex][columnIndex];
    }

    public boolean isCellEditable( int rowIndex, int columnIndex )
    {
        return false;
    }

    /** Void implementation */
    public void addTableModelListener( TableModelListener listener ) {}

    /** Void implementation */
    public void removeTableModelListener( TableModelListener listener ) {}

    /** Void implementation */
    public void setValueAt( Object aValue, int rowIndex, int columnIndex ) {}

}


