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
  color: aliceblue;
}

a {
  color: rgba(252, 132, 132, 0.45);
}

.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  justify-content: center; /* 가로 가운데 정렬을 위해 아이템 가운데 정렬 */
}

nav {
  width: 100%;
  text-align: right;
  z-index: 100;
}

el-header {
  width: 100%;
  text-align: center;
  padding: 10px 0;
}

main {
  max-width: 100vw;
  flex-grow: 1;

  overflow-y: auto; /* 내용이 화면을 벗어날 경우 스크롤 표시 */
}

.router {
  max-width: 100vw;
  margin: 0 auto;
  padding: 20px;
}
</style>