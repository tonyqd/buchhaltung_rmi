package base;

public abstract class RMIStarter {
	 /**
    *
    * @param clazzToAddToServerCodebase a class that should be in the java.rmi.server.codebase property.
    */
   public RMIStarter(Class clazzToAddToServerCodebase, int port) {

//	   System.setProperty("java.rmi.server.codebase", ConnectionInterface.class.getProtectionDomain().getCodeSource().getLocation().toString());
	   
//       System.setProperty("java.rmi.server.codebase", clazzToAddToServerCodebase
//           .getProtectionDomain().getCodeSource().getLocation().toString());

       System.setProperty("java.security.policy", PolicyFileLocator.getLocationOfPolicyFile());

       if(System.getSecurityManager() == null) {
           System.setSecurityManager(new SecurityManager());
       }

       doCustomRmiHandling(port);
   }

   /**
    * extend this class and do RMI handling here
    */
   public abstract void doCustomRmiHandling(int port);
}
