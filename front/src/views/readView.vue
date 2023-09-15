<template>
  <div>
    <h1>{{ post.title }}</h1>

    <!-- Display YouTube video if available -->
    <div v-if="post && post.youtubeUrl">
      <iframe width="560" height="315" :src="embedUrl" frameborder="0" allowfullscreen></iframe>
    </div>

    <!-- Display content -->
    <div class="content">
      <p v-html="formattedContent"></p>

    </div>
  </div>
</template>

<script setup lang="js">
import axios from 'axios';
import { ref, onMounted, computed } from "vue";
import {defineProps} from "vue"

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
  return this.post.value.content.replace(/\n/g, '<br>');
});
</script>

<style scoped>
h1 {
  color: #222222;
}

p {
  color: #222222;
  font-size: 20px;
}
/* 필요한 경우 이미지 뷰어를 스타일링할 CSS를 추가하세요 */
</style>
