<template>

  <body>
  <header>
    <div class="header_menu">
      <div class="menu_small">

        <div class="menu_logo">

        <a href="/home"> MEME </a>
      </div>

      <ul class="menu_nav">
        <li>
          <RouterLink to="/home">인기글</RouterLink>
        </li>
        <li>
          <RouterLink to="/write">글 작성</RouterLink>
        </li>
        <li>
          <RouterLink to="/posts">글 목록</RouterLink>
        </li>
        <li>
          <RouterLink to="/posts">공지사항</RouterLink>
        </li>
      </ul>

      </div>
      <div class="menu_link">
        <div class="logout-button">
          <el-button :id="isLogin === '로그인' ? 'logout-large' : 'logout'" @click="handleLogout">{{ isLogin }}</el-button>
          <el-button id="signup" @click="goSignup" v-if="isLogin === '로그인'">회원가입</el-button>
          <el-button v-if="isLogin === '로그아웃'" @click="goUserInfo">내정보</el-button>
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
const apiBaseUrl = "http://13.125.165.102/api";
const router = useRouter()
const isLogin = computed(() => store.state.isLogin);
const loginRouter = ref('/logout');
onMounted(() =>
{
  checkLogin()
})
function checkLogin(){
  const accessToken = localStorage.getItem("accessToken");
  if (accessToken) {
      axios.get(`${apiBaseUrl}/auth/isValidToken`, {
        headers: {
          'Authorization': accessToken
        }
      }).then(response => {
        if(response.status === 200) {
          store.dispatch('setLoginStatus', '로그아웃'); // Dispatch the action to change isLogin
          loginRouter.value = "/logout"
        } else {
          store.dispatch('setLoginStatus', '로그인'); // Dispatch the action to change isLogin
          loginRouter.value = "/login"
        }
      }).catch(error => {
        store.dispatch('setLoginStatus', '로그인'); // Dispatch the action to change isLogin
        loginRouter.value = "/login"
      });
  } else {
    store.dispatch('setLoginStatus', '로그인'); // Dispatch the action to change isLogin
    loginRouter.value = "/login"
    console.log("로그인 필요")
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

function goUserInfo() {
  router.replace('/userInfo')
}


</script>



  <style>

  body {
    background: white;
    min-width: 95vw;
    display: flow;
    justify-content: center;
    align-items: center;
    margin-top: 10px;

  }

  .header_menu {
    display: flex;
    width: 100%;
    align-items: center;
  }

  hr {
    margin-top: 10px;
  }

  .menu_small {
    display: flex;
    width: 77%;
    margin-left: 50px;
  }


  .menu_logo {
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 25px;
  }

  .menu_nav {
    display: flex;
    font-size: 14px;
    justify-content: center;
    align-items: center;
    width: 60%;
    margin: 20px 0;
    list-style: none;
  }

  .menu_nav li {
    margin: 0px 20px;
    background: white;
  }

  .menu_link {
    display: flex;
    justify-content: flex-end; /* 요소들을 오른쪽 끝으로 정렬 */
    align-items: center; /* 세로 방향 중앙 정렬 */
    width: 23%;
  }








  /* Add media query for mobile devices */
  @media (max-width: 768px) {
    .header_menu {
      flex-direction: column-reverse; /* 세로로 정렬하고, menu_link를 아래에 배치 */
    }

    .logout-button {
      display: flex;
    }

    body {
      min-width: 90vw;
      padding-right: 7px;
    }

    .menu_logo {
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 18px;
    }

    .menu_nav {
      justify-content: start;
      width: 100%;
      display: flex;
      font-size: 9px;
    }

    .menu_nav li {
      margin: 0px 9px;
    }

    .menu_small {
      flex-direction: row;
      margin-left: 0px;
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