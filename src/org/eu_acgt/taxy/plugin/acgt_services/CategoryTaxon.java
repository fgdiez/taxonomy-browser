package org.eu_acgt.taxy.plugin.acgt_services;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import org.eu_acgt.repo.model.FunctionalCategory;

import uk.ac.ebi.taxy.TaxonProperty;
import uk.ac.ebi.taxy.TaxonProxy;
import uk.ac.ebi.taxy.TaxonomyPlugin;


public class CategoryTaxon extends TaxonProxy {

    static final String PROP_DESC = "description";
    static final String PROP_ONTOURI = "ontoUri";
    static final String PROP_MAINCAT = "mainCategory";
    static ImageIcon LEAF_ICON;
    static ImageIcon PARENT_ICON;

    public static final ArrayList<String> propertyNames = new ArrayList<String>(3);
    static {
        propertyNames.add( PROP_DESC);
        propertyNames.add( PROP_ONTOURI);
        propertyNames.add( PROP_MAINCAT);
    }

    final FunctionalCategory cat;
    final TaxonProxy parent;
    ArrayList<TaxonProxy> children = new ArrayList<TaxonProxy>();

    public CategoryTaxon( FunctionalCategory cat, TaxonProxy parent, String id, String name, TaxonomyPlugin taxonomyProvider) {
        super( id, name, taxonomyProvider);
        this.cat = cat;
        this.parent = parent;
        LEAF_ICON = new ImageIcon(TaxonProxy.class.getClassLoader().getResource( "closedFolder.gif"));
        PARENT_ICON = new ImageIcon(TaxonProxy.class.getClassLoader().getResource( "openFolder.gif"));
    }

    @Override
    public TaxonProperty getProperty( String propertyName) {

        if(cat == null) return null;

        if(propertyName.equals(PROP_DESC)) {
            return new TaxonProperty(PROP_DESC, cat.getDescription());
        }
        else if(propertyName.equals(PROP_ONTOURI)) {
            return new TaxonProperty(PROP_ONTOURI, cat.getOntoUri());
        }
        else if(propertyName.equals(PROP_MAINCAT)) {
            return new TaxonProperty(PROP_MAINCAT, "" + cat.getMainCategory());
        }
        return null;
    }

    @Override
    public ArrayList<String> getPropertyNames() {
        return propertyNames;
    }

    @Override
    public ArrayList<TaxonProxy> getChildren() {
        return children;
    }

    @Override
    public TaxonProxy getParent() {
        return parent;
    }

    public void setChildren(ArrayList<TaxonProxy> children) {
        this.children = children;
    }

    @Override
    public boolean hasChildren() {

        return children != null && children.size() > 0;
    }

    @Override
    public ImageIcon getIcon() {
        if( hasChildren() ) return PARENT_ICON;
        return LEAF_ICON;
    }
}