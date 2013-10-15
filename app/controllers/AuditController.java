package controllers;

import static play.data.Form.form;
import models.*;
import play.mvc.*;

import common.*;

import controllers.secure.*;

/**
 * 审核
 * 
 * @author MonsterStorm
 * 
 */
public class AuditController extends Controller {

	/**
	 * 管理员审核
	 * 
	 * @return
	 */
	@Security.Authenticated(SecuredAdmin.class)
	public static Result auditAdmin() {
		String table = form().bindFromRequest().get("table");
		if (Course.TABLE_NAME.equalsIgnoreCase(table)) {// 课程，认证
			return auditAdminCourse();
		} else if (EducationInstitution.TABLE_NAME.equalsIgnoreCase(table)) {
			return auditAdminEdu();
		} else if (Instructor.TABLE_NAME.equalsIgnoreCase(table)) {
			return auditAdminInstructor();
		} else if (Agent.TABLE_NAME.equalsIgnoreCase(table)) {
			return auditAdminAgent();
		} else {
			return badRequest(Constants.MSG_PAGE_NOT_FOUND);
		}
	}

	/**
	 * 课程审核
	 * 
	 * @return
	 */
	public static Result auditAdminCourse() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		Integer status = FormHelper.getInt(form().bindFromRequest(), "status");
		if (id != null && status != null) {
			Course course = Course.updateAudit(id, status);
			if (course != null) {
				return ok(Constants.MSG_SUCCESS);
			} else {
				return internalServerError(Constants.MSG_COURSE_NOT_EXIST);
			}
		}
		return internalServerError(Constants.MSG_INTERNAL_ERROR);
	}

	/**
	 * 教育机构审核
	 * 
	 * @return
	 */
	public static Result auditAdminEdu() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		Integer status = FormHelper.getInt(form().bindFromRequest(), "status");
		if (id != null && status != null) {
			EducationInstitution edu = EducationInstitution.updateAudit(id,
					status);
			if (edu != null) {
				return ok(Constants.MSG_SUCCESS);
			} else {
				return internalServerError(Constants.MSG_EDUCATION_NOT_EXIST);
			}
		}
		return internalServerError(Constants.MSG_INTERNAL_ERROR);
	}
	
	/**
	 * 讲师审核
	 * @return
	 */
	public static Result auditAdminInstructor(){
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		Integer status = FormHelper.getInt(form().bindFromRequest(), "status");
		if (id != null && status != null) {
			Instructor instructor = Instructor.updateAudit(id, status);
			if (instructor != null) {
				return ok(Constants.MSG_SUCCESS);
			} else {
				return internalServerError(Constants.MSG_EDUCATION_NOT_EXIST);
			}
		}
		return internalServerError(Constants.MSG_INTERNAL_ERROR);
	}
	
	/**
	 * 代理人审核
	 * @return
	 */
	public static Result auditAdminAgent(){
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		Integer status = FormHelper.getInt(form().bindFromRequest(), "status");
		if (id != null && status != null) {
			Agent agent = Agent.updateAudit(id, status);
			if (agent != null) {
				return ok(Constants.MSG_SUCCESS);
			} else {
				return internalServerError(Constants.MSG_EDUCATION_NOT_EXIST);
			}
		}
		return internalServerError(Constants.MSG_INTERNAL_ERROR);
	}
}
