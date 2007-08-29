package uk.ac.ebi.taxy;


/**
 * Defines the types of search that can be requested on <code>SearchUI</code>.
 */
public class SearchType {

   public static final SearchType GET_TAXA_BY_NAME = new SearchType("Get Taxa By Name");
   public static final SearchType GET_TAXON_BY_ID = new SearchType("Get Taxon By ID");
   public static final SearchType[] ALL = {GET_TAXA_BY_NAME, GET_TAXON_BY_ID};

   private String _name;

   /**
    * Constructs a new search type with the specified name.
    * 
    * @param name
    *           The name of the search.
    */
   private SearchType( String name) {

      _name = name;
   }

   /**
    * Returns the name of this type of search.
    */
   public String toString() {

      return _name;
   }
}
