<script setup lang = "js">
import axios from "axios";
import {ref} from "vue";


let username = ref("")
let password = ref("")
const write = function () {
  axios.post("http://localhost:8080/auth/login",{
    username: username.value,
    password: password.value
  }
  )}
</script>

<template>

  <body>
  <div class = "system">
    <el-text  class="mx-1" type="primary">아이디</el-text>
    <el-input v-model = "username" placeholder="ddd"/>


    <el-text  class="mx-1" type="primary">비밀번호</el-text>
    <el-input v-model = "password" placeholder="ddd"/>
  </div>
  <el-button type="primary" @click = "write()">글 작성 완료</el-button>
  </body>

</template>

<style scoped>
#login {
  margin-top: 150px;
}
</style>