package Back_End.Ads;

import javax.swing.*;
import java.util.*;

public class AdScheduler {
    private javax.swing.Timer timer;
    private Map<String, javax.swing.Timer> windowTimers;
    private AdManager adManager;
    
    public AdScheduler(AdManager adManager) {
        this.adManager = adManager;
        this.windowTimers = new HashMap<>();
    }
    
    public void scheduleAdForWindow(String windowName, JFrame window) {
        cancelWindowTimer(windowName);
        
        javax.swing.Timer timer = new javax.swing.Timer(45000, e -> {
            if (window.isVisible() && !adManager.isUserPremium()) {
                adManager.showPopupAd(window);
            }
        });
        timer.setRepeats(false);
        timer.start();
        
        windowTimers.put(windowName, timer);
    }
    
    public void cancelWindowTimer(String windowName) {
        javax.swing.Timer timer = windowTimers.get(windowName);
        if (timer != null) {
            timer.stop();
            windowTimers.remove(windowName);
        }
    }
    
    public void cancelAllTimers() {
        for (javax.swing.Timer timer : windowTimers.values()) {
            timer.stop();
        }
        windowTimers.clear();
    }
}