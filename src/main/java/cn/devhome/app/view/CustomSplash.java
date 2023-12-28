package cn.devhome.app.view;

import de.felixroske.jfxsupport.SplashScreen;

/**
 * @author lishiying
 * @date 2022/8/9 009
 */
public class CustomSplash extends SplashScreen {
    /**
     * Use your own splash image instead of the default one
     *
     * @return "/splash/javafx.png"
     */
    @Override
    public String getImagePath() {
        return "/images/splash.png";
    }

    /**
     * Customize if the splash screen should be visible at all
     *
     * @return true by default
     */
    @Override
    public boolean visible() {
        return super.visible();
    }
}
