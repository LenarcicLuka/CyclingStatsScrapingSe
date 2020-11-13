package com.cycling.stats;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import java.util.Scanner;

public class CyclingStatsScraperSe {

    public static void main(String[] args) {

        FirefoxDriver driver;

        final FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setHeadless(true);
        driver = new FirefoxDriver(firefoxOptions);

        // take user input
        Scanner Scan = new Scanner(System.in);
        System.out.println("Enter player name: ");
        String imeIgralca = Scan.nextLine();

        WebDriverWait wait = new WebDriverWait(driver, 30);


        // start page
        driver.navigate().to("https://www.procyclingstats.com/search.php");

        // find search input field, type value, submit search
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='term']")));
        WebElement playerSearchBox = driver.findElementByXPath("(//input[@name='term'])[2]");
        playerSearchBox.sendKeys(imeIgralca);
        WebElement searchPlayers = driver.findElementByXPath("//input[@name='searchf']");
        searchPlayers.click();

        // find anchor element with link and click on it
        WebElement firstPlayer = driver.findElementByXPath("//a[@class='blue searchAnalysis']");
        firstPlayer.click();

        // save content of smaller portion of the page
        Actions action = new Actions(driver);
        WebElement tabela = driver.findElementByCssSelector("[class='page-topnav']");
        WebElement statistics = null;

        // find dropdown box element, hover it with mouse, find desired option, click on it

        for (WebElement e : tabela.findElements(By.tagName("li"))) {
            if (e.getText().contains("STATISTICS"))
                statistics = e;
        }

        action.moveToElement(statistics).perform();
        WebElement wins = driver.findElementByXPath("//a[contains(text(),'Wins')]");
        wait.until(ExpectedConditions.visibilityOf(wins));
        wins.click();

        // find dropdown box filter element, select desired filter, submit filter
        Select typeWins = new Select(driver.findElementByCssSelector("[name='type']"));
        typeWins.selectByVisibleText("Stages");
        WebElement filter = driver.findElementByXPath("//input[@class='filterSubmit']");
        filter.click();

        // get list of elements with data from the table
        WebElement tabelaZmage = driver.findElementByXPath("//div[@class='tableCont ']").findElement(By.tagName("tbody"));

        // loop through the list, print out date and name of the race, but only for races thar are class 2.1
        for (WebElement x : tabelaZmage.findElements(By.tagName("tr"))) {
            List<WebElement> rowElements = x.findElements(By.tagName("td"));
            String date = rowElements.get(3).getText();
            String klasa = rowElements.get(2).getText();
            String race = rowElements.get(1).getText();
            if (klasa.equals("2.1"))
                System.out.println("Date: " + date + " Race:" + race);
        }
    }
}