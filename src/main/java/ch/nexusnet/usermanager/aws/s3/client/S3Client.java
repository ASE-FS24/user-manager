package ch.nexusnet.usermanager.aws.s3.client;

import ch.nexusnet.usermanager.aws.s3.exceptions.UnsupportedFileTypeException;
import ch.nexusnet.usermanager.aws.s3.filetypes.S3FileType;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Client {
    @Value("${usermanager.aws.s3.bucket}")
    private String bucketName;

    private final S3ClientConfiguration s3ClientConfiguration;
    private static final String USER_FILE_PATH = "user-files/";
    private static final String PROFILE_PICTURE = "profile-picture.jpeg";

    public URL uploadFileToS3(String userId , MultipartFile multipartFile) throws IOException, UnsupportedFileTypeException {
        AmazonS3 s3 = getS3Client();

        String keyName;
        if (isFileAProfilePicture(multipartFile)) {
            keyName = USER_FILE_PATH + userId + PROFILE_PICTURE;
        } else {
            keyName = USER_FILE_PATH + userId + "resume.pdf";
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        // Upload the file
        s3.putObject(new PutObjectRequest(bucketName, keyName, multipartFile.getInputStream(), metadata));

        // Return the file URL
        return s3.getUrl(bucketName, keyName);
    }

    public URL getFileFromS3(String userid) {
        AmazonS3 s3 = getS3Client();

        Date expiration = getExpirationDateForURL();

        String keyName = USER_FILE_PATH + userid + PROFILE_PICTURE;
        // Generate the pre-signed URL
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, keyName)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        return s3.generatePresignedUrl(generatePresignedUrlRequest);
    }

    private AmazonS3 getS3Client() {
        return s3ClientConfiguration.getS3client();
    }

    private boolean isFileAProfilePicture(MultipartFile multipartFile) {
        S3FileType s3FileType = getSupportedFileType(multipartFile);
        return s3FileType.equals(S3FileType.PROFILE_PICTURE);
    }

    private S3FileType getSupportedFileType(MultipartFile multipartFile) throws UnsupportedFileTypeException {
        String mimeType = multipartFile.getContentType();
        S3FileType fileType = S3FileType.getByLabel(mimeType);
        log.info(mimeType);

        if (fileType != null) {
            return fileType;
        } else {
            throw new UnsupportedFileTypeException("File Not Supported. Filetype " + mimeType + " was found.");
        }
    }

    private static Date getExpirationDateForURL() {
        // Set expiration time for the pre-signed URL
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1 hour
        expiration.setTime(expTimeMillis);
        return expiration;
    }
}
