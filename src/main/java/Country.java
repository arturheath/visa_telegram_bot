public class Country {
    private String countryNameEng;
    private String countryNameRu;
    private String visaInfoLink;

    public Country(String countryNameEng, String countryNameRu, String visaInfoLink) {
        this.countryNameEng = countryNameEng;
        this.countryNameRu = countryNameRu;
        this.visaInfoLink = visaInfoLink;
    }

    public Country(){}

    public String getCountryNameEng() {
        return countryNameEng;
    }

    public void setCountryNameEng(String countryNameEng) {
        this.countryNameEng = countryNameEng;
    }

    public String getCountryNameRu() {
        return countryNameRu;
    }

    public void setCountryNameRu(String countryNameRu) {
        this.countryNameRu = countryNameRu;
    }

    public String getVisaInfoLink() {
        return visaInfoLink;
    }

    public void setVisaInfoLink(String visaInfoLink) {
        this.visaInfoLink = visaInfoLink;
    }
}
