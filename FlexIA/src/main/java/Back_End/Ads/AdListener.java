package Back_End.Ads;

public interface AdListener {
    void onAdShown(String adType);
    void onAdClosed(String adType);
    void onAdClicked(String adType);
    void onPremiumUpgradeClicked();
}