<script setup lang="js">
import { RouterLink, RouterView } from 'vue-router'

</script>

<template>

  <el-header>

    <label>최신 MEME</label>
    <nav>
      <RouterLink to="/">Home</RouterLink>
      <RouterLink to="/write">글 작성</RouterLink>
      <RouterLink to="/login">로그인</RouterLink>
      <RouterLink to="/posts">글 목록</RouterLink>

    </nav>
  </el-header>

  <RouterView />
</template>

<style>
 * {
   color: aliceblue;
   margin: 15px;
 }

 a {
   color: antiquewhite;
 }
</style>