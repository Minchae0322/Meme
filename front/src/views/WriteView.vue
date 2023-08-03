
<script setup lang = "ts">
import {ref} from "vue";
import axios from 'axios'
import router from "@/router";

const title = ref("")
const content = ref("")


const write = function () {
  axios.post("http://localhost:8080/posts", {
    title: title.value,
    content: content.value
  })
      .then(() => {
        router.replace({
          name: "home"
        })
})}
</script>

<template>
  <header>

  </header>


    <body>
  <div>

    <div>
      <el-text  class="mx-1" type="primary">제목</el-text>
      <el-input v-model = "title" placeholder="제목을 입력하세여"></el-input>
    </div>

    <div>
      <el-input
          v-model="content"
          :cols = "30"
          :rows="15"
          type="textarea"
          placeholder="Please input"
      />
    </div>
    <el-button type="primary" @click = "write()">글 작성 완료</el-button>

  </div>
    </body>

</template>


<style>

</style>