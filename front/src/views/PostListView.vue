<script setup lang="ts">


import {ref} from "vue";
import axios from 'axios'

const posts = ref([])

axios.get("http://localhost:8080/board/posts/list?page=1&size=5")
.then((response) => {
  response.data.forEach((r: any) => {
    posts.value.push(r)
  })
})
</script>


<template>
 <ul>
   <li v-for="post in posts" key="postId">
     <div>
       {{post.title}}

     </div>
     <div>
        {{post.content}}
       {{post.author}}
     </div>

   </li>


 </ul>
</template>


<style scoped>
li {
  margin-bottom: 1rem;
}


</style>