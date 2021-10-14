package hello.upload.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFile {

    private String uploadFileName;
    private String storeFileName; // 서버 내부에서 관리하는 파일명. 저장할 파일명이 겹치지 않도록 관리

}
