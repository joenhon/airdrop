package com.airdrop.service.impl;

import com.airdrop.service.ethService;
import com.airdrop.utils.readFile;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@Service("ethService")
public class ethServiceImpl implements ethService {

    @Override
    public BigInteger Price() throws ExecutionException, InterruptedException {
        return readFile.web3j.ethGasPrice().sendAsync().get().getGasPrice();
    }
}
