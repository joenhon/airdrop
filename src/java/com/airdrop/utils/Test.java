package com.airdrop.utils;

import com.airdrop.service.airdropService;
import com.airdrop.service.impl.airdropServiceImpl;
import org.web3j.crypto.*;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.rx.Web3jRx;
import rx.Subscription;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Test {
    public static void main(String[] ages) throws ExecutionException, InterruptedException, IOException, CipherException {
        String path = PropertyReader.get("path","bitstd.properties");
        String tokenContractAddress = PropertyReader.get("tokenContractAddress","bitstd.properties");
        //创建观察
        new ThreadDrop(tokenContractAddress,DefaultBlockParameterName.PENDING).start();
        new ThreadDrop(tokenContractAddress,DefaultBlockParameterName.LATEST).start();
        //readFile.Hex(new BigInteger("1000000000").toString(16));
        airdropService air = new airdropServiceImpl();

        String addressPath = PropertyReader.get("addressPath","bitstd.properties");
        Scanner scanner=new Scanner(System.in);

        System.out.println("请输入密码");
        String pwd = scanner.nextLine();
        try {
            System.out.println("当前区块GasPrice："+readFile.web3j.ethGasPrice().sendAsync().get().getGasPrice().divide(new BigInteger("1000000000"))+"Gwei,请输入GasPrice：");
        }catch (Exception e){
            e.printStackTrace();
        }
        BigDecimal GP=scanner.nextBigDecimal().multiply(BigDecimal.valueOf(1000000000)).setScale(0);

        BigInteger everyGas = new BigInteger(PropertyReader.get("everyGas","bitstd.properties"));
        BigInteger GasPrice = (new BigInteger(GP.toString()).multiply(readFile.web3j.ethGasPrice().sendAsync().get().getGasPrice()).divide(new BigInteger("1000000000")));
        System.out.println("GasPrice："+GasPrice);

        if (ages[0].equals("airDrop")){
            long Decimals=10;
            Decimals= (long) Math.pow(Decimals,Integer.parseInt(PropertyReader.get("Decimals","bitstd.properties")));
            System.out.println("请输入空投数量！");
            String va= (scanner.nextBigDecimal().multiply(BigDecimal.valueOf(Decimals))).setScale(0).toString();
            BigInteger value = new BigInteger(va);
            try {
                //System.out.println(TransactionData.TransactionContractData("0xb6f362f29705af19cb3559878099f92d183646cd",credentials,tokenContractAddress,readFile.contractAddress,path,GasPrice,value));
                air.airDrop_(tokenContractAddress,path,value,addressPath,pwd,everyGas,GasPrice);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (ages[0].equals("airDropValues")){
            String valuePath=PropertyReader.get("valuePath","bitstd.properties");
            try {
                air.airDropValues(tokenContractAddress,path,valuePath,addressPath,pwd,everyGas,GasPrice);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (ages[0].equals("dataMigration")){
            System.out.println("请输入新合约地址！/Please enter the new contract address!");
            String newContract=scanner.next();
            System.out.println("请输入旧合约地址！/Please enter the old contract address!");
            String oldContract=scanner.next();
            try {
              air.dataMigration(oldContract,newContract,path,addressPath,pwd,everyGas,GasPrice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
