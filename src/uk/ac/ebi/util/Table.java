package uk.ac.ebi.util;

import java.util.logging.Logger;

import javax.swing.event.TableModelListener;

/**
 * This class defines a generic table data structure. It can directly be
 * represented graphically as it implements a <i>swing</i>
 * <code>TableModel</code> and provides a method to return a
 * <code>JTable</code>.
 */
public class Table implements javax.swing.table.TableModel {
   
   private static Logger logger = Logger.getLogger("TaxyCore");
   
   private String[][] rows;
   private String[] _columnNames;

   private javax.swing.JTable _jTable;

   
   /**
    * Constructs a new table with the specified column names and rows.
    * 
    * @param columnNames
    *           The table's column names.
    * @param rows
    *           The table's rows. It is a collection of <code>TableRow</code>.
    */
   public Table( String[] columnNames, String[][] rows) {

      Debug.ASSERT(columnNames != null, "Null column names");
      Debug.ASSERT(rows != null, "Null rows");

      this.rows = rows;
      _columnNames = columnNames;
      _jTable = new javax.swing.JTable(this);
   }

   /**
    * Returns a graphical representation of the table.
    */
   public javax.swing.JTable getJTable() {

      return _jTable;
   }

   // ////////////////////////////
   // TableModel implementation

   public Class<String> getColumnClass( int columnIndex) {

      return String.class;
   }

   public int getColumnCount() {

      return _columnNames.length;
   }

   public String getColumnName( int columnIndex) {

      return _columnNames[columnIndex];
   }

   public int getRowCount() {

      return rows.length;
   }

   public Object getValueAt( int rowIndex, int columnIndex) {

      return rows[rowIndex][columnIndex];
   }

   public boolean isCellEditable( int rowIndex, int columnIndex) {

      return false;
   }

   /** Void implementation */
   public void addTableModelListener( TableModelListener listener) {

   }

   /** Void implementation */
   public void removeTableModelListener( TableModelListener listener) {

   }

   /** Void implementation */
   public void setValueAt( Object aValue, int rowIndex, int columnIndex) {

   }
   
   public void setTableColumnsWidth(int tableWidth) {

      int maxTotalCharSize = 0;
      int[] maxColumnCharSize = new int[_jTable.getColumnCount()];
      
      for(int j = 0; j < _jTable.getColumnCount(); ++j) {
         maxColumnCharSize[j] = 0;
         for(int i = 0; i < _jTable.getRowCount(); ++i) {
            maxColumnCharSize[j] += _jTable.getValueAt(i, j).toString().length();
         }
         if(maxTotalCharSize < maxColumnCharSize[j]) {
            maxTotalCharSize = maxColumnCharSize[j];
         }
      }
      
      for(int j = 0; j < _jTable.getColumnCount(); ++j) {
         int columnWidth = (maxColumnCharSize[j] * tableWidth) / maxTotalCharSize;
//         logger.info("columnWidth: " + columnWidth);
         if(columnWidth < 30) columnWidth = 30;
         _jTable.getColumnModel().getColumn(j).setPreferredWidth(columnWidth);
      }
   }

}
