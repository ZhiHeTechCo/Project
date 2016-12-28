package zh.co.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import zh.co.common.exception.CmnSysException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;

/**
 * <p>[概要] FileUtils</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class FileUtils {

	public static final String REGEXPRESSION_BLANK = "\t*|\r|\n|\\s*";
	
    private static CmnLogger logger = CmnLogger.getLogger(FileUtils.class);

    /**
     * Get system dependent file separator.
     *
     * @return "\" for Windows and "/" for Unix
     */
    public static String fileSeparator() {
        return System.getProperty("file.separator");
    }

    /**
     * Normalize the specified directory path.
     *
     * @param dirPath
     *            directory path
     * @return directory path normalized, without trailing file separator
     */
    public static String normalize(String path) {
        try {
            return new File(path).getCanonicalPath();
        } catch (IOException e) {
            logger.debug("The directory path being processed is : " + path);
            return null;
        }
    }

    /**
     * Extract file name from the specified file path.
     *
     * @param filePath
     *            file path, e.g. /this/is/the/path/to/file.xml
     * @return file name, i.e. the last part after file separator. e.g. file.xml
     *         for /this/is/the/path/to/file.xml
     */
    public static String fileName(String filePath) {
        String[] pathElements = filePath.split("[\\\\/]");
        return pathElements[pathElements.length - 1];
    }

    public static String encodeFileName(String fileName, String userAgent) {
        if (userAgent == null) {
            return fileName;
        }

        if (userAgent.contains("MSIE")) {
            try {
                return URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return fileName;
            }
        } else {
            try {
				return "=?UTF-8?B?"
						+ encodeBase64String(fileName.getBytes("UTF-8")) + "?=";
            } catch (UnsupportedEncodingException e) {
                return fileName;
            }
        }
    }
    
    /**
     * We cannot use Base64#encodeBase64String which is since commons-codec 1.4.
     * @param binaryData
     * @return
     */
	private static String encodeBase64String(byte[] binaryData) {
		return org.apache.commons.codec.binary.StringUtils.newStringUtf8(Base64
				.encodeBase64(binaryData, true));
	}

    /**
     * txt文件读入
     * @param pathname 文件名
     * @param charset charset
     * @return 文件类容（行list）
     * @throws IOException
     */
    public static List<String> readTextFile(String pathname, String charset) throws IOException {

        List<String> content = new ArrayList<String>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(pathname), charset));

            String line = null;
            while ((line = reader.readLine()) != null) {
                content.add(line);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return content;
    }

    /**
     * txt文件读入
     * @param pathname 文件名
     * @param charset charset
     * @return String 文件类容
     * @throws IOException
     */
    public static String readTextFile(String filePath, String charset, boolean readCommentLine) throws IOException {
    	StringBuffer sb = new StringBuffer();
    	
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset));
            String line = null;
            while ((line = reader.readLine()) != null) {
            	String newLine = line.replaceAll(REGEXPRESSION_BLANK, "").trim();
            	if(!readCommentLine) {
            		if(newLine.startsWith("#") || newLine.length() == 0){
            			continue;
            		}
            	}
            	sb.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            logger.log(MessageId.COMMON_E_0002,
                    new String[] { filePath });
            throw new CmnSysException(MessageId.COMMON_E_0002,
                    new String[] { filePath });
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return sb.toString();
    }
    
    /**
     * txt文件生成
     * @param pathname 文件名
     * @param content 文件内容
     * @param charset charset
     * @throws IOException
     */
    public static void writeTextFile(String pathname, String content, String charset) throws IOException {
        writeFile(pathname, content.getBytes(charset));
    }

    /**
     * byte文件生成
     * @param pathname 文件内容
     * @param content byte[]
     * @throws IOException
     */
    public static void writeFile(String pathname, byte[] content) throws IOException {

        BufferedOutputStream bos = null;

        try {
            // 临时文件做成
            String tmpPathname = createTempPathname();
            logger.debug("Create tmp file Start. tmp:" + tmpPathname);
            bos = new BufferedOutputStream(new FileOutputStream(tmpPathname));
            bos.write(content);
            bos.close();

            logger.debug("Move file Start. src:" + tmpPathname + " targ:" + pathname);
            moveFile(tmpPathname, pathname);
            logger.debug("Move file End.");
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                //
            }
        }
    }

    /**
     * txt文件保存（追加）
     * @param pathname 文件名
     * @param content 追加内容
     * @param charset charset
     * @throws IOException
     */
    public static void appendTextFile(String pathname, String content, String charset) throws IOException {
        appendFile(pathname, content.getBytes(charset));
    }

    /**
     * byte文件保存（追加）
     * @param pathname 文件名
     * @param content 追加内容
     * @throws IOException
     */
    public static void appendFile(String pathname, byte[] content) throws IOException {

        BufferedOutputStream bos = null;

        try {
            bos = new BufferedOutputStream(new FileOutputStream(pathname, true));
            bos.write(content);
            bos.close();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                //
            }
        }
    }

    /**
     * 文件移动
     * @param source from
     * @param target to
     * @return 操作结果
     */
    public static boolean moveFile(String source, String target) throws IOException {

        if (StringUtils.isBlank(source) || StringUtils.isBlank(target)) {
            return false;
        }

        logger.debug("Check src file exists Start. src:" + source);
        File sourceFile = new File(source);
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            return false;
        }

        logger.debug("Directory check of targ file Start. targ:" + target);
        File targetFile = new File(target);
        if (targetFile.exists() && targetFile.isDirectory()) {
            return false;
        }

        logger.debug("Check targ file folder exists Start. targ:" + target);
        File parentFile = targetFile.getCanonicalFile().getParentFile();
        if (parentFile != null) {
            parentFile.mkdirs();
        }

        logger.debug("Delete old targ file Start. targ:" + target);
        targetFile.delete();
        logger.debug("Copy file Start. src:" + source + " targ:" + target);
        //boolean ret = sourceFile.renameTo(targetFile);
        org.apache.commons.io.FileUtils.copyFile(sourceFile, targetFile);
        logger.debug("Delete src file Start. src:" + source);
        sourceFile.delete();
        
        return true;
    }

    /**
     * 临时文件生成
     * @return 临时文件名
     */
    public static String createTempPathname() throws IOException {
        File file = File.createTempFile(String.valueOf(System.currentTimeMillis()), null);
        String canonicalPath = file.getCanonicalPath();
        file.delete();
        return canonicalPath;
    }

    /**
     * 指定路径下的指定filter文件名取得
     * @param parentDir 指定路径
     * @param filter 指定filter
     * @return 文件名list
     */
    public static List<String> enumAllFiles(String parentDir, FileFilter filter) {

        File parentFile = new File(parentDir);
        if (!parentFile.exists() || !parentFile.isDirectory()) {
            return Collections.emptyList();
        }

        List<String> pathnameList = new ArrayList<String>();

        try {
            List<String> pathList = new ArrayList<String>();
            File[] childrenFile = parentFile.listFiles();
            Arrays.sort(childrenFile);

            for (File childFile : childrenFile) {
                if (childFile.isFile()) {
                    if (filter == null || filter.accept(childFile)) {
                        pathnameList.add(childFile.getCanonicalPath());
                    }
                } else if (childFile.isDirectory()) {
                    pathList.add(childFile.getCanonicalPath());
                }
            }

            for (String path : pathList) {
                pathnameList.addAll(enumAllFiles(path, filter));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pathnameList;
    }

    /**
     * 取得文件的timestamp
     * @param pathnameList 文件名list
     * @return timestamp Map
     */
    public static Map<String, Long> getAllFilesTimestamp(List<String> pathnameList) {

        Map<String, Long> timestampMap = new HashMap<String, Long>();

        for (String pathname : pathnameList) {
            timestampMap.put(pathname, Long.valueOf(new File(pathname).lastModified()));
        }

        return timestampMap;
    }

    public static List<File> listFile(File f, String suffix, boolean isdepth) {
        List<File> tempFileList = new ArrayList<File>();
        if (f.isDirectory() && isdepth == true) {
            File[] t = f.listFiles();
            Arrays.sort(t, new Comparator<File>() {

                public int compare(File f1, File f2) {
                    if (!f1.isDirectory() && f2.isDirectory()) {
                        return 1;
                    } else if (f1.isDirectory() && !f2.isDirectory()) {
                        return -1;
                    } else {
                        return f2.compareTo(f1);
                    }
                }

            });
            for (int i = 0; i < t.length; i++) {
                tempFileList.addAll(listFile(t[i], suffix, isdepth));
            }
        } else {
            String filePath = f.getAbsolutePath();

            if (suffix != null) {
                int begIndex = filePath.lastIndexOf(".");
                String tempsuffix = "";

                if (begIndex != -1) {
                    tempsuffix = filePath.substring(begIndex + 1, filePath
                            .length());
                }

                if (tempsuffix.equals(suffix)) {
                    tempFileList.add(f);
                }
            } else {
                tempFileList.add(f);
            }

        }
        return tempFileList;
    }
    
    /**
     * 文件拷贝
     * @param src from
     * @param target to
     */
    public static void copyFile(String src, String target) throws IOException {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            // path和文件名分离
            int pos = Math.max(target.lastIndexOf("/"), target.lastIndexOf("\\"));
            String targetPath = pos >= 0 ? target.substring(0, pos + 1) : "";
            String targetName = target.substring(pos + 1);

            // 做成临时文件
            File tmpFile = File.createTempFile(targetName, null);

            // 将内容写入临时文件
            bis = new BufferedInputStream(new FileInputStream(src));
            bos = new BufferedOutputStream(new FileOutputStream(tmpFile));

            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, count);
            }

            bis.close();
            bos.close();

            // 路径作成
            if (StringUtils.isNotBlank(targetPath)) {
                new File(targetPath).mkdirs();
            }

            // 生成文件
            File file = new File(target);
            file.delete();
            //tmpFile.renameTo(file);
            org.apache.commons.io.FileUtils.copyFile(tmpFile, file);
            tmpFile.delete();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                //
            }
        }

    }
    
    /**
     * 文件删除
     * @param filePath 文件全路径
     * @return boolean
     */
    public static boolean deleteFile(String filePath) {

        File file = new File(filePath);
        if (file.exists() && file.canWrite()) {
            return file.delete();
        }
        return false;
    }
    
    /**
     * <p>[概 要]　ファイルアップロード処理</p>
     * <p>[備 考]</p>
     * 
     * @param fileName
     * @param in
     * @param path
     * @throws IOException
     */
    public static void uploadFile(InputStream in, String path, String fileName) throws IOException {
        OutputStream out = null;
        try {
/*        	String separator = File.separator;
            if (!path.contains(separator)) {
                String winSeparator = "\\";
                String unixSeparator = "/";
                if (path.contains(winSeparator) && !winSeparator.equals(separator)) {
                	path = path.replace(winSeparator, separator);
                } else if (path.contains(unixSeparator) && !unixSeparator.equals(separator)) {
                	path = path.replace(unixSeparator, separator);
                }
            }*/
            
            File fileDir = new File(path.toString());
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            File file = new File(path + File.separator + fileName);
            out = new FileOutputStream(file);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
    
    /**
     * UNIXとWindows
     * @return
     */
    public static String getCurrentFileName(String pathName){
        if (StringUtils.isEmpty(pathName)) {
            return null;
        }

        String separator = File.separator;
        if (!pathName.contains(separator)) {
            String winSeparator = "\\";
            String unixSeparator = "/";
            if (pathName.contains(winSeparator) && !winSeparator.equals(separator)) {
                pathName = pathName.replace(winSeparator, separator);
            } else if (pathName.contains(unixSeparator) && !unixSeparator.equals(separator)) {
                pathName = pathName.replace(unixSeparator, separator);
            }
        }
        File file = new File(pathName);
        return file.getName();
    }
    
    public static boolean checkExistFile(String filePath) {
        boolean isExist = false;
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            isExist = true;
        }
        return isExist;
    }
}
