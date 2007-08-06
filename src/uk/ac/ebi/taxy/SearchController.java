package uk.ac.ebi.taxy;

import uk.ac.ebi.util.Debug;

import java.util.Vector;


/**
 * This class is the controller for <code>SearchUI</code>.
 */
public class SearchController
{
    ////////////////////////
    // Private Attributes
    ////////////////////////

    private SearchResultController _resultController;

    private BrowserController _browserController;

    ///////////////////////
    // Public Operations
    ///////////////////////

    /** Constructs a new controller that will operate over the
     * specified <code>SearchResultController</code> and
     * <code>BrowserController</code>.
     */
    public SearchController( SearchResultController resultController,
                             BrowserController browserController )
    {
        _resultController = resultController;
        _browserController = browserController;
    }

    /** Perform a new taxa search.
     * @param expression The search expression.
     * @param type The type of search.
     */
    public void search( String expression, SearchType type )
    {
        try
        {
            TaxonomyPlugin taxonProvider = _browserController.getTaxonProvider();

            Debug.ASSERT( taxonProvider != null, "Taxon provider not set." );

            Vector taxa = null;

            if( type == SearchType.GET_TAXA_BY_NAME )
            {
                taxa = taxonProvider.getTaxaByName( expression );

                if( (taxa == null) || (taxa.size() == 0) )
                {
                    _browserController.showWarningDialog( "Search Result", "No results" );
                    return;
                }

                String title = "Search Dialog";
                String message = null;

                if( taxa.size() > 1 )
                {
                    message = "" + taxa.size() + " taxa found";
                }
                else
                {
                    message = "" + taxa.size() + " taxon found";
                }

                _browserController.showInformativeDialog( title, message );

                java.util.Collections.sort( taxa );

                _resultController.setResult( taxa );
            }
            else if( type == SearchType.GET_TAXON_BY_ID )
            {
                String id = expression;

                TaxonProxy taxon = taxonProvider.getTaxonProxy( id );

                if( taxon == null )
                {
                    String title = "Search Dialog";
                    String message = "No taxon found with this ID";

                    _browserController.showWarningDialog( title, message );
                }
                else
                {
                    taxa = new Vector( 1 );
                    taxa.add( taxon );

                    String title = "Search Dialog";
                    String message = "Taxon found";
                    _browserController.showInformativeDialog( title, message );

                    _resultController.setResult( taxa );
                }
            }
            else
            {
                    Debug.TRACE( "ERROR: Unknown query type" );
                    return;
            }
        }
        catch( NumberFormatException ex )
        {
            String title = "Input Dialog";
            String message = "Illegal Input format.\nYou must type an integer";

            _browserController.showWarningDialog( title, message );
        }
    }
}

