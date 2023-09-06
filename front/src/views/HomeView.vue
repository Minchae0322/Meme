<script setup lang="ts">


import {onMounted, ref} from "vue";
import axios from 'axios'
import router from "@/router/index.js";
import {useRouter} from "vue-router";

const posts = ref([]);

router.afterEach(async (to, from) => {
  {
    console.log(to.name)
    console.log(from.name)
    if(to.name == 'home' && from.name == 'login') {
      console.log("beforeEach")
      // redirect the user to the login page
      location.reload()


    }


  }
})

axios.get("http://localhost:8080/home").then((response) => {
  posts.value.push(response.data)
})


</script>


<template>


</template>


<style scoped>
li {
  margin-bottom: 1rem;
}


</style>