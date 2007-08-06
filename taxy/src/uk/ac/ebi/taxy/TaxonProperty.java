package uk.ac.ebi.taxy;

import uk.ac.ebi.util.*;

/** This class is an abstraction of a taxon's property.
 * A property contains a value that describes something about the taxon.
 * The value can be :
 * <ul>
 * <li> A scalar: a single value. It is implemented as a
 *      {@link java.lang.String}.</li>
 * <li> A {@link uk.ac.ebi.util.Table}: a bidimensional value,
 *      i.e. a matrix of scalars.</li>
 * </ul>
 *
 */
public class TaxonProperty
{
    /** Name of the property */
    private String _name;

    /** The property's scalar value.
     * It is <code>null</code> if <code>_table</code> is not null.
     */
    private String _scalar;

    /** The property's table value.
     * It is <code>null</code> if <code>_table</code> is not null.
     */
    private Table _table;

    /** Constructs a new property containing the specified scalar value.
     */
    public TaxonProperty( String name, String scalar )
    {
//        Debug.ASSERT( scalar != null, "Null scalar" );

        _scalar = scalar;
        _name = name;
    }

    /** Constructs a new property containing the specified table value.
     */
    public TaxonProperty( String name, Table table )
    {
        Debug.ASSERT( table != null, "Null table" );

        _table = table;
        _name = name;
    }

    /** Get the property's name. */
    public String getName()
    {
        return _name;
    }

    /** Get the scalar value of this property.
     * @return The scalar value or <code>null</code> if this property
     *         is not a scalar.
     */
    public String getScalar()
    {
        return _scalar;
    }

    /** Get the table value of this property.
     * @return The table value or <code>null</code> if this property
     *         is not a table.
     */
    public Table getTable()
    {
        return _table;
    }
}


