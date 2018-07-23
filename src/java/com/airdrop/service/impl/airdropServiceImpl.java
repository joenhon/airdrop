package com.airdrop.service.impl;

import com.airdrop.service.airdropService;
import com.airdrop.utils.*;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service("airdropService")
public class airdropServiceImpl implements airdropService {
    private readFile readFile=new readFile();
    @Override
    public List<String> airDrop_(String tokenContractAddress, String path,BigInteger value, String addressPath,String pwd, BigInteger everyGas, BigInteger GasPrice){
        List<String> addressList = new ArrayList<String>();
        String str= readFile.read(path,"");
        addressList.addAll(java.util.Arrays.asList(str.split(",")));
        Credentials credentials= null;
        String fromAddress="";
        try {
            File targetFile = new File(addressPath);
            WalletFile walletFile = (WalletFile) ObjectMapperFactory.getObjectMapper().readValue(targetFile, WalletFile.class);
            fromAddress=walletFile.getAddress();
            readFile.nonce = readFile.web3j.ethGetTransactionCount("0x"+fromAddress,DefaultBlockParameterName.PENDING).send().getTransactionCount();
            credentials = WalletUtils.loadCredentials(pwd, targetFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> hash=new ArrayList<>();
        System.out.println("开始空投！");
        List<String>  addArray=new ArrayList<String>();
        Date d1=new Date();
        List<Boolean> bos=new ArrayList<Boolean>();
        String result=null;
        for (int i=0;i<=Math.ceil(addressList.size()/readFile.ko);i++){
            result=null;
            System.out.println("第"+(i+1)+"次！");
            try {
                addArray.addAll(addressList);
                if (i<(Math.ceil(addressList.size()/readFile.ko))-1){
                    addArray = (addArray.subList(i*readFile.ko,(i*readFile.ko+readFile.ko)));
                }else {
                    if (addressList.size()-((i+1)*readFile.ko)>15){
                        addArray = (addArray.subList(i*readFile.ko,(i*readFile.ko+readFile.ko)));
                    }else{
                        addArray = (addArray.subList(i*readFile.ko,addArray.size()));
                    }
                }
                for (int adi=0;adi<addArray.size();adi++){
                    if(addArray.get(adi).lastIndexOf("0x")!=0 || addArray.get(adi).length()!="0xc387683bd495658b6ba02ca482a7c8614936df6a".length()){
                        addArray.remove(adi);
                        adi--;
                    }
                }
                bos.add(true);
                String hashId=TransactionData.airDrop("0x"+fromAddress,credentials,tokenContractAddress,readFile.contractAddress,addArray,GasPrice,value);
                result="hashId=" + hashId;
                hash.add(hashId);
            }catch (Exception e){
                result+="第"+(i+1)+"次！"+i*readFile.ko+"——"+(i*readFile.ko+addArray.size())+" 空投失败！err:"+e.getMessage();
                StringBuffer strBf=new StringBuffer();
                for (int j=0;j<addArray.size();j++){
                    strBf.append(addArray.get(j));
                    if (j!=addArray.size()-1){
                        strBf.append(",");
                    }
                    if (j%5==1){
                        strBf.append("\r\n");
                    }
                }
                result+=strBf.toString();
                System.out.println(result);
                e.printStackTrace();
            }finally {
                addArray.clear();
                System.out.println(result);
                readFile.write(result,PropertyReader.get("WriteAddress", "bitstd.properties"));
            }

        }
        readFile.write("创建时间："+df.format(d1),PropertyReader.get("WriteAddress", "bitstd.properties"));
        return hash;
    }

    @Override
    public List<String> airDropValues(String tokenContractAddress, String path, String value, String addressPath, String pwd, BigInteger everyGas, BigInteger GasPrice) {
        List<String> addressList = new ArrayList<String>();
        List<BigInteger> valueList=new ArrayList<>();
        String str= readFile.read(path,"");
        addressList.addAll(java.util.Arrays.asList(str.split(",")));
        str=readFile.read(value,"");
        String lo=null;
        Date d1 = new Date();
        for (String  val:java.util.Arrays.asList(str.split(","))) {
            lo=(new BigDecimal(val).multiply(BigDecimal.valueOf(readFile.Decimals))).setScale(0).toString();
            valueList.add(new BigInteger(lo));
        }
        Credentials credentials=null;
        String fromAddress="";
        try {
            File targetFile = new File(addressPath);
            WalletFile walletFile = (WalletFile) ObjectMapperFactory.getObjectMapper().readValue(targetFile, WalletFile.class);
            fromAddress=walletFile.getAddress();
            readFile.nonce = readFile.web3j.ethGetTransactionCount("0x"+fromAddress,DefaultBlockParameterName.PENDING).send().getTransactionCount();
            credentials = WalletUtils.loadCredentials(pwd, targetFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("开始空投！");
        List<String>  addArray=new ArrayList<String>();
        List<BigInteger>  valueArray=new ArrayList<>();
        List<Boolean> bos=new ArrayList<Boolean>();
        String result=null;
        if (valueList.size()!=addressList.size()){
            try {
                throw new Exception("value与address不匹配！");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        List<String> hash=new ArrayList<>();
        for (int i=0;i<=Math.ceil(addressList.size()/readFile.ko);i++){
            result=null;
            System.out.println("第"+(i+1)+"次！");
            BigInteger gross=BigInteger.ZERO;
            try {
                addArray.addAll(addressList);
                valueArray.addAll(valueList);
                if (i<(Math.ceil(addressList.size()/readFile.ko)-1)){
                    addArray = (addArray.subList(i*readFile.ko,(i*readFile.ko+readFile.ko)));
                    valueArray = (valueArray.subList(i*readFile.ko,(i*readFile.ko+readFile.ko)));
                }else {
                    if (addressList.size()-((i+1)*readFile.ko)>10){
                        addArray = (addArray.subList(i*readFile.ko,(i*readFile.ko+readFile.ko)));
                        valueArray = (valueArray.subList(i*readFile.ko,(i*readFile.ko+readFile.ko)));
                    }else{
                        addArray = (addArray.subList(i*readFile.ko,addArray.size()));
                        valueArray = (valueArray.subList(i*readFile.ko,valueArray.size()));
                    }
                }
                for (int adi=0;adi<addArray.size();adi++){
                    if(addArray.get(adi).lastIndexOf("0x")!=0 || addArray.get(adi).length()!="0xc387683bd495658b6ba02ca482a7c8614936df6a".length()){
                        addArray.remove(adi);
                        valueArray.remove(adi);
                        adi--;
                    }else {
                        gross = gross.add(valueArray.get(adi));
                    }
                }

                String hashId=TransactionData.dropValues("0x"+fromAddress,credentials,tokenContractAddress,readFile.contractAddress,addArray,valueArray,GasPrice,gross);
                bos.add(true);
                result="hashId=" + hashId;
                hash.add(hashId);
            }catch (Exception e){
                result+="第"+(i+1)+"次！"+i*readFile.ko+"——"+(i*readFile.ko+addArray.size())+" 空投失败！err:"+e.getMessage();
                StringBuffer strBf=new StringBuffer();
                for (int j=0;j<addArray.size();j++){
                    strBf.append(addArray.get(j));
                    if (j!=addArray.size()-1){
                        strBf.append(",");
                    }
                    if (j%5==1){
                        strBf.append("\r\n");
                    }
                }
                result+=strBf.toString();
            }finally {
                addArray.clear();
                System.out.println(result);
                readFile.write(result,PropertyReader.get("WriteAddress", "bitstd.properties"));
            }

        }
        readFile.write("创建时间："+readFile.df.format(d1),PropertyReader.get("WriteAddress", "bitstd.properties"));
        return hash;
    }

    @Override
    public List<String> dataMigration(String oldTokenContractAddress, String newTokenContractAddress, String path, String addressPath, String pwd, BigInteger everyGas, BigInteger GasPrice) {
        String str=readFile.read(path,"");
        List<String> addressList = new ArrayList<String>();
        addressList.addAll(java.util.Arrays.asList(str.split(",")));
        Airdrop airDrop = null;

        Credentials  credentials=null;
        String fromAddress="";
        try {
            File targetFile = new File(addressPath);
            WalletFile walletFile = (WalletFile) ObjectMapperFactory.getObjectMapper().readValue(targetFile, WalletFile.class);
            fromAddress=walletFile.getAddress();
            readFile.nonce = readFile.web3j.ethGetTransactionCount("0x"+fromAddress,DefaultBlockParameterName.PENDING).send().getTransactionCount();
            credentials = WalletUtils.loadCredentials(pwd, targetFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        ERC20 oldErc20=ERC20.load(oldTokenContractAddress,readFile.web3j,credentials,GasPrice,readFile.GAS_LIMIT);
        ERC20 newErc20=ERC20.load(newTokenContractAddress,readFile.web3j,credentials,GasPrice,readFile.GAS_LIMIT);
        System.out.println("开始数据迁移！");
        List<String>  addArray=new ArrayList<String>();
        Date d1=new Date();
        List<Boolean> bos=new ArrayList<Boolean>();
        String result=null;
        for (int i=0;i<Math.ceil(addressList.size()/readFile.ko);i++){
            result="";
            System.out.println("第"+(i+1)+"次！");
            BigInteger gross=BigInteger.ZERO;
            try {
                addArray.addAll(addressList);
                if (i<(Math.ceil(addressList.size()/readFile.ko)-1)){
                    addArray = (addArray.subList(i*readFile.ko,(i*readFile.ko+readFile.ko)));
                }else {
                    if (addressList.size()-((i+1)*readFile.ko)>15){
                        addArray = (addArray.subList(i*readFile.ko,(i*readFile.ko+readFile.ko)));
                    }else{
                        addArray = (addArray.subList(i*readFile.ko,addArray.size()));
                    }
                }
                for (int adi=0;adi<addArray.size();adi++){
                    BigInteger oldValue = oldErc20.balanceOf(addArray.get(adi)).send();
                    BigInteger newValue = newErc20.balanceOf(addArray.get(adi)).send();
                    if(addArray.get(adi).lastIndexOf("0x")!=0 || addArray.get(adi).length()!="0xc387683bd495658b6ba02ca482a7c8614936df6a".length()|| oldValue.toString().equals("0") || !newValue.toString().equals("0")){
                        addArray.remove(adi);
                        adi--;
                    }else {
                        gross = gross.add(oldValue);
                    }
                }
                if (gross==BigInteger.ZERO){
                    continue;
                }
                String hashId=TransactionData.dataMigration("0x"+fromAddress,credentials,oldTokenContractAddress,newTokenContractAddress,readFile.contractAddress,addArray,GasPrice,gross);
                result="hashId=" + hashId;

            }catch (Exception e){
                if (gross==BigInteger.ZERO){
                    result+="第"+(i+1)+"次！轮空！";
                }else {
                    result+="第"+(i+1)+"次！"+i*readFile.ko+"——"+(i*readFile.ko+addArray.size())+" 迁移失败！err:"+e.getMessage();
                    StringBuffer strBf=new StringBuffer();
                    for (int j=0;j<addArray.size();j++){
                        strBf.append(addArray.get(j));
                        if (j!=addArray.size()-1){
                            strBf.append(",");
                        }
                        if (j%5==1){
                            strBf.append("\r\n");
                        }
                    }
                    result+=strBf.toString();
                    //e.printStackTrace();
                }
            }finally {
                addArray.clear();
                System.out.println(result);
                readFile.write(result,PropertyReader.get("WriteAddress", "bitstd.properties"));
            }

        }
        readFile.write("创建时间："+readFile.df.format(d1),PropertyReader.get("WriteAddress", "bitstd.properties"));
        return null;
    }
}
