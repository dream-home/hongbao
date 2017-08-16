package com.yanbao.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 解压*.zip文件： 把指定输入路径（src）的zip包解压到目标路径(dest)
	 * 
	 * @param src
	 *            指定输入的zip包路径
	 * @param dest
	 *            目标路径，解压后的路径
	 * @return 是否打包成功
	 */
	public static boolean unzip(String src, String dest) {
		File outFile = new File(dest);
		if (!outFile.exists()) {
			outFile.mkdirs();
		}
		try {
			ZipFile zipFile = new ZipFile(src);
			Enumeration<? extends ZipEntry> en = zipFile.entries();
			ZipEntry zipEntry = null;
			while (en.hasMoreElements()) {
				zipEntry = (ZipEntry) en.nextElement();
				if (zipEntry.isDirectory()) {
					String dirName = zipEntry.getName();
					dirName = dirName.substring(0, dirName.length() - 1);
					File f = new File(outFile.getPath() + File.separator + dirName);
					f.mkdirs();
				} else {
					String strFilePath = outFile.getPath() + File.separator + zipEntry.getName();
					File f = new File(strFilePath);

					if (!f.exists()) {
						String[] arrFolderName = zipEntry.getName().split("/");
						String strRealFolder = "";
						for (int i = 0; i < (arrFolderName.length - 1); i++) {
							strRealFolder += arrFolderName[i] + File.separator;
						}
						strRealFolder = outFile.getPath() + File.separator + strRealFolder;
						File tempDir = new File(strRealFolder);
						tempDir.mkdirs();
					}
					f.createNewFile();
					InputStream in = zipFile.getInputStream(zipEntry);
					FileOutputStream out = new FileOutputStream(f);
					try {
						int c;
						byte[] by = new byte[2048];
						while ((c = in.read(by)) != -1) {
							out.write(by, 0, c);
						}
						out.flush();
					} catch (IOException e) {
						throw e;
					} finally {
						out.close();
						in.close();
					}
				}
			}
			zipFile.close();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	/**
	 * 删除非空文件夹
	 * 
	 * @param dir
	 *            待删除的文件夹的路径
	 */
	public static void deleteDirectory(String dir) {
		File f = new File(dir);
		if (f.exists()) {
			if (f.isDirectory()) {
				File[] fs = f.listFiles();
				if (fs.length > 0) {
					for (File file : fs) {
						deleteDirectory(file.getAbsolutePath());
					}
				}
			}
			f.delete();
		}
	}

	public static Iterator<?> findXMLForAll(String objName, String filePath) {
		Iterator<?> iterator = null;
		SAXReader reader = new SAXReader();
		InputStream is = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
			Document document = reader.read(is);
			Element root = document.getRootElement();// 得到根节点
			iterator = root.elementIterator(objName);
		} catch (DocumentException e) {
			System.out.println("读取配置文件" + filePath + "异常" + e.getMessage());
			logger.error("读取配置文件" + filePath + "异常" + e.getMessage(), e);
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return iterator;
	}

	public static void main(String[] args) {
		    List<String> noAuthorized = new ArrayList<String>();


			Iterator<?> iterator = FileUtil.findXMLForAll("url", "no_authorized.xml");
			System.out.println(noAuthorized.size());

			if (iterator != null) {
				while (iterator.hasNext()) {
					Element urlElement = (Element) iterator.next();
					String url = urlElement.getText();
					noAuthorized.add(url);
				}
				System.out.println(noAuthorized.size());
		}
	}
}
