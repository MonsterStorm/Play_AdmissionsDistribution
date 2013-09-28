package common;

import java.text.*;
import java.util.*;

import models.*;
import play.data.format.*;
import play.data.format.Formatters.*;

public class FormFormatter {
	
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
}
