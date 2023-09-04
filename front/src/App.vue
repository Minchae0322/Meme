<script setup lang="js">
import { RouterLink, RouterView } from 'vue-router'
const login = function () {
};
</script>

<template>
  <div class="container">

    <el-header>
      <label>최신 MEME</label>
      <nav>
        <RouterLink to="/home">Home</RouterLink>
        <RouterLink to="/write">글 작성</RouterLink>
        <RouterLink to="/login">로그인</RouterLink>
        <RouterLink to="/posts">글 목록</RouterLink>
      </nav>
    </el-header>


    <main>
      <div class="router">
        <RouterView />
      </div>
    </main>
  </div>
</template>

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