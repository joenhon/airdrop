window.addEventListener('load', function() {

    // Checking if Web3 has been injected by the browser (Mist/MetaMask)
    if (typeof web3 !== 'undefined') {
        // Use Mist/MetaMask's provider
        window.web3 = new Web3(web3.currentProvider);
    } else {
        console.log('No web3? You should consider trying MetaMask!')
        // fallback - use your fallback strategy (local node / hosted node + in-dapp id mgmt / fail)
        window.web3 = new Web3(new Web3.providers.HttpProvider("https://rinkeby.infura.io/v3/30a5e75585ae4636b1c53d216ede9847"));
        //window.web3 = new Web3(new Web3.providers.HttpProvider("https://proxy.mobilefish.com:9070"));
    }

    // Now you can start your app & access web3 freely:
    startApp();
})

// =================================================

function startApp(){
    showNetwork();
    showGasPrice();
    showAccounts();
}

// =================================================

function showNetwork() {
    web3.version.getNetwork((err, res) => {
        var output = "";

    if (!err) {
        if(res > 1000000000000) {
            output = "testrpc";
        } else {
            switch (res) {
                case "1":
                    output = "主网";
                    document.contract="0xbc4c5ebd704251053b68e5c39ad2bcfa3ada1520";
                    document.HTTP="https://mainnet.infura.io/v3/30a5e75585ae4636b1c53d216ede9847";
                    break
                case "2":
                    output = "morden";
                    document.HTTP="https://kovan.infura.io/v3/30a5e75585ae4636b1c53d216ede9847";
                    break
                case "3":
                    output = "ropsten";
                    document.contract="0x989eA41FF29769a962291d15FC60a7d8169c6A24"
                    document.HTTP="https://ropsten.infura.io/v3/30a5e75585ae4636b1c53d216ede9847";
                    break
                case "4":
                    output = "rinkeby";
                    document.contract="0x4FAdaa24a6Afcd0a066D74b7D697677BA842f6eE";
                    document.HTTP="https://rinkeby.infura.io/v3/30a5e75585ae4636b1c53d216ede9847";
                    break
                default:
                    output = "unknown network = "+res;
            }
        }
    } else {
        output = "Error";
    }
    document.getElementById('network').innerHTML = "Network = " + output + "<br />";
})
}
function showAccounts() {
    web3.eth.getAccounts((err, res) => {
        var output = "";

    if (!err==null) {
        output = res.join("<br />");
    } else {
        output = "Error";
    }
    if (res.length>0){
        document.getElementById('accounts').innerHTML = "当前用户：" + res + "<br />";
        document.account=res;
    }else{
        alert("请登录MetaMask并刷新页面！");
    }


})
}
function showGasPrice() {
    web3.eth.getGasPrice((err, res) => {
        var output = "";

    if (!err) {
        output = res;
    } else {
        output = "Error";
    }
    document.getElementById('Price_span').innerHTML =  output/1000000000;
})
}

//签名事务发送
function sendRawTransaction(data) {
    var fromAccount = document.account[0];

    if (fromAccount != null && fromAccount.length > 0 &&
        data != null && data.length > 0
    ) {
        var encryptedMessage = web3.sha3(data);

        web3.eth.sign(fromAccount, encryptedMessage, (err, res) => {
            var output = "";
        if (!err) {
            output=web3.eth.sendRawTransaction(err);
        } else {
            output = "Error";
        }

    })
    }
}
//估算GAS消耗
function estimateGas(Tx){
   return new Web3(new Web3.providers.HttpProvider(document.HTTP)).eth.estimateGas(Tx);
}
//发送交易
function sendTransaction(message){
    web3.eth.sendTransaction(message, (err, res) => {
        var output = "";
    if (!err) {
        output += res;
    } else {
        output = "Error";
    }
    $("#div_1").append("<tr><td>"+output+"</td><td>"+Date()+"</td></tr>");
})
}
function airDrop(to,addressList,amount,fu) {
    var fromAccount = document.account[0];
    var data = fu+airDropData(to,amount,addressList);
    // Use for example 2

    // Use for example 2  gas != null && gas.length > 0 &&
    if (
        to != null && to.length > 0 &&
        amount != null && amount.length > 0
    ) {
        // Example 1: Using the default MetaMask gas and gasPrice
        var message = {to: document.contract, value: 0x0, data: data};
        try{
            var GasLimit=estimateGas(message);
        }catch (e){
            document.getElementById("err").innerHTML=e;
            return "交易失败！";
        }

        message={to:document.contract, data: data, gas: Math.floor(GasLimit+GasLimit*0.1)};
        // Example 2: Setting gas and gasPrice
        //var message = {from: fromAccount, to:to, value: web3.toWei(amount, 'ether'), gas: gas, gasPrice: gasPrice};

        // Example 3: Using the default account
        //web3.eth.defaultAccount = fromAccount;
        //var message = {to:to, value: web3.toWei(amount, 'ether')};
        sendTransaction(message);

    }
}

