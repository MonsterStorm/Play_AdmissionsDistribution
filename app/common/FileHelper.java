package common;

import java.io.*;

import play.*;
import play.mvc.Http.MultipartFormData.FilePart;

/**
 * 文件处理工具类，保存一个文件到play的目录下
 * 
 * @author MonsterStorm
 * 
 */
public class FileHelper {
	private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;// 最大10M的图片

	public static final String PATH_TEMPLATES = "datas/templates/";// 用户模板
	public static final String PATH_LOGOS = "datas/logos/";// 用户头像
	public static final String PATH_TEMPLATES_DEFAULT = "datas/default/templates/";// 默认模板位置
	public static final String PATH_LOGOS_DEFAULT = "datas/default/logos/";// 默认人头像
	public static final String PATH_ADVERTASEMENTS = "datas/advertisments/";// 广告图片

	private static final String VALIDATE_FILE_TYPE = "^.(jpg|jpeg|png|bmp|gif)";// 支持的文件类型

	public static enum ErrorType {
		ERROR_NONE, // 文件正常
		ERROR_FILE_TOO_LARGE, // 文件太大
		ERROR_FILE_TOO_SMALL, // 文件太小
		ERROR_INVALIDATE_TYPE, // 文件类型不合法
		ERROR_INVALIDATE_NAME, // 文件名不合法
		ERROR_FILE_EMPTY, // 文件空
		ERROR_INTERNAL, // 内部错误
	};

	public static void init() {
		File templates = Play.application().getFile(PATH_TEMPLATES);
		if (templates.exists() == false) {
			templates.mkdirs();
		}

		File logos = Play.application().getFile(PATH_LOGOS);
		if (logos.exists() == false) {
			logos.mkdirs();
		}

		File templatesDefault = Play.application().getFile(
				PATH_TEMPLATES_DEFAULT);
		if (templatesDefault.exists() == false) {
			templatesDefault.mkdirs();
		}

		File logosDefault = Play.application().getFile(PATH_LOGOS_DEFAULT);
		if (logosDefault.exists() == false) {
			logosDefault.mkdirs();
		}
		logPath();
	}

	public static void logPath() {
		play.Logger.debug(PATH_TEMPLATES.toString());
		play.Logger.debug(PATH_LOGOS.toString());
		play.Logger.debug(PATH_TEMPLATES_DEFAULT.toString());
		play.Logger.debug(PATH_LOGOS_DEFAULT.toString());
	}

	/**
	 * save user logo
	 * @param filePart
	 * @return
	 */
	public static String saveUserLogo(String userId, FilePart filePart){
		String path = buildPath(PATH_LOGOS, userId);
		String fileName = saveFile(path, filePart);
		return buildPath(null, userId) + fileName;
	}
	
	/**
	 * save default logo
	 * 
	 * @param filePart
	 * @return
	 */
	public static String saveDefaultAdvertismentLogo(FilePart filePart) {
		return saveFile(PATH_ADVERTASEMENTS, filePart);
	}
	
	/**
	 * save default logo
	 * 
	 * @param filePart
	 * @return
	 */
	public static String saveDefaultTemplateTypeLogo(FilePart filePart) {
		return saveFile(PATH_TEMPLATES_DEFAULT, filePart);
	}

	/**
	 * save file
	 * @param filePath
	 * @param filePart
	 * @return
	 */
	private static String saveFile(String filePath, FilePart filePart){
		String newFileName = buildFileName(filePart);
		if (StringHelper.isValidate(newFileName)
				&& saveFile(filePart, filePath, newFileName) == ErrorType.ERROR_NONE) {
			return newFileName;
		}
		return null;
	}
	
	/**
	 * save file with given name
	 * 
	 * @param filePart
	 * @param newFileName
	 * @return
	 */
	public static ErrorType saveFile(FilePart filePart, final String basePath,
			String newFileName) {
		try {
			ErrorType type = checkValidate(filePart);
			play.Logger.debug(type.name() + "," + type.ordinal());
			if (type == ErrorType.ERROR_NONE) {
				File file = filePart.getFile();

				File base = Play.application().getFile(basePath);
				if (base != null && base.exists() == false) {
					base.mkdirs();
				}

				File newFile = new File(base, newFileName);
				if (file.renameTo(newFile) == false) {
					type = ErrorType.ERROR_INTERNAL;
				}
			}
			return type;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ErrorType.ERROR_INTERNAL;
	}

	/**
	 * check if a file is validate
	 * 
	 * @param file
	 * @return
	 */
	public static ErrorType checkValidate(FilePart filePart) {
		try {
			File file = null;
			if (filePart != null && (file = filePart.getFile()) != null) {
				if (file.length() > MAX_FILE_SIZE) {// 文件大小超出限制
					return ErrorType.ERROR_FILE_TOO_LARGE;
				} else if (file.length() <= 0) {
					return ErrorType.ERROR_FILE_TOO_SMALL;
				}

				String filename = filePart.getFilename();
				if (filename.lastIndexOf(".") == -1) {// 文件名不是合法文件名
					return ErrorType.ERROR_INVALIDATE_NAME;
				}
				String postfix = filename.substring(filename.lastIndexOf("."));

				play.Logger.debug(filename + "," + postfix);
				if (StringHelper.isValidate(postfix) == false) {// 文件名不合法
					return ErrorType.ERROR_INVALIDATE_NAME;
				}

				if (postfix.toLowerCase().matches(VALIDATE_FILE_TYPE) == false) {// 文件后缀不匹配，类型不匹配
					return ErrorType.ERROR_INVALIDATE_TYPE;
				}

				return ErrorType.ERROR_NONE;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ErrorType.ERROR_FILE_EMPTY;
	}

	/**
	 * build file name
	 * 
	 * @param filePart
	 * @return
	 */
	public static String buildFileName(FilePart filePart) {
		if (filePart != null) {
			String filename = filePart.getFilename();
			String postfix = filename.substring(filename.lastIndexOf("."));

			File file = filePart.getFile();

			if (file != null) {
				return HashHelper.md5(file.getName()) + DateHelper.getCurrent()
						+ postfix;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param basePath
	 * @param appendPath
	 * @return
	 */
	public static String buildPath(String basePath, String appendPath){
		if(basePath != null){
			return basePath + appendPath + "/";
		} else {
			return appendPath + "/";
		}
	}
}
