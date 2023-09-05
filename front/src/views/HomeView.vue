<script setup lang="ts">


import {ref} from "vue";
import axios from 'axios'
import router from "@/router/index.js";
import {useRouter} from "vue-router";

const posts = ref([]);



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