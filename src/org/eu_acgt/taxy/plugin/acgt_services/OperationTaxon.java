package org.eu_acgt.taxy.plugin.acgt_services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.eu_acgt.repo.model.ArgumentSyntax;
import org.eu_acgt.repo.model.DataType;
import org.eu_acgt.repo.model.Operation;
import org.eu_acgt.repo.model.Parameter;
import org.eu_acgt.repo.model.Service;
import org.eu_acgt.repo.model.ServiceLocation;

import uk.ac.ebi.taxy.TaxonProperty;
import uk.ac.ebi.taxy.TaxonProxy;
import uk.ac.ebi.taxy.TaxonomyPlugin;
import uk.ac.ebi.util.Table;

public class OperationTaxon extends TaxonProxy {

   static final Logger logger = Logger.getLogger("ACGTPlugin");
   static final String PROP_DB_ID = "DB ID";
   static final String PROP_NAME = "Name";
   static final String PROP_DESC = "Description";
   static final String PROP_HELP = "Help";
   static final String PROP_PARAMS = "Parameters";

   static final String PARAM_ID = "ID";
   static final String PARAM_NAME = "Name";
   static final String PARAM_DESC = "Description";
   static final String PARAM_DATATYPE = "DataType";
   static final String PARAM_INPUT = "In/Out/Inout";
   static final String PARAM_SYNTAX = "Syntax";
   static final String[] paramColumnNames = {PARAM_ID, PARAM_NAME, PARAM_DATATYPE, PARAM_INPUT, PARAM_SYNTAX, PARAM_DESC};

   static ImageIcon ICON;

   public static final ArrayList<String> propertyNames = new ArrayList<String>(3);
   static {
      propertyNames.add(PROP_DB_ID);
      propertyNames.add(PROP_NAME);
      propertyNames.add(PROP_DESC);
      propertyNames.add(PROP_HELP);
      propertyNames.add(PROP_PARAMS);
   }

   final Operation oper;
//   final TaxonProxy parent;

   public OperationTaxon( Operation svc, TaxonProxy parent, String id, String name, TaxonomyPlugin taxonomyProvider) {

      super(id, name, taxonomyProvider);
      this.oper = svc;
      this.parent = parent;
      ICON = new ImageIcon(TaxonProxy.class.getClassLoader().getResource("operation.png"));
   }

   @Override
   public TaxonProperty getProperty( String propertyName) {

      if (oper == null) return null;

      if (propertyName.equals(PROP_DB_ID)) {
         return new TaxonProperty(PROP_DB_ID, "" + oper.getId());
      }
      else if (propertyName.equals(PROP_NAME)) {
         return new TaxonProperty(PROP_NAME, oper.getName());
      }
      else if (propertyName.equals(PROP_DESC)) {
         return new TaxonProperty(PROP_DESC, oper.getDescription());
      }
      else if (propertyName.equals(PROP_HELP)) {
         return new TaxonProperty(PROP_HELP, oper.getHelp());
      }
      else if (propertyName.equals(PROP_PARAMS)) {
    	  logger.info("setting Parameters property");
         int rowSize = oper.getParameters().size();
         if(rowSize == 0 ) {
        	 return null;
         }         
		String[][] rows = new String[rowSize][paramColumnNames.length];
         int i = 0;
         for (Iterator<Parameter> paramIter = oper.getParameters().iterator(); paramIter.hasNext();) {
            Parameter param = paramIter.next();
            int j = 0;
            //PARAM_ID, PARAM_NAME, PARAM_DATATYPE, PARAM_INPUT, PARAM_SYNTAX, PARAM_DESC
            rows[i][j++] = String.valueOf(param.getId());
            rows[i][j++] = param.getName();
            DataType dataType = param.getDataType();
            if(dataType == null) {
    			rows[i][j++] = "";
            }
            else {
    			rows[i][j++] = dataType.getName();
            }
            rows[i][j++] = param.getInput();
            rows[i][j++] = param.getSyntax().getSyntax();
            rows[i][j++] = param.getDescription();
            i++;
         }
         Table paramTable = new Table(paramColumnNames, rows);
         return new TaxonProperty(PROP_PARAMS, paramTable);
      }
      return null;
   }

   @Override
   public ArrayList<String> getPropertyNames() {

      return propertyNames;
   }

   @Override
   public ArrayList<TaxonProxy> getChildren() {

      return null;
   }

//   @Override
//   public TaxonProxy getParent() {
//
//      return parent;
//   }

   @Override
   public boolean hasChildren() {

      return false;
   }

   @Override
   public ImageIcon getIcon() {

      return ICON;
   }
   
   @Override
   public String getTaxonTitle() {
   	return "Operation Metadata";
   }
}
