package com.libs.core.common.image;

/**
 * 图片操作相关常量
 *
 * @author zhang.zheng
 * @version 2018-04-21
 */
public class ImageConst {

    /**
     * 图片选择常量
     */
    // 选择参数
    public static final String SELECTOR_CONFIG = "selector_config";
    // 单选结果
    public static final String IMAGE_DATA_SINGLE = "image_data_single";
    // 多选结果
    public static final String IMAGE_DATA_MULTIPLE = "image_data_multiple";

    // 拍照-请求码
    public static final int REQUEST_CODE_CAMERA = 0x9901;

    // 单选-请求码和结果码
    public static final int REQUEST_CODE_SINGLE = 0x9902;
    public static final int RESULT_CODE_SINGLE = 0x9903;

    // 多选-请求码和结果码
    public static final int REQUEST_CODE_MULTIPLE = 0x9904;
    public static final int RESULT_CODE_MULTIPLE = 0x9905;


    /**
     * 图片裁剪常量
     */
    // 裁剪图片路径
    public static final String CROP_IMAGE_PATH = "crop_image_path";
    // 裁剪保存路径
    public static final String CROP_SAVED_PATH = "crop_saved_path";
    // 裁剪图片-请求码和结果码
    public static final int REQUEST_CODE_CROP = 0x9906;
    public static final int RESULT_CODE_CROP = 0x9907;

}
