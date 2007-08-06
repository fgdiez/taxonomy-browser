package uk.ac.ebi.taxy;

import uk.ac.ebi.util.Debug;

/**
 * Defines the types of search that can be requested on <code>SearchUI</code>.
 */
public class SearchType
{
    /////////////////////////
    // Public Constants
    /////////////////////////

    public static final SearchType GET_TAXA_BY_NAME = 
                                   new SearchType( "Get Taxa By Name" );
    
    public static final SearchType GET_TAXON_BY_ID = 
                                   new SearchType( "Get Taxon By ID" );

    
    /////////////////////////
    // Private Attributes
    /////////////////////////
    
    static private int _idCount = 0;

    private String _name;

    private int _id;

    /////////////////////////
    // Public Static Operations
    /////////////////////////

    /** Returns an array with the available search types. 
     */
    public static SearchType[] createArray()
    {
        SearchType[] types = new SearchType[ 2 ];
        
        types[ 0 ] = GET_TAXA_BY_NAME;
        types[ 1 ] = GET_TAXON_BY_ID;

        return types;
    }
    
    /////////////////////////
    // Public Operations
    /////////////////////////
    
    /** Returns the name of this type of search.
     */
    public String toString()
    {
        return _name;
    }

    /** Returns this type's identifier.
     */
    public int getID()
    {
        return _id;
    }

    /** It compares itself with other <code>SearchType</code>.
     * @param other Another <code>SearchType</code>.
     */
    public boolean equals( Object other )
    {
        SearchType otherType = (SearchType)other;
        
        return _id == otherType.getID();
    }

    /** Constructs a new search type with the specified name.
     * @param name The name of the search.
     */
    private SearchType( String name )
    {
        _name = name;
        _id = _idCount++;
    }
    
}

