import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class InstagramScrapper {

    private Connection connection;
    private int height;
    private int count;

    public void scrapInstagramPage(String url, String name, Connection connection){
        this.connection = connection;
        height = 500;
        count = 0;
        WebDriver browser;
        WebDriverManager.chromedriver().setup();
        browser = new ChromeDriver();
        browser.manage().window().maximize();
        browser.get(url);
        sleepForSometime(3000);


        System.out.println("Requesting page - "+name + "("+url+")");;

        scrappGrids(browser);
        browser.close();

    }


    private void scrappGrids(WebDriver browser){
        List<WebElement> allColumns = browser.findElements(By.className("Nnq7C"));
        System.out.println("Number of column found "+allColumns.size());

        List<WebElement> allGrid = browser.findElements(By.className("v1Nh3"));
        for (WebElement grid:allGrid){
            String post_type = getMediaType(grid);
            String post_url = grid.findElement(By.tagName("a")).getAttribute("href");
            String post_image = getSortedImage(grid.findElement(By.className("FFVAD")).getAttribute("srcset"));


            if(post_image!="" || post_image!=null){
                insertPostIntoTable(post_image,post_type,post_url);
            }
            //System.out.println(post_image);
            System.out.println(allGrid.size());
        }

        count++;
        sleepForSometime(5000);
        height = height+500;
        ((JavascriptExecutor) browser).executeScript("window.scrollBy(0,"+height+")");

        if(count<=20){
            scrappGrids(browser);
        }
    }



    private void addSomeHeightBaby(){

//        try{
//            Thread.sleep(3000);
//        }catch (Exception e){
//
//        }
//        height = height+7000;
//
//        ((JavascriptExecutor) browser).executeScript("window.scrollBy(0,"+height+")");
//
//        List<WebElement> allElements2 = browser.findElements(
//                By.xpath("//img[@class='FFVAD']")
//        );
//
//
//        System.out.println(allElements2.size());
//        for(WebElement element:allElements2) {
//            System.out.println(element.getAttribute("srcset"));
//        }


    }

    private void insertPostIntoTable(String icon,String type,String url){
        try{
            Statement statement = connection.createStatement();
            String id = getMd5(url);
            String sql = "INSERT INTO insta_scrapped_post(post_id,post_image,post_type,post_url) VALUES ('"+id+"', '"+icon+"', '"+type+"', '"+url+"')";

            statement.executeUpdate(sql);
        }catch (SQLException e){
            //System.out.println("SQL Connection Error - "+e.toString());
        }
    }

    private String getMediaType(WebElement gridElement){
        String type = "Image";
        try {
            WebElement mediaType = gridElement.findElement(By.className("u7YqG"));
            if(mediaType.isDisplayed()){
                type = mediaType.findElement(By.tagName("span")).getAttribute("aria-label");
            }
        }catch (NoSuchElementException e){
            System.out.println("u7YqG element is not present");
        }

        return type;
    }

    private void sleepForSometime(long milliseconds){
        try{
            Thread.sleep(milliseconds);
        }catch (Exception e){

        }
    }

    private String getSortedImage(String data){
        String[] array = data.split(",");
        int length = array.length;
        String imageData = array[length-1];
        String[] image = imageData.split("\\s+");

        return image[0];
    }


    private String getMd5(String input) {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
