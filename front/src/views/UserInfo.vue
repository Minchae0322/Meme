<template>
<body>

<div class="userInfoContainer" style="margin: 20px" >
<el-form
    :label-position="labelPosition"
    label-width="100px"
    :model="formLabelAlign"
    style="max-width: 460px"
>
  <el-form-item label="아이디">
    <el-text>{{ userInfo.username }}</el-text>

  </el-form-item>
  <el-form-item label="닉네임">
    <el-text>{{userInfo.nickName}}</el-text>
    <el-button class="editNicknameButton" @click="editNickname">닉네임 변경</el-button>
  </el-form-item>

  <el-form-item v-if="editingNickname" label="새로운 닉네임">
    <el-input v-model="newNickname" placeholder="새로운 닉네임을 입력하세요" />
    <el-button @click="changeNickname">확인</el-button>
  </el-form-item>
  <el-form-item label="이메일">
    <el-text>{{userInfo.email}}</el-text>
  </el-form-item>
</el-form>
</div>
</body>

</template>


<script lang="ts" setup>
import {onMounted,reactive, ref, toRefs} from 'vue'
import type { FormProps } from 'element-plus'
import axios from "axios";
import {useRouter} from "vue-router";

const userInfo = ref({});
const apiBaseUrl = "http://13.125.165.102/api";
const router = useRouter()

const labelPosition = ref<FormProps['labelPosition']>('left')

const formLabelAlign = reactive({
  name: '',
  region: '',
  type: '',
})

const { editingNickname, newNickname } = toRefs(reactive({
  editingNickname: false,
  newNickname: ''
}))



onMounted( () => {
  checkLogin()
  getUserInformation()
});

const checkLogin = function () {
  const accessToken = localStorage.getItem("accessToken");
  if (accessToken) {
    try {
      axios.get(`${apiBaseUrl}/auth/isValidToken`, {
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

const getUserInformation = function () {
  const accessToken = localStorage.getItem("accessToken");
  if (accessToken) {
    try {
      axios.get(`${apiBaseUrl}/user/information`, {
        headers: {
          'Authorization': accessToken
        }
      }).then(response => {

        if (response.status === 200) {
          userInfo.value = response.data
        }
      });
    } catch (error) {
      console.error("에러 발생:", error);
    }
  }
};

const editNickname = function () {
  editingNickname.value = !editingNickname.value;

}

// 이 함수를 호출하여 새로운 닉네임을 저장할 수 있습니다.
const changeNickname = function () {
  const accessToken = localStorage.getItem('accessToken')
  console.log(newNickname.value)
  if (accessToken) {
    try {
      axios.post(`${apiBaseUrl}/user/information/isValidNickname`, {
        nickname: newNickname.value
      }, {
        headers: {
          'Authorization': accessToken
        }
      }).then(response => {
        if (response.status === 200) {
          if (response.data === true) {
            axios.post(`${apiBaseUrl}/user/information/changeNickname`, {
              nickname: newNickname.value
            }, {
              headers: {
                'Authorization': accessToken
              }
            }).then(response => {
              if (response.status === 200) {
                router.go(0);
              }
            });
          } else {
            alert("사용할 수 없는 닉네임입니다.")
          }
        }
      });
    } catch (error) {
      console.error('에러 발생:', error)
    }
  }
}


</script>

<style scoped>
.userInfoContainer {
  display: flex;
  justify-content: start;
  align-items: center;
  padding-left: 20%;
  padding-top: 2%;
}

.editNicknameButton {
  margin-left: 10px;
}





</style>