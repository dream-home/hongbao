package com.yanbao.util;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class CopyFileUtil {  
	  
	  
    /** 
     * 复制单个文件 
     *  
     * @param srcFileName 
     *            待复制的文件名 
     * @param descFileName 
     *            目标文件名 
     * @param overlay 
     *            如果目标文件存在，是否覆盖 
     * @return 如果复制成功返回true，否则返回false 
     */  
    public static boolean copyFile(String srcFileName, String destFileName,  
            boolean overlay) {  
        File srcFile = new File(srcFileName);  
  
        // 判断源文件是否存在  
        if (!srcFile.exists()) {  
            /*MESSAGE = "源文件：" + srcFileName + "不存在！";  
            JOptionPane.showMessageDialog(null, MESSAGE);  */
            return false;  
        } else if (!srcFile.isFile()) {  
           /* MESSAGE = "复制文件失败，源文件：" + srcFileName + "不是一个文件！";  
            JOptionPane.showMessageDialog(null, MESSAGE);  */
            return false;  
        }  
  
        // 判断目标文件是否存在  
        File destFile = new File(destFileName);  
        if (destFile.exists()) {  
            // 如果目标文件存在并允许覆盖  
            if (overlay) {  
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件  
                new File(destFileName).delete();  
            }  
        } else {  
            // 如果目标文件所在目录不存在，则创建目录  
            if (!destFile.getParentFile().exists()) {  
                // 目标文件所在目录不存在  
                if (!destFile.getParentFile().mkdirs()) {  
                    // 复制文件失败：创建目标文件所在目录失败  
                    return false;  
                }  
            }  
        }  
  
        // 复制文件  
        int byteread = 0; // 读取的字节数  
        InputStream in = null;  
        OutputStream out = null;  
  
        try {  
            in = new FileInputStream(srcFile);  
            out = new FileOutputStream(destFile);  
            byte[] buffer = new byte[1024];  
  
            while ((byteread = in.read(buffer)) != -1) {  
                out.write(buffer, 0, byteread);  
            }  
            return true;  
        } catch (FileNotFoundException e) {  
            return false;  
        } catch (IOException e) {  
            return false;  
        } finally {  
            try {  
                if (out != null)  
                    out.close();  
                if (in != null)  
                    in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    } 
    
    
    public static boolean getRemoteFile(String strUrl, String fileName) throws IOException { 
        URL url = new URL(strUrl); 
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
        DataInputStream input = new DataInputStream(conn.getInputStream()); 
        DataOutputStream output = new DataOutputStream(new FileOutputStream(fileName)); 
        byte[] buffer = new byte[1024 * 8]; 
        int count = 0; 
        while ((count = input.read(buffer)) > 0) { 
          output.write(buffer, 0, count); 
        } 
        output.close(); 
        input.close(); 
        return true; 
      }
    
    public static String download(String path,String localpath)throws Exception{
    	File filefolder = new File(localpath);
    	if(!filefolder .exists()&&!filefolder .isDirectory()){
			//文件夹不存在,创建文件夹
    		filefolder .mkdirs();
		}
		File file = new File(path);
		String filename = file.getName();
		CopyFileUtil.getRemoteFile(path, localpath+filename);
		return filename;
	}
    
    
    public static Map<Long,String> downloadFiles(Map<Long,String> map,String savePath)throws Exception{
    	Map<Long,String> resultMap=new HashMap<Long, String>();
		for (Long key : map.keySet()) {
			 try {
				 String filename=download(map.get(key), savePath);
				 if(!"".equals(filename)){
					 resultMap.put(key, filename);
				 }
			 } catch (Exception e) {
					e.printStackTrace();
			 }
		}
    	return resultMap;
    }
    
    public static void renameFile(String path,String oldname,String newname){ 
        if(!oldname.equals(newname)){//新的文件名和以前文件名不同时,才有必要进行重命名 
            File oldfile=new File(path+"/"+oldname); 
            File newfile=new File(path+"/"+newname); 
            if(!oldfile.exists()){
                return;//重命名文件不存在
            }
            if(newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名 
                System.out.println(newname+"已经存在！"); 
            else{ 
                oldfile.renameTo(newfile); 
            } 
        }else{
            System.out.println("新文件名和旧文件名相同...");
        }
    }
    
    public static String getQrCodeContent(String Url){
    	String content = null;
    	try {
    		EnumMap<DecodeHintType, Object> hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);  
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            //将图片下载到临时位置
            String path="/tmp/ErCode/";
            String fileName="tmpErCode";
            File filefolder = new File(path);
        	if(!filefolder .exists()&&!filefolder .isDirectory()){
    			//没有则创建文件夹
        		filefolder .mkdirs();
    		}
        	getRemoteFile(Url, path+fileName);
        	File imgFile=new File(path+fileName);
        	if(!imgFile.isFile()){  
                System.out.println("输入非文件");  
                return content;  
            }
        	BufferedImage image = ImageIO.read(imgFile);  
            LuminanceSource source = new BufferedImageLuminanceSource(image);  
            Binarizer binarizer = new HybridBinarizer(source);  
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);  
            MultiFormatReader reader = new MultiFormatReader();  
            Result result = reader.decode(binaryBitmap, hints);  
            content = result.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return content;
    }
    
    public static void main(String[] args) throws Exception{
    	EnumMap<DecodeHintType, Object> hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);  
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");  
        String path="C:\\Users\\Administrator\\Desktop\\sql\\test";
    	getRemoteFile("http://oq6g0a31o.bkt.clouddn.com/FlmnZ_AbhQ3GuqTrWyaoOx5FCWdb", path);
    	File imgFile=new File(path);
    	if(!imgFile.isFile()){  
            System.out.println("输入非文件");  
            return;  
        }
    	String content = null;
    	BufferedImage image = ImageIO.read(imgFile);  
        LuminanceSource source = new BufferedImageLuminanceSource(image);  
        Binarizer binarizer = new HybridBinarizer(source);  
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);  
        MultiFormatReader reader = new MultiFormatReader();  
        Result result = reader.decode(binaryBitmap, hints);  
        content = result.getText();  
        System.out.println(content);
	}
}