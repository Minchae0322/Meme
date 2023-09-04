<script setup lang = "js">
import axios from "axios";
import {ref} from "vue";
import { useRouter } from "vue-router";


const router = useRouter()
const isAlphaNumeric = function(password) {
  // 정규 표현식을 사용하여 영문자와 숫자로만 이루어진지 확인
  const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/;
  return regex.test(password);
}

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
      router.replace('/home');
    }
  })

  }

</script>

<template>
  <div class = "container">




      <div class="custom-container">
        <div>
          <h1>Sign In</h1>
          <el-form>
            <el-form-item label="Username">
              <el-input v-model="username" placeholder="Enter your username"></el-input>
            </el-form-item>
            <el-form-item label="Password">
              <el-input v-model="password" placeholder="Enter your password" type="password"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="login">Login</el-button>
            </el-form-item>
          </el-form>
        </div>

      </div>

  </div>




  <el-button type="primary" @click = "write()">로그인</el-button>





</template>

<style scoped>
.login-container {
  width: 40vw;
  margin: 0 auto;
  padding: 20px;
  text-align: center; /* 가로 중앙 정렬을 위해 텍스트 가운데 정렬 */

  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;

  align-items: center; /* 수직 중앙 정렬을 위해 아이템 가운데 정렬 */
}

.usernameContainer {
  display: flex; /* 가로로 배치 */
  flex-direction: row; /* 가로로 배치 */
  align-items: center; /* 수직 중앙 정렬을 위해 아이템 가운데 정렬 */
  justify-content: flex-start; /* 왼쪽 정렬을 위해 아이템을 왼쪽으로 정렬 */
}

.passwordContainer {
  display: flex; /* 가로로 배치 */
  flex-direction: row; /* 가로로 배치 */
  align-items: center; /* 수직 중앙 정렬을 위해 아이템 가운데 정렬 */
  justify-content: flex-start; /* 왼쪽 정렬을 위해 아이템을 왼쪽으로 정렬 */
}




.login-container h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #064183;
}

/* 입력 필드 및 레이블 스타일링 */


el-input {
  width: 100%; /* 가로 길이를 100%로 설정 */
  height: 80px; /* 원하는 세로 길이로 설정 */
  line-height: 20px; /* 세로 중앙 정렬을 위해 line-height 설정 */
}

label,
el-input {
  margin-right: 2px; /* 원하는 간격으로 조정 */
  margin-left: 0px; /* 원하는 간격으로 조정 */
}

.form-group label {
  font-weight: bold;
}



/* 로그인 버튼 스타일링 */
.el-button {
  width: 40%;

  border-radius: 3px;
  cursor: pointer;
  transition: background-color 0.3s;
  background-color: #040a0e;
  color: #000000;
  border: none;
}

</style>