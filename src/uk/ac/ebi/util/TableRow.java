package uk.ac.ebi.util;


/**
 * This class models a generic table row. Together with
 * {@link uk.ac.ebi.util.Table Table} it defines a generic 
 * model for graphical tables (JTable).
 */
public interface TableRow
{
    /** Gets the specified row's column */
    public Object get( int columnIndex );

    /** Sets the value of the specified column */
    //public void set( int columnIndex, Object value );

    /** Returns the number of columns */
    //public int size();
}

