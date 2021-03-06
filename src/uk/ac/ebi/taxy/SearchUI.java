package uk.ac.ebi.taxy;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import uk.ac.ebi.util.Debug;

/**
 * This class defines an UI for requesting taxa searches.
 */
@SuppressWarnings("serial")
public class SearchUI extends JPanel implements ActionListener {

   /**
    * length in characters of _inputField
    */
   private final int TEXT_FIELD_LENGTH = 30;

   /**
    * Controller of this UI
    */
   private SearchController _controller;

   /**
    * button for the submission of a search request.
    */
   private JButton _searchButton;

   /**
    * Text field for user inputs.
    */
   private JTextField _inputField;

   /**
    * Combo box for selection of search type.
    */
   private JComboBox _queryTypes;

   /**
    * Constructs a new search UI that operates over the specified
    * <code>SearchResultController</code> and <code>BrowserController</code>.
    */
   public SearchUI( SearchResultController resultController, TaxyController browserController) {

      // compose graphycal elements
      compose();

      // create controller
      _controller = new SearchController(resultController, browserController);
   }

   /**
    * Returns the controller of this UI.
    */
   public SearchController getController() {

      return _controller;
   }

   /**
    * Overrides the setEnabled operation. It is required for disabling properly
    * the UI components contained whinin this class.
    */
   public void setEnabled( boolean enabled) {

      super.setEnabled(enabled);

      Component[] components = getComponents();

      for (int i = 0; i < components.length; i++) {
         components[i].setEnabled(enabled);
      }
   }

   /**
    * Clears the content of this UI.
    */
   public void clearView() {

      removeAll();
      compose();
      revalidate();
   }

   /**
    * Reacts to events from the <i>Search</i> button. It forwards the search
    * request to the controller.
    */
   public void actionPerformed( ActionEvent e) {

      Object source = e.getSource();

      if ((source == _searchButton) || (source == _inputField)) {
         // search button clicked or Enter key pressed in the input field

         String searchExpression = _inputField.getText();

         SearchType searchType = (SearchType) _queryTypes.getSelectedItem();

         _controller.search(searchExpression, searchType);
      }
      else {
         Debug.TRACE("ERROR: Unknown event");
      }

   } 

   /**
    * Composes the graphical elements of this UI component.
    */
   private void compose() {

      // creation of graphycal elements
      _searchButton = new JButton("Search!");

      _searchButton.addActionListener(this);

      _inputField = new JTextField();

      _inputField.addActionListener(this);

      _queryTypes = new JComboBox(SearchType.ALL);

      // add elements
      add(new JLabel("Search Form"), BorderLayout.NORTH);

      _inputField.setColumns(TEXT_FIELD_LENGTH);

      add(_inputField, BorderLayout.CENTER);

      add(_searchButton, BorderLayout.CENTER);

      add(_queryTypes);

   } // operation compose

} // class SearchUI

