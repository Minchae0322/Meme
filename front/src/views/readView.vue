<template>
  <div>
    <!-- youtubeUrl이 있으면 iframe으로 비디오를 표시하고, 그렇지 않으면 이미지 표시 -->
    <div>
      <iframe width="560" height="315" :src="posts.youtubeUrl" frameborder="0" allowfullscreen></iframe>
    </div>
    <div>
      <img :src="imageSrc" alt="포스트 이미지" />
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import {ref} from "vue";

const posts = ref("")
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
      const postId = 229; // 실제 포스트 ID로 대체하세요
      axios
          .get(`http://localhost:8080/board/posts/${postId}`, { responseType: 'arraybuffer' })
          .then((response) => {
            posts.value.push(response.data)
            // HTTP 응답이 이미지인 경우 이미지 표시
            const contentType = response.headers['content-type'];
            console.log(posts.value)
            if (contentType.startsWith('image/')) {
              const imageBase64 = btoa(
                  new Uint8Array(response.data).reduce((data, byte) => data + String.fromCharCode(byte), '')
              );
              this.imageSrc = `data:${contentType};base64,${imageBase64}`;
            }
            // HTTP 응답이 비디오인 경우 youtubeUrl 표시
            else if (contentType.startsWith('video/')) {

            }
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