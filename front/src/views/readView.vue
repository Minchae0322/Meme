<template>
  <head><title>MEME 게시판</title></head>
  <div>
  <div class="titleContainer">
    <h1 class="title">{{ post.title }}</h1>

    <div class="authorContainer">
      <div class="authorTitle">작성자 : </div>
      <div class="author">{{post.author}}</div>
    </div>



    <div class="postInfo">
      <!-- Display createTime as a localized date and time -->
      <div class="createdTime" v-if="post.createdTime">
        {{ formatCreateTime }}
      </div>

      <div class="viewContainer">
        <div class="viewTitle"> 조회수</div>
        <font-awesome-icon class="viewIcon" icon="fa-solid fa-eye" />
        <div class="view">{{post.views}}</div>


      </div>


      <div class="recommendationContainer">
        <font-awesome-icon class="recommendationIcon" icon="fa-regular fa-thumbs-up" />
        <div class="recommendation">{{post.recommendation}}</div>


      </div>

      <div class="commentContainer">
        <font-awesome-icon class="commentIcon" icon="fa-regular fa-comment" />
        <div class="commentSize">{{commentSize}}</div>


      </div>


      <div class="deleteContainer">
        <router-link  class="update" :to="{ name: 'update', params: { postId: post.postId } }">수정</router-link>
        <a>|</a>
        <a  @click = "deletePost">삭제</a>
      </div>



    </div>
  </div>



    <hr/>
    <div class="contentContainer">
    <!-- Display YouTube video if available -->
    <div class="youtubeContainer" v-if="post && post.youtubeUrl">
      <iframe width="720" height="405" :src="embedUrl" frameborder="0" allowfullscreen></iframe>
    </div>

    <!-- Display content -->
    <div class="content">
      <div v-for="(imageData, index) in images" :key="index" class="imageContainer" >
        <img :src="getImageSrc(imageData)" alt="Image" />
      </div>
      <p v-html="formattedContent"></p>

    </div>
    </div>


    <div class="upContainer">
      <a class="up" @click = "upRecommendation">
      <font-awesome-icon  icon="fa-regular fa-thumbs-up" />
      <div class="upCount">{{post.recommendation}}</div>
      </a>
    </div>

    <div class="commentInputContainer">
      <el-input class="writeComment" v-model="commentText" type="textarea" placeholder="댓글을 입력하세요"
      rows="5"/>
      <el-button @click="submitComment">댓글 작성</el-button>
    </div>

    <div class="commentListContainer">
      <div v-for="(comment, index) in comments" :key="index" class="commentItem">

        <div class="commentInfo">
        <div class="commentAuthor">{{ comment.author }}</div>
        <div class="commentCreatedTime">{{new Date(comment.createdTime).toLocaleString()}}</div>
          <div class="deleteContainer">
            <a  @click = "deleteComment(comment.commentId)">삭제</a>
          </div>
        </div>
        <div class="commentText">{{ comment.comment }}</div>
        <hr width="80%"/>


      </div>
    </div>
  </div>
</template>

<script setup lang="js">
import axios from 'axios';
import { ref, onMounted, computed } from "vue";
import {defineProps} from "vue"
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import { useRouter } from "vue-router";

const router = useRouter()
const post = ref({});
const imageSrc = ref(""); // Set a default image source or placeholder
const props = defineProps({
  postId: {
    type: Number,
    required:true,
  }
})
const apiBaseUrl = "http://13.125.165.102/api";
const commentText = ref(""); // Data property to store the comment text
const commentSize = ref("");

onMounted(() => {

}
)
const images = ref([])
const fetchImage = function () {
  axios
      .get(`${apiBaseUrl}/board/posts/${props.postId}/image`, {
      })
      .then((response) => {
        console.log(response.data.imageData)
        images.value = response.data.imageData
        // Assuming you have a data property called 'imageSrc'
        imageSrc.value = `data:image/jpeg;base64,${images.value.imageData[2]}`;
        //console.log(imageSrc.value)
      })
      .catch((error) => {
       // console.error('이미지 불러오기 오류:', error);
      });
};

const updatePost = function () {

};

