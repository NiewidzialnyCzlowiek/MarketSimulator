package io.bartlomiejszal.marketsimulator.sampleData;

import io.bartlomiejszal.marketsimulator.model.assets.CurrencyAsset;
import io.bartlomiejszal.marketsimulator.model.assets.Good;
import io.bartlomiejszal.marketsimulator.model.assets.Share;
import io.bartlomiejszal.marketsimulator.model.dictionaries.Country;
import io.bartlomiejszal.marketsimulator.model.dictionaries.Currency;
import io.bartlomiejszal.marketsimulator.model.dictionaries.TradeUnit;
import io.bartlomiejszal.marketsimulator.model.markets.CurrencyMarket;
import io.bartlomiejszal.marketsimulator.model.markets.GoodsMarket;
import io.bartlomiejszal.marketsimulator.model.markets.Index;
import io.bartlomiejszal.marketsimulator.model.markets.StockExchange;
import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Company;
import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Fund;
import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Investor;
import javafx.util.Pair;
import io.bartlomiejszal.marketsimulator.setup.UIDGenerator;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import static java.util.Arrays.asList;
import static io.bartlomiejszal.marketsimulator.setup.UIDGenerator.getUID;

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
        return getRandomElementFromList(firstNames);
    }
    public static String getLastName() {
        return getRandomElementFromList(lastNames);
    }
    public static String getPlace() {
        return getRandomElementFromList(places);
    }
    public static String getCompanyName() {
        return getPlace() + " " + getRandomElementFromList(companySuffixes);
    }
    public static String getCountryName() {
        return getRandomElementFromList(countryNames);
    }
    public static Pair<String, String> getCurrencyCodeAndName() {
        if (countryNames.size() > 0) {
            Random random = new Random();
            return currencyCodesAndNames.get(random.nextInt(currencyCodesAndNames.size()));
        }
        return null;
    }
    public static String getTradeUnitName() {
        return getRandomElementFromList(tradeUnitNames);
    }
    public static String getGoodName() {
        return getRandomElementFromList(goodNames);
    }
    public static void loadData() {
        loadFirstNames();
        loadLastNames();
        loadPlaces();
        loadCountriesAndCurrencies();
        loadGoodsNames();
    }

    private static List<String> readLinesFromInputStream(InputStream inputStream) throws IOException {
        List<String> result = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line + "\n");
            }
        }
        return result;
    }

    private static void loadGoodsNames() {
        try {
            List<String> lines = readLinesFromInputStream(SampleDataGenerator.class.getResourceAsStream("/data/goods.txt"));
            for (String s : lines) {
                goodNames.add(s.replace("\r",""));
            }
        } catch (IOException e) {
            System.out.println("Cannot read file goods.txt");
        }
    }
    private static void loadFirstNames() {
        try {
            List<String> lines = readLinesFromInputStream(SampleDataGenerator.class.getResourceAsStream("/data/first-names.txt"));
            for (String s : lines) {
                firstNames.add(s.replace("\r", ""));
            }
        } catch (IOException e) {
            System.out.println("Cannot read file first-names.txt");

        }
    }
    private static void loadLastNames() {
        try {
            List<String> lines = readLinesFromInputStream(SampleDataGenerator.class.getResourceAsStream("/data/names.txt"));
            for (String s : lines) {
                lastNames.add(s.replace("\r", ""));
            }
        } catch (IOException e) {
            System.out.println("Cannot read file names.txt");
        }
    }
    private static void loadPlaces() {
        try {
            List<String> lines = readLinesFromInputStream(SampleDataGenerator.class.getResourceAsStream("/data/places.txt"));
            for (String s : lines) {
                places.add(s.replace("\r", ""));
            }
        } catch (IOException e) {
            System.out.println("Cannot read file places.txt");
        }
    }
    private static void loadCountriesAndCurrencies() {
        try {
            List<String> lines = readLinesFromInputStream(SampleDataGenerator.class.getResourceAsStream("/data/currencies.txt"));
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

    public static List<TradeUnit> getTradeUnits() {
        List<TradeUnit> tradeUnits = new ArrayList<>();
        tradeUnitNames.forEach(name -> tradeUnits.add(new TradeUnit(UIDGenerator.getUID(), name)));
        return tradeUnits;
    }

    private static long createPersonalId() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i< 11; i++) {
            stringBuilder.append(String.valueOf(random.nextInt(10)));
        }
        return Long.parseLong(stringBuilder.toString());
    }

    private static String getRandomElementFromList(List<String> valueList) {
        if(valueList.size() == 0) return "";
        Random random = new Random();
        return  valueList.get(random.nextInt(valueList.size()));
    }
}
