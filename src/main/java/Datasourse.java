import java.sql.*;
import java.util.*;

// this class is a singleton
public class Datasourse {

    // singleton set up
    private Datasourse() {
    }

    private static Datasourse datasourse = new Datasourse();

    public static Datasourse getInstance() {
        return datasourse;
    }

    // constants for creating a connection
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/" +
            "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    // TODO later on change this constants for properties file
    private static final String CONNECTION_USERNAME = "student";
    private static final String CONNECTION_PASSWORD = "student";

    // constants for creating a datasourse
    private static final String DATABASE_NAME = "visa_info";
    private static final String TABLE_COUNTRIES_DATA = "countries_data";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_COUNTRY_NAME_ENG = "country_name_eng";
    private static final String COLUMN_COUNTRY_NAME_RU = "country_name_ru";
    private static final String COLUMN_TRAVELRU = "travel_ru";

    private static final String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
    private static final String USE_DATABASE = "USE " + DATABASE_NAME;

    private static final String CREATE_TABLE_COUNTRIES_DATA = "CREATE TABLE IF NOT EXISTS " + TABLE_COUNTRIES_DATA
            + " ("
            + COLUMN_ID + " INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + COLUMN_COUNTRY_NAME_ENG + " VARCHAR(200) NOT NULL, "
            + COLUMN_COUNTRY_NAME_RU + " VARCHAR(200) NOT NULL, "
            + COLUMN_TRAVELRU + " VARCHAR(2083)"
            + ")";

    private static final String DROP_TABLE_COUNTRIES_DATA = "DROP TABLE " + TABLE_COUNTRIES_DATA;

    private static final String INSERT_COUNTRY_DATA = "INSERT INTO " + TABLE_COUNTRIES_DATA
            + "( "
            + COLUMN_COUNTRY_NAME_ENG + ", " + COLUMN_COUNTRY_NAME_RU + ", " + COLUMN_TRAVELRU
            + ") "
            + " VALUES (?,?,?)";

    private static final String QUERY_COUNTRY_DATA_ESTIMATE = "SELECT * FROM " + TABLE_COUNTRIES_DATA
            + " WHERE " + COLUMN_COUNTRY_NAME_ENG + " LIKE ?"
            + " OR " + COLUMN_COUNTRY_NAME_RU + " LIKE ?";

    private static final String QUERY_COUNTRY_DATA_PRECISE = "SELECT * FROM " + TABLE_COUNTRIES_DATA
            + " WHERE " + COLUMN_COUNTRY_NAME_ENG + " =?"
            + " OR " + COLUMN_COUNTRY_NAME_RU + " =?";

    private static final String percentSign = "%";

    private Connection conn;
    private PreparedStatement insertCountryData;
    private PreparedStatement queryCountryDataEstimate;
    private PreparedStatement queryCountryDataPrecise;

