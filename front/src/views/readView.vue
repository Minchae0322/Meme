<template>

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
        <div class="comment">{{commentSize}}</div>


      </div>


      <div class="deleteContainer">
        <a  @click = "deletePost">수정</a>
        <a>|</a>
        <a  @click = "deletePost">삭제</a>
      </div>

    </div>
  </div>



    <hr/>
    <div class="contentContainer">
    <!-- Display YouTube video if available -->
    <div class="youtubeContainer" v-if="post && post.youtubeUrl">
      <iframe width="560" height="315" :src="embedUrl" frameborder="0" allowfullscreen></iframe>
    </div>

    <!-- Display content -->
    <div class="content">
      <div v-for="(imageData, index) in images" :key="index" class="imageContainer">
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

const commentSize = ref("")

onMounted(() => {

}
)

/*const getImageSrc =  function (imageData) {
  console.log(imageData[2])
  const imagesData = btoa(new Uint8Array(imageData[2]).reduce(
      (data, byte) => data + String.fromCharCode(byte),''
  ))
  // Create a data URL for the image data
  return `data:image/jpeg;base64,${imagesData}`;
}*/
/*const fetchImages = function () {
  axios
      .get(`http://localhost:8080/board/posts/${props.postId}/image`, {

      })
      .then((response) => {
        images.value = response.data;
        console.log(images.value[0])
      })
      .catch((error) => {
        console.error('Error fetching images:', error);
      });
}*/

const images = ref([])
const fetchImage = function () {
  axios
      .get(`http://localhost:8080/board/posts/${props.postId}/image`, {
      })
      .then((response) => {

        images.value = response.data
        // Assuming you have a data property called 'imageSrc'
        imageSrc.value = `data:image/jpeg;base64,${images.value.imageData[2]}`;
        console.log(imageSrc.value)
      })
      .catch((error) => {
        console.error('이미지 불러오기 오류:', error);
      });
};


const upRecommendation = function () {
  axios.get(`http://localhost:8080/board/user/${props.postId}/up`, {
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
    axios.delete(`http://localhost:8080/board/user/${props.postId}`, {
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
const loadPost = function () {
  axios
      .get(`http://localhost:8080/board/posts/${props.postId}`)
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


onMounted(() => {
  loadPost();
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
  font-size: 17px;
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;
}



.createdTime {
  color: #333333;
  font-size: 15px;
  margin: 10px 0px;
}
.postInfo {
  display: flex; /* Use flexbox to arrange child elements horizontally */
  align-items: center; /* Center align child elements vertically */


}




.authorContainer {
  display: flex;
  color: #333333;
  font-size: 15px;
  align-items: center;
  margin-left: auto;
  margin-right: 50px

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
  margin-bottom: 50px;
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

.comment {
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



.titleContainer {
  margin: 10px 10px;
  padding: 10px;
}

.author {
  color: #157e7e;
  margin: 0 10px;
}
/* 필요한 경우 이미지 뷰어를 스타일링할 CSS를 추가하세요 */
</style>
