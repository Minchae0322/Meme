
  <template>
    <div class="main">


    <header>


    <div class="nav_bar">
      <div class="nav_logo">
        <i></i>
        <a href = ""> MEME </a>
      </div>

      <ul class="nav">
        <nav>
        <RouterLink to="/home">Home</RouterLink>
        <RouterLink to="/write">글 작성</RouterLink>
        <RouterLink to="/posts">글 목록</RouterLink>
        </nav>
      </ul>

      <div class="nav_link">

        <div class="logout-button">
          <el-button :id="isLogin === '로그인' ? 'logout-large' : 'logout'" @click="handleLogout">{{ isLogin }}</el-button>
          <el-button id="signup" @click="goSignup" v-if="isLogin === '로그인'">회원가입</el-button>
        </div>
      </div>


    </div>
    </header>


    <main>
      <child-component @login-success="onLoginSuccess"></child-component>
      <RouterView />
    </main>


    </div>
  </template>



<script setup lang="js">
import {ref} from "vue";
import axios from "axios";

import { useRouter} from "vue-router";
import { onMounted , watch} from "vue";


const router = useRouter()

const isLogin = ref("로그아웃");
const loginRouter = ref('/logout');

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

checkLogin();

</script>



<style>
.main {
  display: flow;
}
.nav_bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 5vw;

  padding: 6px 12px;
  width: 85vw;
}

header {
  margin: 0;

}


body {
  background: white;
  margin: 0;
}

.nav {
  display: flex;
  justify-content: left;
  font-size: 20px;
  padding-left: 100px;
  width: 50%;
}

.nav_logo {
  font-size: 30px;
}

.nav_link {
  display: flex;
}

.nav_link  {
  width: 35%;
  font-size: 12px;
}

</style>