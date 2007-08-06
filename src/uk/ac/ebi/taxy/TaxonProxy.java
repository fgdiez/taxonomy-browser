package uk.ac.ebi.taxy;


import java.util.*;

import javax.swing.ImageIcon;


/**
 * This class defines the abstraction of a taxon.
 * It defines a taxon as an element owning a collection of <code>TaxonProperty</code>
 * and a hierarchical relation with other taxa so that there is a parent and any number
 * of children associated with a given taxon.
 * The properties are referenced by a name, which should be unique within the taxon.
 * There are two properties that have separate accessing methods:<br>
 * <ul>
 * <li>ID : the taxon identifier.</li>
 * <li>Name : the main name of the taxon (a.k.a. scientific name).</li>
 * </ul>
 * The link to proterties and other taxa is proxied, so that they do not need to be
 * loaded in memory until they are needed.
 *
 *
 * @see uk.ac.ebi.taxy.TaxonProperty
 */
public abstract class TaxonProxy
    implements Comparable
{
    /////////////////////////////////
    // Private Attributes
    /////////////////////////////////

    protected TaxonomyPlugin _taxonomyProvider;

    private String _id;

    /////////////////////////
    // parent attributes

    private String _parentID;

    private TaxonProxy _parent;

    private boolean _parentRetrieved;

    /////////////////////////
    // lineage attributes

    private ArrayList<TaxonProxy> _parents;

    private String _lineage;

    /////////////////////////
    // children attributes

    private Boolean _hasChildren;

    private ArrayList<TaxonProxy> _children;

    //////////////////////////
    // properties attributes

    private java.util.Hashtable _properties;

    private String _name;


    /////////////////////////////////
    // Public Operations
    /////////////////////////////////

    /** Constructs a new <code>TaxonProxy</code> with the specified
     * taxon identifier, taxon main name and taxonomy provider.
     * The taxonomy provider is used to retrieve the
     * proxied properties and taxa associated with this taxon
     * (its parent and children).
     * @param id Taxon's id.
     * @param name Taxon's main name. Usually is the taxon's scientifi name.
     * @param taxonomyProvider The plug-in from which to retrieve the proxied data.
     */
    public TaxonProxy( String id, String name, TaxonomyPlugin taxonomyProvider )
    {
        _taxonomyProvider = taxonomyProvider;

        _id = id;

        _name = name;

        _parentRetrieved = false;

        _properties = new java.util.Hashtable();
    }

    /** Return the main name of the taxon.
     */
    public String toString()
    {
        return _name;
    }

    /** Compares with another <code>TaxonProxy</code>.
     */
    public int compareTo( Object obj )
    {
        TaxonProxy other = (TaxonProxy) obj;

        return _id.compareTo( other.getID() );
    }


    //////////////////////////////
    // Get Operations
    //////////////////////////////


    /**
     * Gets the names of the available properties.
     * @return The names of the properties associated with this taxon.
     */
    public abstract ArrayList<String> getPropertyNames();
//  public Vector getPropertyNames()
//    {
//        return _taxonomyProvider.getPropertyNames();
//    }

    /**
     * Gets a property with the specified name.
     * @return The specified property or <code>null</code> if it does not exist.
     */
  public abstract TaxonProperty getProperty( String propertyName );
//    public TaxonProperty getProperty( String propertyName )
//    {
//        TaxonProperty p = (TaxonProperty) _properties.get( propertyName );
//
//        if( p == null )
//        {
//            p = _taxonomyProvider.getProperty( _id, propertyName );
//        }
//        return p;
//    }

    /** Gets the taxon's identifier.
     */
    public String getID()
    {
        return _id;
    }


    /** Gets the taxon's main name.
     */
    public String getName()
    {
        return _name;
    }


    ////////////////////////
    // proxy Getters

    /** Gets the parent of this taxon.
     */
    public abstract TaxonProxy getParent();
//    {
//        if( ! _parentRetrieved )
//        {
//            if( _parentID == null )
//            {
//                _parentID = _taxonomyProvider.getParentID( _id );
//            }
//
//            if( _parentID != null )
//            {
//                _parent = _taxonomyProvider.getTaxonProxy( _parentID );
//            }
//
//            _parentRetrieved = true;
//        }
//
//        return _parent;
//    }


    ///////////////////////////////////
    // Higher level Getters

    /** Gets all the parental lineage up to the root.
     * @return A collection of <code>TaxonProxy</code> with
     *         all the parents.
     */
    public ArrayList<TaxonProxy> getParents()
    {
        if( _parents == null )
        {
            java.util.LinkedList list = new java.util.LinkedList();

            _parents = new ArrayList<TaxonProxy>();

            for( TaxonProxy parent = getParent();
                 parent != null;
                 parent = parent.getParent() )
            {
                list.addFirst( parent );
            }
            _parents = new ArrayList<TaxonProxy>( list );
        }

        return _parents;
    }


    /** Gets the lineage of this taxon, including itself.
     * @return A string with ';' separating the components of the lineage.
     */
    public String getLineage()
    {
        if( _lineage == null )
        {
            StringBuffer lineage = new StringBuffer( );

            ArrayList<TaxonProxy> parents = getParents();

            for( int i = 0; i < parents.size(); ++i )
            {
                TaxonProxy parent = (TaxonProxy) parents.get( i );

                lineage.append( parent.getName() + "; " );
            }

            lineage.append( getName() );

            _lineage = lineage.toString();
        }

        return _lineage;
    }


    /** Return <true> if this taxon has any children
     */
    public abstract boolean hasChildren();
//    {
//        if( _hasChildren == null )
//        {
//            boolean hasChildren = _taxonomyProvider.hasChildren( getID() );
//
//            _hasChildren = new Boolean( hasChildren );
//        }
//        return _hasChildren.booleanValue();
//    }

    /** Returns the collection of <code>TaxonProxy</code> that are children
     * of this taxon.
     */
    public abstract ArrayList<TaxonProxy> getChildren();
//    {
//        if( _children == null )
//        {
//            _children = _taxonomyProvider.getChildren( getID() );
//            java.util.Collections.sort( _children );
//        }
//
//        return _children;
//    }

    public abstract ImageIcon getIcon();
}


