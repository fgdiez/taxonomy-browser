package org.eu_acgt.taxy.plugin.acgt_services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.eu_acgt.repo.model.Operation;
import org.eu_acgt.repo.model.Service;
import org.eu_acgt.repo.model.ServiceLocation;

import uk.ac.ebi.taxy.TaxonProperty;
import uk.ac.ebi.taxy.TaxonProxy;
import uk.ac.ebi.taxy.TaxonomyPlugin;
import uk.ac.ebi.util.Table;


public class ServiceTaxon extends TaxonProxy {

//    Set<ServiceLocation> serviceLocations = new HashSet<ServiceLocation>();
//    Set<ServiceQualityInstance> serviceQualities = new HashSet<ServiceQualityInstance>();
//    Set<FunctionalCategory> functionalCategories = new HashSet<FunctionalCategory>();
    static final String PROP_DESC = "description";
    static final String PROP_LSID = "lsid";
    static final String PROP_AUTHOR = "author";
    static final String PROP_AUTHORITY = "authority";
    static final String PROP_HELP = "help";
    static final String PROP_CREATED = "created";
    static final String PROP_OPERATIONS = "operations";
	static final String PROP_LOCATION = "Location";

    static final String OPER_ID = "OperationID";
    static final String OPER_NAME = "OperationName";
    static final String OPER_DESC = "Description";
    static final String OPER_HELP = "OperationHelp";
    static final String OPER_NUM_PARAMS = "#Params";

	static final String LOCATION_URI = "EndPointURI";
	static final String LOCATION_STATUS = "Status";
	static ImageIcon ICON;

    public static final ArrayList<String> propertyNames = new ArrayList<String>(3);
    static {
//      propertyNames.add( PROP_LSID);
        propertyNames.add( PROP_DESC);
        propertyNames.add( PROP_HELP);
        propertyNames.add( PROP_AUTHOR);
        propertyNames.add( PROP_AUTHORITY);
        propertyNames.add( PROP_CREATED);
        propertyNames.add( PROP_OPERATIONS);
        propertyNames.add( PROP_LOCATION);
    }

    final Service svc;
    final TaxonProxy parent;

    public ServiceTaxon( Service svc, TaxonProxy parent, String id, String name, TaxonomyPlugin taxonomyProvider) {
        super( id, name, taxonomyProvider);
        this.svc = svc;
        this.parent = parent;
        ICON = new ImageIcon(TaxonProxy.class.getClassLoader().getResource( "file.gif"));
    }

    @Override
    public TaxonProperty getProperty( String propertyName) {

        if(svc == null) return null;

        if(propertyName.equals(PROP_DESC)) {
            return new TaxonProperty(PROP_DESC, svc.getDescription());
        }
        else if(propertyName.equals(PROP_LSID)) {
            return new TaxonProperty(PROP_LSID, svc.getLsid());
        }
        else if(propertyName.equals(PROP_AUTHOR)) {
            return new TaxonProperty(PROP_AUTHOR, svc.getAuthor());
        }
        else if(propertyName.equals(PROP_AUTHORITY)) {
            return new TaxonProperty(PROP_AUTHORITY, svc.getAuthority());
        }
        else if(propertyName.equals(PROP_HELP)) {
            return new TaxonProperty(PROP_HELP, svc.getHelp());
        }
        else if(propertyName.equals(PROP_CREATED)) {
            return new TaxonProperty(PROP_CREATED, "" + svc.getCreated());
        }
        else if( propertyName.equals(PROP_OPERATIONS)) {
        	Vector<String> columnNames = new Vector<String>();
        	columnNames.add(OPER_ID);
        	columnNames.add(OPER_NAME);
        	columnNames.add(OPER_DESC);
        	columnNames.add(OPER_HELP);
        	columnNames.add(OPER_NUM_PARAMS);
        	String[][] rows = new String[svc.getOperations().size()][columnNames.size()];
        	int i=0;
        	for(Iterator<Operation> operIter = svc.getOperations().iterator(); operIter.hasNext();) {
        		Operation oper = operIter.next();
        		int j=0;
        		rows[i][j++] = String.valueOf(oper.getId());
        		rows[i][j++] = oper.getName();
        		rows[i][j++] = oper.getDescription();
        		rows[i][j++] = oper.getHelp();
        		rows[i][j++] = String.valueOf(oper.getParameters().size());
        		i++;
        	}
        	Table operTable = new Table(columnNames, rows);
        	return new TaxonProperty(PROP_OPERATIONS, operTable);
        }
        else if( propertyName.equals(PROP_LOCATION)) {
        	Vector<String> columnNames = new Vector<String>();
        	columnNames.add(LOCATION_URI);
        	columnNames.add(LOCATION_STATUS);
        	String[][] rows = new String[svc.getServiceLocations().size()][columnNames.size()];
        	int i=0;
        	for(Iterator<ServiceLocation> locationIter = svc.getServiceLocations().iterator(); 
        	    locationIter.hasNext();) {
        		ServiceLocation location = locationIter.next();
        		int j=0;
        		rows[i][j++] = location.getEndpointUri();
        		rows[i][j++] = location.getServiceStatus();
        		i++;
        	}
        	Table operTable = new Table(columnNames, rows);
        	return new TaxonProperty(PROP_LOCATION, operTable);
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

    @Override
    public TaxonProxy getParent() {
        return parent;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public ImageIcon getIcon() {
        return ICON;
    }
}