const checkLogin =  function () {
  const accessToken = localStorage.getItem("accessToken");
  if (accessToken) {
    axios.get(`${apiBaseUrl}/auth/isValidToken`, {
        headers: {
          'Authorization': accessToken
        }
      }).then(response => {
        return true;
      }).catch(error => {
      return false;
    });
  }
  return false
};
const submitComment = function () {
  if (commentText.value.trim() === "" || commentText.value.length > 90) {
    alert("공백이거나 90자 이상 적을 수 없습니다.");

  } else {
    const accessToken = localStorage.getItem("accessToken");
    if (accessToken) {
      axios.get(`${apiBaseUrl}/auth/isValidToken`, {
        headers: {
          'Authorization': accessToken
        }
      }).then(response => {
        if (response.status === 200) {
          axios.post(`${apiBaseUrl}/board/user/${props.postId}/comments`, {
            comment: commentText.value
          }, {
            headers: {
              'Authorization': localStorage.getItem("accessToken")
            }
          }).then(response => {
            router.go(0)
          });
        }

      }).catch(error => {

      });
    } else {
      alert("로그인 후 작성해주세요.")
      router.push({name : "login"})
    }
  }



};
const getImageSrc = function (imageData) {
  //console.log(imageData)
  // Create a data URL for the image data
  return `data:image/jpeg;base64,${imageData}`;
}


const upRecommendation = function () {
  axios.get(`${apiBaseUrl}/board/user/${props.postId}/up`, {
    headers: {
      'Authorization': localStorage.getItem("accessToken")
    }
  }).then(response => {
    post.value.recommendation = response.data
  }).catch(error => {
    alert("추천을 할 수 없습니다.")
  })
};
const deletePost = function () {
  if(confirm("정말 삭제하시겠습니까?")) {
    axios.delete(`${apiBaseUrl}/board/user/${props.postId}`, {
      headers: {
        'Authorization': localStorage.getItem("accessToken")
      }
    })
        .then(response => {
          if (response.status === 200) {
            alert("삭제되었습니다.")
            router.replace({name: "postList"})
          } else {
            alert("권한이 없습니다.")
          }
        })
        .catch(error => {
          alert("권한이 없습니다.")
        })
  }

}

const deleteComment = function (commendId) {
  if(confirm("정말 삭제하시겠습니까?")) {
    axios.delete(`${apiBaseUrl}/board/user/${props.postId}/${commendId}/delete`, {
      headers: {
        'Authorization': localStorage.getItem("accessToken")
      }
    })
        .then(response => {
          if (response.status === 200) {
            alert("삭제되었습니다.")
            router.go(0)
          } else {
            alert("권한이 없습니다.")
          }
        })
        .catch(error => {
          alert("권한이 없습니다.")
        })
  }

}
const loadPost = function () {
  axios
      .get(`${apiBaseUrl}/board/posts/${props.postId}`)
      .then((response) => {
        // Assuming the response contains the post object with youtubeUrl
        post.value = response.data;
        commentSize.value = post.value.comments.length
        fetchImage()
      })
      .catch((error) => {
        console.error('이미지 불러오기 오류:', error);
      });
}


const comments = ref([]);
const loadComment = function () {
  axios
      .get(`${apiBaseUrl}/board/posts/${props.postId}/comments`)
      .then((response) => {
        // Assuming the response contains the post object with youtubeUrl
        comments.value = response.data;
      })
      .catch((error) => {
        console.error('댓글 불러오기 오류:', error);
      });
}


onMounted(() => {
  loadPost();
  loadComment();
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
  if (post.value && post.value.content) {
    return post.value.content.replace(/\n/g, '<br>');
  }
  return '';
});

// Format createTime as a localized date and time
const formatCreateTime = computed(() => {
  if (post.value && post.value.createdTime) {
    const createTime = new Date(post.value.createdTime);
    return createTime.toLocaleString(); // Adjust to your preferred date and time format
  }
  return '';
});

</script>


<style scoped>


.commentInputContainer {
  display: flex;
  margin: 30px 0;
}

.commentText {
  font-size: 14px;
}

h1 {
  color: #222222;
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;


}
.title {
  margin: 15px 0px;

}
p {
  color: #222222;
  font-size: 18px;
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;
}

.commentInfo {
  display: flex;
  color: #222222;
  font-size: 16px;
  align-items: center;
  margin: 10px 0px;

}