function airDropValues(to,addressList,amount,fu) {
    var fromAccount = document.account[0];
    var data = fu+airDropValuesData(to,amount,addressList);
    if (
        to != null && to.length > 0 &&
        amount != null && amount.length > 0
    ) {
        // Example 1: Using the default MetaMask gas and gasPrice
        var message = {to: document.contract, value: 0x0, data: data};
        try{
            var GasLimit=estimateGas(message);
        }catch (e){
            document.getElementById("err").innerHTML=e;
            return "交易失败！";
        }
        message={to:document.contract, data: data, gas: Math.floor(GasLimit+GasLimit*0.1)};
        sendTransaction(message);

    }
}

function approve(tokenContractAddress,fu){
    var fromAccount = document.account[0];
    var data = fu+approveData();
    var message = {to:tokenContractAddress, data: data};
    var GasLimit=estimateGas(message);
    message={to:tokenContractAddress, data: data, gas: Math.floor(GasLimit+GasLimit*0.1)};
    sendTransaction(message);
}

function toWei(val) {
    if (val != null && val.length > 0 && !isNaN(val)) {
        return web3.toWei(val,"ether");
    }
}
function toBigNumber(val) {
    if (val != null && val.length > 0 && !isNaN(val)) {
        return web3.toBigNumber(val);
    }
}
function isAddress(address) {
    if (address != null && address.length > 0) {
        return web3.isAddress(address);
    }
}
function toDecimal(val) {
    if (val != null && val.length > 0) {
        return web3.toDecimal(val);
    }
}
// =================================================
// Helper Functions
// =================================================
function parseResultObject(res){
    var output = "";

    for (var key in res) {
        if (res.hasOwnProperty(key)) {

            if( Object.prototype.toString.call( res[key] ) === '[object Array]' ) {
                var arrObj = res[key];

                if(arrObj.length == 0) {
                    output += key + " -> []<br />";
                } else {

                    for(var i=0; i < arrObj.length; i++){
                        for (var key2 in arrObj[i]) {
                            if (arrObj[i].hasOwnProperty(key2)) {
                                output += key + "["+i+"]." + key2 + "-> " + arrObj[i][key2] + "<br />";
                            }
                        }
                    }

                }
            } else {
                output += key + " -> " + res[key] + "<br />";
            }
        }
    }
    return output;
}

function Hex(s) {
    var output=s;
    while (output.length<64){
        output="0"+output;
    }
    return output;
}

function StringListRemake0x(addArray){
    var output = "";
    for (var adi=0;adi<addArray.length;adi++) {
        output+=remake0x(addArray[adi]);
    }
    return output;
}
function numberListRemake0x(valArray) {
    var output = "";
    for (var adi=0;adi<valArray.length;adi++) {
        output+=Hex((parseFloat(valArray[adi]).mul(1e18)).toString(16));
    }
    return output;
}

function remake0x(address){
    var output="";
    if (address.lastIndexOf("0x") == 0 && address.length == "0xc387683bd495658b6ba02ca482a7c8614936df6a".length) {
        output=Hex(address.substring(2,address.length));
    }
    return output;
}
//60是addres[] ,120是uint256[]
function airDropData(tokenContractAddress,value,addArray){
    var length = addArray.length*1,output = "",value_ = value*(1e18);
    output += (remake0x(tokenContractAddress)
        +"0000000000000000000000000000000000000000000000000000000000000060"
        +Hex(value_.toString(16))
        +Hex(length.toString(16))
        +StringListRemake0x(addArray));
    return output.toString();
}

function airDropValuesData(tokenContractAddress,valArray,addArray) {
    var length = addArray.length*1,value=0,output ="";
    for (var i=0;i<length;i++){
        value = parseFloat(value).add(parseFloat(valArray[i]));
    }
    output += (remake0x(tokenContractAddress)
        +"0000000000000000000000000000000000000000000000000000000000000080"
        +Hex(((valArray.length+2)*20).toString())
        +Hex(value.mul(1e18).toString(16))
        +Hex(length.toString(16))
        +StringListRemake0x(addArray)
        +Hex(length.toString(16))
        +numberListRemake0x(valArray)
    );
    return output.toString();
}

function approveData() {
    var value_=1e28;
    var output = "";
    output+=(remake0x(document.contract)
        +Hex(value_.toString(16))
    );
    return output.toString();
}
//乘法函数，用来得到精确的乘法结果

//说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。

//调用：accMul(arg1,arg2)

//返回值：arg1乘以arg2的精确结果
function accMul(arg1,arg2) {

    var m=0,s1=arg1.toString(),s2=arg2.toString();

    try{m+=s1.split(".")[1].length}catch(e){}

    try{m+=s2.split(".")[1].length}catch(e){}

    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)

}
//给Number类型增加一个mul方法，调用起来更加方便。

Number.prototype.mul = function (arg){

    return accMul(arg, this);

}

//加法函数，用来得到精确的加法结果

//说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。

//调用：accAdd(arg1,arg2)

//返回值：arg1加上arg2的精确结果
function accAdd(arg1,arg2){

    var r1,r2,m;

    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}

    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}

    m=Math.pow(10,Math.max(r1,r2))

    return (arg1*m+arg2*m)/m

}

//给Number类型增加一个add方法，调用起来更加方便。

Number.prototype.add = function (arg){

    return accAdd(arg,this);

}
