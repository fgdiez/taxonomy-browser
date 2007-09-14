package org.eu_acgt.taxy.plugin.acgt_services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;

import org.eu_acgt.repo.model.DataType;

import uk.ac.ebi.taxy.TaxonProperty;
import uk.ac.ebi.taxy.TaxonProxy;
import uk.ac.ebi.taxy.TaxonomyPlugin;

public class DataTypeTaxon extends TaxonProxy {

	static final String PROP_URI_ID = "URI ID";

	static final String PROP_DB_ID = "DB ID";

	static final String PROP_NAME = "Name";

	static final String PROP_VERSION = "Version";

	static final String PROP_CREATED = "Created";

	static final String PROP_FORMAT = "Format";

	static final String PROP_PRIMITIVE = "Is primitive";

	static final String[] propertyNames = { 
		PROP_URI_ID, PROP_DB_ID, PROP_NAME, PROP_VERSION, PROP_CREATED, PROP_FORMAT, PROP_PRIMITIVE };

	static ImageIcon ICON;

	final DataType type;

//	final TaxonProxy parent;

	public DataTypeTaxon( DataType type, TaxonProxy parent, String id, String name, TaxonomyPlugin taxonomyProvider) {

		super( id, name, taxonomyProvider);
		this.type = type;
		this.parent = parent;
		ICON = new ImageIcon( TaxonProxy.class.getClassLoader().getResource( "datatype.png"));
	}

	@Override
	public TaxonProperty getProperty( String propertyName) {

		if( type == null) return null;

		if( propertyName.equals( PROP_URI_ID)) {
			return new TaxonProperty( PROP_URI_ID, type.getUriId());
		}
		else if( propertyName.equals( PROP_DB_ID)) {
			return new TaxonProperty( PROP_DB_ID, "" + type.getId());
		}
		else if( propertyName.equals( PROP_NAME)) {
			return new TaxonProperty( PROP_NAME, type.getName());
		}
		else if( propertyName.equals( PROP_VERSION)) {
			return new TaxonProperty( PROP_VERSION, "" + type.getVersion());
		}
		else if( propertyName.equals( PROP_FORMAT)) {
			return new TaxonProperty( PROP_FORMAT, "" + type.getFormat());
		}
		else if( propertyName.equals( PROP_PRIMITIVE)) {
			return new TaxonProperty( PROP_PRIMITIVE, "" + type.getPrimitiveType());
		}
		else if( propertyName.equals( PROP_CREATED)) {
			return new TaxonProperty( PROP_CREATED, "" + type.getCreated());
		}
		return null;
	}

	@Override
	public List< String> getPropertyNames() {

		return Arrays.asList( propertyNames);
	}

	@Override
	public ArrayList< TaxonProxy> getChildren() {

		return null;
	}

//	@Override
//	public TaxonProxy getParent() {
//
//		return parent;
//	}

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
		return "Data Type Metadata";
	}
}
