package simpledb.server;

import simpledb.remote.*;
import java.rmi.registry.*;

public class Startup {
   public static void main(String args[]) throws Exception {
      // configure and initialize the database
      SimpleDB.init(args[0]);
      
      List<String> l = new ArrayList<>();
      l.add("node1,node2;2");
      l.add("node2,node3;4");
      l.add("node2,node4;6");
      List<String> output = new ArrayList<>();
      shortPath s = new shortPath();
      output = s.shortPath(l);
      for(String str : output){
         System.out.println(str);
      }
      
      
      
      // create a registry specific for the server on the default port
      Registry reg = LocateRegistry.createRegistry(1099);
      
      // and post the server entry in it
      RemoteDriver d = new RemoteDriverImpl();
      reg.rebind("simpledb", d);
      
      System.out.println("database server ready");
   }
}
