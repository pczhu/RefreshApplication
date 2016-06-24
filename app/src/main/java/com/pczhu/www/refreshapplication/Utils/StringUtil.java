package com.pczhu.www.refreshapplication.Utils;

import android.annotation.SuppressLint;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

@SuppressLint("DefaultLocale")
public class StringUtil {

    public static boolean isEmpty(String str) {
        return (str == null || "".equals(str) || "".equals(str.trim()));
    }

    public static int toInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return 0;
        }
    }
    /**
     * 流转字符串
     * @param tInputStream
     * @return
     */
	public static String getStreamString(InputStream tInputStream){
		StringBuffer tStringBuffer = new StringBuffer();
		if (tInputStream != null){
			try{
				BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream));
				
				String sTempOneLine = new String("");
				while ((sTempOneLine = tBufferedReader.readLine()) != null){
					tStringBuffer.append(sTempOneLine);
				}
				return tStringBuffer.toString();
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}
		return tStringBuffer.toString();
	}
	/**
	 * 字符串装换为Txt文件保存到Cache目录
	 * @param data
	 * @param filename
	 */
	public static void StringToTxt(String data,String filename){
		File newTextFile  = new File(SystemUtils.getContext().getCacheDir().getAbsolutePath()+"/"+filename+".txt");
		try {
		    FileWriter fw;
		    fw = new FileWriter(newTextFile);
		    fw.write(data);
		    fw.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}


	
	/**
	 * 截取  从小数点后两位向前
	 * @param targetdata
	 * @return
	 */
	public static String subString(String targetdata) {
		if (Double.parseDouble(targetdata) > 0
				&& Double.parseDouble(targetdata) < 0.01)
			return "<0.01";
		else if (Double.parseDouble(targetdata) == 0)
			return "0";
		if (targetdata.length() > 5){
			String temp = targetdata.substring(0, targetdata.lastIndexOf(".") + 3);
			if(temp.lastIndexOf("00") == temp.length()-2){
				return temp.substring(0, temp.lastIndexOf("."));
			}
			if(temp.lastIndexOf("0") == (temp.length()-1)){
				return temp.substring(0, temp.lastIndexOf(".") + 2);
			}
			
			return temp;
		}
			
		return targetdata;
	}
	
	/**
	 * 将输入流转换为字符串
	 * @param inputStream
	 * @return
	 */
	public static String convertStream2String(InputStream inputStream){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[512];
		int len =0;
		try {
			while((len = inputStream.read(buffer))!=-1){
				baos.write(buffer, 0, len);
			}
			inputStream.close();
			
			return new String(baos.toByteArray());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 1.判断字符串是否仅为数字:
	 * 
	 * @param str
	 * @return
	 */

	public static boolean isNumeric1(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 用正则表达式
	 * 判断字符串是否仅为数字:
	 * @param str
	 * @return
	 */
	public static boolean isNumeric2(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
	/**
	 * 返回字符串中所有的数字
	 * @param str
	 * @return
	 */
	public static String getNumeric(String str){
		StringBuilder sb = new StringBuilder();
		for (int i = str.length()-1; i >= 0;i--) {
			char charAt = str.charAt(i);
			if (Character.isDigit(charAt)) {
				sb.append(charAt);
				
			}
		}
		return sb.reverse().toString();
	}
	/**
	 * 名字隐藏中间字符
	 * @param name
	 * @return
	 */
	public static String getXName(String name){
		StringBuffer sb = new StringBuffer();
		char[] charArray = name.toCharArray();
		
		if(charArray.length == 2){
			sb.append(charArray[0]+"X");
		}else if(charArray.length >= 3){
			sb.append(charArray[0]+"X"+charArray[charArray.length-1]);
		}
		
		return sb.toString();
	}


}
