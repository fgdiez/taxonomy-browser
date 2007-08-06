package uk.ac.ebi.taxy;


import javax.swing.tree.*;
import java.util.Vector;
import java.util.Enumeration;



/** This class defines a tree node that contains a <code>TaxonProxy</code>.
 */
public class TaxonProxyTreeNode extends DefaultMutableTreeNode
{
    ///////////////////////
    // Public Operations
    ///////////////////////

    /** Constructs a new node containing the specified taxon.
     * 
     * @param taxon  The node's content.
     */
    public TaxonProxyTreeNode( TaxonProxy taxon ) 
    {
        super( taxon, true );

        boolean hasChildren = taxon.hasChildren();
        
        setAllowsChildren( hasChildren );
    }

    /**
     * Returns whether or not the taxon's children have already been added 
     * to the node.
     */
    public boolean isComplete()
    {
        boolean hasChildren = (super.children != null) && (super.children.size() > 0);

        return (!super.allowsChildren) || hasChildren;
    }
}

