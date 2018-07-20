package com.airdrop.service;

import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public interface airdropService {
public List<String> airDrop_(String tokenContractAddress, String path, BigInteger value, String addressPath, String pwd, BigInteger everyGas, BigInteger GasPrice) throws IOException, CipherException;
public List<String> airDropValues(String tokenContractAddress, String path, String value, String addressPath, String pwd, BigInteger everyGas, BigInteger GasPrice);
public List<String> dataMigration(String oldTokenContractAddress,String newTokenContractAddress, String path, String addressPath, String pwd,BigInteger everyGas, BigInteger GasPrice);
}
