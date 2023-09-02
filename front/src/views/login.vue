<script setup lang = "js">
import axios from "axios";
import {ref} from "vue";


let username = ref("")
let password = ref("")
axios.defaults.withCredentials = true; // withCredentials 전역 설정
const write = function () {
  axios.post("http://localhost:8080/auth/login", {
        username: username.value,
        password: password.value
      }
      , {
        headers: {}
      }
  ).then((response) => {
    if (response.status === 200) {
      let accessToken = response.headers['authorization'];
      if (accessToken) {
        localStorage.setItem('accessToken', accessToken);
      }
      console.log('access 토큰 :', accessToken);
    }
  })

  }

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