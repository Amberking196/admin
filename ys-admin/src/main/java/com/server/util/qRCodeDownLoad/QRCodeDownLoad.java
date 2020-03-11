package com.server.util.qRCodeDownLoad;

import java.io.File;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.common.paramconfig.AlipayConfig;

/**
 * author name: why create time: 2018-05-11 16:47:23
 */
@Component
public class QRCodeDownLoad {

	private static Logger log = Logger.getLogger(QRCodeDownLoad.class);
	@Autowired
	private  AlipayConfig alipayConfig;
	
	public  String dowanLoadCode(String code,String path) {
		CodeCreator creator = new CodeCreator();
		CodeModel info = new CodeModel();
		info.setWidth(400);
		info.setHeight(400);
		info.setFontSize(24);
		String url=alipayConfig.getUrl();
		info.setContents(url+"?vmCode="+code+"");
		String img = alipayConfig.getImg().trim();
		info.setLogoFile(new File(img));
		info.setDesc("        " +code);
		String picture=path+code+"." + info.getFormat();
		creator.createCodeImage(info,picture);
		return picture;
	}
	
	public  String dowanLoadCodeVision(String code,String path) {
		CodeCreator creator = new CodeCreator();
		CodeModel info = new CodeModel();
		info.setWidth(400);
		info.setHeight(400);
		info.setFontSize(24);
		String url=alipayConfig.getVisionUrl();
		info.setContents(url+"?vmCode="+code+"");
		String img = alipayConfig.getImg().trim();
		info.setLogoFile(new File(img));
		info.setDesc("        " +code);
		String picture=path+code+"." + info.getFormat();
		creator.createCodeImage(info,picture);
		return picture;
	}
	
	
	public  String dowanLoadCode2(Long customerId,String code,String path) {
		CodeCreator creator = new CodeCreator();
		CodeModel info = new CodeModel();
		info.setWidth(400);
		info.setHeight(400);
		info.setFontSize(24);
		String url=alipayConfig.getSmsRegister();
		info.setContents(url+"?vmCode="+code+""+"&Id="+customerId+"&flag=1");
		String img = alipayConfig.getImg().trim();
		info.setLogoFile(new File(img));
		info.setDesc(" ");
		String picture=path+code+"." + info.getFormat();
		creator.createCodeImage(info,picture);
		return code+"." + info.getFormat();
	}
	public String downLoadALiCode(String code,String path) {
		CodeCreator creator = new CodeCreator();
		CodeModel info = new CodeModel();
		info.setWidth(400);
		info.setHeight(400);
		info.setFontSize(24);
		String url=alipayConfig.getAliUrl();
		info.setContents(url+"?vmCode="+code+"");
		String img = alipayConfig.getImg().trim();
		info.setLogoFile(new File(img));
		info.setDesc("        " +code);
		String picture=path+code+"." + info.getFormat();
		creator.createCodeImage(info,picture);
		return picture;
	}
	public static void main(String Args[]) {
		CodeDecoder decoder = new CodeDecoder();
		//String result = decoder.decode(input);
		String code="1991000014";
		String path="d://codeImg/";
		CodeCreator creator = new CodeCreator();
		CodeModel info = new CodeModel();
		info.setWidth(400);
		info.setHeight(400);
		info.setFontSize(24);
		String url="http://sms.youshuidaojia.com:8301/middle";
		info.setContents(url+"?vmCode="+code+"");
		String img = "d://codeImg/logo.png";
		info.setLogoFile(new File(img));
		info.setDesc("        " +code);
		String picture=path+code+"." + info.getFormat();
		creator.createCodeImage(info,picture);
		
	}
	public  void decode1() {
		CodeDecoder decoder = new CodeDecoder();
		//String result = decoder.decode(input);
		String code="1991000001";
		String path="d://codeImg/";
		CodeCreator creator = new CodeCreator();
		CodeModel info = new CodeModel();
		info.setWidth(400);
		info.setHeight(400);
		info.setFontSize(24);
		String url="http://sms.youshuidaojia.com/main";
		info.setContents(url+"?vmCode="+code+"");
		String img = "d://codeImg/logo.png";
		info.setLogoFile(new File(img));
		info.setDesc("        " +code);
		String picture=path+code+"." + info.getFormat();
		creator.createCodeImage(info,picture);
		//System.out.println(result);
	}
}