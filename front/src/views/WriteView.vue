
<script setup lang = "js">
import {ref} from "vue";
import axios from 'axios'
import router from "@/router";

const title = ref("")
const content = ref("")
let images = new Image()


const youtubeUrl = /(http:|https:)?(\/\/)?(www\.)?(youtube.com|youtu.be)\/(watch|embed)?(\?v=|\/)?(\S+)?/g


const write = function () {


  axios.post("http://localhost:8080/board/user/writePost", {
    title: title.value,
    content: content.value,
    image: imageUrl

  })
      .then(() => {
        router.replace({
          name: "home"
        })

      })}

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
  <header>

  </header>


  <body>
  <div>



  </div>
  <div>
    <div class="container">
      <div class="image-upload" id="image-upload">

        <form method="post" enctype="multipart/form-data">
          <div class="button">
            <label for="chooseFile">
              👉 CLICK HERE! 👈
            </label>
          </div>
          <input type="file" id="image" name="chooseFile" accept="image/*" multiple @change = upload(this) >
        </form>

        <div class="fileContainer">
          <div class="fileInput">
            <p>FILE NAME: </p>
            <p id="fileName"></p>
          </div>
          <div class="buttonContainer">
            <div class="submitButton" id="submitButton" @click = showImage >SUBMIT </div>
          </div>
        </div>
      </div>

      <div class="image-show" id="image-show"></div>
    </div>
  </div>

  <div>
    <el-text  class="mx-1" type="primary">제목</el-text>
    <el-input v-model = "title" placeholder="제목을 입력하세여"></el-input>
  </div>

  <div>
    <el-input
        v-model="content"
        :cols = "30"
        :rows="15"
        type="textarea"
        placeholder="Please input"
    />
  </div>
  <el-button type="primary" @click = "write()">글 작성 완료</el-button>

  </body>

</template>





<style>

</style>