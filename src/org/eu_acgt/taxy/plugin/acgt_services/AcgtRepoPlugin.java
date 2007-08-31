package org.eu_acgt.taxy.plugin.acgt_services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.eu_acgt.repo.client.ManagerException;
import org.eu_acgt.repo.client.RepoPersistenceWSClient;
import org.eu_acgt.repo.model.FunctionalCategory;
import org.eu_acgt.repo.model.Service;

import uk.ac.ebi.taxy.TaxonProxy;
import uk.ac.ebi.taxy.TaxonomyPlugin;

/**
 * This plug-in allows to connect to and navigate through the hierarchy of
 * services available in ACGT Metadata Repository.
 * 
 */
public class AcgtRepoPlugin implements TaxonomyPlugin {

   RepoPersistenceWSClient repo;
   FunctionalCategory root;
   HashMap<String, TaxonProxy> taxa = new HashMap<String, TaxonProxy>();
   Logger logger = Logger.getLogger("AcgtRepoPlugin");
   static final String ERROR_MESSAGE = "Error connecting to ACGT metadata repository.\n\nError message: ";
   
   public boolean connect() {

      try {
         logger.info("connecting..");
         repo = new RepoPersistenceWSClient("http://mango.ac.uma.es/axis2/services/repo-persistence");
         root = repo.loadFunctionalCategory("urn:lsid:biomoby.org:servicetype:Service");
         CategoryTaxon rootTaxon = createCategoryTaxon(root, null);
         taxa.put(rootTaxon.getID(), rootTaxon);
         addAllChildren(taxa, rootTaxon);
         logger.info("# Functional Categories: " + taxa.size());
         return true;
      }
      catch (org.apache.axis2.AxisFault e) {
         JOptionPane.showMessageDialog(null, getErrorMessage(e), "Connection error", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      catch (org.eu_acgt.repo.client.ManagerException e) {
         JOptionPane.showMessageDialog(null, getErrorMessage(e), "Connection error", JOptionPane.ERROR_MESSAGE);
         return false;
      }
   }

   String getErrorMessage(Throwable e) {
      String message = e.getMessage();
      while(e.getCause()!= null) {
         e = e.getCause();
      }
      return ERROR_MESSAGE + message + "\nError cause: " + e.getClass().getSimpleName() + ": " + e.getMessage();
   }

   void addAllChildren( HashMap<String, TaxonProxy> taxa, CategoryTaxon parent) {

      ArrayList<TaxonProxy> children = getChildren(parent);
      parent.setChildren(children);

      for (Iterator<TaxonProxy> childIter = children.iterator(); childIter.hasNext();) {
         TaxonProxy child = childIter.next();
         taxa.put(child.getID(), child);
         if (CategoryTaxon.class.isInstance(child)) {
            addAllChildren(taxa, (CategoryTaxon) child);
         }
      }
   }

   ArrayList<TaxonProxy> getChildren( CategoryTaxon parentTaxon) {

      ArrayList<TaxonProxy> children = new ArrayList<TaxonProxy>();
      FunctionalCategory parent = parentTaxon.cat;
      try {
         List<Service> svcs = repo.loadServiceByFunctionalCategory(parent.getOntoUri());
         for (Iterator<Service> svcIter = svcs.iterator(); svcIter.hasNext();) {
            Service svc = svcIter.next();
            children.add(createServiceTaxon(svc, parentTaxon));
         }
      }
      catch (ManagerException e) {
         logger.info(e.getMessage());
      }

      for (Iterator<FunctionalCategory> childIter = parent.getChildren().iterator(); childIter.hasNext();) {
         FunctionalCategory cat = childIter.next();
         TaxonProxy childTaxon = createCategoryTaxon(cat, parentTaxon);
         children.add(childTaxon);
      }
      return children;
   }

   CategoryTaxon createCategoryTaxon( FunctionalCategory cat, TaxonProxy parent) {

      if (cat == null) return null;
      return new CategoryTaxon(cat, parent, cat.getOntoUri(), cat.getName(), this);
   }

   ServiceTaxon createServiceTaxon( Service svc, TaxonProxy parent) {

      if (svc == null) return null;
      return new ServiceTaxon(svc, parent, svc.getLsid(), svc.getName(), this);
   }

   public boolean disconnect() {

      taxa = null;
      return true;
   }

   // FunctionalCategory findParent(Collection<FunctionalCategory> elements, int
   // childId) {
   // for(Iterator<FunctionalCategory> parentIter = elements.iterator();
   // parentIter.hasNext(); ){
   // FunctionalCategory parent = parentIter.next();
   // for(Iterator<FunctionalCategory> childIter =
   // parent.getChildren().iterator(); childIter.hasNext(); ) {
   // FunctionalCategory child = childIter.next();
   // if( child.getId() == childId) {
   // return parent;
   // }
   // }
   // }
   // return null;
   // }

   // public TaxonProperty getProperty( Integer taxonID, String propertyName) {
   // return taxa.get( taxonID).getProperty( propertyName);
   // }

   public List<TaxonProxy> getTaxaByName( String name) {

      ArrayList<TaxonProxy> result = new ArrayList<TaxonProxy>();
      for (Iterator<TaxonProxy> taxonIter = taxa.values().iterator(); taxonIter.hasNext();) {
         TaxonProxy taxon = taxonIter.next();
         if (matchText(name.toLowerCase(), taxon.getName().toLowerCase())) {
            result.add(taxon);
         }
      }
      return result;
   }

   private boolean matchText( String expr, String text) {

      if (text.indexOf(expr) >= 0) return true;
      return false;
   }

   public TaxonProxy getTaxonProxy( String taxonID) {

      return taxa.get(taxonID);
   }

   public boolean hasChildren( String taxonID) {

      TaxonProxy element = taxa.get(taxonID);
      if (element == null) return false;

      return element.getChildren().size() > 0;
   }

   public void setParentFrame( JFrame parent) {
      
   }

   public TaxonProxy getRoot() {

      return taxa.get(root.getOntoUri());
   }

}
