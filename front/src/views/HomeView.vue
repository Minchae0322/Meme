<template>
  <div class="post-list">
    <ul>
      <li v-for="post in posts" :key="post.postId" class="post-item">
        <div class="post-info">
          <router-link class="titleContainer" :to="{ name: 'read', params: { postId: post.postId } }">
            <div class="postTypeContainer">
              <div>[&nbsp </div>
              <div class="type">HOT</div>
              <div> &nbsp]</div>
            </div>

            <div class="title">{{ truncateTitle(post.title) }}</div>
            <div class="commentContainer">
              <font-awesome-icon class="commentIcon" icon="fa-regular fa-comment" />
              <div class="comment">{{ post.comments.length }}</div>


            </div>
          </router-link>
          <div class = "infoContainer">
            <div class="authorContainer">
              <div class="authorTitle">작성자 : </div>
              <div class="author">{{post.author}}</div>
            </div>
            <div class="viewAndUp">



              <div class="viewContainer">
                <font-awesome-icon class="viewIcon" icon="fa-solid fa-eye" />
                <div class="view">{{post.views}}</div>


              </div>


              <div class="recommendationContainer">
                <font-awesome-icon class="recommendationIcon" icon="fa-regular fa-thumbs-up" />
                <div class="recommendation">{{post.recommendation}}</div>


              </div>
            </div>
          </div>
        </div>

      </li>
    </ul>
  </div>

  <!-- Pagination Controls -->
  <div class="page">
    <ul class="pagination model">
      <li><a href="#" class="first" @click="loadPage(1)">처음 페이지</a></li>
      <li><a href="#" class="arrow left">이전</a></li>
      <li><a href="#" class="active num" @click="loadPage(1)">1</a> </li>
      <li><a href="#" class="num" @click="loadPage(2)">2</a> </li>
      <li><a href="#" class="num" @click="loadPage(3)">3</a> </li>
      <li><a href="#" class="num" @click="loadPage(4)">4</a> </li>
      <li><a href="#" class="num" @click="loadPage(5)">5</a> </li>
      <li><a href="#" class="arrow right">다음</a></li>
      <li><a href="#" class="last" @click="loadPage(pageRange.length)">끝 페이지</a></li>


    </ul>
  </div>
</template>


<script setup lang="js">
import axios from 'axios';
import { ref, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {checkLogin} from "@/global/globalFunction";

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

const commentSize = ref("")

const router = useRoute();



const truncateTitle = (title) => {
  if (title.length > 30) {
    return title.slice(0, 37) + '...';
  }
  return title;
};
const getCommentSize = function (post) {
  commentSize.value = post.value.comments.length
  return commentSize
}
const loadPage = async (pageNumber) => {
  if (pageNumber <= 0) return;
  const route = useRoute();
  try {
    const response = await axios.get(`http://localhost:8080/board/posts/hotList?page=${pageNumber}&size=10`);
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
/* Your existing styles */
.post-info {
  display: flex;
  margin: 30px 10px;
}


.titleContainer {
  width: 75%;
  align-items: center;
  padding-left: 150px;
}

.commentContainer {
  display: flex;
  color: #333333;
  font-size: 11px;
  align-items: center;

}

.comment {
  margin: 0 5px;

}

.commentIcon {
  margin-left: 0;
}
.title {
  margin: 0 20px;
  padding-left: 50px;
  font-size: 20px;

  white-space: nowrap; /* Prevent line breaks */
  overflow: hidden; /* Hide overflowing text */
  text-overflow: ellipsis; /* Display ellipsis for overflowed text */
}
.author {

  width: 15%;
  margin-left: 5px;
  font-size: 15px;
  color: #157e7e;
}

.postTypeContainer {
  display: flex;
  font-size: 15px;
  color: #da6500;
}
.page {
  text-align: center;
  width: 100%; /* Use full width for mobile devices */
  margin-top: 20px;
}

.pagination {
  list-style: none;
  display: flex; /* Use flexbox for better alignment */
  justify-content: center; /* Center the pagination controls */
  padding: 0;
  margin: 0;
}

.viewContainer {
  display: flex;
  color: rgba(51, 51, 51, 0.58);
  font-size: 14px;
  align-items: center;
  margin-left: 5px;


}

.recommendationContainer {
  display: flex;
  color: rgba(51, 51, 51, 0.58);
  font-size: 14px;
  align-items: center;
  margin-left: 10px


}

.recommendation {
  margin-left: 5px;
}
.view {
  margin: 0 5px;

}

.pagination li {
  margin: 0 5px; /* Add some space between pagination items */
}


.authorContainer {
  display: flex;
  color: #333333;
  margin-bottom: 10px;
  font-size: 12px;
  align-items: center;
  margin-left: auto;

}

.pagination a {
  display: block;
  font-size: 14px;
  text-decoration: none;
  padding: 5px 12px;
  color: #222222;
  line-height: 1.5;
  border: 1px solid #ccc; /* Add a border for better visibility */
  border-radius: 4px; /* Round the corners */
  transition: background-color 0.3s; /* Add a smooth hover effect */
}

li {
  list-style: none;
}

.pagination a:hover {
  background-color: #f0f0f0; /* Change background color on hover */
}

.titleContainer {
  display: flex;
}

.infoContainer {
  display: flow;
  margin-left: 10px;
}

.viewAndUp {
  display: flex;
  font-size: 10px;
}

/* Media query for mobile devices */
@media screen and (max-width: 480px) {
  .pagination {
    flex-direction: column; /* Stack pagination items vertically */
    align-items: center; /* Center items vertically */
  }

  .pagination li {
    margin: 5px 0; /* Add space above and below each item */
  }

  .pagination a {
    display: inline-block; /* Remove block display for inline layout */
    width: auto; /* Let items expand to fit content */
  }
}
</style>