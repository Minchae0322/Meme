<template>
  <div>
    <button @click="fetchPost">포스트 가져오기</button>
    <div v-if="post">
      <h1>{{ post.title }}</h1>
      <p>{{ post.content }}</p>
      <img :src="post.imageUrl" alt="포스트 이미지" />
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      post: null,
    };
  },
  methods: {
    fetchPost() {
      const url = 'http://localhost:8080/board/posts/33';

      axios.get(url)
          .then((response) => {
            // 서버에서 받아온 데이터를 post 변수에 저장
            this.post = response.data;
          })
          .catch((error) => {
            console.error('데이터를 가져오는 중 오류 발생:', error);
          });
    },
  },
};
</script>