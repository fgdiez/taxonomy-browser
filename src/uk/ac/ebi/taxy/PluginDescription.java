package uk.ac.ebi.taxy;

/**
 * This interface defines the methods that provide a description of a
 * <code>TaxonomyPlugin</code> implementation. In orther to create a new
 * plug-in you will need to implement this interface together with
 * <code>TaxonomyPlugin</code>. The implementation must have a constructor
 * with no arguments.
 * 
 * It is used by <code>PluginDialog</code> to get a description during the
 * plug-in selection process.
 * 
 * @see uk.ac.ebi.taxy.TaxonomyPlugin
 */
public interface PluginDescription {

   /**
    * Get the full name (with package path inclusive) of the described
    * <code>TaxonomyPlugin</code> implementation.
    */
   public String getPluginClassName();

   /** Get a short name for the Plug-in. */
   public String getName();

   /** Get a description of the Plug-in. */
   public String getDescription();
}
