<template>
  <div class="postsContainer">
    <ul>
      <li v-for="post in posts" :key="post.postId" class="post-item">
        <div class="postInfo">
          <router-link class="titleContainer" :to="{ name: 'read', params: { postId: post.postId } }">
            <div class="postTypeContainer">
              <div>[&nbsp </div>
              <div class="type">{{post.postType}}</div>
              <div> &nbsp]</div>
            </div>

            <div class="title">{{ truncateTitle(post.title) }}</div>
            <div class="commentContainer">
              <font-awesome-icon class="commentIcon" icon="fa-regular fa-comment" />
              <div class="commentSize">{{ post.comments.length }}</div>


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
      <li><a href="#" class="arrow left" @click="previousPage">이전</a></li>
      <li v-for="pageNumber in visiblePageNumbers" :key="pageNumber">
        <a
            href="#"
            class="num"
            @click="loadPage(pageNumber)"
            :class="{ active: pageNumber === page }"
        >{{ pageNumber }}</a>
      </li>
      <li><a href="#" class="arrow right" @click="nextPage">다음</a></li>
    </ul>
  </div>
</template>


<script setup lang="ts">
import axios from 'axios';
import { ref, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

const posts = ref<any[]>([]);
const page = ref<number>(1); // Initial page number
const pageSize = 10; // Number of items per page
const postType = ref("")

const totalPosts = ref<number>(10);
const apiBaseUrl = "http://13.125.165.102/api";
const pageRange = computed(() => {
  const totalPages = Math.ceil(totalPosts.value / pageSize);
  const range: number[] = [];
  for (let i = 1; i <= totalPages; i++) {
    range.push(i);
  }
  return range;
});

const commentSize = ref<string>("");

const visiblePageNumbers = computed(() => {
  const currentPage = page.value;
  const totalPages = Math.ceil(totalPosts.value / pageSize);
  const startPage = Math.max(1, currentPage - 2);
  const endPage = Math.min(totalPages, currentPage + 2);

  const pageNumbers = [];
  for (let i = startPage; i <= endPage; i++) {
    pageNumbers.push(i);
  }

  return pageNumbers;
});
const truncateTitle = (title: string): string => {
  if (title.length > 30) {
    return title.slice(0, 50) + '...';
  }
  return title;
};
const getTotalPosts = function () {
  axios.get(`${apiBaseUrl}/board/posts/count`,{
  })
      .then(response => {
        if (response.status === 200) {
          totalPosts.value = response.data;
        }
        console.log(totalPosts.value)
      });
};
const getCommentSize = (post: any): string => {
  commentSize.value = post.comments.length.toString();
  return commentSize.value;
};

const loadPage = async (pageNumber: number): Promise<void> => {
  if (pageNumber <= 0) return;
  const route = useRoute();
  getTotalPosts()
  try {
    const response = await axios.get(`${apiBaseUrl}/board/posts/list?page=${pageNumber}&size=10`);
    posts.value = response.data;
    page.value = pageNumber;
    route.query.page = "1";
  } catch (error) {
    console.error('Error loading page:', error);
  }
};


const nextPage = () => {
  const totalPages = Math.ceil(totalPosts.value / pageSize);
  if (page.value + 5 <= totalPages) {
    loadPage(page.value + 5);
  } else {
    loadPage(totalPages);
  }
};

const previousPage = () => {
  if (page.value - 5 >= 1) {
    loadPage(page.value - 5);
  } else {
    loadPage(1);
  }
};

// Load the initial page when the component is mounted
onMounted(() => {
  // Check if the initial page number is provided in the route query
  const route = useRoute();
  const initialPage = parseInt(route.query.page as string);
  if (!isNaN(initialPage) && initialPage > 0) {
    loadPage(initialPage);
  } else {
    loadPage(1); // Load the first page by default
  }
});
</script>

<style scoped>

.postsContainer {

}
/* Your existing styles */
.postInfo {
  display: flex;
  margin: 30px 10px;

}

.titleContainer {
  width: 75%;
  align-items: center;
  padding-left: 150px;
  display: flex;

}

.postTypeContainer {
  display: flex;
  font-size: 12px;
  color: brown;
}


.title {
  margin: 0 20px;
  padding-left: 50px;
  font-size: 16px;
  white-space: nowrap; /* Prevent line breaks */
  overflow: hidden; /* Hide overflowing text */
  text-overflow: ellipsis; /* Display ellipsis for overflowed text */
}



.commentContainer {
  display: flex;
  color: #333333;
  font-size: 10px;
  align-items: center;

}

.commentSize {
  margin: 0 5px;

}

.commentIcon {

}

.infoContainer {
  display: flow;
  width: 25%;
  margin-left: 10px;
}


.authorContainer {
  display: flex;
  color: #333333;
  margin-bottom: 10px;
  font-size: 12px;
  align-items: center;
}

.author {
  margin-left: 5px;
  font-size: 12px;
  color: #157e7e;
}

.viewAndUp {
  display: flex;
  font-size: 10px;
}

.viewContainer {
  display: flex;
  color: rgba(51, 51, 51, 0.58);
  font-size: 12px;
  align-items: center;
  margin-left: 5px;
}


.recommendationContainer {
  display: flex;
  color: rgba(51, 51, 51, 0.58);
  font-size: 12px;
  align-items: center;
  margin-left: 10px


}

.recommendation {
  margin-left: 5px;
}
.view {
  margin: 0 5px;

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







.pagination li {
  margin: 0 5px; /* Add some space between pagination items */
}




.pagination a {
  display: block;
  font-size: 12px;
  text-decoration: none;
  padding: 3px 8px;
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





/* Media query for mobile devices */
@media (max-width: 768px) {

  .postInfo {
    justify-content: start;
    margin: 20px 0;

  }

  .titleContainer {
    width: 75%;
    align-items: center;
    padding-left: 0px;
    margin-left: 0px;
  }

  .pagination a {
    font-size: 8px;
  }

  .postTypeContainer {
    font-size: 6px;
    width: 17%;
  }

  .title {
    margin-left: 0;
    padding-left: 10px;
    font-size: small;
  }

  .recommendationContainer {
    font-size: 8px;
  }


  .author {
    font-size: 6px;
  }

  .authorTitle {
    font-size: 5px;
  }
  .viewContainer {
    font-size: 6px;
  }

  .commentContainer {
    font-size: 6px;
  }
}


</style>