package org.project.resumeboost.s3;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3UploadService {
  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public String upload(MultipartFile multipartFile, String dirName) throws IOException {
    // 파일 이름을 UUID로 변경하여 중복 방지
    String fileName = dirName + "/" + newImgName(multipartFile.getOriginalFilename());
    return putS3(multipartFile, fileName);
  }

  // S3에 파일 업로드
  private String putS3(MultipartFile multipartFile, String fileName) throws IOException {
    try (InputStream inputStream = multipartFile.getInputStream()) {
      amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, null));
    } catch (IOException e) {
      System.err.println("파일 업로드 중 오류 발생: " + e.getMessage());
      throw e;
    }

    return amazonS3Client.getUrl(bucket, fileName).toString();
  }

  // 새로운 파일 이름 생성 (UUID)
  public String newImgName(String oldImgName) {
    UUID uuid = UUID.randomUUID();
    return uuid + "_" + oldImgName;
  }

  public void delete(String fileName) {
    try {
      amazonS3Client.deleteObject(bucket, fileName);
      System.out.println("S3에서 파일 삭제: " + fileName);
    } catch (Exception e) {
      System.err.println("파일 삭제 중 오류 발생: " + e.getMessage());
    }
  }
}
