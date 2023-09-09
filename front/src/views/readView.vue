<template>
  <div>
    <button @click="loadImages">Load Images</button>
    <div v-for="(imageData, index) in imageList" :key="index">
      <img :src="imageData" alt="Image" />
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      imageList: [], // 이미지 데이터를 저장할 배열
    };
  },
  methods: {
    loadImages() {
      // Spring Boot에서 이미지 데이터 가져오는 API 호출
      axios.get("http://localhost:8080/board/posts/103/image")
          .then((response) => {
            this.imageList = response.data;
          })
          .catch((error) => {
            console.error("Error loading images", error);
          });
    },
  },
};
</script>