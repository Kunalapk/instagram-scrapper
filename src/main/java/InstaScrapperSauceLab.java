import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InstaScrapperSauceLab {
    @Test
    public void startScraping(){
        JDBCConnector jdbcConnector = new JDBCConnector();
        final Connection connection = jdbcConnector.connect();

        final InstagramScrapper instagramScrapper = new InstagramScrapper();


        Statement stmt = null;
        String query = "select * from insta_pages where page_status='active'";
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                final String name = rs.getString("page_name");
                final String url = rs.getString("page_url");

                instagramScrapper.scrapInstagramPage(url,name,connection);

            }
        } catch (Exception e ) {
            System.out.println("Error "+e.toString());
        }
    }
}
