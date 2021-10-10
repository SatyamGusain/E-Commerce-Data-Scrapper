import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class EcommerceCSV {
    WebDriver driver;
    @BeforeTest
    public void shuru() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "D:\\BROWSERDRIVERS\\chromedriver.exe");
        driver= new ChromeDriver();
        driver.get("https://www.amazon.in/");
        Thread.sleep(2000);
        driver.manage().window().maximize();
        driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']")).sendKeys("shoes");
        driver.findElement(By.xpath("//input[@id='nav-search-submit-button']")).click();
        Thread.sleep(2000);
    }
    @Test
    public void tocsv() throws IOException, CsvValidationException {
        List<WebElement>products=driver.findElements(By.xpath("//body/div[@id='a-page']/div[@id='search']/div[1]/div[1]/div[1]/div[1]/span[3]/div[2]/div"));
//        System.out.println(driver.findElement(By.xpath("//body/div[@id='a-page']/div[@id='search']/div[1]/div[1]/div[1]/div[1]/span[3]/div[2]/div[11]")).getAttribute("data-asin").length());
        CSVWriter writer = new CSVWriter(new FileWriter("NamesNPrices.csv"));
        String[] header = {"Names", "Prices(in Rs)"};
        writer.writeNext(header);
        int num=1;
        Random rand = new Random();
//        (int)(Math.random()*100000)
        for(WebElement i :products){
            if(i.getAttribute("data-asin").length()>0 && driver.findElements(By.xpath("//body/div[@id='a-page']/div[@id='search']/div[1]/div[1]/div[1]/div[1]/span[3]/div[2]/div["+(num)+"]//img[@class='s-image']")).size()>0 && driver.findElements(By.xpath("//body/div[@id='a-page']/div[@id='search']/div[1]/div[1]/div[1]/div[1]/span[3]/div[2]/div["+(num)+"]//span[@class='a-price-whole']")).size()>0){
                String name=driver.findElement(By.xpath("//body/div[@id='a-page']/div[@id='search']/div[1]/div[1]/div[1]/div[1]/span[3]/div[2]/div["+(num)+"]//img[@class='s-image']")).getAttribute("alt");
                String price=driver.findElement(By.xpath("//body/div[@id='a-page']/div[@id='search']/div[1]/div[1]/div[1]/div[1]/span[3]/div[2]/div["+(num)+"]//span[@class='a-price-whole']")).getText();
                String[] temp = {name+"_"+rand.nextInt(1000000), price};
//                System.out.println(name);
//                System.out.println(price);
                writer.writeNext(temp);
            }
            num++;
        }
        writer.close();
        CSVReader reader = new CSVReader(new FileReader("NamesNPrices.csv"));
        String [] nextLine;
        int check=0,sum=0;
        while ((nextLine = reader.readNext())!= null) {
            if(check==0){
                check++;
                continue;
            }
            String price=nextLine[1];
            int i=0,temp=1,asli=0;
            for(i=price.length()-1;i>=0;i--){
                if(price.charAt(i)!=','){
                    asli= asli+(Character.getNumericValue(price.charAt(i)))*temp;
                    temp=temp*10;
                }
            }
            sum=sum+asli;
        }
        System.out.println(sum);
    }

}
