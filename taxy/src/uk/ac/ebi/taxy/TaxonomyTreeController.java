package uk.ac.ebi.taxy;

/**
 * This class defines the controller of <code>TaxonomyTreeUI</code>.
 * @see uk.ac.ebi.taxy.TaxonomyTreeUI
 */
public class TaxonomyTreeController
{
    ////////////////////////////////
    // Private Attributes
    ////////////////////////////////

    private TaxonomyTreeUI _ui;

    private TaxonController _taxonController;

    private StatusBarController _statusBarController;

    /////////////////////////////////
    // Public Operations
    /////////////////////////////////

    /** Constructs a new controller for the specified <code>TaxonomyTreeUI</code>
     * and which operates on the specified <code>TaxonController</code> and
     * <code>StatusBarController</code>.
     */
    public TaxonomyTreeController( TaxonomyTreeUI      ui, 
                                   TaxonController     taxonController,
                                   StatusBarController statusBarController )
    {
        _ui                = ui;
        _taxonController     = taxonController;
        _statusBarController = statusBarController;
    }

    /** Notify that the specified taxon has been selected in the UI.
     * The selection happen on the <code>TaxonomyTreeUI</code> associated 
     * with this controller and will be forwarded to the appropiate controllers.
     */
    public void notifySelection( TaxonProxy taxon )
    {
        _taxonController.setTaxon( taxon );

        _statusBarController.setText( taxon.getLineage() );
    }

    /** Shows a taxon in the UI. 
     */
    public void showTaxon( TaxonProxy taxon )
    {
        _ui.showTaxon( taxon );
    }

    /** Clears any current taxon selection on the tree.
     */
    public void clearSelection()
    {
        _ui.clearSelection();
    }
}

