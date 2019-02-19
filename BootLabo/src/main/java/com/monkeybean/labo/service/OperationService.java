package com.monkeybean.labo.service;

import com.monkeybean.labo.component.reqres.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by MonkeyBean on 2018/5/26.
 */
public interface OperationService {

    Result<String> imageUpload(int accountId, final String fileCode, String fileName);

    Result<Map<String, Object>> getImageList(int accountId, int shareType, int currentPage, int pageSize);

    Result<String> changeImageInfo(int accountId, int imageId, String fileName, String fileDes);

    Result<String> changeImageStatus(int accountId, List<Integer> imageIds, int operateType);

    Result<List<String>> uploadMultiImage(int accountId, MultipartFile[] fileImg);

    Result<Map<String, Object>> getOtherProjectInfo(int accountId, int projectType, int currentPage, int pageSize, int totalNum);

    Result<String> addOtherProjectInfo(int accountId, int type, String name, String url, String image, String des);

    Result<String> deleteOtherProjectInfo(int accountId, int id);
}