    public boolean openConnection() {
        try {
            conn = DriverManager.getConnection(CONNECTION_URL, CONNECTION_USERNAME, CONNECTION_PASSWORD);
            insertCountryData = conn.prepareStatement(INSERT_COUNTRY_DATA, Statement.RETURN_GENERATED_KEYS);
            queryCountryDataEstimate = conn.prepareStatement(QUERY_COUNTRY_DATA_ESTIMATE);
            queryCountryDataPrecise = conn.prepareStatement(QUERY_COUNTRY_DATA_PRECISE);
            return true;
        } catch (SQLException e) {
            System.out.println("Connection failed " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean closeConnection() {
        try {
            if (insertCountryData != null) {
                insertCountryData.close();
            }
            if (queryCountryDataEstimate != null) {
                queryCountryDataEstimate.close();
            }
            if (queryCountryDataPrecise != null) {
                queryCountryDataPrecise.close();
            }
            if (conn != null) {
                conn.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't close the connection " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean createDatabase() {
        try (Statement createDB = conn.createStatement()) {
            createDB.execute(CREATE_DATABASE);
            System.out.println("Query executed: " + CREATE_DATABASE);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't create database " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void useDatabase() {
        try (Statement useDB = conn.createStatement()) {
            useDB.execute(USE_DATABASE);
            System.out.println("Query executed: " + USE_DATABASE);
        } catch (SQLException e) {
            System.out.println("Couldn't create datasourse " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean createTableCountriesData() {
        try (Statement createTable = conn.createStatement()) {
            createTable.execute(CREATE_TABLE_COUNTRIES_DATA);
            System.out.println("Query executed: " + CREATE_TABLE_COUNTRIES_DATA);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't create table countries_data " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean dropTableCountriesData() {
        try (Statement dropTable = conn.createStatement()) {
            dropTable.execute(DROP_TABLE_COUNTRIES_DATA);
            System.out.println("Query executed: " + DROP_TABLE_COUNTRIES_DATA);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't drop the table countries_data " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Country> queryCountryDataEstimate(String userInput) {
        StringBuilder estimateCountryName = new StringBuilder(percentSign);
        estimateCountryName.append(userInput);
        estimateCountryName.append(percentSign);
        // to store all the found countries with unique names
        Map<String, Country> uniqueCountries = new HashMap<>();

        try {
            // first query using the whole input string
            queryCountryDataEstimate.setString(1, estimateCountryName.toString());
            queryCountryDataEstimate.setString(2, estimateCountryName.toString());
            ResultSet result = queryCountryDataEstimate.executeQuery();
            System.out.println("Query executed: " + queryCountryDataEstimate);

            while (result.next()) {
                Country country = new Country();

                String nameEng = result.getString(COLUMN_COUNTRY_NAME_ENG);
                country.setCountryNameEng(nameEng);

                String nameRu = result.getString(COLUMN_COUNTRY_NAME_RU);
                country.setCountryNameRu(nameRu);

                String travelRuLink = result.getString(COLUMN_TRAVELRU);
                country.setVisaInfoLink(travelRuLink);

                uniqueCountries.put(nameEng, country);
                System.out.println("Country found in queryCountryDataEstimate " + nameEng);
            }
            // then query single words from the input
            String[] inputText = userInput.split("[^a-zA-Zа-яА-Я]");
            for (String word : inputText) {
                estimateCountryName = new StringBuilder(percentSign);
                estimateCountryName.append(word);
                estimateCountryName.append(percentSign);

                queryCountryDataEstimate.setString(1, estimateCountryName.toString());
                queryCountryDataEstimate.setString(2, estimateCountryName.toString());
                result = queryCountryDataEstimate.executeQuery();
                System.out.println("Query executed: " + queryCountryDataEstimate);

                while (result.next()) {
                    Country country = new Country();

                    String nameEng = result.getString(COLUMN_COUNTRY_NAME_ENG);
                    country.setCountryNameEng(nameEng);

                    String nameRu = result.getString(COLUMN_COUNTRY_NAME_RU);
                    country.setCountryNameRu(nameRu);

                    String travelRuLink = result.getString(COLUMN_TRAVELRU);
                    country.setVisaInfoLink(travelRuLink);

                    uniqueCountries.put(nameEng, country);
                    System.out.println("Country found in queryCountryDataEstimate " + nameEng);
                }
            }
            result.close();
            return uniqueCountries;
        } catch (SQLException e) {
            System.out.println("Couldn't query data " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Country queryCountryDataPrecise(String userInput) {
        try {
            queryCountryDataPrecise.setString(1, userInput);
            queryCountryDataPrecise.setString(2, userInput);
            ResultSet result = queryCountryDataPrecise.executeQuery();
            System.out.println("Query executed: " + queryCountryDataPrecise);

            if (result.next()) {
                Country country = new Country();

                String nameEng = result.getString(COLUMN_COUNTRY_NAME_ENG);
                country.setCountryNameEng(nameEng);

                String nameRu = result.getString(COLUMN_COUNTRY_NAME_RU);
                country.setCountryNameRu(nameRu);

                String travelRuLink = result.getString(COLUMN_TRAVELRU);
                country.setVisaInfoLink(travelRuLink);

                System.out.println("Country found in queryCountryDataPrecise " + nameEng);
                result.close();
                return country;
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Couldn't query data " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public int insertCountryData(String countryNameEng, String countryNameRu, String visaInfo) throws SQLException {
        // first checking if country already exists in database
        queryCountryDataEstimate.setString(1, countryNameEng);
        queryCountryDataEstimate.setString(2, countryNameRu);
        ResultSet result = queryCountryDataEstimate.executeQuery();

        if (result.next()) {
            System.out.println("Country is already in the database");
            return result.getInt(COLUMN_ID);
        } else {
            // if we are here it doesn't exist so we can insert
            insertCountryData.setString(1, countryNameEng);
            insertCountryData.setString(2, countryNameRu);
            insertCountryData.setString(3, visaInfo);
            int affectedRows = insertCountryData.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert country data");
            }
            ResultSet returnedId = insertCountryData.getGeneratedKeys();
            if (returnedId.next()) {
                System.out.println("Query executed: " + insertCountryData.toString());
                return returnedId.getInt(1);
            } else {
                throw new SQLException("Couldn't get an id for country data");
            }
        }
    }
}
