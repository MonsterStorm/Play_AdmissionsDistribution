package common;

import static play.data.Form.form;
import models.interfaces.*;
import play.mvc.*;

public class ControllerHelper{

	/*public static <T extends Model> pageDetail(T model) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());

		if (isAddNew) {
			return ok(views.html.module.common.eduDetail.render(null));
		}

		if (model == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				model = T.find(id);
			}
		}
		return ok(views.html.module.common.eduDetail.render(edu));
	}*/
}
