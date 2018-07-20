<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en" style="height: 100%;">
<head>
    <meta charset="UTF-8">
    <title></title>
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
            width: 180px;
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
            width: 38%;
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
</head>
<body style="height: 100%;">
<div class="wrap">
    <!--空投页面数据的提交？-->
    <div>
        <h2></h2>
        <form name="form_" enctype="multipart/form-data">
            <div class="air-drop">
                <span class="spantext">发币地址文件：</span><input type="file" id="account_one" name="addressPath" >
            </div>
            <div class="air-drop">
                <span class="spantext">Gas价格：</span><input type="text" id="GasPrice" name="GasPrice" >
            </div>
            <div class="air-drop">
                <span class="spantext">合约地址：</span><input type="text" id="from_" value="0x98334013ce045b5b84448a5c28fe9000c76fe54b" name="tokenContractAddress">
            </div>
            <div class="air-drop">
                <span class="spantext">pwd：</span><input type="text" id="addr" value= "130171155707" name="pwd">
            </div>
            <div class="air-drop">
                <span class="spantext">收币地址文件：</span><input type="file" id="to" value="BitSTD_array.txt" name="path">
            </div>
            <!--<input type="button" value="添加" onclick="add()">-->
            <!--<input value="迁移" type="submit" onclick="dataMigration()" class="inputcont">-->
            <div class="air-drop">
                <span class="spantext">发送数量：</span><input type="text" id="value" name="value" >
            </div>
            <div class="air-drop">
                <span class="spantext">everyGas：</span><input type="text" id="everyGas" name="everyGas" value="68000" >
            </div>
            <input value="空投" type="submit" onclick="airDrop_()" class="inputcont">
        </form>
    </div>
</div>
<div class="tableshuju">
    <div class="left">
        <table >
            <tr>
                <th>Header</th>
                <th>日期</th>
            </tr>
            <tr>
                <td>12</td>
                <td>Data</td>
            </tr>
        </table>
    </div>
    <div class="right">
        <center>
            <div id="div_2" style="display:none;position: absolute;">
                <img src='img/01110e57bbc1400000012e7ec358d8.gif'>
            </div>
        </center>
    </div>
</div>

<!--<script type="text/javascript" src="/jquery"></script>-->
<script src="js/jquery.min.js"></script>
<script type="text/javascript">
    $(function(){
           function gasPrice() {
               $.get("/GasPrice",function(data){


                   if ( $("#GasPrice").val(data)==""){
                       gasPrice();
                   }
               }).error(function(){
                   console.log("没有连接节点！");
                   $("[type=\"submit\"]").attr('disabled',true);
                   $("h2").text("没有连接节点！").css("color","red");
                   $("#GasPrice").val(1);
               });
           }
        gasPrice();

    });
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
                $("#div_1").append("<tr><td>编号</td><td>Hash</td><td>操作</td></tr>");
                for(i=0;i<data.length;i++){
                    $("#div_1").append("<tr><td>"+i+"</td><td>"+data[i]+"</td><td> <a href='https://rinkeby.etherscan.io/tx/"+data[i]+"'  target='iframe_a'>查询区块</a></td></tr>");
                }
            }

        });
    }
    function airDrop_(){
        $(".tableshuju").css("display","block");
        $(".wrap").css("display","none");
        $("#div_2").css("display","block");//none
        var fm = new FormData();
        fm.append('pwd', document.form_.pwd.value);
        fm.append('addressPath', document.form_.addressPath.files[0]);
        fm.append('tokenContractAddress', document.form_.tokenContractAddress.value);
        fm.append('path', document.form_.path.files[0]);
        fm.append('value', document.form_.value.value);
        fm.append('GasPrice', document.form_.GasPrice.value);
        fm.append('everyGas', document.form_.everyGas.value);
        $.ajax({
            url: 'http://192.168.0.173:8090/airDrop',
            type: "POST",
            async:false,//数据请求同步和异步true为异步，false为同步
            data: fm,
            contentType: false, //禁止设置请求类型
            processData: false, //禁止jquery对DAta数据的处理,默认会处理
            //禁止的原因是,FormData已经帮我们做了处理
           /* data: {
                addressPath: account_one,//发币地址文件
                tokenContractAddress: from_,//合约地址
                path:to,//收币地址文件
                value:value,//发送数量
                GasPrice:gas,//Gas价格
                pwd:addr,//pwd
                everyGas:everyGas
            },*/
            success: function(data) {
                console.log(1)
            },
        });
    }
</script>
</body>
</html>