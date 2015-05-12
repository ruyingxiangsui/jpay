package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

public class FileHelper {
	// 读文件
	public static String readFile(String filepath) {
		String res = null;
		try {
			File file = new File(filepath);
			File dir = file.getParentFile();
			if (dir != null && !dir.exists()) {
				dir.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			FileInputStream fis = new FileInputStream(file);

			int length = fis.available();

			byte[] buffer = new byte[length];
			fis.read(buffer);

			res = EncodingUtils.getString(buffer, "UTF-8");

			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	// 写文件
	public static void writeFile(String filepath, String write_str) {

		try {
			File file = new File(filepath);
			File dir = file.getParentFile();
			if (dir != null && !dir.exists()) {
				dir.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);

			byte[] bytes = write_str.getBytes();

			fos.write(bytes);

			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
