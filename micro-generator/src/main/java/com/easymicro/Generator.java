package com.easymicro;

import com.easymicro.engine.BeetlEngine;
import com.easymicro.persistence.modular.model.business.Config;

/**************************************
 * 代码配置
 *@author LinYingQiang
 *@date 2018-08-11 22:22
 *@qq 961410800
 *
 ************************************/

public class Generator {


    public static void main(String[] args) {

        BeetlEngine engine = new BeetlEngine(Config.class);
        engine.build();
    }


}
