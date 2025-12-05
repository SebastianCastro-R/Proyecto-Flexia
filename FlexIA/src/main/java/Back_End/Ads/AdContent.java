package Back_End.Ads;

public class AdContent {
    private String title;
    private String description;
    private String imagePath;
    private String actionUrl;
    private String targetScreen; // "home", "ejercicios", etc.
    
    public AdContent(String title, String description, String imagePath, String actionUrl, String targetScreen) {
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.actionUrl = actionUrl;
        this.targetScreen = targetScreen;
    }
    
    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }
    public String getActionUrl() { return actionUrl; }
    public String getTargetScreen() { return targetScreen; }
}