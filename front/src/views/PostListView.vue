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


const moveToRead = function () {

};


</script>


<template>
  <div class="post-list">
    <li v-for="post in posts" :key="post.postId" class="post-item">
      <div class="post-info">
        <router-link class="title" :to="{name: 'read', params: {postId : post.postId} }">{{ post.title }}</router-link>
        <div class="author">{{ post.author }}</div>
      </div>
    </li>
  </div>
</template>


<style scoped>
.post-list {
  list-style: none;
  display: flex;
  flex-wrap: wrap; /* Wrap items to the next line if they don't fit */
  gap: 20px; /* Add spacing between items */
}

.post-item {
  width: calc(100%); /* Adjust the width as needed */
}

.post-info {
  display: flex;
  flex-direction: row; /* Display title and author horizontally */
  justify-content: space-between; /* Add space between title and author */
  align-items: center; /* Center-align vertically within each item */
}

.title {
  font-weight: bold;
  text-align: center; /* Center-align the title horizontally */
  width: 80%; /* Set title width to 80% */
}

.author {
  color: gray;

  width: 20%; /* Set author width to 20% */
}


</style>