package com.deepTear.springboot.utils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ValidateCode{

	private final Log log = LogFactory.getFactory().getInstance(this.getClass().getName());

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 8890246076114743676L;

	private static final int IMAGE_WIDTH = 100;

	private static final int IMAGE_HEIGHT = 44;

	private static Map<String,Object> dataMap = new HashMap<String,Object>();

	@SuppressWarnings("unused")
	private static final String SVG_SOURCE1 = "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\" \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">"
			+ "<svg width=\""
			+ IMAGE_WIDTH
			+ "\" height=\""
			+ IMAGE_HEIGHT
			+ "\" xmlns=\"http://www.w3.org/2000/svg\">"
			+ "<text x=\"0\" y=\""
			+ IMAGE_HEIGHT
			+ "\" font-family=\"Arial\" font-size=\""
			+ IMAGE_HEIGHT + "\" fill=\"black\">";

	@SuppressWarnings("unused")
	private static final String SVG_SOURCE2 = "</text></svg>";

	@SuppressWarnings("unused")
	private boolean svgMode = false;

	private BufferedImage createJPEGWriter(String vcode)throws IOException {
		BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT,BufferedImage.TYPE_INT_RGB);
		Random random = new Random();
		Graphics g = image.getGraphics();
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		g.setFont(new Font("Times New Roman", Font.HANGING_BASELINE, 30));
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 300; i++) {
			int x = random.nextInt(IMAGE_WIDTH);
			int y = random.nextInt(IMAGE_HEIGHT);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		for (int i = 1; i <= 4; i++) {
			String rand = vcode.substring(i - 1, i);
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 23 * (i - 1) + 6, 32);
		}
		return image;
	}

	private static String sn2vcode() {
		String sRand = "";
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
		}
		return sRand;
	}

	public ValidateCode() throws ServletException {
		try {
			GraphicsEnvironment.getLocalGraphicsEnvironment();
		} catch (Throwable e) {
			if(log.isErrorEnabled()){
				e.printStackTrace();
				log.debug(e.getMessage());
			}
			svgMode = true;
		}
	}

	/**
	 * ��������ɫ
	 *
	 * @param fc
	 * @param bc
	 * @return
	 */

	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	public Map<String,Object> createValidateCodeImage(){
		try {
			String code = sn2vcode();
			BufferedImage image = createJPEGWriter(code);
			dataMap.put("code",code);
			dataMap.put("image",image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataMap;
	}
}
