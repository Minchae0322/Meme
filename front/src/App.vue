<template>
  <head>
    <title>MEME 게시판</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
  </head>
  <body>
  <header>
    <div class="header_menu">
      <div class="menu_small">

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

      </div>
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
    height: 7%;
    width: 90vw;
    align-items: center;

  }

  .menu_small {
    display: flex;
    height: 7%;
    width: 77%;
    margin-left: 20px;
    align-items: center;
  }

  el-button {
    width: 10px;
  }

  .menu_logo {
    justify-content: start;
    margin: 20px 10px;
    font-size: 25px;
  }

  .menu_link {
    display: flex;
    justify-content: flex-end; /* 요소들을 오른쪽 끝으로 정렬 */
    align-items: center; /* 세로 방향 중앙 정렬 */
    width: 25%;
  }




  .menu_nav {
    display: flex;
    font-size: 17px;
    justify-content: center;
    width: 60%;
    margin: 20px 0;
    list-style: none;
  }

  .menu_nav div {
    margin: 0px 20px;
    background: white;
  }


  body {
    background: white;
    min-width: 90vw;
    width: 90vw;
    display: flow;
    padding: 0 10px; /* Add some padding to the body */
  }

  /* Add media query for mobile devices */
  @media (max-width: 768px) {
    .header_menu {
      flex-direction: column-reverse; /* 세로로 정렬하고, menu_link를 아래에 배치 */
    }

    .logout-button {
      display: flex;
    }

    .menu_logo {
      justify-content: start;
      align-items: start;
      font-size: 20px;
    }

    .menu_nav {
      justify-content: start;
      width: 100%;
      display: flex;
      font-size: 14px;
      text-align: center;
    }

    .menu_nav div {
      margin: 0px 14px;
    }

    .menu_small {
      flex-direction: row;
      font-size: 13px;
      width: 83%;
    }

    .logout-button {
      align-items: center;
      justify-content: center;
    }
  }

  * {
    font-family: 'Noto Sans KR', sans-serif;
    font-weight: 500;
  }


  </style>