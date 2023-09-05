<script setup lang = "js">
import axios from "axios";
import {ref} from "vue";
import { useRouter } from "vue-router";


const router = useRouter()

const logout = function () {
  console.log('access 토큰 :', "로그아웃");
  axios.post("http://localhost:8080/auth/logout", {}
  ).then((response) => {

      localStorage.removeItem("accessToken")
      console.log('access 토큰 :', "로그아웃");
      router.replace('/home');

  })

}

write()

</script>
<template>



</template>


<style>


</style>