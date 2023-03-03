package com.portaria.model.pessoa;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.var;


@Service
public class FileStorageService {

	private final Path fileStorageLocation;

	public FileStorageService(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new RuntimeException("Não consegui criar o diretorio para salvar os arquivos.", ex);
		}

	}

	public String storeFile(MultipartFile file) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		var enderecoSalvo = "";
		try {
			// Check if the file's name contains valid  characters or not
			if (fileName.contains("..")) {
				throw new RuntimeException("Desculpa! O nome do arquivo possui caracteres especiais. Remova-os. Nome =  " + fileName);
			}
			// Copy file to the target place (Replacing existing file with the same name)
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			enderecoSalvo = targetLocation.toString();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return enderecoSalvo;

	}


	public byte[] downloadImageFromFileSystem(String filePath) throws IOException {
		byte[] images = Files.readAllBytes(new File(filePath).toPath());
		return images;
	}
}