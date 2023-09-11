<template>
  <div>
    <img :src="imageSrc" alt="포스트 이미지" />
  </div>
</template>

<script>

import axios from 'axios'
export default {
  data() {
    return {
      imageSrc: null,
    };
  },
  mounted() {
    this.loadImage();
  },
  methods: {
    loadImage() {
      const postId = 129; // 실제 포스트 ID로 대체하세요
      // HTTP GET 요청을 사용하여 Spring Boot 백엔드로 요청을 보냅니다.
      // 요청을 보내는 데 axios 또는 선택한 다른 HTTP 라이브러리를 사용할 수 있습니다.
      // API 엔드포인트 '/api/board/posts/'를 실제 엔드포인트로 대체하세요.
      axios
          .get(`http://localhost:8080/board/posts/${postId}/image`, { responseType: 'arraybuffer' })
          .then((response) => {
            // 받은 이진 데이터를 base64로 인코딩한 데이터 URL로 변환합니다.
            const imageBase64 = btoa(
                new Uint8Array(response.data).reduce(
                    (data, byte) => data + String.fromCharCode(byte),
                    ''
                )
            );
            this.imageSrc = `data:image/jpeg;base64,${imageBase64}`;
          })
          .catch((error) => {
            console.error('이미지 불러오기 오류:', error);
          });
    },
  },
};
</script>

<style scoped>
/* 필요한 경우 이미지 뷰어를 스타일링할 CSS를 추가하세요 */
</style>