package uk.ac.ebi.taxy;

import java.util.Vector;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * This interface defines the methods required by the graphical components
 * of the application for accessing the taxonomic data.
 * The taxonomic data is modeled through the abstraction of
 * <code>TaxonProxy</code> which is a proxy for accessing
 * the properties of a taxon. This interface therefore, provides methods
 * to:
 * <ul>
 * <li>Connect/disconnect to/from the source of taxonomic data.</li>
 * <li>Search and navigate through the taxonomy tree
 *     represented by a hierarchy of <code>TaxonProxy</code> nodes.</li>
 * <li>Access the properties of taxon nodes.</li>
 * </ul>
 * <br>
 * In orther to create a new plug-in you will need to implement this interface
 * together with <code>PluginDescription</code>. The implementation must have
 * a constructor with no arguments.
 *
 * @see uk.ac.ebi.taxy.PluginDescription
 * @see uk.ac.ebi.taxy.TaxonProxy
 */
public interface TaxonomyPlugin
{
    /**
     * Set the parent frame for any dialog rised from a plug-in.
     * This allows to launch dialogs with a location relative to the
     * main application window as well as to launch modal dialogs.
     *
     * @param parent The parent window.
     */
    public void setFrameParent( javax.swing.JFrame parent );

    //////////////////////////
    // Connection operations

    /**
     * Connect to the taxonomic source of data.
     */
    public boolean connect();

    /**
     * Disconnect from the taxonomic source of data.
     */
    public boolean disconnect();

    /**
     * get root taxon
     * @return the root taxon
     */
    public TaxonProxy getRoot();

    ///////////////////////////
    // taxon access operations

    /**
     * Get a taxon by ID. Searches for a taxon with a given ID.
     * The match must be exact and unique.
     *
     * @param taxonID The identifier of the taxon.
     */
    public TaxonProxy getTaxonProxy( String taxonID );

    /**
     * Get taxa that matches a given name. The name can contain
     * '*' in any possition meaning any span of characters.
     */
    public Vector getTaxaByName( String name );

    /**
     * Get the children of a given taxon. Retrieves all
     * the nodes whose parent has a given ID. It only returns
     * the inmediate children, not any of the grand-children.
     *
     * @param taxonID The identifier of the parent taxon.
     */
    //public Vector getChildren( String taxonID );

    /**
     * Returns whether or not a given taxon has children.
     *
     *@param taxonID the taxon identifier.
     */
    //public boolean hasChildren( String taxonID );

    /**
     *  Get the identifier of a given taxon.
     *
     * @param taxonID The identifier of the "son" taxon.
     */
//    public String getParentID( String taxonID );

    /**
     * Get a property by name. It provides access to any property
     * of a given taxon.
     * @param taxonID The identifier of taxon whose property we are interested on.
     * @param propertyName The name of the property.
     * @return The property of the taxon or <code>null</code> if it was not found.
     * @see uk.ac.ebi.taxy.TaxonProperty
     */
//    public TaxonProperty getProperty( String taxonID, String propertyName );

    /**
     * Returns all the property names available.
     */
//    public Vector getPropertyNames();

    ////////////////////////////
    // taxon command operations
/*
    public void executeCommand( TaxonProxy taxon, String command )
        throws CommandException;

    public Vector // command-name list
           getCommands( TaxonProxy taxon );
*/
}


