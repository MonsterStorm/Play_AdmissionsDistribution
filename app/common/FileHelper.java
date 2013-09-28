package common;

import java.io.*;

import play.*;

/**
 * 文件处理工具类，保存一个文件到play的目录下
 * @author MonsterStorm
 *
 */
public class FileHelper {
	public static final File PATH_TEMPLATES = Play.application().getFile("datas/templates/");
	public static final File PATH_LOGOS = Play.application().getFile("datas/templates/");
	public static final File PATH_TEMPLATES_DEFAULT = Play.application().getFile("datas/default/templates/");
	public static final File PATH_LOGOS_DEFAULT = Play.application().getFile("datas/default/logos/");
	
	public static void init(){
		if(PATH_TEMPLATES.exists() == false){
			PATH_TEMPLATES.mkdirs();
		}
		if(PATH_LOGOS.exists() == false){
			PATH_LOGOS.mkdirs();
		}
		if(PATH_TEMPLATES_DEFAULT.exists() == false){
			PATH_TEMPLATES_DEFAULT.mkdirs();
		}
		if(PATH_LOGOS_DEFAULT.exists() == false){
			PATH_LOGOS_DEFAULT.mkdirs();
		}
		logPath();
	}
	
	public static void logPath(){
		play.Logger.debug(PATH_TEMPLATES.toString());
		play.Logger.debug(PATH_LOGOS.toString());
		play.Logger.debug(PATH_TEMPLATES_DEFAULT.toString());
		play.Logger.debug(PATH_LOGOS_DEFAULT.toString());
	}
	
	/**
	 * save a file
	 * @param file
	 * @return
	 */
	public static boolean saveFile(File file){
		try{
			play.Logger.debug(file.getName() + file.getTotalSpace() + "," + file.length());
			return file.renameTo(new File(PATH_TEMPLATES, file.getName() + ".jpg"));
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
}
