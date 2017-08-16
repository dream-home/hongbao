package com.yanbao.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.*;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * 
 * @author zhuzih
 * @date 2015年12月29日
 */
public class BASE64Util {

	/**
	 * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	 * 
	 * @param imgFilePath
	 * @return
	 */
	public static String GetImageStr(String imgFilePath) {
		byte[] data = null;

		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	/**
	 * 对字节数组字符串进行Base64解码并生成图片
	 * 
	 * @param imgStr
	 * @param imgFilePath
	 * @return
	 */
	public static boolean GenerateImage(String imgStr, File imgFilePath) {
		if (imgStr == null)
			return false;
		imgStr = imgStr.substring(imgStr.indexOf(",") + 1); // 去掉头部信息
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] bytes = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			// 生成jpeg图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	//图片转成base64后固定生成的前缀
	private final static String STR_PRESS="data:image/jpeg;base64,";
	/**
	 * 将图片按指定高宽进行压缩
	 * @param w 宽
	 * @param h 高
	 * @param base64string 图片转成base64的字符串
	 * */
	public static String resize(int w, int h,String base64string) throws IOException {
		if(base64string == null || StringUtils.isBlank(base64string)){
			return "";
		}
		//将固定前缀去掉，否定不会生成图片
		base64string = base64string.substring(base64string.indexOf(",") + 1); // 去掉头部信息
		//将base64转成字节流
		ByteArrayInputStream byteInput = BaseToInputStream(base64string);

		Image img = ImageIO.read(byteInput);      // 构造Image对象
		int imageWidth = img.getWidth(null);
		int imageHeight = img.getHeight(null);
		float scale = getRatio(imageWidth,imageHeight,w,h);
		imageWidth = (int)(scale*imageWidth);
		imageHeight = (int)(scale*imageHeight);
		img = img.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);
		Image temp = new ImageIcon(img).getImage();
		// SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
		BufferedImage image = new BufferedImage(temp.getWidth(null), temp.getHeight(null),BufferedImage.TYPE_INT_RGB );
		image.getGraphics().drawImage(temp, 0, 0, temp.getWidth(null), temp.getHeight(null), null); // 绘制缩小后的图

		Graphics2D g2 = image.createGraphics();
		g2.drawImage(temp, 0, 0,temp.getWidth(null), temp.getHeight(null), Color.white,null);
		g2.dispose();
		float[] kernelData2 = {
				-0.125f, -0.125f, -0.125f,
				-0.125f,2, -0.125f,
				-0.125f,-0.125f, -0.125f };
		Kernel kernel = new Kernel(3, 3, kernelData2);
		ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		image = cOp.filter(image, null);

		//再将压缩后的图片转成base64
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		OutputStream b64 = new Base64OutputStream(os);
//		ImageIO.write(image, "jpg", b64);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(b64);
		JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(image);
//		压缩质量
		jep.setQuality(0.95f, true);
		encoder.encode(image, jep);
//		encoder.encode(image);
		String result = os.toString("UTF-8").replaceAll("[\\t\\n\\r]", "");//转成base64并且去掉回车换行符
		//将前缀给加上
		result=STR_PRESS.concat(result);
		return result;
	}
	/**
	 * 将base64的图片转换成字节流
	 * @param base64string 编码的图片字符串
	 * @return 返回字节流
	 * */
	private static ByteArrayInputStream BaseToInputStream(String base64string){
		ByteArrayInputStream stream = null;
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] bytes1 = decoder.decodeBuffer(base64string);
			stream = new ByteArrayInputStream(bytes1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stream;
	}
	/**
	*长宽与原图大小的比率
	* */
	public static float getRatio(int width,int height,int maxWidth,int maxHeight){
		float Ratio = 1.0f;
		float widthRatio ;
		float heightRatio ;
		widthRatio = (float)maxWidth/width;
		heightRatio = (float)maxHeight/height;
		if(widthRatio<1.0 || heightRatio<1.0){
			Ratio = widthRatio<=heightRatio?widthRatio:heightRatio;
		}
		return Ratio;
	}
}
