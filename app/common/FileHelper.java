package common;

import java.io.*;

import play.*;
import play.mvc.Http.MultipartFormData.FilePart;

/**
 * 文件处理工具类，保存一个文件到play的目录下
 * @author MonsterStorm
 *
 */
public class FileHelper {
	private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;//最大10M的图片
	
	public static final File PATH_TEMPLATES = Play.application().getFile("datas/templates/");
	public static final File PATH_LOGOS = Play.application().getFile("datas/templates/");
	public static final File PATH_TEMPLATES_DEFAULT = Play.application().getFile("datas/default/templates/");
	public static final File PATH_LOGOS_DEFAULT = Play.application().getFile("datas/default/logos/");
	
	private static final String VALIDATE_FILE_TYPE = "^.(jpg|jpeg|png|bmp|gif)";//支持的文件类型
	
	public static enum ErrorType {
		ERROR_NONE, //文件正常
		ERROR_FILE_TOO_LARGE, //文件太大
		ERROR_FILE_TOO_SMALL, //文件太小
		ERROR_INVALIDATE_TYPE,//文件类型不合法
		ERROR_INVALIDATE_NAME,//文件名不合法
		ERROR_FILE_EMPTY,//文件空
		ERROR_INTERNAL,//内部错误
		};
	
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
	 * save default logo
	 * @param filePart
	 * @return
	 */
	public static ErrorType saveDefaultLogo(FilePart filePart){
		String fileName = filePart.getFilename();
		String contentType = filePart.getContentType();
		play.Logger.debug(fileName + "," + contentType + "," + filePart.getFile().length() + "," + filePart.getFile().getTotalSpace());
		return saveFile(PATH_LOGOS_DEFAULT, filePart);
	}
	
	/**
	 * save a file
	 * @param file
	 * @return
	 */
	public static ErrorType saveFile(final File basePath, FilePart filePart){
		try{
			ErrorType type = checkValidate(filePart);
			play.Logger.debug(type.name() + "," + type.ordinal());
			if(type == ErrorType.ERROR_NONE){
				File file = filePart.getFile();
				
				String filename = filePart.getFilename();
				String postfix = filename.substring(filename.lastIndexOf("."));
				
				if(basePath != null && basePath.exists() == false){
					basePath.mkdirs();
				}
				File newFile = new File(basePath, HashHelper.md5(file.getName()) + postfix);
				if(file.renameTo(newFile) == false){
					type = ErrorType.ERROR_INTERNAL;
				}
			}
			return type;
		} catch (Exception e){
			e.printStackTrace();
		}
		return ErrorType.ERROR_INTERNAL;
	}
	
	/**
	 * check if a file is validate
	 * @param file
	 * @return
	 */
	private static ErrorType checkValidate(FilePart filePart){
		try{
			File file = null;
			if(filePart != null && (file = filePart.getFile()) != null){
				if(file.length() > MAX_FILE_SIZE){//文件大小超出限制
					return ErrorType.ERROR_FILE_TOO_LARGE;
				} else if (file.length() <= 0){
					return ErrorType.ERROR_FILE_TOO_SMALL;
				}
				
				String filename = filePart.getFilename();
				if(filename.lastIndexOf(".") == -1){//文件名不是合法文件名
					return ErrorType.ERROR_INVALIDATE_NAME;
				}
				String postfix = filename.substring(filename.lastIndexOf("."));
				
				play.Logger.debug(filename + "," + postfix);
				if(StringHelper.isValidate(postfix) == false){//文件名不合法
					return ErrorType.ERROR_INVALIDATE_NAME;
				}
				
				if(postfix.toLowerCase().matches(VALIDATE_FILE_TYPE) == false){//文件后缀不匹配，类型不匹配
					return ErrorType.ERROR_INVALIDATE_TYPE;
				}
				
				return ErrorType.ERROR_NONE;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return ErrorType.ERROR_FILE_EMPTY;
	}
	
}
