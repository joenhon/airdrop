package com.airdrop.utils;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThreadDrop extends Thread{
    private String address;
    private DefaultBlockParameterName defaultBlockParameterName;
    private readFile readFile=new readFile();
    public  List<String> Hash=new ArrayList<>();
    public ThreadDrop (String address,DefaultBlockParameterName defaultBlockParameterName){
        this.address=address;
        this.defaultBlockParameterName=defaultBlockParameterName;
    }
    public void run(){
        try {
            String str=(defaultBlockParameterName.getValue()=="latest"?"]完成！":"]进行中！");
            EthFilter filter = new EthFilter(
                    defaultBlockParameterName,
                    defaultBlockParameterName,
                    this.address);
            readFile.web3j.ethLogObservable(filter).subscribe(log -> {
                if (!Hash.contains(log.getTransactionHash())){
                    readFile.write("交易：["+log.getTransactionHash()+str+"创建时间："+readFile.df.format(new Date()),PropertyReader.get("monitoring.WriteAddress", "bitstd.properties")+defaultBlockParameterName.getValue()+".log");
                    System.out.println("交易：["+log.getTransactionHash()+str+"创建时间："+readFile.df.format(new Date()));//+","+log.getTopics().get(log.getTopics().size()-2));
                    Hash.add(log.getTransactionHash());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
