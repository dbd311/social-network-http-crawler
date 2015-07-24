package utilities;

/**@author Duy Dinh <dinhbaduy@gmail.com>**/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;

/***
 * US-ASCII Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of
 * the Unicode character set ISO-8859-1 ISO Latin Alphabet No. 1, a.k.a.
 * ISO-LATIN-1 UTF-8 Eight-bit UCS Transformation Format UTF-16BE Sixteen-bit
 * UCS Transformation Format, big-endian byte order UTF-16LE Sixteen-bit UCS
 * Transformation Format, little-endian byte order UTF-16 Sixteen-bit UCS
 * Transformation Format, byte order identified by an optional byte-order mark /
 */

public class FileManager {

	/**
	 * Writes text content to the output file
	 */
	public static void WriteToFile(String text, String outputFileName,
			String charsetName) {
		try {
			if (text == "" || text == null)
				return;

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputFileName), charsetName));

			writer.write(text);
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void WriteToFile(String text, String outputFileName) {
		try {
			if (text == "" || text == null)
				return;

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputFileName)));

			writer.write(text);
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Reads text from an input file
	 * 
	 * @returns Returns the text content, each line is seperated by '\n'
	 **/
	public static String ReadFile(String fileName, String charsetName) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName), charsetName));
			String s;
			while ((s = reader.readLine()) != null)
				sb.append(s + "\n");
			reader.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// remove the bizar character in the UTF file
		if (charsetName.contains("UTF"))
			sb.delete(0, 1);
		return sb.toString();
	}

	public static String ReadFile(String fileName) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName)));
			String s;
			while ((s = reader.readLine()) != null)
				sb.append(s + "\n");
			reader.close();
		} catch (Exception e1) {
			// e1.printStackTrace();
			System.out.println("File '" + fileName + "' does not exist!");
		}

		return sb.toString();
	}

	// Read the last line from a fine
	public static String ReadLastLine(String fileName) {
		String str = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName)));
			String s;
			while ((s = reader.readLine()) != null)
				str = s;
			reader.close();
		} catch (Exception e1) {
			// e1.printStackTrace();
			System.out.println("File '" + fileName + "' does not exist!");
		}

		return str;
	}

	public static ArrayList<String> ReadLines(String fileName,
			String charsetName) {
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName), charsetName));
			String s;
			while ((s = reader.readLine()) != null)
				if (!s.startsWith("#"))
					lines.add(s.trim());
			reader.close();
		} catch (Exception e1) {
			// e1.printStackTrace();
			System.out.println("File '" + fileName + "' does not exist!");
		}

		return lines;
	}

	public static ArrayList<String> ReadLines(String fileName) {

		return ReadLines(fileName, "UTF-8");
	}

	public static ArrayList<String> ReadNLines(String fileName, int numOfLines) {
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader reader = null;
		int count = 0;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName)));
			String s;
			while ((s = reader.readLine()) != null && (count < numOfLines)) {
				if (!s.startsWith("#")) {
					lines.add(s.trim());
					count++;
				}

			}

			reader.close();
		} catch (Exception e1) {
			// e1.printStackTrace();
			System.out.println("File '" + fileName + "' does not exist!");
		}

		return lines;
	}

	public static void AppendFile(String text, String outputFileName) {
		try {
			if (text.equals("") || text == null)
				return;

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputFileName, true)));

			writer.write(text);
			writer.flush();
			writer.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void AppendFile(String text, String outputFileName,
			String charset) {
		try {
			if (text.equals("") || text == null)
				return;

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputFileName, true), charset));

			writer.write(text);
			writer.flush();
			writer.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void RemoveFile(String output) {
		File f = new File(output);
		if (f.exists())
			f.delete();
	}

	/** Get next topic in the topic collection **/
	public static String GetNextTopic(BufferedReader reader) {

		return getNextItem(reader, "TOP");
	}

	/** Get next document in the document collection **/
	public static String GetNextDocument(BufferedReader reader) {
		return getNextItem(reader, "DOC");
	}

	/**
	 * Gets the next item in the document given its label. The item is started
	 * with a starting tag and ended with the corresponding ending tag
	 */
	protected static String getNextItem(BufferedReader reader, String itemLabel) {
		StringBuilder sb = null;
		String text = null;
		String line = "";
		try {
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.equalsIgnoreCase("<" + itemLabel + ">")) // start
				{
					sb = new StringBuilder();
					sb.append(line + "\n");
				} else if (line.equalsIgnoreCase("</" + itemLabel + ">")
						|| line.endsWith("</" + itemLabel + ">")) // end
				{
					sb.append(line + "\n");
					text = sb.toString();
					break;
				} else if (sb != null)
					sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	public static BufferedReader getFileReader(String path)
			throws FileNotFoundException, UnsupportedEncodingException {
		File file = new File(path);
		FileReader reader = new FileReader(file);
		BufferedReader buffer = new BufferedReader(reader);
		return buffer;
	}

	public static String makeFile(String[] pathElements,
			String outputShortFileName) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < pathElements.length; i++) {
			sb.append(pathElements[i]).append("/");
			File f = new File(sb.toString());
			if (!f.exists()) {
				f.mkdir();
			}
		}
		return sb.append(outputShortFileName).toString();
	}

	public static void copyFile(File from, File to) {
		try {
			Files.copy(from.toPath(), to.toPath(),
					java.nio.file.StandardCopyOption.REPLACE_EXISTING,
					java.nio.file.StandardCopyOption.COPY_ATTRIBUTES,
					java.nio.file.LinkOption.NOFOLLOW_LINKS);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void copyFile(String fn1, String fn2) {
		copyFile(new File(fn1), new File(fn2));
	}

}
