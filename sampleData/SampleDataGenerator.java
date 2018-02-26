package sampleData;

import Models.assets.CurrencyAsset;
import Models.assets.Good;
import Models.assets.Share;
import Models.dictionaries.Country;
import Models.dictionaries.Currency;
import Models.dictionaries.TradeUnit;
import Models.markets.CurrencyMarket;
import Models.markets.GoodsMarket;
import Models.markets.Index;
import Models.markets.StockExchange;
import Models.usersAndCustomers.Company;
import Models.usersAndCustomers.Fund;
import Models.usersAndCustomers.Investor;
import javafx.util.Pair;
import setup.UIDGenerator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static java.util.Arrays.asList;
import static setup.UIDGenerator.getUID;

public class SampleDataGenerator {
    private static List<String> firstNames = new ArrayList<>();
    private static List<String> lastNames = new ArrayList<>();
    private static List<String> places = new ArrayList<>();
    private static List<String> companySuffixes = asList("Co.","Corp", "Corporation", "Solutions", "Inc.", "Technologies", "Tech", "Investments", "Designs", "Services", "Ltd.");
    private static List<String> countryNames = new ArrayList<>();
    private static List<Pair<String, String>> currencyCodesAndNames = new ArrayList<>();
    private static List<String> tradeUnitNames = asList("Ounce", "Barrel", "gram", "kg", "pound", "liter", "running meter");
    private static List<String> goodNames = new ArrayList<>();

    public static String getFirstName() {
        if (firstNames.size() > 0) {
            Random random = new Random();
            return firstNames.get(random.nextInt(firstNames.size()));
        }
        return "";
    }
    public static String getLastName() {
        if (lastNames.size() > 0) {
            Random random = new Random();
            return lastNames.get(random.nextInt(lastNames.size()));
        }
        return "";
    }
    public static String getPlace() {
        if (places.size() > 0) {
            Random random = new Random();
            return places.get(random.nextInt(places.size()));
        }
        return "";
    }
    public static String getCompanyName() {
        if (companySuffixes.size() > 0) {
            Random random = new Random();
            return getPlace() + " " + companySuffixes.get(random.nextInt(companySuffixes.size()));
        }
        return "";
    }
    public static String getCountryName() {
        if (countryNames.size() > 0) {
            Random random = new Random();
            return countryNames.get(random.nextInt(countryNames.size()));
        }
        return "";
    }
    public static Pair<String, String> getCurrencyCodeAndName() {
        if (countryNames.size() > 0) {
            Random random = new Random();
            return currencyCodesAndNames.get(random.nextInt(currencyCodesAndNames.size()));
        }
        return null;
    }
    public static String getTradeUnitName() {
        if (tradeUnitNames.size() > 0) {
            Random random = new Random();
            return tradeUnitNames.get(random.nextInt(tradeUnitNames.size()));
        }
        return "";
    }
    public static String getGoodName() {
        if (goodNames.size() > 0) {
            Random random = new Random();
            return goodNames.get(random.nextInt(goodNames.size()));
        }
        return "";
    }
    public static void loadData() {
        loadFirstNames();
        loadLastNames();
        loadPlaces();
        loadCountriesAndCurrencies();
        loadGoodsNames();
    }

    private static void loadGoodsNames() {
        try {
            InputStream inputStream = SampleDataGenerator.class.getResourceAsStream("goods.txt");
            List<String> strings = Arrays.asList(new  String(inputStream.readAllBytes()).split("\n"));
            for (String s : strings) {
                goodNames.add(s.replace("\r",""));
            }
        } catch (IOException e) {
            System.out.println("Cannot read file goods.txt");
        }
    }
    private static void loadFirstNames() {
        try {
            InputStream inputStream = SampleDataGenerator.class.getResourceAsStream("first-names.txt");
            List<String> strings = Arrays.asList(new  String(inputStream.readAllBytes()).split("\n"));
            for (String s : strings) {
                firstNames.add(s.replace("\r", ""));
            }
        } catch (IOException e) {
            System.out.println("Cannot read file first-names.txt");

        }
    }
    private static void loadLastNames() {
        try {
            InputStream inputStream = SampleDataGenerator.class.getResourceAsStream("names.txt");
            List<String> strings = Arrays.asList(new  String(inputStream.readAllBytes()).split("\n"));
            for (String s : strings) {
                lastNames.add(s.replace("\r", ""));
            }
        } catch (IOException e) {
            System.out.println("Cannot read file names.txt");
        }
    }
    private static void loadPlaces() {
        try {
            InputStream inputStream = SampleDataGenerator.class.getResourceAsStream("places.txt");
            List<String> strings = Arrays.asList(new  String(inputStream.readAllBytes()).split("\n"));
            for (String s : strings) {
                places.add(s.replace("\r", ""));
            }
        } catch (IOException e) {
            System.out.println("Cannot read file places.txt");
        }
    }
    private static void loadCountriesAndCurrencies() {
        try {
            InputStream inputStream = SampleDataGenerator.class.getResourceAsStream("currencies.txt");
            List<String> lines = Arrays.asList(new  String(inputStream.readAllBytes()).split("\n"));
            lines.forEach(line -> {
                String[] words = line.split(",");
                if (words.length == 3) {
                    countryNames.add(words[0]);
                    currencyCodesAndNames.add(new Pair<>(words[1], words[2].replace("\r", "")));
                }
            });
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find file currencies.txt");
        } catch (IOException e) {
            System.out.println("Cannot read file currencies.txt");
        }
    }

