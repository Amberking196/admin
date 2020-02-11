package com.server.module.system.machineManage.machineList;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.server.common.paramconfig.AlipayConfig;
import com.server.common.persistence.BaseDao;
import com.server.util.StringUtil;

/**
 * 
 * author name: why create time: 2018-04-16 10:57:20
 */
@Repository
public class DownLoadDaoImpl extends BaseDao<VendingMachinesInfoBean> implements IDownLoadDao {

	public static Logger log = LogManager.getLogger(DownLoadDaoImpl.class);

	@Autowired
	private AlipayConfig alipayConfig;
	/**
	 * 批量 下载售货机二维码
	 */
	@Override
	public void downLoadQRCode(List<MachinesInfoAndBaseDto> list, HttpServletResponse response) {
		log.info("<DownLoadDaoImpl>----<downLoadQRCode>----start");
		try {
			String filename = new String("picture.zip".getBytes("UTF-8"), "ISO8859-1");
			// 控制文件名编码
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ZipOutputStream zos = new ZipOutputStream(bos);
			for (MachinesInfoAndBaseDto dto : list) {
				//log.info("二维码路径  >>>>>>" + dto.getQrCode());
				try {
					if (StringUtil.isNotBlank(dto.getQrCode())) {
						// 图片名称
						zos.putNextEntry(new ZipEntry(dto.getCode() + ".png"));
						// 正式二维码路径 得到数据库中二维码的图片的名称
						String img = dto.getQrCode().split("/")[5];
						String url=alipayConfig.getQrCode();
						byte[] bytes = getImageFromURL(url+img);
						if(bytes != null) {
							zos.write(bytes, 0, bytes.length);
							zos.closeEntry();
						}
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error(e.getMessage());
				}
			}
			try {
				zos.close();
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=" + filename);// 设置文件名
				OutputStream os = response.getOutputStream();
				os.write(bos.toByteArray());
				os.close();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		log.info("<DownLoadDaoImpl>----<downLoadQRCode>----end");
		
	}

	public byte[] getImageFromURL(String urlPath) {
		byte[] data = null;
		InputStream is = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			// conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(6000);
			is = conn.getInputStream();
			if (conn.getResponseCode() == 200) {
				data = readInputStream(is);
			} else {
				data = null;
			}
		} catch (MalformedURLException e) {
			log.error("【错误信息】error{}", e);
		} catch (FileNotFoundException e) {
			log.error("【错误信息】error{}", e);
		}
		catch (IOException e) {
			log.error("【错误信息】error{}", e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				log.error("【错误信息】error{}", e);
			}
			conn.disconnect();
		}
		return data;
	}

	public byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

}
