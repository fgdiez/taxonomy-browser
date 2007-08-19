package uk.ac.ebi.taxy;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import uk.ac.ebi.util.Debug;

/**
 * This class defines a tree of <code>TaxonProxy</code>.
 */
@SuppressWarnings("serial")
public class TaxonProxyTree extends JTree {

   // /////////////////////
   // Private Attributes
   // /////////////////////

   private AlphabeticComparator _taxonComparator = new AlphabeticComparator();

   private java.util.Hashtable<String, TaxonProxyTreeNode> _hash = new java.util.Hashtable<String, TaxonProxyTreeNode>();

   /**
    * Tree container.
    */
   private JScrollPane _pane; // container of the tree

   /**
    * Path to the last taxon in the lineage when you set the content of the
    * tree.
    */
//   private TreePath _mainTaxonPath;

   // /////////////////////
   // Public Operations
   // /////////////////////

   /**
    * Constructs a new empty tree.
    */
   public TaxonProxyTree() {

      setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Root")));
      getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

      setRootVisible(false);

      setShowsRootHandles(true);

      setScrollsOnExpand(false);

      _pane = new JScrollPane(this);

      DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {

         public Component getTreeCellRendererComponent( JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            TaxonProxyTreeNode node = (TaxonProxyTreeNode) value;
            setIcon(((TaxonProxy) node.getUserObject()).getIcon());
            return this;
         }
      };

      renderer.setLeafIcon(new ImageIcon(getClass().getClassLoader().getResource("file.gif")));
      setCellRenderer(renderer);
   }

   /**
    * Returns a graphical container of the tree.
    */
   public JComponent getContainer() {

      return _pane;
   }

   /**
    * Shows the specified taxon. In case that the taxon is not already in the
    * tree, it adds all the missing tree nodes from that taxon up to the root of
    * the tree. It scrolls to make the specified taxon visible in the tree view.
    */
   public void showTaxon( TaxonProxy taxon) {

      Debug.TRACE("TaxonProxyTree: setTaxon with ID = " + taxon.getID());

      TaxonProxyTreeNode targetNode = _hash.get(taxon.getID());
      if (targetNode != null) {
         TreePath path = new TreePath(targetNode.getPath());
         clearSelection();
         addSelectionPath(path);
         scrollPathToVisible(path);
         return;
      }

      TaxonProxyTreeNode parentNode = null;
      boolean taxonMatched = false;
      TaxonProxy parentTaxon = taxon.getParent();

      // iterate over the parents
      for (; (!taxonMatched) && (parentTaxon != null); parentTaxon = parentTaxon.getParent()) {
         Debug.TRACE("TaxonProxyTree.setTaxon: parentID = " + parentTaxon.getID());

         String parentID = parentTaxon.getID();

         parentNode = _hash.get(parentTaxon.getID());

         if (parentNode != null) {
            taxonMatched = true;
         }
         else {
            taxonMatched = false;

            parentNode = new TaxonProxyTreeNode(parentTaxon);
            _hash.put(parentID, parentNode);
         }

         ArrayList<TaxonProxy> childrenTaxa = parentTaxon.getChildren();
         Debug.TRACE("TaxonProxyTree.setTaxon: " + childrenTaxa.size() + " children");

         java.util.Collections.sort(childrenTaxa, _taxonComparator);

         // iterate over the children
         for (int j = 0; j < childrenTaxa.size(); ++j) {
            TaxonProxy child = childrenTaxa.get(j);

            Debug.TRACE("TaxonProxyTree.setTaxon: childID = " + child.getID());

            TaxonProxyTreeNode childNode = _hash.get(child.getID());

            if (childNode == null) {
               childNode = new TaxonProxyTreeNode(child);
               _hash.put(child.getID(), childNode);
            }

            parentNode.add(childNode);
         }
      }

      if (!taxonMatched) {
         // set tree root
         if (parentNode == null) {
            // 'taxon' is the root
            parentNode = new TaxonProxyTreeNode(taxon);
            _hash.put(taxon.getID(), parentNode);
         }

         DefaultTreeModel model = new DefaultTreeModel(parentNode);

         model.setAsksAllowsChildren(true);
         setModel(model);
         setRootVisible(true);
      }

      targetNode = _hash.get(taxon.getID());

      Debug.ASSERT(targetNode != null, "Target node not found!");

      TreePath path = new TreePath(targetNode.getPath());
      clearSelection();
      addSelectionPath(path);
      scrollPathToVisible(path);
   }

   /**
    * Expands the specified node. It adds to the node all the children of the
    * taxon that it contains.
    */
   public void expandNode( TaxonProxyTreeNode node) {

      if (node.isComplete()) { return; }

      // add children to the node

      TaxonProxy taxon = (TaxonProxy) node.getUserObject();

      ArrayList<TaxonProxy> children = taxon.getChildren();

      java.util.Collections.sort(children, _taxonComparator);

      for (int i = 0; i < children.size(); ++i) {
         TaxonProxy childTaxon = children.get(i);

         if (!_hash.containsKey(childTaxon.getID())) {
            TaxonProxyTreeNode childNode = new TaxonProxyTreeNode(childTaxon);

            _hash.put(childTaxon.getID(), childNode);

            node.add(childNode);
         }
      }
   }

   /**
    * Compares alphabetically two <code>TaxonProxy</code>.
    */
   class AlphabeticComparator implements Comparator<TaxonProxy> {

      /**
       * Compares two <code>TaxonProxy</code>.
       * 
       * @returns The result of comparing alphabetically the main name of both
       *          taxa.
       */
      public int compare( TaxonProxy taxonA, TaxonProxy taxonB) {

         String nameA = taxonA.getName().toLowerCase();
         String nameB = taxonB.getName().toLowerCase();
         return nameA.compareTo(nameB);
      }

      public boolean equals( Object a) {

         return equals(a);
      }
   };
}
