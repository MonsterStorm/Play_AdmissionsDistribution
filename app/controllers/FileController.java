package controllers;

import java.io.*;

import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

/**
 * 文件上传
 * 
 * @author MonsterStorm
 * 
 */
public class FileController extends Controller {

	/**
	 * update an photo
	 * @return
	 */
	public static Result uploadPhoto() {
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart logo = body.getFile("logo");
		if (logo != null) {
			String fileName = logo.getFilename();
			String contentType = logo.getContentType();
			File file = logo.getFile();
			return ok("File uploaded");
		} else {
			flash("error", "Missing file");
			return redirect(routes.Application.index());
		}
	}
}
