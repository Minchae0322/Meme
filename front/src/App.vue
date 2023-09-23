
  <template>
    <head><title>MEME 게시판</title></head>
    <body>
    <header>


    <div class="nav_bar">
      <div class="nav_logo">
        <i></i>
        <a href = "/home"> MEME </a>
      </div>

      <ul class="nav">
        <div>
        <RouterLink to="/home">Home</RouterLink>
        </div>
        <div>
          <RouterLink to="/write">글 작성</RouterLink>
        </div>
        <div>
            <RouterLink to="/posts">글 목록</RouterLink>
        </div>


      </ul>

      <div class="nav_link">

        <div class="logout-button">
          <el-button :id="isLogin === '로그인' ? 'logout-large' : 'logout'" @click="handleLogout">{{ isLogin }}</el-button>
          <el-button id="signup" @click="goSignup" v-if="isLogin === '로그인'">회원가입</el-button>
        </div>
      </div>


    </div>
    </header>
    <hr/>

    <main>
      <child-component @login-success="onLoginSuccess"></child-component>
      <RouterView />
    </main>

    </body>
  </template>



<script setup lang="js">
import {ref} from "vue";
import axios from "axios";

import { useRouter} from "vue-router";
import { onMounted , watch} from "vue";


const router = useRouter()

export const isLogin = ref("로그아웃");
const loginRouter = ref('/logout');
onMounted(() =>
{
  checkLogin()
})
function checkLogin(){
  if (!localStorage.getItem("accessToken")) {
    console.log(localStorage.getItem("accessToken"))
      isLogin.value = "로그인";
      loginRouter.value = "/login"
    console.log("로그인 필요")
  } else {
    isLogin.value = "로그아웃";
  }
  return 0
}



const logout = function () {
  axios.post("http://localhost:8080/auth/logout", {} ,{headers: {
        'Authorization': localStorage.getItem("accessToken")
  }}
  ).then((response) => {
    if (response.status === 200) {
      localStorage.removeItem("accessToken");
      console.log('access 토큰 :', "로그아웃");
    }
  })

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
  .nav_bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 7%;
    margin-left: 100px;
    padding: 6px 20px;
    margin-top: 20px;
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

  .nav {
    display: flex;
    font-size: 20px;
    width: 60%;
    margin: 20px 0;
    list-style: none;
  }

  .nav div {
    margin: 9px 20px;
    background: white;
  }

  .nav_logo {
    font-size: 30px;
  }



* {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;
}



  </style>