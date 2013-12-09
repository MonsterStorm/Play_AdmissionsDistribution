package models;

import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.db.ebean.*;
import views.html.helper.*;

import com.avaje.ebean.*;
import common.*;

import controllers.*;
/**
*统计统一通过该类完成
*对于教育机构 收入为某一时间段内Enroll中 confirmOfEdu的总和  支出为 某一时间段内（lastReceiptOfEdu中的确认时间） Rebate 的 rebateToPlatform
*对于平台 收入为 某一时间段内 Rebate 的 rebateToPlatform     支出为  Rebate（lastReceiptOfPlatform中的确认时间）的 rebateToAgent  $ 还可以加个实际收入 为这些rebate的 lastReceiptOfPlatform中money总数 $
*对于代理人 无支出   收入为   Rebate（ lastReceiptOfPlatform中的确认时间）的 rebateToAgent  $ 还可以加个实际收入 为这些Rebate的 lastReceiptOfAgent中的money总数  $
**/
public class Statistics{
	public static final int EDU_STATISTICS = 1;//教育机构统计
	public static final int PLATFORM_STATISTICS = 2;//平台统计
	public static final int AGENT_STATISTICS = 3;//代理人统计

	public Long startTime;//开始时间
	public Long endTime;//结束时间
	public EducationInstitution edu;//教育机构
	public Double income;//总收入
	public Double pay;//总支出
	public int StatisticsType;//统计类型   

	public static Statistics getEduStatistics(  EducationInstitution edu,DynamicForm form){
		Statistics st = new Statistics();
		st.startTime =  FormHelper.getLong(form, "startTime");
		st.endTime =  FormHelper.getLong(form, "endTime");
		st.income =Double.parseDouble("0");
		st.pay =Double.parseDouble("0");

		List<Enroll> enrolls = Enroll.findByEdu(edu, st.startTime, st.endTime);
		for( Enroll enroll : enrolls ){
			st.income += enroll.confirmOfEdu.money;
		}

		List<CourseDistribution> cds = CourseDistribution.findByEdu(edu, st.startTime, st.endTime);
		for( CourseDistribution cd : cds ){
			st.pay += cd.rebate.rebateToPlatform;
		}
		return st;
	}

	public static Statistics getAgentStatistics(  Agent agent,DynamicForm form){
		Statistics st = new Statistics();
		st.startTime =  FormHelper.getLong(form, "startTime");
		st.endTime =  FormHelper.getLong(form, "endTime");
		st.income =Double.parseDouble("0");
		st.pay =Double.parseDouble("0");


		List<CourseDistribution> cds = CourseDistribution.findByAgent(agent, st.startTime, st.endTime);
		for( CourseDistribution cd : cds ){
			st.income += cd.rebate.rebateToAgent;
		}
		return st;
	}

	public static Statistics getPlatformStatistics( DynamicForm form){
		Statistics st = new Statistics();
		st.startTime =  FormHelper.getLong(form, "startTime");
		st.endTime =  FormHelper.getLong(form, "endTime");
		st.income =Double.parseDouble("0");
		st.pay =Double.parseDouble("0");


		List<CourseDistribution> cds = CourseDistribution.findByPlatformIncome(st.startTime, st.endTime);
		for( CourseDistribution cd : cds ){
			st.income += cd.rebate.rebateToPlatform;
		}

		List<CourseDistribution> cds1 = CourseDistribution.findByPlatformPay(st.startTime, st.endTime);
		for( CourseDistribution cd : cds1 ){
			st.pay += cd.rebate.rebateToAgent;
		}

		return st;
	}

}