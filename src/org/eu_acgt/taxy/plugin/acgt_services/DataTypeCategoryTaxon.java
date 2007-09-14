package org.eu_acgt.taxy.plugin.acgt_services;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import org.eu_acgt.repo.model.TypeTaxon;

import uk.ac.ebi.taxy.TaxonProperty;
import uk.ac.ebi.taxy.TaxonProxy;
import uk.ac.ebi.taxy.TaxonomyPlugin;

public class DataTypeCategoryTaxon extends TaxonProxy {

//  String uriId;
//  String name;
//  long id;
//  String description;
//  boolean mainTaxon;
//  TypeTaxon parent;
//  Set<TypeTaxon> children = new HashSet<TypeTaxon>();	
   static final String PROP_URI_ID = "URI ID";
   static final String PROP_DB_ID = "DB ID";
   static final String PROP_NAME = "Name";
   static final String PROP_DESC = "Description";
   static final String PROP_MAIN_TAXON = "Main Taxon";

   public static final ArrayList<String> propertyNames = new ArrayList<String>(3);
   static {
      propertyNames.add(PROP_URI_ID);
      propertyNames.add(PROP_DB_ID);
      propertyNames.add(PROP_NAME);
      propertyNames.add(PROP_DESC);
      propertyNames.add(PROP_MAIN_TAXON);
   }

   final TypeTaxon cat;
//   final TaxonProxy parent;
   ArrayList<TaxonProxy> children = new ArrayList<TaxonProxy>();
   private static final ImageIcon ICON = new ImageIcon(TaxonProxy.class.getClassLoader().getResource("datatype_class.png"));

   public DataTypeCategoryTaxon( TypeTaxon cat, TaxonProxy parent, String id, String name, TaxonomyPlugin taxonomyProvider) {

      super(id, name, taxonomyProvider);
      this.cat = cat;
      this.parent = parent;
   }

   @Override
   public TaxonProperty getProperty( String propertyName) {

      if (cat == null) return null;

      if (propertyName.equals(PROP_URI_ID)) {
         return new TaxonProperty(PROP_URI_ID, cat.getUriId());
      }
      else if (propertyName.equals(PROP_DB_ID)) {
          return new TaxonProperty(PROP_DB_ID, "" + cat.getId());
       }
      else if (propertyName.equals(PROP_NAME)) {
          return new TaxonProperty(PROP_NAME, cat.getName());
       }
      else if (propertyName.equals(PROP_DESC)) {
         return new TaxonProperty(PROP_DESC, cat.getDescription());
      }
      else if (propertyName.equals(PROP_MAIN_TAXON)) { 
    	  return new TaxonProperty(PROP_MAIN_TAXON, "" + cat.isMainTaxon()); }
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

//   @Override
//   public TaxonProxy getParent() {
//
//      return parent;
//   }

   public void setChildren( ArrayList<TaxonProxy> children) {

      this.children = children;
   }

   @Override
   public boolean hasChildren() {

      return children != null && children.size() > 0;
   }

   public ImageIcon getIcon() {

       return ICON;
    }

   @Override
   public String getTaxonTitle() {
   	return "Data Type Category Metadata";
   }
}
