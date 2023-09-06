<script setup lang = "js">
import axios from "axios";
import { ref, onMounted, onUpdated} from "vue";


import { useRouter } from "vue-router";


const router = useRouter()
const isAlphaNumeric = function(password) {
  // 정규 표현식을 사용하여 영문자와 숫자로만 이루어진지 확인
  const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/;
  return regex.test(password);
}

let username = ref("")
let password = ref("")
let isLogin = ref(false)
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

        router.replace("/home")
        isLogin.value = true;

      }
    }).catch(function (error) {
      console.error('에러 발생:', error);
      alert("아이디 또는 비밀번호가 틀렸습니다.")
    })



  }

</script>

<template>
  <div class = "container">




      <div class="custom-container">
        <h1>Sign In</h1>
        <div class="usernameContainer">
          <el-input id = "inputUsername" v-model="username" placeholder="아이디를 입력해주세요"></el-input>
        </div>
        <div class = "passwordContainer">
          <el-input v-model="password" placeholder="비밀번호를 입력해주세요" type="password"></el-input>
        </div>

      </div>

  </div>




  <el-button type="primary" @click = "write()">로그인</el-button>





</template>

<style scoped>/*
.custom-container {
  width: 40vw;
  margin: 0 auto; !* Center-align horizontally *!
  padding: 20px;
  text-align: center;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center; !* Center-align vertically *!
  justify-content: center; !* Center-align horizontally *!
}*/
.usernameContainer {
  align-items: center;
  display: flex;
  flex-direction: row;
  width: 90%;

}


.id {
  aspect-ratio: 3 / 1;
  font-size: 12px;
  margin-right: 20px; /* Add margin between label and input */
}















</style>