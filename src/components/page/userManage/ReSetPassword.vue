<template>
    
    <div class="bj">
      <a href="/#/login">
        <img src="../../../assets/img/back.jpg" style="width:3%" title="返回登录" alt="加载错误">
      </a>
      <div class="reSet">
      <h1 class="title">重置密码</h1>
        <smsChangePassword/>
    </div>
    </div>
</template>
<script>
import smsChangePassword from '../myComponents/sendSmsChangePassword'
export default {
  components:{
    smsChangePassword
  }
  
}
</script>
<style  scoped>
  /* .bj{
     background: rgba(151, 151, 151, 0.473);
     height: 10000px;
  } */
  .reSet{
    margin-left: 15%;
    width: 70%;
    margin-top: 50px;
  }
  .title{
    text-align: center;
    font-family:cursive
  }
</style>