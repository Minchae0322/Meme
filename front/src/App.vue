
  <template>
    <div class="container">
      <el-header>
        <label>최신 MEME</label>
        <div>
          <nav>
            <RouterLink to="/home">Home</RouterLink>
            <RouterLink to="/write">글 작성</RouterLink>
            <RouterLink to="/posts">글 목록</RouterLink>
          </nav>
        </div>
        <div class="logout-button">
          <el-button id="logout" @click="handleLogout">{{ isLogin }}</el-button>
        </div>
      </el-header>
      <child-component @login-success="onLoginSuccess"></child-component>
      <RouterView />
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

checkLogin();

</script>



<style>
* {
  background-color: white; /* 배경색을 흰색으로 설정 */
}

a {
  color: rgba(232, 63, 63, 0.45);
}

label {
  color: #05203b;
}

.logout-button {
  position: absolute;
  top: 10px;
  right: 10px;
}


.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 80vw;
  justify-content: flex-end;
}

nav {
  width: 100%;
  text-align: right;
  z-index: 100;
}

el-header {
  width: 100vw;
  text-align: center;
  padding: 10px 0;
}

main {
  max-width: 100vw;
  display: flex;
  justify-content: flex-end;
  margin-right: 10px;
}

.router {
  max-width: 100vw;
  margin: 0 auto;
  padding: 20px;
}
</style>