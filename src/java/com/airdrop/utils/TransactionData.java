package com.airdrop.utils;

import com.airdrop.ETHenum.function;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class TransactionData {
    public static BigInteger ethEstimateGas(String fromAddress,String contractAddress, String data){
        //System.out.println(readFile.adta(tokenContractAddress,new BigInteger("1000000000"),readFile.fileToList(path).subList(0,100)));
        Transaction transaction=Transaction.createEthCallTransaction(fromAddress,contractAddress, data);
        BigInteger GasLimit=BigInteger.ZERO;
        try {
            GasLimit = BigInteger.valueOf((new BigDecimal(readFile.web3j.ethEstimateGas(transaction).send().getAmountUsed()).multiply(BigDecimal.valueOf(1.1))).intValue());
            return GasLimit;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String airDrop(String fromAddress, Credentials credentials, String tokenContractAddress, String contractAddress, List<String> addArray, BigInteger GasPrice, BigInteger value){
        String data=function.airdrop.getValue()+readFile.airDropData(tokenContractAddress,value,addArray);
        return Transaction(fromAddress,GasPrice,contractAddress,data,credentials);
    }
    public static String dropValues(String fromAddress, Credentials credentials, String tokenContractAddress, String contractAddress, List<String> addArray,List<BigInteger> valArray , BigInteger GasPrice, BigInteger value){
        String data=function.airdropValues.getValue()+readFile.airDropValuesData(tokenContractAddress,value,addArray,valArray);
        return Transaction(fromAddress,GasPrice,contractAddress,data,credentials);
    }
    public static String dataMigration(String fromAddress, Credentials credentials,String oldTokenContractAddress,  String tokenContractAddress, String contractAddress, List<String> addArray, BigInteger GasPrice, BigInteger value){
        String data=function.dataMigration.getValue()+readFile.dataMigrationData(oldTokenContractAddress,tokenContractAddress,value,addArray);
        return Transaction(fromAddress,GasPrice,contractAddress,data,credentials);
    }
    private static String Transaction(String fromAddress,BigInteger GasPrice,String contractAddress,String data,Credentials credentials){
        String transactionHash=null;
        try {
            while (transactionHash==null){
                RawTransaction rawTransaction= RawTransaction.createTransaction(readFile.nonce
                        ,GasPrice
                        ,ethEstimateGas(fromAddress,contractAddress,data)
                        ,contractAddress
                        ,data);
                byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
                String hexValue = Numeric.toHexString(signedMessage);
                EthSendTransaction ethSendTransaction = readFile.web3j.ethSendRawTransaction(hexValue).sendAsync().get();
                transactionHash = ethSendTransaction.getTransactionHash();
                readFile.nonce=readFile.nonce.add(BigInteger.ONE);
            }
            return transactionHash;
        } catch (Exception  e) {
            e.printStackTrace();
        }
        return null;
    }
}
