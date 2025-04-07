package com.backend.productservice.services.serviceImpl;

import com.backend.productservice.exception.FileValidationException;
import com.backend.productservice.services.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CLOUDINARY-SERVICE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryServiceImpl implements CloudinaryService {
    Cloudinary cloudinary;

    private static final List<String> ALLOWED_EXTENSIONS =
            List.of("jpg", "jpeg", "png");
    private static final String fileRong = "Ảnh rỗng";
    private static final String anhToiDa = "Kích thước ảnh vượt mức tối đa ";
    private static final String anhDinhDang = "Định dạng ảnh phải là ";
    private static final String tenAnh = "Tên ảnh rỗng hoặc null";

    private static final long MAX_FILE_SIZE_MB = 5; // 5MB size limit
    private static final long MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024;

    //    @PreAuthorize("isAuthenticated()")
    @Override
    public String uploadImage(MultipartFile file) {
        try {
            log.info("CloudinaryService: validating file before upload");
            validateFile(file);
            log.info("CloudinaryService: uploading image");
            var result = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "/upload",
                            "use_filename", true,
                            "unique_filename", true,
                            "resource_type", "auto"
                    ));
            return result.get("secure_url").toString();
        } catch (IOException io) {
            log.error("Failed to upload image: {}", io.getMessage());
            throw new RuntimeException("Image upload failed: " + io.getMessage());
        }
    }
    private void validateFile(MultipartFile file) {
        // Check if file is empty
        if (file == null || file.isEmpty()) {
            log.error(fileRong);
            throw new FileValidationException(fileRong);
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            log.error(anhToiDa + MAX_FILE_SIZE_MB + "MB");
            throw new FileValidationException(anhToiDa + MAX_FILE_SIZE_MB + " MB");
        }
        // Check file format
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && !originalFilename.isEmpty()) {
            String fileExtension = getFileExtension(originalFilename).toLowerCase();
            if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
                log.error(anhDinhDang + "{}", fileExtension);
                throw new FileValidationException(anhDinhDang + String.join(", ", ALLOWED_EXTENSIONS));
            }
        } else {
            log.error(tenAnh);
            throw new FileValidationException(tenAnh);
        }
        log.info("File validated successfully");
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
