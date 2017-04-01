package nl.tue.ddss.ifcrdf.compare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class IfcCompare {
   

	private Workbook wb=new HSSFWorkbook();
    CreationHelper createHelper = wb.getCreationHelper();
    Sheet sheet = wb.createSheet("new sheet");
    int rowPointer=0;

	public static void main(String[] args) throws IOException {
		IfcCompare c = new IfcCompare();
	    c.compareFolder("resources/IFC_original", "resources/IFC_roundtrip");
	    FileOutputStream fileOut = new FileOutputStream("compare_result.xls");
	    c.wb.write(fileOut);
	    fileOut.close();
	}

	public void compareFolder(String inputFolder1, String inputFolder2) throws IOException {
		File dir = new File(inputFolder1);
		String[] directoryListing = dir.list();
		printRow(rowPointer,"IFC files","MD5","SHA-1");
		rowPointer++;
		for (String child : directoryListing) {
			if (child.endsWith("ifc")) {
				try {
					String file2 = child.substring(0, child.indexOf(".")) + ".ifc.ifc";
					File dir2 = new File(inputFolder2);
					String[] directoryListing2 = dir2.list();
					if (Arrays.asList(directoryListing2).contains(file2)) {
							printCheckSum(inputFolder1+"/"+child);
							printCheckSum(inputFolder2+"/"+file2);		
					}
				} catch (StackOverflowError e) {
					System.out.println("Fail");
					System.out.println(e.getCause());
				}

			}
		}

	}
	
	public void printCheckSum(String ifcFile){
		StringBuilder sb=new StringBuilder();
		try {

			BufferedReader br = new BufferedReader(new FileReader(ifcFile));
			String line;
			while ((line =br.readLine())!= null ) {
				while (line!=null&&!line.startsWith("#")) {
					line = br.readLine();
				}
				if(line!=null){
					line=removeCommentInLine(line);
					line = line.replaceAll("\\s", "");
					sb.append(line);
				}
			}
			byte[] bytes=sb.toString().getBytes();
			printRow(rowPointer,ifcFile.substring(ifcFile.lastIndexOf("/")+1),convertByteArrayToHexString(md5(bytes)),convertByteArrayToHexString(sha1(bytes)));
			rowPointer++;
			
            br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void printRow(int rowNum,String...strings) throws IOException{
		    Row row = sheet.createRow(rowNum);		    
		    for (int i=0;i<strings.length;i++){
		    	row.createCell(i).setCellValue(strings[i]);
		    }		    
	}
	
	private static String convertByteArrayToHexString(byte[] arrayBytes) {
	    StringBuffer stringBuffer = new StringBuffer();
	    for (int i = 0; i < arrayBytes.length; i++) {
	        stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
	                .substring(1));
	    }
	    return stringBuffer.toString();
	}
	
	public byte[] sha1(byte[] inputBytes){
		byte[] hashBytes=null;
		try {
			MessageDigest md=MessageDigest.getInstance("SHA-1");
					 
		    hashBytes = md.digest(inputBytes);
		    
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashBytes;
	}
	
	public byte[] md5(byte[] inputBytes){
		byte[] hashBytes=null;
		try {
			MessageDigest md=MessageDigest.getInstance("MD5");
					 
		    hashBytes = md.digest(inputBytes);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashBytes;
	}
	
	public String removeCommentInLine(String line){
         StringBuilder sb=new StringBuilder();
         boolean comment=false;
		 for (int i = 0; i < line.length(); i++) {
	            char ch = line.charAt(i);
	            char next=0;
	            if(i<line.length()-1){
	            next=line.charAt(i+1);
	            }
	            if(ch=='/' && next=='*'){
	            	comment =true;
	            	i=i+2;
	            } 
	            if (ch=='*'&&next=='/'&&comment==true){
	            	comment=false;
	            	i=i+2;
	            	continue;
	            }
	            if(comment==false){
	            	sb.append(line.charAt(i));
	            }
		 }
		 return sb.toString();
	}
	







}
