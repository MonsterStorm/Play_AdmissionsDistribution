package controllers;

import models.*;
import play.data.*;
import play.mvc.*;
import static play.data.Form.*;

public class PlatformController extends BaseController {

	public static Result index() {
		return ok(views.html.module.platform.index.render());
	}
}
