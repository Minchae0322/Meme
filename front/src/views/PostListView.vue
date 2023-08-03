<script setup lang="ts">


import {ref} from "vue";
import axios from 'axios'

const posts = [
  {id: 1, title:"제목1",content:"내용 1"}
]

</script>


<template>
 <el-result>
   <li v-for="post in posts" key="postId">
     <div>
       {{post.title}}

     </div>
     <div>
        {{post.content}}
     </div>

   </li>


 </el-result>
</template>


<style scoped>
li {
  margin-bottom: 1rem;
}


</style>