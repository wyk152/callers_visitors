package com.easymicro.core.base.controller;


import com.easymicro.core.base.tips.SuccessTip;
import com.easymicro.core.base.warpper.BaseControllerWarpper;
import com.easymicro.core.page.PageInfoBT;
import com.easymicro.core.support.HttpKit;
import com.easymicro.core.util.FileUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseController {

    protected static String SUCCESS = "SUCCESS";
    protected static String ERROR = "ERROR";

    protected static String REDIRECT = "redirect:";
    protected static String FORWARD = "forward:";

    private static final String DEFAULT_OS = "Windows";

    // 虚拟访问路径
    public static final String VIRTUAL_FILE_PATH = "/file/";

    /**
     * 系统存放路径 window系统默认在 d:\file下 ;linux默认在 /file下
     */
    public static String REAL_FILE_PATH;

    static {
        String osName = System.getProperty("os.name", DEFAULT_OS);
        if (osName != null) {
            if (osName.contains(DEFAULT_OS)) {
                REAL_FILE_PATH = "d:" + File.separator + "file" + File.separator;
            } else {
                REAL_FILE_PATH = File.separator + "file" + File.separator;
            }
        }
    }

    protected static SuccessTip SUCCESS_TIP = new SuccessTip();

    protected HttpServletRequest getHttpServletRequest() {
        return HttpKit.getRequest();
    }

    protected HttpServletResponse getHttpServletResponse() {
        return HttpKit.getResponse();
    }

    protected HttpSession getSession() {
        return HttpKit.getRequest().getSession();
    }

    protected HttpSession getSession(Boolean flag) {
        return HttpKit.getRequest().getSession(flag);
    }

    protected String getPara(String name) {
        return HttpKit.getRequest().getParameter(name);
    }

    protected void setAttr(String name, Object value) {
        HttpKit.getRequest().setAttribute(name, value);
    }

    protected Integer getSystemInvokCount() {
        return (Integer) this.getHttpServletRequest().getServletContext().getAttribute("systemCount");
    }

    /**
     * 把service层的分页信息，封装为bootstrap table通用的分页封装
     */
    protected PageInfoBT packForBT(Page<?> page) {
        return new PageInfoBT(page);
    }

    /**
     * 把service层的分页信息，封装为bootstrap table通用的分页封装
     */
    protected  PageInfoBT packForBT(Page<?> page,Class<? extends BaseControllerWarpper> warpper) {
        return new PageInfoBT(page,warpper);
    }




    /**
     * 包装一个list，让list增加额外属性
     */
    protected Object warpObject(BaseControllerWarpper warpper) {
        return warpper.warp();
    }

    /**
     * 删除cookie
     */
    protected void deleteCookieByName(String cookieName) {
        Cookie[] cookies = this.getHttpServletRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                Cookie temp = new Cookie(cookie.getName(), "");
                temp.setMaxAge(0);
                this.getHttpServletResponse().addCookie(temp);
            }
        }
    }

    /**
     * 删除所有cookie
     */
    protected void deleteAllCookie() {
        Cookie[] cookies = this.getHttpServletRequest().getCookies();
        for (Cookie cookie : cookies) {
            Cookie temp = new Cookie(cookie.getName(), "");
            temp.setMaxAge(0);
            this.getHttpServletResponse().addCookie(temp);
        }
    }

    /**
     * 返回前台文件流
     *
     * @author fengshuonan
     * @date 2017年2月28日 下午2:53:19
     */
    protected ResponseEntity<byte[]> renderFile(String fileName, String filePath) {
        byte[] bytes = FileUtil.toByteArray(filePath);
        return renderFile(fileName, bytes);
    }

    /**
     * 获取单个文件上传
     *
     * @param multipartFile
     * @return
     */
    public String uploadPicture(MultipartFile multipartFile) {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 当前年月日
            String currentTimeString = sdf.format(new Date());
            File directoryPath = new File(REAL_FILE_PATH, currentTimeString);
            if (!directoryPath.exists()) {
                directoryPath.mkdirs();
            }
            String originalFilename = multipartFile.getOriginalFilename();// 原文件名称
            String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."),
                    originalFilename.length());// 文件后缀
            originalFilename = System.currentTimeMillis() + suffixName;
            File pictureFile = new File(directoryPath, originalFilename);
            String networkVisit = VIRTUAL_FILE_PATH // 文件上传虚拟目录
                    + currentTimeString // 文件夹按照年月日建立
                    + "/" + originalFilename; // 文件项目访问地址
            try {
                // 为了节省带宽,启用图片压缩
                Thumbnails.of(multipartFile.getInputStream()).scale(1f).outputQuality(0.25f).toFile(pictureFile);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return networkVisit;
        }
        return "";
    }

    /**
     * 返回前台文件流
     *
     * @author fengshuonan
     * @date 2017年2月28日 下午2:53:19
     */
    protected ResponseEntity<byte[]> renderFile(String fileName, byte[] fileBytes) {
        String dfileName = null;
        try {
            dfileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", dfileName);
        return new ResponseEntity<byte[]>(fileBytes, headers, HttpStatus.CREATED);
    }
}
