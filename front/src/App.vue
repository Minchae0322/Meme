<template>
  <head>
    <title>MEME 게시판</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
  </head>
  <body>
  <header>
    <div class="header_menu">
      <div class="menu_logo">

        <a href="/home"> MEME </a>
      </div>

      <ul class="menu_nav">
        <div>
          <RouterLink to="/home">인기글</RouterLink>
        </div>
        <div>
          <RouterLink to="/write">글 작성</RouterLink>
        </div>
        <div>
          <RouterLink to="/posts">글 목록</RouterLink>
        </div>


      </ul>
      <div class="menu_link">
        <div class="logout-button">
          <el-button :id="isLogin === '로그인' ? 'logout-large' : 'logout'" @click="handleLogout">{{ isLogin }}</el-button>
          <el-button id="signup" @click="goSignup" v-if="isLogin === '로그인'">회원가입</el-button>
        </div>
      </div>
    </div>
  </header>
  <hr />
  <main>
    <RouterView />
  </main>
  </body>
</template>



<script setup lang="ts">
import {computed, ref} from "vue";
import axios from "axios";

import { useRouter} from "vue-router";
import { onMounted , watch} from "vue";
import store from "@/stores/store";
//import { useStore } from 'vuex'; // Import useStore to access the store
//const store = useStore(); // Access the Vuex store

const router = useRouter()
const isLogin = computed(() =>store.state.isLogin);
const loginRouter = ref('/logout');
onMounted(() =>
{
  checkLogin()
})
function checkLogin(){
  if (!localStorage.getItem("accessToken")) {
    console.log(localStorage.getItem("accessToken"))
    store.dispatch('setLoginStatus', '로그인'); // Dispatch the action to change isLogin
      loginRouter.value = "/login"
    console.log("로그인 필요")
  } else {
    store.dispatch('setLoginStatus', '로그아웃'); // Dispatch the action to change isLogin
  }
  return 0
}



const logout = function () {
  axios.post("http://13.125.165.102/api/auth/logout", {} ,{headers: {
        'Authorization': localStorage.getItem("accessToken")
  }}
  ).then((response) => {
    if (response.status === 200) {
      localStorage.removeItem("accessToken");
      console.log('access 토큰 :', "로그아웃");
    }
  })
  localStorage.removeItem("accessToken");
}

function handleLogout() {
  if (isLogin.value === "로그인") {
    router.push('/login'); // 로그인 상태가 아니면 클릭하면 /login으로 이동
  } else {
    logout(); // 로그아웃 상태이면 로그아웃 함수 호출
    router.go(0)
  }

}


function goSignup() {

  router.push('/signup'); // 로그인 상태가 아니면 클릭하면 /login으로 이동

}


</script>



  <style>
  .header_menu {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 7%;
    margin: 20px 0;
    padding: 6px 20px;
  }

  header {
    margin: 0;
  }

  hr {
    margin-top: 10px;
  }

  body {
    background: white;
    margin: 0 auto;
    display: flow;
    min-width: 90vw;
    width: 90vw;
  }

  .menu_nav {
    display: flex;
    font-size: 17px;
    width: 60%;
    margin: 20px 0;
    list-style: none;
  }

  .menu_nav div {
    margin: 0px 20px;
    background: white;
  }

  .menu_logo {
    justify-content: start;
    font-size: 25px;
  }

  body {
    background: white;
    margin: 0 auto;
    min-width: 90vw;
    width: 90vw;
    padding: 0 10px; /* Add some padding to the body */
  }

  /* Add media query for mobile devices */
  @media (max-width: 768px) {
    .header_menu {
      flex-direction: row;


    }
    .menu_logo {


      justify-content: start;
    }
    .menu_nav {
      justify-content: start;
      width: 100%;
      display: flex;
      font-size: 21px;
      text-align: center;
    }
  }

  * {
    font-family: 'Noto Sans KR', sans-serif;
    font-weight: 500;
  }
  </style>