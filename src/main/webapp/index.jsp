<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en" style="height: 100%;">
<head>
    <meta charset="UTF-8">
    <title>空投</title>
    <style type="text/css">
        body,html{
            width: 100%;
            padding: 0px;
            margin: 0px;
        }
        .wrap{
            width: 700px;
            height: auto;
            /*border: 1px solid lightgray;*/
            margin:0 auto;
            text-align: center;
        }
        .air-drop{
            margin-top: 15px;
            height: 30px;
        }
        .spantext{
            display: inline-block;
            width: 160px;
            height: 100%;
            /*border: 1px solid red;*/
            text-align: right;
            line-height: 30px;
            margin-right: 10px;
        }
        input{
            width: 380px;
            height: 30px;
            padding-left: 8px;
        }
        .inputcont{
            width: 120px;
            margin-top: 20px;
            margin-left: 170px;
        }
        .tableshuju{
            width: 80%;
            margin: 0 auto;
            margin-top: 40px;
            display: none;
            /*border: 1px solid red;*/
        }
        .tableshuju:after{
            clear:both;
            content:'.';
            display:block;
            width: 0;
            height: 0;
            visibility:hidden;
        }
        .left{
            float: left;
            width: 60%;
        }
        .right{
            width: 60%;
            height: 300px;
            border: 1px solid red;
            float: right;
        }
        table {
            border-collapse: collapse;
            border-spacing: 0;
            width: 600px;
            height: 80px;
        }
        table thead tr th{
            font-weight: bold;
            background-color: #8EB4E3;
        }
        table td,table th{
            height: 20px;
            border:1px solid #A6A6A6;
            font-size: 14px;
            text-align: center;
        }

    </style>
    <meta charset="utf-8">
    <!--script src="https://cdn.jsdelivr.net/gh/ethereum/web3.js/dist/web3.min.js"></script-->
    <script type="text/javascript" src="https://www.mobilefish.com/scripts/web3/web3.min.js"></script>
    <!-- for ecrecover -->
    <script type="text/javascript" src="https://www.mobilefish.com/scripts/ethereumjs/ethereumjs-util.js"></script>
    <script type="text/javascript" src="js/MetaMask_web3.js"></script>
    <script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
</head>
<body style="height: 100%;">
<span id="network"></span>
<span id="accounts"></span>
<p>
    当前区块GasPrice：<span id="Price_span"></span>Gwei
</p>
<a class="wrap">
    <!--空投页面数据的提交？-->
    <div>
        <h2 id="err"></h2>
        <form name="form_">
            <div class="air-drop">
                <span class="spantext">合约地址：</span><input type="text" id="from_" placeholder="格式：0x00000000......" value="0xd97b92acc0aefebc2a65cd88ed12b50e4f8f2d59" name="tokenContractAddress">

            </div>

            <input value="授权" type="button" onclick="approve_()" class="inputcont">

            <div class="air-drop">
                <span class="spantext">收币地址：</span><input type="text" id="to" name="addressList" placeholder="格式：0x0,0x1,0x2" >
                <%--<textarea id="to" rows="10" cols="80" style="margin: 0px; height: 188px; width: 574px;" name="addressList" >格式：0x0,0x1,0x2</textarea>--%>
            </div>
            <div class="air-drop">
                <span class="spantext">发送数量：</span><input type="text" id="value" name="value" >
            </div>
            <input value="空投" type="button" onclick="airDrop_()" class="inputcont">
        </form>
    </div>
    <a href="/airdropValues.jsp"><div>异值空投</div></a>
</div>
    <div class="left">
        <table id="div_1">
            <tr>
                <th>Header</th>
                <th>日期</th>
            </tr>

        </table>
    </div>

<!--<script type="text/javascript" src="/jquery"></script>-->
<script src="js/jquery.min.js"></script>
<script type="text/javascript">
    $(function(){
        $("[type=\"text\"]").blur(function () {
            $(this).val($(this).val().replace(/\s+/g,""));
        })
    });
    function gasPrice() {
        $.get("/GasPrice",function(data){


            if ( $("#GasPrice").val(data/1000000000)==""){
                gasPrice();
            }
        }).error(function(){
            console.log("没有连接节点！");
            $("[type=\"submit\"]").attr('disabled',true);
            $("h2").text("没有连接节点！").css("color","red");
            $("#GasPrice").val(1);
        });
    }
    //gasPrice();

    function dataMigration(){
        $("#div_2").css("display","block");//none
        var addr = document.getElementById("addr").value;
        var from_ = document.getElementById("from_").value;
        var to = document.getElementById("to").value;
        var value = document.getElementById("value").value;
        var gas = document.getElementById("gas").value;
        var account_one=document.getElementById("account_one").value;
        $.get("/dataMigration?from_="+from_+"&file="+to+"&value="+value+"&pwd="+addr+"&gas="+gas+"&account_one="+account_one, function(data){
            $("#div_2").css("display","none");
            if(data=="error"){
                $("#address_test").html("error");
            }else{

            }

        });
    }
    function airDrop_(){
        $(".tableshuju").css("display","block");
       /* $(".wrap").css("display","none");
        $("#div_2").css("display","block");//none*/
        var tokenContractAddress = document.form_.tokenContractAddress.value;
        var addressList = document.form_.addressList.value.split(",");
        var value = document.form_.value.value;
        var add=new Array();
        for (var adi=0;adi<addressList.length;adi++) {
            if (!isAddress(addressList[adi])){
                add.push(addressList[adi]);
            }
        }
        if (!isAddress(tokenContractAddress)){
            document.getElementById("err").innerHTML="Address错误，请查看Address的正确性";
            if (!add.length>0){
                return;
            }
        }
        if (add.length>0){
            var addr="";
            for (var adi=0;adi<add.length;adi++) {
                addr+= ""+add+" ";
            }
            document.getElementById("err").innerHTML= document.getElementById("err").innerHTML+addr;
            return;
        }
        document.getElementById("err").innerHTML="";
        airDrop(tokenContractAddress,addressList,value,"0x1d833aae");
    }
    function approve_(){
        var tokenContractAddress = document.form_.tokenContractAddress.value;
        if (!isAddress(tokenContractAddress)){
            document.getElementById("err").innerHTML="Address错误，请查看Address的正确性";
            return;
        }
        document.getElementById("err").innerHTML="";
        approve(tokenContractAddress,"0x095ea7b3");
    }
</script>
</body>
</html>