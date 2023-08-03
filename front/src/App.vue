<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'

</script>

<template>
  <el-header>
    <nav>
      <RouterLink to="/">Home</RouterLink>
      <RouterLink to="/write">글 작성</RouterLink>
    </nav>
  </el-header>
 <p>

 </p>
  <RouterView />
</template>

<style>

</style>