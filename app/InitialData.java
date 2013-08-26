import java.util.*;

import models.*;
import play.*;
import play.libs.*;

import com.avaje.ebean.*;

/**
 * init data for app
 * 
 * @author MonsterStorm
 * 
 */
public class InitialData {
	/**
	 * insert data from initial-data.yml
	 * 
	 * @param app
	 */
	public static void insert(Application app) {
		if (Ebean.find(User.class).findRowCount() == 0) {

			System.out.println("xxx");
			Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml
					.load("initial-data.yml");

			// System.out.println(all.size());
			// Insert functions first
			Ebean.save(all.get("functions"));

			// Insert roles first
			Ebean.save(all.get("roles"));

			// Insert users first
			Ebean.save(all.get("users"));

			// Insert projects
			Ebean.save(all.get("projects"));

			// Insert course
			Ebean.save(all.get("courses"));

			// Insert enroll
			Ebean.save(all.get("enrolls"));

		}
	}

}