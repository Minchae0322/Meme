<template>
  <div class="registration form">
    <header>Signup</header>
    <form action="#">
      <input type="text"  v-model= "username" placeholder="Enter your id">
      <input type="text"  v-model= "email" placeholder="Enter your email">
      <input type="text"  v-model= "phoneNum" placeholder="Enter your phoneNumber">
      <input type="password" v-model="password" placeholder="Create a password">
      <input type="password" v-model="passwordCheck" placeholder="Confirm your password">
      <input type="button" class="button" @click="write" value="Signup">
    </form>
    <div class="signup">
        <span class="signup">Already have an account?
         <label for="check" @click="goLogin">Login</label>
        </span>
    </div>
  </div>
</template>

<script lang="js" setup>
import axios from "axios";
import { ref, onMounted, onUpdated} from "vue";


import { useRouter } from "vue-router";


const router = useRouter()
const isAlphaNumeric = function(password) {
  // 정규 표현식을 사용하여 영문자와 숫자로만 이루어진지 확인
  const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/;
  return regex.test(password);
}

let username = ref("")
let password = ref("")
let passwordCheck = ref("")
let email = ref("")
let phoneNum = ref("")


const write = function () {
  if (password.value === passwordCheck.value) {
    axios.post("http://localhost:8080/auth/signup", {
          username: username.value,
          password: password.value,
      email:email.value

        }
        , {
          headers: {}
        }
    ).then((response) => {
      if (response.status === 200) {
        router.replace("/login");
      } else {
        alert("다시 입력")
      }
    });
  } else {
    alert("다시 입력")
  }

}

const goLogin = function () {
  router.replace({name: "login"})
};

</script>

<style scoped>

.registration {
  width: 30%;
  align-items: center;
  padding-bottom: 20px;
  margin: 0 auto; /* Add this line to horizontally center the element */
}

#check:checked ~ .registration{
  display: block;
}
#check:checked ~ .login{
  display: none;
}
#check{
  display: none;
}
.container .form{
  padding: 2rem;
}
.form header{
  font-size: 2rem;
  font-weight: 500;
  text-align: center;
  margin-bottom: 1.5rem;
}
.form input{
  height: 60px;
  width: 100%;
  padding: 0 15px;
  font-size: 17px;
  margin-bottom: 1.3rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  outline: none;
}
.form input:focus{
  box-shadow: 0 1px 0 rgba(0,0,0,0.2);
}
.form a{
  font-size: 16px;
  color: #009579;
  text-decoration: none;
}
.form a:hover{
  text-decoration: underline;
}
.form input.button{
  color: #fff;
  background: #009579;
  font-size: 1.2rem;
  font-weight: 500;
  letter-spacing: 1px;
  margin-top: 1.7rem;
  cursor: pointer;
  transition: 0.4s;
}
.form input.button:hover{
  background: #006653;
}
.signup{
  font-size: 17px;
  text-align: center;
}
.signup label{
  color: #009579;
  cursor: pointer;
}
.signup label:hover{
  text-decoration: underline;
}
</style>
