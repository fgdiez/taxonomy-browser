package uk.ac.ebi.taxy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import uk.ac.ebi.util.SwingWorker;

/**
 * This class is the controller for <code>SearchUI</code>.
 */
public class SearchController {
   private static final Logger logger = Logger.getLogger("TaxyCore");
   private SearchResultController _resultController;
   private TaxyController _browserController;

   SearchWorker searchWorker = new SearchWorker();
   
   /**
    * Constructs a new controller that will operate over the specified
    * <code>SearchResultController</code> and <code>BrowserController</code>.
    */
   public SearchController( SearchResultController resultController, TaxyController browserController) {

      _resultController = resultController;
      _browserController = browserController;
   }

   /**
    * Perform a new taxa search.
    * 
    * @param expression
    *           The search expression.
    * @param type
    *           The type of search.
    */
   public void search(String expression, SearchType type) {
      searchWorker.interrupt();
      searchWorker.search(expression, type);
      searchWorker.start();
   }
   
   final class SearchWorker extends SwingWorker {
     String expression;
     SearchType type;
     
     public void search(String expression, SearchType type) {
        this.expression = expression;
        this.type = type;
     }
     
     public Object construct() {
        try {
           logger.info("Worker construct entered");
           _browserController.setWaitCursor();
           if (type == SearchType.GET_TAXA_BY_NAME) {
              searchByName(expression);
           }
           else if (type == SearchType.GET_TAXON_BY_ID) {
              searchByID(expression);
           }
           return null;
        }
        finally {
           _browserController.setDefaultCursor();
        }
     }
   }
   
   void searchByName(final String expression){
      logger.info("searching by name");
      List<TaxonProxy>taxa = _browserController.getTaxonProvider().getTaxaByName(expression);
      if ((taxa == null) || (taxa.size() == 0)) {
         _browserController.showWarningDialog("Search Result", "No results");
         return;
      }
      String title = "Search Dialog";
      String message = null;

      if (taxa.size() > 1) {
         message = "" + taxa.size() + " taxa found";
      }
      else {
         message = "" + taxa.size() + " taxon found";
      }

      _browserController.showInformativeDialog(title, message);

      java.util.Collections.sort(taxa);

      _resultController.setResult(taxa);
   }

   void searchByID(final String id){
      TaxonProxy taxon = _browserController.getTaxonProvider().getTaxonProxy(id);

      if (taxon == null) {
         String title = "Search Dialog";
         String message = "No taxon found with this ID";

         _browserController.showWarningDialog(title, message);
      }
      else {
         List<TaxonProxy> taxa = new ArrayList<TaxonProxy>(1);
         taxa.add(taxon);

         String title = "Search Dialog";
         String message = "Taxon found";
         _browserController.showInformativeDialog(title, message);

         _resultController.setResult(taxa);
      }
   }

}
