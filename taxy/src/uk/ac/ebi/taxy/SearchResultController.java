package uk.ac.ebi.taxy;

import java.util.Vector;


/**
 * This class is the controller for <code>SearchResultUI</code>. 
 */
public class SearchResultController
{
    //////////////////////////
    // Private Attributes
    //////////////////////////

    private SearchResultUI _ui;
    
    private TaxonomyTreeController _taxonomyTreeController;

    private TaxonController _taxonController;

    private StatusBarController _statusBarController;

    //////////////////////////
    // Public Operations
    //////////////////////////

    /** Constructs a new controller that will operate over the
     * specified <code>SearchResultUI</code>, 
     * <code>TaxonomyTreeController</code>, <code>TaxonController</code> 
     * and <code>StatusBarController</code>
     */
    public SearchResultController( SearchResultUI ui, 
                                   TaxonomyTreeController lineageController,
                                   TaxonController taxonController,
                                   StatusBarController statusBarController )
    {
        _ui = ui;
        _taxonomyTreeController = lineageController;
        _taxonController = taxonController;
        _statusBarController = statusBarController;
    }

    /**
     * Sets the search result to be displayed on the
     * associated <code>SearchResultUI</code>.
     * 
     * @param taxa The result to be shown.
     */
    public void setResult( Vector taxa )
    {
        _ui.setContent( taxa );
    }


    /** Notifies to this controller of a taxon selection. The selection
     * will be forwarded to the appropiate destinations (controllers for
     * other UI components of the application).
     * @param taxon The selected taxon.
     */
    public void notifySelection( TaxonProxy taxon )
    {
        _taxonomyTreeController.showTaxon( taxon );

        _taxonController.setTaxon( taxon );

        _statusBarController.setText( taxon.getLineage() );
    }
}

