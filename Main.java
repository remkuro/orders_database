import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.sql.*;

import java.net.InetSocketAddress;
import java.util.Map;

//For compiling on the shell on repl: Same on mac
//javac -cp sqlite-jdbc-3.23.1.jar: Main.java
//java -cp sqlite-jdbc-3.23.1.jar: Main

//Use for windows
//javac -cp sqlite-jdbc-3.23.1.jar; Main.java
class Main {

 public static void main(String[] args)throws IOException{
    (new Main()).init();
  }


  void print(Object o){ System.out.println(o);}
  void printt(Object o){ System.out.print(o);}

  void init() throws IOException{
   

    // create a port - our Gateway
    int port = 8500;
      
    //create the HTTPserver object
    HttpServer server = HttpServer.create(new InetSocketAddress(port),0);

    // create the database object
    Database db = new Database("jdbc:sqlite:orders.db");
    
   // Add your  code here
    
    System.out.println("Server is listening on port "+port);
    //1
    server.createContext("/", new RouteHandler( "You are connected, but route not given or incorrect...."));
    //2
    String sql = "";
    sql = "Select * from orderinfo;"; //had to change table name because it was the database name "orders"
    server.createContext("/orders", new RouteHandler(db,sql) );
    //3
    sql  = " Select * from product ";
    server.createContext("/products", new RouteHandler(db,sql) );


    //4 ERROR - solved, the database is too big so u have to limit it to 20
    sql  = " Select * From orderinfo";
    sql += " Inner Join user ON orderinfo.orderid=user.orderId ";
    sql += " Inner Join transactioninfo ON user.userId=transactioninfo.userId ";
    server.createContext("/ordersinfo", new RouteHandler(db,sql) );


  //5
    sql  = " Select orderinfo.firstName, orderinfo.lastName, orderinfo.payment_method From orderinfo";
    sql += " Inner Join user ON orderinfo.orderid=user.orderId ";
    sql += " Inner Join transactioninfo ON user.userId=transactioninfo.userId ";
    server.createContext("/transactioninfooo", new RouteHandler(db,sql) );
    // Start the server
    server.start();

       
      
    }    
}
