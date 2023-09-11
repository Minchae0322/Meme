<script setup lang = "js">
import {ref} from "vue";
import axios from 'axios'
import router from "@/router";


const title = ref("")
const content = ref("")
let images = new Image()


const youtubeUrl = /(http:|https:)?(\/\/)?(www\.)?(youtube.com|youtu.be)\/(watch|embed)?(\?v=|\/)?(\S+)?/g
const frm = new FormData();

/*const write = function () {
  const file = document.querySelector("#image").files[0]
  images.src = URL.createObjectURL(file)
  console.log(images)
  axios.post("http://localhost:8080/board/user/writePost",{
    headers: {
      "Content-Type": "multipart/form-data",
    },
   // title: title.value,
    //content: content.value,
    imageFile: images

  })
      .then(() => {
        router.replace({
          name: "home"
        })

      })}*/
const checkLogin = function() {
  if (!localStorage.getItem("accessToken")) {
    router.replace({name: "login"})
  }
  console.log("실행")
  return 0
}

checkLogin();
const write = function () {
  const file = document.querySelector("#image").files[0]
  let params = JSON.stringify({ title: title.value, content: content.value });

  frm.append("post", new Blob([JSON.stringify({ title: title.value, content: content.value, postType: "ALL" })], {type: "application/json"}));
  frm.append("imageFile", file);

  console.log(images)

  axios.post("http://localhost:8080/board/user/writePost", frm, {
    headers: {
      'Content-Type': 'multipart/form-data',
      'Authorization': localStorage.getItem("accessToken")
    },

  })
  console.log(localStorage.getItem("accessToken"))
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







</script>

<template>
  <div class="container">
    <header class="header">글 작성</header>

    <div class="file-upload">
      <label for="image" class="file-upload-label">
        📸 Upload Image
      </label>
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

    <div class="form-container">
      <div class="input-container">
        <label class="input-label">제목</label>
        <el-input v-model="title" placeholder="제목을 입력하세요"></el-input>
      </div>

      <div class="input-container">
        <label class="input-label">내용</label>
        <el-input
            v-model="content"
            type="textarea"
            rows="10"
            placeholder="내용을 입력하세요"
        ></el-input>
      </div>

      <el-button type="primary" @click="writePost">글 작성 완료</el-button>
    </div>
  </div>
</template>

<style scoped>
.container {
  margin: 20px;
  width: 90vw;
  text-align: center;
}

.header {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333; /* Change the header text color */
}

.file-upload {
  margin-bottom: 20px;
}

.file-upload-label {
  background-color: #4caf50;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s ease;
}

.file-upload-label:hover {
  background-color: #45a049;
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

.input-label {
  font-size: 18px;
  font-weight: bold;
  color: #333; /* Change the label text color */
}

.el-input {
  width: 100%;
  max-width: 500px; /* Limit input width for better readability */
  font-size: 16px;
}

.el-button {
  margin-top: 20px;
  background-color: #4caf50;
  color: white;
  font-weight: bold;
}

.el-button:hover {
  background-color: #45a049;
}
</style>