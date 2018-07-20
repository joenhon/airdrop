package com.airdrop.utils;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.ipc.IpcService;
import org.web3j.protocol.ipc.UnixIpcService;

import java.io.*;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class readFile {
    public static final String ethHost = PropertyReader.get("eth.api.host", "bitstd.properties");
    public static int ko=Integer.parseInt(PropertyReader.get("PerformNumber", "bitstd.properties"));
    public static String contractAddress = PropertyReader.get("eth.api.contract", "bitstd.properties");
    public static BigInteger GAS_LIMIT=new BigInteger(PropertyReader.get("GAS_LIMIT", "bitstd.properties"));
    public static Web3j web3j = Web3j.build(new HttpService(ethHost));
    public static long Decimals=10;
    public static List<String> Hash=new ArrayList<>();
    public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static BigInteger nonce;
    static{
        //web3j=Web3j.build(new UnixIpcService(ethHost));
        Decimals= (long) Math.pow(Decimals,Integer.parseInt(PropertyReader.get("Decimals","bitstd.properties")));
    }

    public String read(String file,String n){
       // Admin admin=new Admin.build(new HttpService(ethHost));
        StringBuffer str = new StringBuffer("");;
        try {
            Reader reader = new FileReader(file);
            // 这里我们用到了字符操作的BufferedReader类
             BufferedReader bufferedReader = new BufferedReader(reader);
             String string = null;
             // 按行读取，结束的判断是是否为null，按字节或者字符读取时结束的标志是-1
             while ((string = bufferedReader.readLine()) != null) {
                 // 这里我们用到了StringBuffer的append方法，这个比string的“+”要高效
                 str.append(string + n);
             }
             // 注意这两个关闭的顺序
             bufferedReader.close();
             reader.close();
        } catch (Exception  e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    public boolean write(String string,String path){
        File file = new File(path);
        String str=null;
        Writer writer = null;
        if (!file.exists()) {
             try {
                 // 如果文件找不到，就new一个
                 file.createNewFile();
             } catch (IOException e) {

                 e.printStackTrace();
                 return false;
             }
        }else {
            str=read(path,"\n")+string;
        }
        try {
            writer = new FileWriter(file);
            writer.write(str);
            writer.write("\r\n");
            // 在这一定要记得关闭流

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
    public static String Hex(String s) {
        StringBuffer str=new StringBuffer(s);
        while (str.length()<64){
           str.insert(0,"0");
        }
        return str.toString();
    }
    public static String listRemake0x(List<String> addArray){
        StringBuffer stringBuffer=new StringBuffer();
        for (int adi=0;adi<addArray.size();adi++) {
            stringBuffer.append(remake0x(addArray.get(adi)));
        }
        return stringBuffer.toString();
    }
    public static String remake0x(String address){
        String str="";
        //if (address.lastIndexOf("0x") == 0 && address.length() == "0xc387683bd495658b6ba02ca482a7c8614936df6a".length()) {
        str=Hex(address.replaceAll("0x",""));
       // }
        return str;
    }
    //60是addres[] ,120是uint256[]
    public static String adta(String tokenContractAddress,BigInteger value,List<String> addArray){
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(remake0x(tokenContractAddress)
                +"0000000000000000000000000000000000000000000000000000000000000060"
                +Hex(value.toString(16))
                +Hex(BigInteger.valueOf(addArray.size()).toString(16))
                +listRemake0x(addArray));
        return stringBuffer.toString();
    }
    public static List<String> fileToList(String path){
        List<String> addressList = new ArrayList<String>();

        String str= new readFile().read(path,"");
        addressList.addAll(java.util.Arrays.asList(str.split(",")));
        return addressList;
    }
}
