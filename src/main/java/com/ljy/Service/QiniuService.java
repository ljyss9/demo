package com.ljy.Service;

import com.alibaba.fastjson.JSONObject;
import com.ljy.util.DemoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by ljy on 2017/2/15.
 */
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "TqNgBsgztHuJvHKf3T-F_I5EhNqZTcEws2Rkva8-";
    String SECRET_KEY = "Ss7RPaURcAccd_ZaZMBFD8qw8ksDhVrW2YXZ3dvj";
    //要上传的空间
    String bucketname = "ljystore";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //创建上传对象
    Zone z = Zone.autoZone();
    Configuration c = new Configuration(z);
    //创建上传对象
    UploadManager uploadManager = new UploadManager(c);


    private static String QINIU_IMAGE_DOMAIN = "http://olf3df8c1.bkt.clouddn.com/";
    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public String saveImage(MultipartFile file) throws IOException {
        try {
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            if (!DemoUtil.isFileAllowed(fileExt)) {
                return null;
            }

            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
            if (res.isOK() && res.isJson()) {
                return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key");
            } else {
                logger.error("七牛异常：" + res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            logger.error("七牛异常：" + e.getMessage());
            return null;
        }
    }
}

