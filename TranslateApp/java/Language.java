public class Language {
    private final String shortName, fullName;

    public Language(String shortName, String fullName){
        this.shortName = shortName;
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }
}
