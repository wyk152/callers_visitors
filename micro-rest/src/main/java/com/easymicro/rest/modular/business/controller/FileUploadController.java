package com.easymicro.rest.modular.business.controller;


import com.easymicro.rest.core.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**************************************
 * 文件上传
 * @author LinYingQiang
 * @date 2018-10-15 16:34
 * @qq 961410800
 *
************************************/
@RestController
@RequestMapping(value = "/api/fileupload")
public class FileUploadController {

    /**
     * base64图片上传
     * @param file
     * @return
     */
    @RequestMapping()
    public Object upload(@RequestBody String file) {
        ApiResult<String> apiResult = new ApiResult<>();

        if (StringUtils.isBlank(file)) {
            apiResult.setCode(ApiResult.OPERATION_ERROR);
            apiResult.setMsg("参数错误");
        }

        BASE64Decoder decoder = new BASE64Decoder();
        try
        {
            //Base64解码
            byte[] b = decoder.decodeBuffer(file);
            //  System.out.println("解码完成");
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            // System.out.println("开始生成图片");
            //生成jpeg图片
            OutputStream out = new FileOutputStream("C://");
            out.write(b);
            out.flush();
            out.close();

        }
        catch (Exception e)
        {
            apiResult.setCode(ApiResult.OPERATION_ERROR);
            apiResult.setMsg("系统异常");
        }

        return apiResult;
    }
}
