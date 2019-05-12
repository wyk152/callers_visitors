package com.easymicro.admin.modular.system.controller;

import com.easymicro.core.base.controller.BaseController;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通用文件上传
 * 
 * @author LinYingQiang
 *
 */
@Controller
@RequestMapping("/upload")
public class PictureController extends BaseController {

	/**
	 * WangEditor图片上传
	 * 
	 * @return
	 */
	@RequestMapping("/wangEditorUpload")
	@ResponseBody
	public Map<String, Object> wangEditorUpload(HttpServletRequest request) {
		Map<String, Object> rst = new HashMap<>();
		// --返回参数--
		rst.put("errno", 0);// 默认没有返回错误,如果有抛出异常则重复put参数
		// ----------
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 当前年月日
			String currentTimeString = sdf.format(new Date());
			File directoryPath = new File(REAL_FILE_PATH, currentTimeString);
			String fileName = null;// 上传文件名称
			String suffixName = null;// 文件后缀
			String networkVisit = null;
			List<String> files = new ArrayList<>();// 文件路径列表
			if (!directoryPath.exists()) {
				directoryPath.mkdirs();
			}
			while (iter.hasNext()) {
				fileName = iter.next();// 文件名称
				MultipartFile file = multiRequest.getFile(fileName);// 文件实体
                String name = file.getOriginalFilename();
                suffixName = name.substring(name.lastIndexOf("."), name.length());
				fileName = System.currentTimeMillis() + suffixName;
				File pictureFile = new File(directoryPath, fileName);
				// 文件项目访问地址
				networkVisit = VIRTUAL_FILE_PATH // 文件上传虚拟目录
						+ currentTimeString // 文件夹按照年月日建立
						+ "/" + fileName; // 文件项目访问地址
				files.add(networkVisit);
				try {
					Thumbnails.of(file.getInputStream()).scale(1f).outputQuality(0.25f).toFile(pictureFile);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			rst.put("data", files);
		}
		return rst;
	}

    /**
     * fileInput图片上传
     *
     */
    @RequestMapping("/fileInputUpload")
    @ResponseBody
    public Map<String, Object> fileInputUpload(HttpServletRequest request) {
        Map<String, Object> rst = new HashMap<>();
        try {
            // --返回参数--
            rst.put("code", 200);// 默认没有返回错误,如果有抛出异常则重复put参数
            // ----------
            CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getServletContext());
            if (commonsMultipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                Iterator<String> iter = multiRequest.getFileNames();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 当前年月日
                String currentTimeString = sdf.format(new Date());
                File directoryPath = new File(REAL_FILE_PATH, currentTimeString);
                String fileName = null;// 上传文件名称
                String suffixName = null;// 文件后缀
                String networkVisit = null;
                List<String> files = new ArrayList<>();// 文件路径列表
                if (!directoryPath.exists()) {
                    directoryPath.mkdirs();
                }
                while (iter.hasNext()) {
                    fileName = iter.next();// 文件名称
                    MultipartFile file = multiRequest.getFile(fileName);// 文件实体
                    String name = file.getOriginalFilename();
                    suffixName = name.substring(name.lastIndexOf("."), name.length());
                    fileName = System.currentTimeMillis() + suffixName;
                    File pictureFile = new File(directoryPath, fileName);
                    // 文件项目访问地址
                    networkVisit = VIRTUAL_FILE_PATH // 文件上传虚拟目录
                            + currentTimeString // 文件夹按照年月日建立
                            + "/" + fileName; // 文件项目访问地址
                    //uploadFileService.insert(uploadFile);
                    files.add(networkVisit);
                    try {
                        Thumbnails.of(file.getInputStream()).scale(1f).outputQuality(0.25f).toFile(pictureFile);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                rst.put("data", files);
            }
        } catch (Exception e) {
            // --返回参数--
            rst.put("code", 500);// 默认没有返回错误,如果有抛出异常则重复put参数
        }
        return rst;
    }

}
