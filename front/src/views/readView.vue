<template>
  <div>
    <h1>{{ post.title }}</h1>

    <!-- Display YouTube video if available -->
    <div v-if="post && post.youtubeUrl">
      <iframe width="560" height="315" :src="embedUrl" frameborder="0" allowfullscreen></iframe>
    </div>

    <!-- Display content -->
    <div>
      <p>{{ post.content }}</p>
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
  return match && match[1] ? match[1] : '';
}
</script>

<style scoped>
/* 필요한 경우 이미지 뷰어를 스타일링할 CSS를 추가하세요 */
</style>
