package uk.ac.ebi.util;

import java.lang.reflect.Method;

/**
 * This class defines debugging utilities. It contains methods to help trace
 * messages from the application execution, as well as control facilities for
 * exiting from the application.
 * 
 */
public class Debug {

   /**
    * Whether or not the debugging is enabled.
    */
   private static boolean _enabled = false;

   /**
    * The method to be called back when exiting the application.
    */
   private static Method _exitCallback;

   /**
    * The object on which the exit call back method is invoked.
    */
   private static Object _exitCallbackObject;

   /**
    * Returns whether or not the debbuging is enabled.
    */
   public static boolean isEnabled() {

      return _enabled;
   }

   /**
    * Enable or disable the debugging.
    */
   public static void setEnabled( boolean enabled) {

      _enabled = enabled;
   }

   /**
    * Prints a trace message if the debugging is enabled.
    * 
    * @param message
    *           Message to be printed.
    */
   public static void TRACE( String message) {

      if (_enabled) {
         System.out.println("TRACE: " + message);
      }
   }

   /**
    * Prints an error message if the debugging is enabled.
    * 
    * @param message
    *           Message to be printed.
    */
   public static void ERROR( String message) {

      if (_enabled) {
         System.err.println("ERROR: " + message);
      }
   }

   /**
    * Check for a condition if the debugging is enabled. If the condition fails
    * a message is shown and the program execution is aborted.
    * 
    * @param condition
    *           Condition to be checked
    * @param message
    *           Message to be shown when condicion fails.
    */
   public static void ASSERT( boolean condition, String message) {

      if (_enabled) {
         try {
            if (!condition) {
               Exception ex = new Exception("ERROR: Assertion failed: " + message);
               throw ex;
            }
         }
         catch (Exception ex) {
            ex.printStackTrace();

            exit();
         }
      }
   }

   /**
    * Register an object that implements an <code>exit()</code> function. This
    * function should not have any arguments, otherwise it will not be
    * registered. Once registered, it will be called back whenever the
    * {@link Debug#exit Debug.exit()} function is called.
    * 
    * @param exitCallbackObject
    *           object to be called back when exiting the application.
    * @return <code>true</code> if the object contains a valid exit function.
    *         <code>false</code> otherwise.
    */
   public static boolean registerExitCallback( Object exitCallbackObject) {

      boolean success = true;
      try {
         _exitCallback = exitCallbackObject.getClass().getMethod("exit", new Class[0]);

         _exitCallbackObject = exitCallbackObject;
      }
      catch (java.lang.NoSuchMethodException ex) {
         ex.printStackTrace();
         success = false;
      }
      return success;
   }

   /**
    * Exit from the application. If there is a registered exit callback, it will
    * call that function before doing the exit.
    */
   public static void exit() {

      try {
         if (_exitCallback != null) {
            _exitCallback.invoke(_exitCallbackObject, new Object[0]);
         }
         else {
            System.exit(1);
         }
      }
      catch (java.lang.IllegalAccessException ex) {
         ex.printStackTrace();
         System.exit(1);
      }
      catch (java.lang.reflect.InvocationTargetException ex) {
         ex.printStackTrace();
         System.exit(1);
      }
   }
}
