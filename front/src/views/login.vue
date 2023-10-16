<script setup lang="ts">
import axios from "axios";
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import store from "@/stores/store";
//import { useStore } from 'vuex';

//const store = useStore();
const router = useRouter();
const apiBaseUrl = "http://localhost:8080";
const isAlphaNumeric = function(password: string): boolean {
  // 정규 표현식을 사용하여 영문자와 숫자로만 이루어진지 확인
  const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/;
  return regex.test(password);
}

const username = ref<string>("");
const password = ref<string>("");

axios.defaults.withCredentials = true; // withCredentials 전역 설정

onMounted( () => {
  checkLogin();
});

const checkLogin = async function (): Promise<void> {
  const accessToken = localStorage.getItem("accessToken");
  if (accessToken) {
    try {
      const response = await axios.get(`${apiBaseUrl}/auth/isValidToken`, {
        headers: {
          'Authorization': accessToken
        }
      });
      if (response.status === 200) {
        router.replace({ name: "home" });
      }
    } catch (error) {
      console.error("에러 발생:", error);
    }
  }
};

const goSignup = function (): void {
  router.replace({ name: "signup" });
};

const write = async function (): Promise<void> {
  try {
    const response = await axios.post(`${apiBaseUrl}/auth/login`, {
      username: username.value,
      password: password.value
    }, {
      headers: {}
    });

    if (response.status === 200) {
      const accessToken = response.headers['authorization'];
      if (accessToken) {
        localStorage.setItem('accessToken', accessToken);
      }
      console.log('access 토큰 :', accessToken);
      store.dispatch('setLoginStatus', '로그아웃'); // Dispatch the action to change isLogin
      router.replace("/home");
    }
  } catch (error) {
    console.error('에러 발생:', error);
    alert("아이디 또는 비밀번호가 틀렸습니다.");
  }
}
</script>


<template>

  <div class="login form">
    <header>Login</header>
    <form action="#">
      <input id = "inputUsername" v-model="username" type="text" placeholder="아이디를 입력해주세요">
      <input type="password" @keyup.enter="write" v-model="password" placeholder="비밀번호를 입력해주세요">
<!--      <a href="#">Forgot password?</a>-->
      <input type="button" @click = "write" class="button" value="Login">
    </form>
    <div class="signup">
        <span class="signup">Don't have an account?
         <label for="check" @click="goSignup">Signup</label>
        </span>
    </div>
  </div>

</template>

<style scoped>
.login{
  width: 35%;
  align-items: center;
  padding-top: 50px;
  margin: 0 auto; /* Add this line to horizontally center the element */
}
.container{
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%,-50%);
  max-width: 430px;
  width: 100%;
  background: #fff;
  border-radius: 7px;
  box-shadow: 0 5px 10px rgba(0,0,0,0.3);
}
.container .registration{
  display: none;
}
#check:checked ~ .registration{
  display: block;
}
#check:checked ~ .login{
  display: none;
}
#check{
  display: none;
}
.container .form{
  padding: 2rem;
}
.form header{
  font-size: 2rem;
  font-weight: 500;
  text-align: center;
  margin-bottom: 1.5rem;
}
 .form input{
   height: 60px;
   width: 100%;
   padding: 0 15px;
   font-size: 17px;
   margin-bottom: 1.3rem;
   border: 1px solid #ddd;
   border-radius: 6px;
   outline: none;
 }
 .form input:focus{
   box-shadow: 0 1px 0 rgba(0,0,0,0.2);
 }
.form a{
  font-size: 16px;
  color: #009579;
  text-decoration: none;
}
.form a:hover{
  text-decoration: underline;
}
.form input.button{
  color: #fff;
  background: #009579;
  font-size: 1.2rem;
  font-weight: 500;
  letter-spacing: 1px;
  margin-top: 1.7rem;
  cursor: pointer;
  transition: 0.4s;
}
.form input.button:hover{
  background: #006653;
}
.signup{
  font-size: 17px;
  text-align: center;
}
.signup label{
  color: #009579;
  cursor: pointer;
}

@media (max-width: 900px) {
   .login {
     width: 70%;
   }
}

</style>