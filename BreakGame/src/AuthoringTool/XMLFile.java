package AuthoringTool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class XMLFile {
	public static File file;
	String filePath;
	public XMLFile(String filePath) {
		this.filePath = filePath;
		openFile(filePath);
	}
	// 파일 새로 생성하고 여는 메소드
	public void openFile(String filePath) {
		file = new File(filePath);
		try {
			boolean success = file.createNewFile();
		} catch (IOException e) {
			return;
		}
	}
	// 파일 쓰는 메소드
	public void writeFile(String text, File file) {
		FileWriter writer = null; // 파일 쓰는 객체 생성
		try {			
			writer = new FileWriter(file, true);			
		} catch (IOException e1) {			
			e1.printStackTrace();			
		}
		try {
			
			writer.write(text + '\n');
			writer.flush();					
			writer.close();
			
		} catch (IOException e) {
			return;
		}

	}
}
