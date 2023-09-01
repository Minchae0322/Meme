<script setup lang = "js">
import axios from "axios";
import {ref} from "vue";


const username = ref("")
const password = ref("")
const write = function () {
  axios.post("http://localhost:8080/board/posts/",{
        username: username.value,
        password: password.value
      }
  )}
</script>