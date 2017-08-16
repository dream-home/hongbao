package com.yanbao.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.constant.CacheKey;
import com.yanbao.redis.Strings;

/**
 * Created by summer on 2016-12-12:11:04;
 */
@Controller
public class PictureCodeController {

    private static int width = 90;//定义图片的width
    private static int height = 20;//定义图片的height
    private static int codeCount = 4;//定义图片上显示验证码的个数
    private static int xx = 15;
    private static int fontHeight = 18;
    private static int codeY = 16;
    static char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};




    // 图片验证码
    @RequestMapping("/picCode")
    @ResponseBody
    public void getIdentifyCode(String nickName, HttpServletResponse response) {
        String picCode = null;
        try {
            response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Set-Cookie", "name=value; HttpOnly");//设置HttpOnly属性,防止Xss攻击
            response.setDateHeader("Expire", 0);
            OutputStream outputStream=response.getOutputStream();
            picCode = generateCode(outputStream);
            Strings.setEx(CacheKey.getPicCodeKey(nickName),3*60,picCode);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Strings.setEx(CacheKey.getPicCodeKey(nickName), 3 * 60, picCode);

    }

    public static String generateCode(OutputStream outputStream)
            throws IOException {

        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//		Graphics2D gd = buffImg.createGraphics();
        //Graphics2D gd = (Graphics2D) buffImg.getGraphics();
        Graphics gd = buffImg.getGraphics();
        // 创建一个随机数生成器类
        Random random = new Random();
        // 将图像填充为白色
        gd.setColor(Color.WHITE);
        gd.fillRect(0, 0, width, height);

        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
        // 设置字体。
        gd.setFont(font);

        // 画边框。
        gd.setColor(Color.BLACK);
        gd.drawRect(0, 0, width - 1, height - 1);

        // 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。
        gd.setColor(Color.BLACK);
        for (int i = 0; i < 40; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            gd.drawLine(x, y, x + xl, y + yl);
        }

        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;

        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。
            String code = String.valueOf(codeSequence[random.nextInt(36)]);
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);

            // 用随机产生的颜色将验证码绘制到图像中。
            gd.setColor(new Color(red, green, blue));
            gd.drawString(code, (i + 1) * xx, codeY);

            // 将产生的四个随机数组合在一起。
            randomCode.append(code);
        }
        ImageIO.write(buffImg, "jpeg", outputStream);
        String str = randomCode.toString();
        return str;
    }

}
