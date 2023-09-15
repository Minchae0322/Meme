<template>

  <div>
  <div class="titleContainer">
    <h1 class="title">{{ post.title }}</h1>

    <div class="authorContainer">
      <div class="authorTitle">작성자 : </div>
      <div class="author">{{post.author}}</div>
    </div>



    <div class="postInfo">


      <!-- Display createTime as a localized date and time -->
      <div class="createdTime" v-if="post.createdTime">
        {{ formatCreateTime }}
      </div>

      <div class="viewContainer">
        <font-awesome-icon icon="fa-solid fa-eye" />
        <div class="viewTitle"> 조회수</div>
        <div class="view">{{post.views}}</div>


      </div>



    </div>
  </div>
    <div class="contentContainer">
    <!-- Display YouTube video if available -->
    <div class="youtubeContainer" v-if="post && post.youtubeUrl">
      <iframe width="560" height="315" :src="embedUrl" frameborder="0" allowfullscreen></iframe>
    </div>

    <!-- Display content -->
    <div class="content">
      <p v-html="formattedContent"></p>

    </div>
    </div>
  </div>
</template>

<script setup lang="js">
import axios from 'axios';
import { ref, onMounted, computed } from "vue";
import {defineProps} from "vue"
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";

const post = ref({});
const imageSrc = ref(''); // Set a default image source or placeholder
const props = defineProps({
  postId: {
    type: Number,
    required:true,
  }
})

onMounted(() => {

}
)



const loadImage = function () {
  axios
      .get(`http://localhost:8080/board/posts/${props.postId}`)
      .then((response) => {
        // Assuming the response contains the post object with youtubeUrl
        post.value = response.data;
      })
      .catch((error) => {
        console.error('이미지 불러오기 오류:', error);
      });
}


onMounted(() => {
  loadImage();
});

// Compute the YouTube embed URL based on the extracted video ID
const embedUrl = computed(() => {
  if (post.value && post.value.youtubeUrl) {
    // Extract the video ID from the YouTube URL
    const videoId = extractVideoId(post.value.youtubeUrl);
    // Construct the embed URL
    return `https://www.youtube.com/embed/${videoId}`;
  }
  return '';
});

// Function to extract the video ID from a YouTube URL
function extractVideoId(url) {
  const match = url.match(/[?&]v=([^&]+)/);
  if (match && match[1]) {
    return match[1];
  } else {
    // If no match is found with the regular expression, try to check for /shorts/ in the URL
    const shortsMatch = url.match(/\/shorts\/([^/]+)/);
    return shortsMatch && shortsMatch[1] ? shortsMatch[1] : '';
  }
}

const formattedContent = computed(() => {
  if (post.value && post.value.content) {
    return post.value.content.replace(/\n/g, '<br>');
  }
  return '';
});

// Format createTime as a localized date and time
const formatCreateTime = computed(() => {
  if (post.value && post.value.createdTime) {
    const createTime = new Date(post.value.createdTime);
    return createTime.toLocaleString(); // Adjust to your preferred date and time format
  }
  return '';
});
</script>

<style scoped>
h1 {
  color: #222222;
}
.title {
  margin: 10px 0px;
}
p {
  color: #222222;
  font-size: 20px;
}

.createdTime {
  color: #333333;
  font-size: 15px;
  margin: 10px 0px;
}
.postInfo {
  display: flex; /* Use flexbox to arrange child elements horizontally */
  align-items: center; /* Center align child elements vertically */
}

.viewContainer {
  display: flex;
  color: #333333;
  font-size: 15px;
  align-items: center;
  margin-left: 20px;


}


.authorContainer {
  display: flex;
  color: #333333;
  font-size: 15px;
  align-items: center;
  margin-left: auto;
  margin-right: 50px

}
.content {
  margin: 30px 10px;
  padding: 10px;
}
.view {
  margin: 0 10px;

}

.viewTitle {
  margin-left: 5px;
}

.youtubeContainer {
  margin: 30px 10px;
  padding: 10px;
}

.titleContainer {
  margin: 20px 10px;
  padding: 10px;
}

.author {
  color: #157e7e;
  margin: 0 10px;
}
/* 필요한 경우 이미지 뷰어를 스타일링할 CSS를 추가하세요 */
</style>
