<script setup lang="ts">


import {ref} from "vue";
import axios from 'axios'
import router from "@/router";

const posts = ref([]);

axios.get("http://localhost:8080/board/posts/498").then((response) => {
  posts.value.push(response.data)
})


</script>


<template>

  <body>
  <ul>
    <li v-for="post in posts" key="postId">
      <div>
        {{post.title}}

      </div>
      <div>
        {{post.content}}
      </div>

    </li>

  </ul>
  </body>
</template>


<style scoped>
li {
  margin-bottom: 1rem;
}


</style>