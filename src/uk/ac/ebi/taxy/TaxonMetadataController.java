package uk.ac.ebi.taxy;

import uk.ac.ebi.util.Debug;

/** This class is the controller for <code>TaxonUI</code>.
 * @see uk.ac.ebi.taxy.TaxonMetadataUI
 */
public class TaxonMetadataController
{
    ///////////////////////////////
    // Private Attributes
    ///////////////////////////////
    private TaxonMetadataUI _ui;

    ///////////////////////////////
    // Public Operations
    ///////////////////////////////

    /** Constructs a new controller for the specified UI.
     */
    public TaxonMetadataController( TaxonMetadataUI ui)
    {
        _ui = ui;
    }

    /** Sets the taxon to be displayed in the <code>TaxonUI</code>
     * associated with this controller.
     */
    public void setTaxon( TaxonProxy taxon )
    {
        Debug.ASSERT( taxon != null, "Null taxon" );

        _ui.showProperties( taxon );
    }
    
}
