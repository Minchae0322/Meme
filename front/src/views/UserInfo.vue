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
    <el-text v-model="formLabelAlign.name" >{{"아아아"}}</el-text>
  </el-form-item>
  <el-form-item label="닉네임">
    <el-text v-model="formLabelAlign.region" />
  </el-form-item>
  <el-form-item label="이메일">
    <el-text v-model="formLabelAlign.type" />
  </el-form-item>
</el-form>
</div>
</body>

</template>


<script lang="ts" setup>
import {onMounted, reactive, ref} from 'vue'
import type { FormProps } from 'element-plus'
import axios from "axios";
import {useRouter} from "vue-router";


const apiBaseUrl = "http://localhost:8080";
const router = useRouter()

const labelPosition = ref<FormProps['labelPosition']>('left')

const formLabelAlign = reactive({
  name: '',
  region: '',
  type: '',
})


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



</script>

<style scoped>
.userInfoContainer {
  display: flex;
  justify-content: start;
  align-items: center;
}




</style>