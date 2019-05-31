import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CountriesData {

    // Parts of travel_ru source link. A country's name should be in between
    private static final String TRAVELRU_URL_BEGINNING = "http://guide.travel.ru/";
    private static final String TRAVELRU_URL_ENDING = "/formalities/visas/";

    private static final Map<String, Country> countriesData = new HashMap<>();

    public static Map<String, Country> getCountriesData(){
        return new HashMap<>(countriesData);
    }

    static {
        // Retrieving ISO's countries codes
        String[] locales = Locale.getISOCountries();
        // Instantiating Google cloud translate service
        Translate translate = TranslateOptions.getDefaultInstance().getService();

        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            // Getting country name from Locale (in English)
            String countryNameEng = obj.getDisplayCountry(Locale.ENGLISH);

            // Will be using Translation to get Russian name due very pure default translation from Locale
            Translation translation = translate.translate(
                    // Adding 'country' due google doesn't always treat the input as a country name
                    // (e.g. Turkey, Reunion)
                    "country " + countryNameEng,
                    Translate.TranslateOption.targetLanguage("ru"));

            String countryNameRu = translation.getTranslatedText();
            countryNameRu = countryNameRu.replaceAll("[Сс]тран[ыа]", "").trim();
            countryNameRu = countryNameRu.substring(0, 1).toUpperCase() + countryNameRu.substring(1);

            String visaInfoLink;

            // Countries that have > 1 word in their name have unique approach in link forming on travel_ru
            if (countryNameEng.split("[^a-zA-Z]").length > 1) {
                switch (countryNameEng) {
                    case "United Arab Emirates":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "uae" + TRAVELRU_URL_ENDING;
                        break;
                    case "Antigua and Barbuda":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "antigua_and_barbuda" + TRAVELRU_URL_ENDING;
                        break;
                    case "Bosnia and Herzegovina":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "bih" + TRAVELRU_URL_ENDING;
                        break;
                    case "Burkina Faso":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "burkina_faso" + TRAVELRU_URL_ENDING;
                        break;
                    case "Bonaire, Sint Eustatius and Saba":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "bonair_saba_sint-estatius" + TRAVELRU_URL_ENDING;
                        break;
                    case "The Democratic Republic Of Congo":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "congo_dr" + TRAVELRU_URL_ENDING;
                        break;
                    case "Central African Republic":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "car" + TRAVELRU_URL_ENDING;
                        break;
                    case "Côte d'Ivoire":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "cote_divoire" + TRAVELRU_URL_ENDING;
                        break;
                    case "Cook Islands":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "cook" + TRAVELRU_URL_ENDING;
                        break;
                    case "Costa Rica":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "costa_rica" + TRAVELRU_URL_ENDING;
                        break;
                    case "Cape Verde":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "cape_verde" + TRAVELRU_URL_ENDING;
                        break;
                    case "Czech Republic":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "czechia" + TRAVELRU_URL_ENDING;
                        break;
                    case "Western Sahara":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "western_sahara" + TRAVELRU_URL_ENDING;
                        break;
                    case "Falkland Islands":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "falkland" + TRAVELRU_URL_ENDING;
                        break;
                    case "Faroe Islands":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "faroe" + TRAVELRU_URL_ENDING;
                        break;
                    case "United Kingdom":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "united_kingdom" + TRAVELRU_URL_ENDING;
                        break;
                    case "French Guiana":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "french_guyana" + TRAVELRU_URL_ENDING;
                        break;
                    case "Equatorial Guinea":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "equatorial_guinea" + TRAVELRU_URL_ENDING;
                        break;
                    case "Guinea-Bissau":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "guinea_bissau" + TRAVELRU_URL_ENDING;
                        break;
                    case "Saint Kitts And Nevis":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "saint_kitts_and_nevis" + TRAVELRU_URL_ENDING;
                        break;
                    case "North Korea":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "north_korea" + TRAVELRU_URL_ENDING;
                        break;
                    case "South Korea":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "south_korea" + TRAVELRU_URL_ENDING;
                        break;
                    case "Cayman Islands":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "cayman" + TRAVELRU_URL_ENDING;
                        break;
                    case "Saint Lucia":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "saint_lucia" + TRAVELRU_URL_ENDING;
                        break;
                    case "Sri Lanka":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "sri_lanka" + TRAVELRU_URL_ENDING;
                        break;
                    case "Marshall Islands":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "marshalls" + TRAVELRU_URL_ENDING;
                        break;
                    case "Northern Mariana Islands":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "mariana_n" + TRAVELRU_URL_ENDING;
                        break;
                    case "New Caledonia":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "new_caledonia_fr" + TRAVELRU_URL_ENDING;
                        break;
                    case "Norfolk Island":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "norfolk" + TRAVELRU_URL_ENDING;
                        break;
                    case "New Zealand":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "new_zealand" + TRAVELRU_URL_ENDING;
                        break;
                    case "French Polynesia":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "polynesia_fr" + TRAVELRU_URL_ENDING;
                        break;
                    case "Papua New Guinea":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "papua_new_guinea" + TRAVELRU_URL_ENDING;
                        break;
                    case "Saint Pierre And Miquelon":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "saint_pierre_and_miquelon" + TRAVELRU_URL_ENDING;
                        break;
                    case "Puerto Rico":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "puerto_rico" + TRAVELRU_URL_ENDING;
                        break;
                    case "Saudi Arabia":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "saudi_arabia" + TRAVELRU_URL_ENDING;
                        break;
                    case "Solomon Islands":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "solomon_islands" + TRAVELRU_URL_ENDING;
                        break;
                    case "Saint Helena":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "st_helena" + TRAVELRU_URL_ENDING;
                        break;
                    case "Svalbard And Jan Mayen":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "svalbard" + TRAVELRU_URL_ENDING;
                        break;
                    case "Sierra Leone":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "sierra_leone" + TRAVELRU_URL_ENDING;
                        break;
                    case "San Marino":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "san_marino" + TRAVELRU_URL_ENDING;
                        break;
                    case "South Sudan":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "south_sudan" + TRAVELRU_URL_ENDING;
                        break;
                    case "Sao Tome And Principe":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "saint_tome_and_principe" + TRAVELRU_URL_ENDING;
                        break;
                    case "El Salvador":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "el_salvador" + TRAVELRU_URL_ENDING;
                        break;
                    case "Timor-Leste":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "timor_leste" + TRAVELRU_URL_ENDING;
                        break;
                    case "Trinidad and Tobago":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "trinidad_and_tobago" + TRAVELRU_URL_ENDING;
                        break;
                    case "United States":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "usa" + TRAVELRU_URL_ENDING;
                        break;
                    case "Saint Vincent And The Grenadines":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "saint_vincent_and_grenadines" + TRAVELRU_URL_ENDING;
                        break;
                    case "British Virgin Islands":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "virgin_islands_uk" + TRAVELRU_URL_ENDING;
                        break;
                    case "U.S. Virgin Islands":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "virgin_islands_usa" + TRAVELRU_URL_ENDING;
                        break;
                    case "Wallis And Futuna":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "wallis_and_futuna" + TRAVELRU_URL_ENDING;
                        break;
                    case "South Africa":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "south_africa" + TRAVELRU_URL_ENDING;
                        break;
                    case "American Samoa":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "american_samoa" + TRAVELRU_URL_ENDING;
                        break;
                    case "Saint Barthélemy":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "saint_barthelemy" + TRAVELRU_URL_ENDING;
                        break;
                    case "Cocos Islands":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "guam_usa" + TRAVELRU_URL_ENDING;
                        break;
                    case "Heard Island And McDonald Islands":
                        visaInfoLink = TRAVELRU_URL_BEGINNING + "heard_and_mcdonald" + TRAVELRU_URL_ENDING;
                        break;
                    default:
                        visaInfoLink = "Информация о порядке получения визы для страны " + countryNameRu + " отсутствует. \n"
                                + "Попробуйте изменить поиск или пройдите по ссылке для поиска" +
                                " непосредственно на сайте. \n" + TRAVELRU_URL_BEGINNING;
                        break;
                }
            } else {
                visaInfoLink = TRAVELRU_URL_BEGINNING + countryNameEng + TRAVELRU_URL_ENDING;
            }
            Country country = new Country();

            country.setCountryNameEng(countryNameEng);
            country.setCountryNameRu(countryNameRu);
            country.setVisaInfoLink(visaInfoLink);

            countriesData.put(countryNameEng, country);
        }
    }
}
