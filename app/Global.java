import common.*;

import play.*;
/**
 * global setting for the project
 * @author MonsterStorm
 *
 */
public class Global extends GlobalSettings {
    
    public void onStart(Application app) {
        InitialData.insert(app);
        
        FileHelper.init();
    }
    
}