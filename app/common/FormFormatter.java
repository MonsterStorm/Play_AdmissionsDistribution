package common;

import java.text.*;
import java.util.*;

import models.*;
import play.data.format.*;
import play.data.format.Formatters.*;

public class FormFormatter {
	/**
	 * register for user 
	 */
	public static void registerUser(){
		Formatters.register(User.class, new SimpleFormatter<User>() {

			@Override
			public User parse(String arg, Locale l) throws ParseException {
				try{
					Long userId = Long.parseLong(arg);
					User user = User.find(userId);
					return user;
				} catch (Exception e){
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public String print(User user, Locale l) {
				if(user != null){
					return user.username;
				}
				return null;
			}

		});
	}
	/**
	 * register for course class
	 */
	public static void registerCourseClass(){
		Formatters.register(CourseClass.class, new SimpleFormatter<CourseClass>() {

			@Override
			public CourseClass parse(String arg, Locale l) throws ParseException {
				try{
					Long courseClassId = Long.parseLong(arg);
					CourseClass courseClass = CourseClass.find(courseClassId);
					return courseClass;
				} catch (Exception e){
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public String print(CourseClass type, Locale l) {
				if(type != null){
					return type.name;
				}
				return null;
			}

		});
	}

	/**
	 * register for course type
	 */
	public static void registerCourseType(){
		Formatters.register(CourseType.class, new SimpleFormatter<CourseType>() {

			@Override
			public CourseType parse(String arg, Locale l) throws ParseException {
				try{
					Long courseTypeId = Long.parseLong(arg);
					CourseType courseType = CourseType.find(courseTypeId);
					return courseType;
				} catch (Exception e){
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public String print(CourseType type, Locale l) {
				if(type != null){
					return type.name;
				}
				return null;
			}

		});
	}
	
	/**
	 * register for contract type
	 */
	public static void registerContractType(){
		Formatters.register(ContractType.class, new SimpleFormatter<ContractType>() {

			@Override
			public ContractType parse(String arg, Locale l) throws ParseException {
				try{
					Long contractTypeId = Long.parseLong(arg);
					ContractType courseType = ContractType.find(contractTypeId);
					return courseType;
				} catch (Exception e){
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public String print(ContractType type, Locale l) {
				if(type != null){
					return type.name;
				}
				return null;
			}

		});
	}
	
	/**
	 * register for news time
	 */
	public static void registerNewsType(){
		Formatters.register(NewsType.class, new SimpleFormatter<NewsType>() {

			@Override
			public NewsType parse(String arg, Locale l) throws ParseException {
				try{
					Long newsTypeId = Long.parseLong(arg);
					NewsType newsType = NewsType.find(newsTypeId);
					return newsType;
				} catch (Exception e){
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public String print(NewsType type, Locale l) {
				if(type != null){
					return type.name;
				}
				return null;
			}

		});
	}

	/**
	 * register for news time
	 */
	public static void registerEducationType(){
		Formatters.register(EducationInstitution.class, new SimpleFormatter<EducationInstitution>() {

			@Override
			public EducationInstitution parse(String arg, Locale l) throws ParseException {
				try{
					Long newsTypeId = Long.parseLong(arg);
					EducationInstitution newsType = EducationInstitution.find(newsTypeId);
					return newsType;
				} catch (Exception e){
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public String print(EducationInstitution type, Locale l) {
				if(type != null){
					return type.name;
				}
				return null;
			}

		});
	}
}
