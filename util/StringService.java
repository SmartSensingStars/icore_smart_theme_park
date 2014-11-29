package com.larcloud.util;

import java.util.ArrayList;
import java.util.List;

public class StringService {
	public static String[] decodeStringList(String stringList,String regex){
		try{
			if(stringList!=null&&!stringList.equals("")){
				String[] list = stringList.split(regex);
				if(list!=null&&list.length>0){
					return list;
				}else {
					return null;
				}
			}else{
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static List<Integer> decodeStringToList(String stringList,String regex){
		List<Integer> list = new ArrayList<Integer>();
		try{
			if(stringList!=null&&!stringList.equals("")){
				String[] slist = stringList.split(regex);
				if(slist!=null&&slist.length>0){
					for(int i=0;i<slist.length;i++){
						list.add(Integer.decode(slist[i]));
					}
					return list;
				}else {
					return null;
				}
			}else{
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	public static List<String> decodeStringToStringList(String stringList,String regex){
		List<String> list = new ArrayList<String>();
		try{
			if(stringList!=null&&!stringList.equals("")){
				String[] slist = stringList.split(regex);
				if(slist!=null&&slist.length>0){
					for(int i=0;i<slist.length;i++){
						list.add(slist[i]);
					}
					return list;
				}else {
					return null;
				}
			}else{
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	
	public static String changeListtoString(List<Integer> list,String regx){
		if(list!=null&&list.size()>0){
			StringBuilder builder = new StringBuilder();
			for(int i=0;i<list.size();i++){
				builder.append(regx+list.get(i));
			}
			return builder.toString().substring(1);
		}else{
			return "";
		}
	}
	
	public static String changeStringListtoString(List<String> list,String regx){
		if(list!=null&&list.size()>0){
			StringBuilder builder = new StringBuilder();
			for(int i=0;i<list.size();i++){
				builder.append(regx+list.get(i));
			}
			return builder.toString().substring(1);
		}else{
			return "";
		}
	}
	
	
	public static final void main(String[] args){
		String aa = "1,,larcloud,,admin,,admin,,1,,RT3052,,1,,3,,0,,1,,0,,0,,0,,,,0,,,,0,,1,,0,,,,1,,0,,,,0,,,,0,,0,,0,,0,,0,,,,time.nist.gov,,1,,0,,CST_008,,1,,0,,,,larcloud,,192.168.0.254,,211.93.80.130,,8.8.8.8,,1,,0,,,,,,,,,,0,,1";
		List<String> list = StringService.decodeStringToStringList(aa, ",,");
		System.out.println(list.size());
		for(int i = 0;i<list.size();i++){
			System.out.println(i+":"+list.get(i));
		}
	}
}