.commentInfo div {
  margin: 10px 10px;
}


.createdTime {
  color: #333333;
  font-size: 15px;
  margin: 10px 0px;
}
.postInfo {
  display: flex; /* Use flexbox to arrange child elements horizontally */
  align-content: center; /* Center align child elements vertically */
  width: 90vw;

}
.commentInfo hr {
  margin: 20px;
}



.authorContainer {
  display: flex;
  color: #333333;
  font-size: 15px;
  align-items: center;
  margin-left: auto;
  margin-right: 50px

}

.commentText {
  color: #222222;
  margin-left: 10px;
  margin-bottom: 20px;
  font-size: 20px;
}


.content {

  margin: 10px 10px;
  padding: 10px;
}
.imageContainer {
  margin-bottom: 20px;

}
.viewContainer {
  display: flex;
  color: #333333;
  font-size: 15px;
  align-items: center;
  margin-left: 20px;


}

.view {
  margin: 0 5px;

}




.viewTitle {
  margin-left: 5px;
}

.viewIcon {
  margin-left: 10px;
}

.recommendationContainer {
  display: flex;
  color: #333333;
  font-size: 15px;
  align-items: center;
  margin-left: 20px;



}
.up {
  margin-right: auto;
  margin-left: auto;
  display: flex;
  align-items: center;
  color: rgba(0, 0, 0, 0.61);
  padding: 12px 20px;
  font-size: 17px;
  margin-bottom: 20px;
  border: 2px solid rgba(7, 7, 7, 0.37);
  border-radius: 5px;
}

.upCount {
  margin-left: 10px;
}
.recommendation {
  margin: 0 5px;

}


.recommendationTitle {
  margin-left: 5px;
}

.recommendationIcon {
  margin-left: 10px;
}

.commentContainer {
  display: flex;
  color: #333333;
  font-size: 15px;
  align-items: center;
  margin-left: 20px;



}

.commentSize {
  margin: 0 5px;

}

.commentIcon {
  margin-left: 10px;
}

.deleteContainer {
  display: flex;
  font-size: 13px;
  align-items: center;
  margin-left: auto;
  margin-right: 200px;
}

.deleteContainer a {
  color: rgba(0, 0, 0, 0.66);
  margin: 0 3px;
}
.upContainer {
  display: flex;
  align-items: center;
  margin: 30px;

}
.youtubeContainer {
  margin: 30px 10px;
  padding: 10px;

}

.commentListContainer {
  margin: 30px 0;
}

.titleContainer {
  margin: 10px 10px;
  padding: 10px;
}

.author {
  color: #157e7e;
  margin: 0 10px;
}

.commentAuthor {
  color: #157e7e;
}

.writeComment {
  width: 80%;
}

@media (max-width: 768px) {
  .titleContainer {
    margin: 5px 5px; /* 모바일 화면에서는 여백을 작게 조정 */
    padding: 5px;

  }

  .commentContainer {
    margin: 0 0;
  }

  h1.title {
    font-size: 26px; /* 모바일 화면에서 제목 폰트 크기 줄이기 */
  }

  p {
    font-size: 13px; /* 모바일 화면에서 내용 폰트 크기 줄이기 */
  }

  .commentText {
    font-size: 14px; /* 모바일 화면에서 댓글 폰트 크기 줄이기 */
  }

  .postInfo {
    font-size: 8px;
  }

  .authorContainer {
    font-size: 14px;
  }

  .viewContainer {
    margin-left: 4px;
    font-size: 11px;

  }

  .createdTime {
    font-size: 11px;
  }

  .recommendationContainer {
    font-size: 11px;
    margin-left: 0;
  }

  .commentContainer {
    font-size: 11px;
    margin-left: 0;
  }

  .deleteContainer {
    margin-right: auto;
    font-size: 9px;
  }

  iframe {
    width: 340px;
    height: 260px;
  }

  .youtubeContainer {
    margin: 10px 0;
  }



  .content {
    margin: 10px 0;
  }

  img {
    width: 300px;
  }
  /* 다른 요소들에 대한 스타일도 필요한 경우 추가하세요. */
}
/* 필요한 경우 이미지 뷰어를 스타일링할 CSS를 추가하세요 */
</style>