    public static CurrencyAsset createCurrencyAsset(Currency currency) {
        Random random = new Random();
        double saleFactor = random.nextDouble() * random.nextInt(5);
        return new CurrencyAsset(
                getUID(),
                currency.getName(),
                currency,
                saleFactor,
                saleFactor
        );
    }
    public static Good createGood(TradeUnit tradeUnit) {
        Random random = new Random();
        double currentValueBase = random.nextDouble() * random.nextInt(1000) + 100;
        return new Good(getUID(),
                getGoodName(),
                tradeUnit,
                currentValueBase,
                currentValueBase - random.nextDouble() * currentValueBase,
                currentValueBase + random.nextDouble() * currentValueBase
//                random.nextInt(1000)
        );
    }
    public static Share createShare(Company company) {
        Random random = new Random();
        Share share = new Share(getUID(), company, random.nextInt(500) + 50);
        company.setShares(company.getShares() + share.getQuantity());
        company.setShare(share);
        return share;
    }
    public static Country createCountry() {
        return new Country(
                getUID(),
                getCountryName()
        );
    }
    public static Currency createCurrency() {
        Pair<String, String> currency = getCurrencyCodeAndName();
        List<Country> countries = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < random.nextInt(4); i++) {
            countries.add(createCountry());
        }
        return new Currency(
                getUID(),
                currency.getValue(),
                currency.getKey(),
                false,
                countries
        );
    }
    public static TradeUnit createTradeUnit() {
        return new TradeUnit(
                getUID(),
                getTradeUnitName()
        );
    }
    public static CurrencyMarket createCurrencyMarket(Country country, Currency currency) {
        Random random = new Random();
        String city = getPlace();
        return new CurrencyMarket(getUID(),
                city + " Forex Market",
                country,
                currency,
                city,
                getPlace() + " Street",
                (double)random.nextInt(20)/1000
        );
    }
    public static GoodsMarket createGoodsMarket(Country country, Currency currency) {
        Random random = new Random();
        String city = getPlace();
        return new GoodsMarket(getUID(),
                city + " Goods Market",
                country,
                currency,
                city,
                getPlace() + " Street",
                (double)random.nextInt(20)/1000
        );
    }
    public static StockExchange createStockExchange(Country country, Currency currency) {
        Random random = new Random();
        String city = getPlace();
        return new StockExchange(getUID(),
                city + " Stock Exchange",
                country,
                currency,
                city,
                getPlace() + " Street",
                (double)random.nextInt(20)/1000
        );
    }
    public static Index createIndex(StockExchange stockExchange, List<Company> companies) {
        return new Index(getUID(),
                companies,
                stockExchange.getName() + " " + getPlace(),
                0
        );
    }
    public static Company createCompany() {
        Random random = new Random();
        double sharePrice = random.nextDouble() * (random.nextInt(500)) + 50;
        double revenue = random.nextInt(30000) + 100000;
        int year = random.nextInt(38) + 1980;
        int month = random.nextInt(12) + 1;
        int day = random.nextInt(28) + 1;
        return new Company(
                getUID(),
                getCompanyName(),
                LocalDate.of(year, month, day),
                sharePrice,
                sharePrice,
                sharePrice - (random.nextInt(40) + 5),
                sharePrice + (random.nextInt(50) + 5),
                0,
                revenue - random.nextDouble() * revenue,
                revenue,
                10000 + random.nextInt(500000),
                0,
                0,
                revenue + (random.nextDouble() * revenue * 0.2)
        );
    }

    public static Fund createFund() {
        Random random = new Random();
        return new Fund(getUID(),
                getPlace() + " Fund",
                random.nextInt(100000) + 10000,
                getFirstName(),
                getLastName()
        );
    }
    public static Investor createInvestor() {
        Random random = new Random();
        return new Investor(getUID(),
                getFirstName(),
                10000 + random.nextInt(100000),
                getLastName(),
                createPersonalId()
        );
    }
    private static long createPersonalId() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i< 11; i++) {
            stringBuilder.append(String.valueOf(random.nextInt(10)));
        }
        return Long.parseLong(stringBuilder.toString());
    }

    public static List<TradeUnit> getTradeUnits() {
        List<TradeUnit> tradeUnits = new ArrayList<>();
        tradeUnitNames.forEach(name -> tradeUnits.add(new TradeUnit(UIDGenerator.getUID(), name)));
        return tradeUnits;
    }
}
