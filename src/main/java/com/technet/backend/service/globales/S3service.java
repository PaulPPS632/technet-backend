package com.technet.backend.service.globales;

import com.technet.backend.model.entity.globales.Archivo;
import com.technet.backend.repository.globales.ArchivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3service {
    private final S3Client s3Client;
    private final ArchivoRepository archivoRepository;
    @Value("${aws.namebucket}")
    private String namebucket;

    @Value("${aws.region}")
    private String region;

    public Archivo uploadObject(MultipartFile file, String tipo, String descripcion) {
        String originalFilename = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String key = String.format("%s.%s", UUID.randomUUID(), extension);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(namebucket)
                .key(key)
                .build();
        //(namebucket, key, file.getInputStream(), objectMetadadata)
        //.withCannedAcl(CannedAccessControlList.PublicRead);
        //amazon.putObject(putObjectRequest);

        try {
            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromInputStream(
                    file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return archivoRepository.save(Archivo.builder()
                .nombre(originalFilename)
                .descripcion(descripcion)
                .bucketname(namebucket)
                .namekey(key)
                .tipo_Archivo(tipo)
                .url(String.format("https://%s.s3.%s.amazonaws.com/%s", namebucket, region, key))
                .build());
    }
    public List<Archivo> uploadsObjects(List<MultipartFile> files, String tipo, String descripcion) {
        return files.stream()
                .map(file -> {
                    try {
                        return uploadObject(file, tipo, descripcion);
                    } catch (Exception e) {
                        throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
                    }
                })
                .collect(Collectors.toList());
    }
    public void deleteFile(Archivo archivo) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(namebucket)
                .key(archivo.getNamekey())
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }
    public void deleteFiles(List<Archivo> archivos) {
        List<ObjectIdentifier> objectsToDelete = archivos.stream()
                .map(archivo -> ObjectIdentifier.builder()
                        .key(archivo.getNamekey())
                        .build())
                .collect(Collectors.toList());

        DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                .bucket(namebucket)
                .delete(Delete.builder().objects(objectsToDelete).build())
                .build();
        s3Client.deleteObjects(deleteObjectsRequest);
    }
}
