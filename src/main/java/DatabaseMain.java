import java.sql.SQLException;

public class DatabaseMain {
    public static void main(String[] args) {

        // creating and setting up database
        Datasourse datasourse = Datasourse.getInstance();
        if (!datasourse.openConnection()){
            return;
        }
        if (datasourse.createDatabase()){
            return;
        }
        datasourse.useDatabase();
        datasourse.dropTableCountriesData();
        if (datasourse.createTableCountriesData()){
            return;
        }
        for (Country country : CountriesData.getCountriesData().values()){
            try {
                datasourse.insertCountryData(country.getCountryNameEng()
                        , country.getCountryNameRu()
                        , country.getVisaInfoLink());
            } catch (SQLException e) {
                System.out.println("Data insertion error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        datasourse.closeConnection();
    }
}
