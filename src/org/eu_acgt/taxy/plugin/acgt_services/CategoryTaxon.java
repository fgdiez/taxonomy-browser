package org.eu_acgt.taxy.plugin.acgt_services;

import java.util.ArrayList;

import org.eu_acgt.repo.model.FunctionalCategory;

import uk.ac.ebi.taxy.TaxonProperty;
import uk.ac.ebi.taxy.TaxonProxy;
import uk.ac.ebi.taxy.TaxonomyPlugin;

public class CategoryTaxon extends TaxonProxy {

   static final String PROP_ONTOURI = "ID";
   static final String PROP_NAME = "Name";
   static final String PROP_DESC = "Description";
   static final String PROP_MAINCAT = "MainCategory";

   public static final ArrayList<String> propertyNames = new ArrayList<String>(3);
   static {
      propertyNames.add(PROP_ONTOURI);
      propertyNames.add(PROP_NAME);
      propertyNames.add(PROP_DESC);
      propertyNames.add(PROP_MAINCAT);
   }

   final FunctionalCategory cat;
   final TaxonProxy parent;
   ArrayList<TaxonProxy> children = new ArrayList<TaxonProxy>();

   public CategoryTaxon( FunctionalCategory cat, TaxonProxy parent, String id, String name, TaxonomyPlugin taxonomyProvider) {

      super(id, name, taxonomyProvider);
      this.cat = cat;
      this.parent = parent;
   }

   @Override
   public TaxonProperty getProperty( String propertyName) {

      if (cat == null) return null;

      if (propertyName.equals(PROP_ONTOURI)) {
         return new TaxonProperty(PROP_ONTOURI, cat.getOntoUri());
      }
      else if (propertyName.equals(PROP_NAME)) {
         return new TaxonProperty(PROP_NAME, cat.getName());
      }
      else if (propertyName.equals(PROP_DESC)) {
         return new TaxonProperty(PROP_DESC, cat.getDescription());
      }
      else if (propertyName.equals(PROP_MAINCAT)) { return new TaxonProperty(PROP_MAINCAT, ""
                                                                                           + cat.getMainCategory()); }
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

   public void setChildren( ArrayList<TaxonProxy> children) {

      this.children = children;
   }

   @Override
   public boolean hasChildren() {

      return children != null && children.size() > 0;
   }
}
