<template>
  <div class="container">
    <h3 class="header">글 쓰기</h3>
    <hr/>



    <div class="form-container">

      <div class="titleContainer">
        <el-select class="selectBox" v-model="postType" placeholder="Select">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
        <el-input class="title" v-model="title" placeholder="제목을 입력하세요" style="height: 50px"></el-input>

      </div>
      <div class="fileContainer">
      <el-upload
          v-model:file-list="fileList"
          multiple
          :auto-upload="false"
          class="upload"
          :on-remove="handleRemove"
          :limit="3"
          list-type="picture"
          style="margin-top: 10px"
      >
        <el-button type="primary">이미지 업로드</el-button>
        <template #tip>
          <div class="el-upload__tip">
            jpg/png files with a size less than 10MB
          </div>
        </template>


      </el-upload>
        <div class="youtubeUrlContainer">
          <el-input v-model="youtubeUrl" placeholder="YouTube 동영상 URL을 입력하세요" style="height: 50px"></el-input>
        </div>
      </div>
      <div class="contentContainer">
        <el-input class="content" v-model="content" type="textarea" rows="25" placeholder="내용을 입력하세요"></el-input>
      </div>

      <el-button type="primary" @click="write">글 작성 완료</el-button>

      <!-- Conditional rendering of the warning text -->

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
const showWarning = ref(false); // Initialize the warning state
const options = [
  {
    value: '자유',
    label: '자유',
  },
  {
    value: '최신',
    label: '최신',
    disabled: true,
  }
]

onMounted( () => {
  checkLogin()
});

const checkLogin = function () {
  const accessToken = localStorage.getItem("accessToken");
  if (accessToken) {
    try {
      axios.get("http://13.125.165.102/api/auth/isValidToken", {
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

const postType = ref("자유"); // Initialize with a default value
const fileList = ref([]);
const handleRemove = (file, fileList) => {
  // 파일 삭제 시 fileList에서 해당 파일 제거
  const index = fileList.indexOf(file);
  if (index !== -1) {
    fileList.splice(index, 1);
  }
  console.log("Previewing file:", fileList);
};

const frm = new FormData();
const write = function () {
  // const file = fileList.value[0];
  if (title.value.trim() === "" || content.value.trim() === "") {
    alert("제목과 내용은 공백일 수 없습니다.")
    return; // Stop here and don't proceed with the API call
  }
  // 새로운 코드: fileList.value를 사용하여 파일 가져오기
  const files = fileList.value.map((item) => item.raw);
  console.log(files)
  let params = JSON.stringify({ title: title.value, content: content.value });

  frm.append("post", new Blob([JSON.stringify({ title: title.value, content: content.value, postType: postType.value, youtubeUrl: youtubeUrl.value })], { type: "application/json" }));
  files.forEach((file) => {
    frm.append("imageFile", file);
  });

  axios
      .post("http://13.125.165.102/api/board/user/writePost", frm, {
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

.fileContainer {
  display: flex;
  width: 90%;
}

h3 {
  color: #222222;
  margin: 20px 0;

}

.contentContainer {
  color: #181818;
  margin-top: 20px;
  width:80%;
}


.titleContainer {
  display: flex;
 color: #181818;
  width:70%;
  align-items: center;
  margin-top: 15px;
}

.youtubeUrlContainer {
  color: #181818;
  width: 80%;
  margin: 20px 20px;

}

hr {
  width: 80%;
}

.titleCharacter {
  margin: 10px 0;
}

.youtubeTitle {
  margin: 10px 0;
}
.selectBox {
  margin-right: 10px;
  font-size: 15px;
  width: 10%;
}
.warning-text {
  color: red;
  margin-top: 10px;
  display: block;
}
</style>