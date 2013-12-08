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
		/*if (Ebean.find(User.class).findRowCount() == 0) {

			System.out.println("===InitialData===");
			Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml
					.load("initial-data.yml");

// 			//System.out.println(all.size());
// 			//Insert functions first
// 			Ebean.save(all.get("functions"));
// 			for (Object fun : all.get("functions")) {// save many to many
// 				Ebean.saveManyToManyAssociations(fun, "modules");
// 			}

// 			// Insert modules first
// 			Ebean.save(all.get("modules"));
// 			for (Object module : all.get("modules")) {// save many to many
// 				Ebean.saveManyToManyAssociations(module, "functions");
// 			}

// 			//Insert roles first
// 			Ebean.save(all.get("roles"));
// 			for (Object role : all.get("roles")) {// save many to many
// 				Ebean.saveManyToManyAssociations(role, "modules");
// 			}

// 			// Insert userinfos first
// 			Ebean.save(all.get("userinfos"));

// //			Insert users first
			Ebean.save(all.get("users"));
// 			for (Object user : all.get("users")) {// save many to many
// 				Ebean.saveManyToManyAssociations(user, "roles");
// 			}

// 			// Insert EducationInstitution first
// 			Ebean.save(all.get("edus"));

// 			// Insert Instuctor
// 			Ebean.save(all.get("instructors"));

// 			// Insert CourseType
// 			Ebean.save(all.get("courseTypes"));

// 			// Insert course
// 			Ebean.save(all.get("courses"));

// 			// Insert projects
// 			Ebean.save(all.get("projects"));

// 			// Insert enroll
// 			Ebean.save(all.get("enrolls"));

// 			// Insert News Type
// 			Ebean.save(all.get("newsType"));

// 			// Insert News
// 			Ebean.save(all.get("news"));

// 			// Insert Messages
// 			Ebean.save(all.get("messages"));

// 			// Insert Domains
// 			Ebean.save(all.get("domains"));

// 			// Insert Contracts
// 			Ebean.save(all.get("contracts"));

// 			// Insert TemplateTypes
// 			Ebean.save(all.get("templateTypes"));

// 			// Insert RebateTypes
// 			Ebean.save(all.get("rebateTypes"));

// 			// Insert ConfirmReceipts
// 			Ebean.save(all.get("confirmReceipts"));

// 			// Insert Rebates
// 			Ebean.save(all.get("rebates"));
		}*/
	}

}