<template>
  <div class="post-list">
    <ul>
      <li v-for="post in posts" :key="post.postId" class="post-item">
        <div class="post-info">
          <router-link class="title" :to="{ name: 'read', params: { postId: post.postId } }">{{ post.title }}</router-link>
          <div class="author">{{ post.author }}</div>
        </div>
      </li>
    </ul>
    </div>

    <!-- Pagination Controls -->
    <div class="page">
      <ul class="pagination model">
        <li><a href="#" class="first" @click="loadPage(1)">처음 페이지</a></li>
        <li><a href="#" class="arrow left"></a> </li>
        <li><a href="#" class="active num" @click="loadPage(1)">1</a> </li>
        <li><a href="#" class="num" @click="loadPage(2)">2</a> </li>
        <li><a href="#" class="num" @click="loadPage(3)">3</a> </li>
        <li><a href="#" class="num" @click="loadPage(4)">4</a> </li>
        <li><a href="#" class="num" @click="loadPage(5)">5</a> </li>
        <li><a href="#" class="arrow right"></a> </li>
        <li><a href="#" class="last" @click="loadPage(pageRange.length)">끝 페이지</a></li>


      </ul>
  </div>
</template>

<script setup lang="ts">
import axios from 'axios';
import { ref, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';

const posts = ref([]);
const page = ref(1); // Initial page number
const pageSize = 10; // Number of items per page

const totalPosts = ref(0);

const pageRange = computed(() => {
  const totalPages = Math.ceil(totalPosts.value / pageSize);
  const range = [];
  for (let i = 1; i <= totalPages; i++) {
    range.push(i);
  }
  return range;
});

const loadPage = async (pageNumber) => {
  if (pageNumber <= 0) return;
  const route = useRoute();
  try {
    const response = await axios.get(`http://localhost:8080/board/posts/list?page=${pageNumber}&size=5`);
    posts.value = response.data;
    page.value = pageNumber;
    route.query.page = pageNumber;
  } catch (error) {
    console.error('Error loading page:', error);
  }
};

// Load the initial page when the component is mounted
onMounted(() => {
  // Check if the initial page number is provided in the route query
  const route = useRoute();
  const initialPage = parseInt(route.query.page);
  if (!isNaN(initialPage) && initialPage > 0) {
    loadPage(initialPage);
  } else {
    loadPage(1); // Load the first page by default
  }
});
</script>

<style scoped>
.page{
  text-align: center;
  width: 40%;
}

.pagination {
  list-style: none;
  display: inline-block;
  padding: 0;
  margin-top: 20px;
}

.pagination li {
  display: inline;
  text-align: center;
}

.pagination a{
  float: left;
  display: block;
  font-size: 14px;
  text-decoration: none;
  padding: 5px 12px;
  color: #222222;
  line-height: 1.5;
}

/* Your CSS styles for the component */
</style>