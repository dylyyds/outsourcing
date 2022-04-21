package com.weichuang.outsourcing.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 自定义文件上传工具类，文件存放的位置为project目录
 *
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 系统文件分隔符 File.separator /
     */
    private static final String SEPARATOR = System.getProperty("file.separator");

    /**
     * 这里上传指定的文件到此module所属的project目录
     */
    private static final String BASE_DIR = System.getProperty("user.dir") + SEPARATOR;

    /**
     * 一级目录
     */
    private static final String FIRST_LEVEL_DIR = "uploads";

    /**
     * 默认二级目录
     */
    private static final String SECOND_LEVEL_DIR_DEFAULT = "default";

    /**
     * 二级用户端目录
     */
    private static final String SECOND_LEVEL_DIR_PORTAL = "portal";

    /**
     * 二级管理端端目录
     */
    private static final String SECOND_LEVEL_DIR_ADMIN = "admin";

    /**
     * 三级用户文件目录
     */
    private static final String THIRD_LEVEL_DIR = "user";

    /**
     * 保存营业执照的二级目录
     */
    private static final String BUSINESS_LICENSE_DIR = "business_license";

    /**
     * @param file file
     * @return relative path
     * @throws IOException IOException
     */
    public static String upload(MultipartFile file) throws IOException {
        return upload(file, SECOND_LEVEL_DIR_DEFAULT);
    }

    /**
     * 按相对路径删除文件
     *
     * @param relativePath relative path
     * @return status
     */
    public static boolean delete(String relativePath) {
        File file = new File(BASE_DIR + relativePath);
        return file.delete();
    }

    /**
     * 按路径删除项目文件
     *
     * @param relativePath relative path
     * @return status
     */
    public static boolean deleteProjectFiles(String relativePath) {
        File file = new File(BASE_DIR + relativePath);
        //System.out.println(file);
        //System.gc();
        //file.setWritable(true);
        if(file.exists()){
            //System.out.println("存在");
        }
        return file.delete();
    }

    /**
     * <p>
     * 一级目录: {@code upload}
     * 二级目录: {@code 请提供}
     * </p><p>
     * 命名格式 {@link FileUtil#fullFilename(String)}
     * <pre>
     * {@code 20200603142533_84678.jpg}
     * </pre>
     * </p>
     *
     * @param file        file
     * @param secondLPath second level directory
     * @return relative path
     * @throws IOException IOException
     */
    public static String upload(MultipartFile file, String secondLPath) throws IOException {
        String filename = fullFilename(file.getOriginalFilename());
        String compressedFilename = fullFilename(file.getOriginalFilename());

        // 保存文件的路径，后跟full filename即可
        String savePath = BASE_DIR + FIRST_LEVEL_DIR + SEPARATOR + secondLPath + SEPARATOR;

        // 文件和枷锁文件
        String fileSavePath = savePath + filename;
        String compressedFileSavePath = savePath + compressedFilename;

        File originFile = new File(fileSavePath);
        if (!originFile.getParentFile().exists()) {
            originFile.getParentFile().mkdirs();
        }
        // 写入，multipartFile只能写一次，流就关闭
        file.transferTo(originFile);

        // 压缩选择，返回最终的filepath
        String relativePath = zipFile(file, originFile, compressedFileSavePath, fileSavePath);
        relativePath = relativePath.replace(BASE_DIR, "");
        logger.info("writing a file: " + fileSavePath + " | " + "its relative path to BASE DIR: " + relativePath);
        return relativePath;
    }

    /**
     * 用户端保存用户上传的营业执照照片
     *
     * @param file 用户上传的文件
     * @return 文件相对于BASE_DIR的相对路径
     * @throws IOException 如果文件操作出现异常，函数抛出IOException
     */
    public static String portalUploadBusinessLicense(MultipartFile file) throws IOException {
        // 生成新的文件名
        String filename = fullFilename(file.getOriginalFilename());
        // 文件的保存路径
        String savePath = BASE_DIR + FIRST_LEVEL_DIR + SEPARATOR + SECOND_LEVEL_DIR_PORTAL + SEPARATOR + BUSINESS_LICENSE_DIR + SEPARATOR;
        // 文件的全名（路径+文件名）
        String fileSavePath = savePath + filename;
        File originFile = new File(fileSavePath);
        if (!originFile.getParentFile().exists()) {
            if (!originFile.getParentFile().mkdirs()) {
                logger.debug("uploadBusinessLicense -> mkdirs failed !");
            }
        }
        file.transferTo(originFile);
        return fileSavePath.replace(BASE_DIR, "");
    }

    /**
     * 用户端保存用户上传的学生证照片
     *
     * @param file 用户上传的文件
     * @return 文件相对于BASE_DIR的相对路径
     * @throws IOException 如果文件操作出现异常，函数抛出IOException
     */
    public static String portalUploadStudentCardImage(MultipartFile file) throws IOException {
        // 生成新的文件名
        String filename = fullFilename(file.getOriginalFilename());
        // 文件的保存路径
        String savePath = BASE_DIR + FIRST_LEVEL_DIR + SEPARATOR + SECOND_LEVEL_DIR_PORTAL + SEPARATOR  + THIRD_LEVEL_DIR + SEPARATOR;
        // 文件的全名（路径+文件名）
        String fileSavePath = savePath + filename;
        File originFile = new File(fileSavePath);
        if (!originFile.getParentFile().exists()) {
            if (!originFile.getParentFile().mkdirs()) {
                logger.debug("uploadBusinessLicense -> mkdirs failed !");
            }
        }
        file.transferTo(originFile);
        return fileSavePath.replace(BASE_DIR, "");
    }

    /**
     * 管理端保存用户上传的营业执照照片
     *
     * @param file 用户上传的文件
     * @return 文件相对于BASE_DIR的相对路径
     * @throws IOException 如果文件操作出现异常，函数抛出IOException
     */
    public static String adminUploadBusinessLicense(MultipartFile file) throws IOException {
        // 生成新的文件名
        String filename = fullFilename(file.getOriginalFilename());
        // 文件的保存路径
        String savePath = BASE_DIR + FIRST_LEVEL_DIR + SEPARATOR + SECOND_LEVEL_DIR_ADMIN + SEPARATOR + BUSINESS_LICENSE_DIR + SEPARATOR;
        // 文件的全名（路径+文件名）
        String fileSavePath = savePath + filename;
        File originFile = new File(fileSavePath);
        if (!originFile.getParentFile().exists()) {
            if (!originFile.getParentFile().mkdirs()) {
                logger.debug("uploadBusinessLicense -> mkdirs failed !");
            }
        }
        file.transferTo(originFile);
        return fileSavePath.replace(BASE_DIR, "");
    }

    /**
     * 用户端按照用户id保存用户上传文件
     *
     * @param file 用户上传的文件
     * @return 文件相对于BASE_DIR的相对路径
     * @throws IOException 如果文件操作出现异常，函数抛出IOException
     */
    public static String portalUploadFileByUserId(Long userId, MultipartFile file,int tag) throws IOException {
        // 生成新的文件名
        String filename = fullFilename(file.getOriginalFilename());
        // 文件的保存路径
        String savePath = "";
        if(tag == 1){
            savePath = BASE_DIR + FIRST_LEVEL_DIR + SEPARATOR + SECOND_LEVEL_DIR_PORTAL + SEPARATOR
                    + THIRD_LEVEL_DIR + SEPARATOR + userId.toString() + SEPARATOR;
        }else if(tag == 2){
            savePath = BASE_DIR + FIRST_LEVEL_DIR + SEPARATOR + SECOND_LEVEL_DIR_PORTAL + SEPARATOR
                    + THIRD_LEVEL_DIR + SEPARATOR + userId.toString() + SEPARATOR + "project" + SEPARATOR;
        }

        // 文件的全名（路径+文件名）
        String fileSavePath = savePath + filename;
        File originFile = new File(fileSavePath);
        if (!originFile.getParentFile().exists()) {
            if (!originFile.getParentFile().mkdirs()) {
                logger.debug("uploadFile -> mkdirs failed !");
            }
        }
        file.transferTo(originFile);
        return fileSavePath.replace(BASE_DIR, "");
    }

    /**
     * 用户端按照项目id保存项目相关文件
     *
     * @param file 用户上传的文件
     * @return 文件相对于BASE_DIR的相对路径
     * @throws IOException 如果文件操作出现异常，函数抛出IOException
     */
    public static String portalUploadFileByProjectId(Long projectId, MultipartFile file) throws IOException {
        // 生成新的文件名
        String filename = fullFilename(file.getOriginalFilename());
        // 文件的保存路径
        String savePath = BASE_DIR + FIRST_LEVEL_DIR + SEPARATOR + SECOND_LEVEL_DIR_PORTAL + SEPARATOR
                + THIRD_LEVEL_DIR + SEPARATOR + projectId.toString() + SEPARATOR;
        // 文件的全名（路径+文件名）
        String fileSavePath = savePath + filename;
        File originFile = new File(fileSavePath);
        if (!originFile.getParentFile().exists()) {
            if (!originFile.getParentFile().mkdirs()) {
                logger.debug("uploadFile -> mkdirs failed !");
            }
        }
        file.transferTo(originFile);
        return fileSavePath.replace(BASE_DIR, "");
    }

    /**
     * 管理端按照用户id保存用户上传文件
     *
     * @param file 用户上传的文件
     * @return 文件相对于BASE_DIR的相对路径
     * @throws IOException 如果文件操作出现异常，函数抛出IOException
     */
    public static String adminUploadFileByUserId(Long userId, MultipartFile file) throws IOException {
        // 生成新的文件名
        String filename = fullFilename(file.getOriginalFilename());
        // 文件的保存路径
        String savePath = BASE_DIR + FIRST_LEVEL_DIR + SEPARATOR + SECOND_LEVEL_DIR_ADMIN + SEPARATOR
                + THIRD_LEVEL_DIR + SEPARATOR + userId.toString() + SEPARATOR;
        // 文件的全名（路径+文件名）
        String fileSavePath = savePath + filename;
        File originFile = new File(fileSavePath);
        if (!originFile.getParentFile().exists()) {
            if (!originFile.getParentFile().mkdirs()) {
                logger.debug("uploadFile -> mkdirs failed !");
            }
        }
        file.transferTo(originFile);
        return fileSavePath.replace(BASE_DIR, "");
    }

    /**
     * 文件删除
     *
     * @param url  url
     * @param path path
     * @return code
     */
    public static boolean delete(String url, String path) {
        //TODO 文件删除
        return false;
    }

    /**
     * 压缩图片文件
     * @param file 前端传递过来的 MultipartFile 对象
     * @param tempFile 临时文件对象
     * @param zipFileSavePath 压缩文件保存路径
     * @param tempFileSavePath 临时文件保存路径
     * @return 临时文件保存路径
     */
    public static String zipFile(MultipartFile file, File tempFile, String zipFileSavePath, String tempFileSavePath) {
        try {
            // 转KB
            long size = file.getSize() / 1024;
            // 判断文件大小对图片质量进行压缩,尺寸不变，
            // 范围0.01~1.0,值越低压缩效率越高。图片低于600K不进行压缩
            if (size >= 7380) {
                Thumbnails.of(tempFileSavePath).scale(1f).outputQuality(0.01f).toFile(zipFileSavePath);
                tempFile.delete();
                return zipFileSavePath;
            } else if (size >= 4096) {
                Thumbnails.of(tempFileSavePath).scale(1f).outputQuality(0.2f).toFile(zipFileSavePath);
                tempFile.delete();
                return zipFileSavePath;
            } else if (size >= 1024) {
                Thumbnails.of(tempFileSavePath).scale(1f).outputQuality(0.3f).toFile(zipFileSavePath);
                tempFile.delete();
                return zipFileSavePath;
            } else if (size >= 600) {
                Thumbnails.of(tempFileSavePath).scale(1f).outputQuality(1f).toFile(zipFileSavePath);
                tempFile.delete();
                return zipFileSavePath;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
        // 低于600K不进行压缩
        return tempFileSavePath;
    }

    /**
     * 按照文件本身的扩展名，生成满足格式的新文件名
     *
     * @param originalFilename originalFilename
     * @return full filename
     */
    public static String fullFilename(String originalFilename) {
        // 当前毫秒数
        long currentMillions = DateUtil.current(false);
        // 前后缀
        String prefix = DateUtil.format(new Date(currentMillions), "yyyyMMddHHmmss");
        String suffix = RandomUtil.randomNumbers(5);
        String newFileName = prefix + "_" + suffix;
        String extensionName = FilenameUtils.getExtension(originalFilename);
        return newFileName + "." + extensionName;
    }
}
