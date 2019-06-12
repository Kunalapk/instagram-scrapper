import java.sql.Connection;
import java.sql.DriverManager;


public class JDBCConnector {

    private boolean status = false;

    public Connection connect(){

        Connection connection = null;
        try{
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName); // here is the ClassNotFoundException

            String serverName = "localhost";
            String mydatabase = "insta_scrapper";
            String url = "jdbc:mysql://" + serverName + "/" + mydatabase;

            String username = "root";
            String password = "hello1234";
            connection = DriverManager.getConnection(url, username, password);

            if(!connection.isClosed()){
                status = true;
            }

            System.out.println("Database connetion - "+status);


        }catch (Exception e){
            System.out.println(e.toString());
        }


        return connection;
    }
}
