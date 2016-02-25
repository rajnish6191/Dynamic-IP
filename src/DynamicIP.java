import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.SessionNotFoundException;

public class DynamicIP {

    static boolean glbflag = true;
	static int skip = 0;
	static int sleepInMillis = 1000;
	static int refreshcount = 10;
	static int Count = 0;

	static {
		try {
			Runtime rt = Runtime.getRuntime();
			if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
				System.out.println(rt.exec("taskkill "
						+ "/F /IM chromedriver5.exe"));
				rt.exec("taskkill " + "/F /IM chrome5.exe");

			} else {
				rt.exec("killall -9 " + "chromedriver5");
				rt.exec("killall -9 " + "chrome5");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String removeMultipleSpaces(String data) throws Exception {
		
		Pattern pattern = Pattern.compile("\\s+");
		Matcher matcher = pattern.matcher(data);
		data = matcher.replaceAll(" ");
		data = StringUtils.trim(data);
		return data;
	}

public static void main(String[] args) {
		String line = "";
		WebDriver driver = null;
		try {
			//String in = "D:\\IP1.txt";
			String in = "D:\\MISC\\CHM\\Input\\IPAddress.txt";
			
		    String out = "D:\\MISC\\CHM\\Output\\IPOutput.txt";

			BufferedReader br = new BufferedReader(new FileReader(new File(in)));

			BufferedWriter bw = null;

			System.setProperty("webdriver.chrome.driver",
					"D:\\MISC\\CHM\\Swati\\Swati_data\\Driver\\chromedriver.exe");

			driver = new ChromeDriver();

			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			bw = new BufferedWriter(new FileWriter(new File(out)));

			int cnt = 0;

			while ((line = br.readLine()) != null) {
				try {
					cnt++;

					if (cnt >= skip) {
						if (StringUtils.isNoneBlank(line)) {
							while (process(line, driver, bw))

							{
								driver.quit();

								System.setProperty("webdriver.chrome.driver",
										"D:\\MISC\\CHM\\Swati\\Swati_data\\Driver\\chromedriver.exe");

								driver = new ChromeDriver();

								driver.manage().timeouts()
										.implicitlyWait(10, TimeUnit.SECONDS);
							}
						}

					if (cnt % refreshcount == 0) {
							bw.flush();

							driver.quit();

							System.setProperty("webdriver.chrome.driver",
									"D:\\MISC\\CHM\\Swati\\Swati_data\\Driver\\chromedriver.exe");

							driver = new ChromeDriver();
							glbflag = true;

							driver.manage()
									.timeouts()
									.implicitlyWait(sleepInMillis,
											TimeUnit.SECONDS);
						}  
					}		
				}
					catch (Exception e) {
					e.printStackTrace();

				}
			}
			
			bw.flush();
			bw.close();
			br.close();
			driver.quit();
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				Runtime rt = Runtime.getRuntime();
				if (System.getProperty("os.name").toLowerCase()
						.indexOf("windows") > -1) {
					System.out.println("taskkill " + "/F /IM chromedriver5.exe");

				} else {
					rt.exec("killall -9 " + "chromedriver5");

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean process(String ip, WebDriver driver,
			BufferedWriter bw) {
		try {
			driver.get("https://www.iplocation.net/");

			WebElement element = driver.findElement(By
					.xpath("//form/table/tbody/tr[4]/td[2]/input"));

			element.sendKeys(Keys.CONTROL + "a");

			element.sendKeys(Keys.CLEAR);
			
			ip= removeMultipleSpaces(ip);
			
			//String[] str = StringUtils.split(ip); 
			//element.sendKeys(str[1].trim());
			
			element.sendKeys(ip);

			WebElement ele = driver.findElement(By
					.xpath("//form/table/tbody/tr[4]/td[3]/input"));

            ele.click();
			
			Thread.sleep(3000);

			driver.navigate().refresh();
			
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

			WebElement scroll = driver.findElement(By
					.xpath("//form/table/tbody/tr[4]/td[3]/input"));

			scroll.sendKeys(Keys.PAGE_DOWN);

	
	/*	try {
		    WebElement htmltable = driver.findElement(By.xpath("//*[@id='geolocation']/table[3]"));
			List<WebElement> rows = htmltable.findElements(By.tagName("tr"));
 
			for (int rnum = 0; rnum < rows.size(); rnum++) {
				List<WebElement> columns = rows.get(rnum).findElements(
						By.tagName("td"));

				for (int cnum = 0; cnum < columns.size(); cnum++) {
					
					bw.write(columns.get(cnum).getText() + "|");
					
				}
					}    
			bw.newLine();
			bw.flush();
			glbflag = false;
		    return false;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}     */
		
		WebElement ele1 = driver.findElement(By.xpath("//*[@id='geolocation']/table[3]"));
				List<WebElement> cols = ele1.findElements(By.tagName("td"));
				  
				try {
					 // for(int cnum = 7;cnum<19;cnum++)
					if(cols.size() < 25) {
						
						bw.write("Geolocation Information for this IP is not available");
						
					} else {
						
						System.out.println("Number of columns are: " + cols.size());
						 for(int cnum =7;cnum <23;cnum++)
						// for(int cnum=19;cnum <23 ; cnum++)
					    //for(int cnum=7,cnum1=19;cnum < 13 && cnum1 < 23 ;cnum++,cnum1++)	
						{
							if(cnum>13 && cnum <19) {
								
								continue;
							} else {
								
								bw.write(cols.get(cnum).getText() + "|") ;
		                           
							}
						    
	                     }
					}
					
				
			bw.newLine();
			bw.flush();
			glbflag = false;
			return false;
			}
			catch(StaleElementReferenceException e)
			{
				e.toString();
			} 

		} catch (SessionNotFoundException e) {
			driver.quit();

			System.setProperty("webdriver.chrome.driver",
					"D:\\MISC\\CHM\\Swati\\Swati_data\\Driver\\chromedriver.exe");

			driver = new ChromeDriver();

			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		} 
	
	    catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}