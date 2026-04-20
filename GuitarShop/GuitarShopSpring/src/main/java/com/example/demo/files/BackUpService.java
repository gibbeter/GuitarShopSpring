package com.example.demo.files;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.example.demo.exception.InvalidBackUpPathException;
import com.opencsv.CSVWriter;

@Service
public class BackUpService {

	private String basePath = "E:/Java/GuitarShopSpring/GuitarShop/GuitarShopSpring/src/main/db_backups/";
	
	public <T> boolean saveBackup(List<T> dataList, String prefix, String fileName, String[] headers, Function<T, String[]> mapper) {
		String timestamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
	    String targetPath = basePath + prefix + "/" + fileName + "_" + timestamp + ".csv";
	    
	    Path prefixPath = Paths.get(basePath + prefix);
	    if(!Files.exists(prefixPath))
			try {
				Files.createDirectories(prefixPath);
			} catch (IOException e) {
				throw new InvalidBackUpPathException(e.getMessage());
			}
	    
	    
	    try (CSVWriter writer = new CSVWriter(new FileWriter(targetPath))) {

	        writer.writeNext(headers);

	        for (T item : dataList) {
	            String[] row = mapper.apply(item);
	            writer.writeNext(row);
	        }
	        return true;
	        
	    } catch (IOException e) {
	    	throw new InvalidBackUpPathException(e.getMessage());
	    }
	}
}
