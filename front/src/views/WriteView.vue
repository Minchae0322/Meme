<template>
  <div class="container">
    <h3 class="header">글 작성</h3>

    <div class="file-upload">
      <input class="upload-name" value="파일선택" disabled="disabled">
      <label for="ex_filename">업로드</label>
      <input
          type="file"
          id="image"
          name="image"
          accept="image/*"
          @change="uploadImage"
      />
    </div>

    <div class="image-preview">
      <div class="image-container">
        <img
            :src="imageUrl"
            alt="Uploaded Image"
            class="uploaded-image"
            v-if="imageUrl"
        />
      </div>
    </div>

    <div class="youtubeUrlContainer">
      <label class="input-label">YouTube URL</label>
      <el-input v-model="youtubeUrl" placeholder="YouTube 동영상 URL을 입력하세요"></el-input>
    </div>

    <div class="form-container">
      <div class="titleContainer">
        <label class="input-label">제목</label>
        <el-input class = "title" v-model="title" placeholder="제목을 입력하세요"></el-input>
      </div>

      <div class="input-container">
        <label class="input-label">내용</label>
        <el-input
            v-model="content"
            type="textarea"
            rows="20"
            placeholder="내용을 입력하세요"
        ></el-input>
      </div>



      <el-button type="primary" @click="write">글 작성 완료</el-button>
    </div>
  </div>
</template>

<script setup lang = "js">
import {onMounted, ref, watch} from "vue";
import axios from 'axios'
import { useRouter } from "vue-router";

const router = useRouter()

const title = ref("")
const content = ref("")
let images = new Image()
let youtubeUrl = ref("");
let videoId = ref("");



onMounted( () => {
  checkLogin()
});

const checkLogin = function () {
  const accessToken = localStorage.getItem("accessToken");
  if (accessToken) {
    try {
      axios.get("http://localhost:8080/auth/isValidToken", {
        headers: {
          'Authorization': accessToken
        }
      }).then(response => {

        if (response.status !== 200) {
          router.replace({name: "login"})
        }
      });
    } catch (error) {
      console.error("에러 발생:", error);
    }
  } else {
    router.replace({name: "login"})
  }
};

const frm = new FormData();
const write = function () {
  const file = document.querySelector("#image").files[0];
  let params = JSON.stringify({ title: title.value, content: content.value });

  frm.append("post", new Blob([JSON.stringify({ title: title.value, content: content.value, postType: "ALL", youtubeUrl: youtubeUrl.value })], { type: "application/json" }));
  frm.append("imageFile", file);

  axios
      .post("http://localhost:8080/board/user/writePost", frm, {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': localStorage.getItem("accessToken")
        },
      })
      .then((response) => {
        // Handle the successful write action here
        console.log("글 작성 완료", response);

        // Redirect to /posts route
        router.replace({ name: 'postList' });
      })
      .catch((error) => {
        console.error('글 작성 오류:', error);});
}


const submit = document.getElementById("submitButton");
//Submit 버튼 클릭시 이미지 보여주기

function showImage() {
  const newImage = document.getElementById('image-show').lastElementChild;

  //이미지는 화면에 나타나고
  newImage.style.visibility = "visible";

  //이미지 업로드 버튼은 숨겨진다
  document.getElementById('image-upload').style.visibility = 'hidden';

  document.getElementById('fileName').textContent = null;     //기존 파일 이름 지우기
}

const upload = function (image) {

  const file = document.querySelector("#image").files[0]

  const newImage = document.createElement("img");
  newImage.setAttribute("class", 'img');

  //이미지 source 가져오기
  newImage.src = URL.createObjectURL(file);

  newImage.style.width = "70%";
  newImage.style.height = "70%";
  newImage.style.visibility = "hidden";   //버튼을 누르기 전까지는 이미지를 숨긴다
  newImage.style.objectFit = "contain";

  //이미지를 image-show div에 추가
  const container = document.getElementById('image-show');
  container.appendChild(newImage);

};


// ...



const extractVideoId = () => {
  const matches = youtubeUrl.match(/youtu\.be\/([^"&?/ ]{11})/);
  if (matches && matches.length > 1) {
    videoId.value = matches[1];
  } else {
    videoId.value = ""; // URL에서 VIDEO_ID를 찾을 수 없으면 비우기
  }
};

// youtubeUrl이 변경될 때마다 VIDEO_ID 추출
watch(youtubeUrl, extractVideoId);

// ...




</script>



<style scoped>

.container {
  margin: 40px;
  width: 90vw;
}


.file-upload {
  margin-bottom: 20px;
}

h3 {
  color: #222222;
  margin: 20px 10px;

}

.image-preview {
  text-align: center;
}

.image-container {
  max-width: 100%; /* Ensure the image doesn't overflow */
}

.uploaded-image {
  max-width: 100%;
  height: auto; /* Maintain aspect ratio */
  border: 1px solid #ccc; /* Add a subtle border around the image */
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* Add a shadow to the image */
}

.input-container {
  margin-bottom: 20px;
}





.file-upload label {
  display: inline-block;
  padding: .5em .75em;
  color: #999;
  font-size: inherit;
  line-height: normal;
  vertical-align: middle;
  background-color: #fdfdfd;
  cursor: pointer;
  border: 1px solid #ebebeb;
  border-bottom-color: #e2e2e2;
  border-radius: .25em;
}
/* named upload */
.file-upload .upload-name {
  display: inline-block;
  padding: .5em .75em;  /* label의 패딩값과 일치 */
  font-size: inherit;
  font-family: inherit;
  line-height: normal;
  vertical-align: middle;
  background-color: #f5f5f5;
  border: 1px solid #ebebeb;
  border-bottom-color: #e2e2e2;
  border-radius: .25em;
  -webkit-appearance: none; /* 네이티브 외형 감추기 */
  -moz-appearance: none;
  appearance: none;
}
.file-upload el-input[type="file"] {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip:rect(0,0,0,0);
  border: 0;
}
.titleContainer {
 color: #181818;
  width:70%;
}

.youtubeUrlContainer {
  color: #181818;
  width: 30%;
}

</style>