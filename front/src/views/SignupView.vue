<template>
  <div class="registration form">
    <header>Signup</header>
    <form action="#">
      <div>
        <input type="text" v-model="username" :class="{ error: usernameEmpty || isAlphaNumeric }" placeholder="아이디를 입력하세요">
        <div class="error-message" v-if="usernameEmpty">아이디를 입력하세요</div>
      </div>
      <div class="emailContainer">
        <input type="text" v-model="email" :class="{ error: emailEmpty || !isValidEmail }" placeholder="이메일을 입력하세요">
        <div class="error-message" v-if="emailEmpty">이메일을 입력하세요</div>
        <div class="error-message" v-if="!isValidEmail">정확한 이메일을 입력하세요</div>
      </div>
      <div v-if="isVerificationCodeSent">
        <input
            type="text"
            v-model="verificationCode"
            :placeholder="timerCount > 0 ? `인증코드를 입력하세요 (${formattedTime})` : '인증코드를 입력하세요'"
            :class="{ error: validCodeEmpty}"
        >
        <div class="error-message" v-if="validCodeEmpty">인증번호를 입력해주세요</div> <!-- 추가된 부분 -->
      </div>
      <button type="button" class="sendButton" v-if="!isVerificationCodeSent" @click="sendVerificationCode">이메일로 인증번호를 발송합니다</button>
      <div>
        <input type="text" v-model="phoneNum" :class="{ error: phoneNumEmpty }" placeholder="핸드폰 번호를 입력해주세요">
        <div class="error-message" v-if="phoneNumEmpty">핸드폰 번호를 입력해주세요</div>
      </div>
      <div>
        <input type="password" v-model="password" :class="{ error: passwordEmpty }" placeholder="비밀번호를 입력해주세요">
        <div class="error-message" v-if="passwordEmpty">비밀번호를 입력해주세요</div>
      </div>
      <div>
        <input type="password" v-model="passwordCheck" :class="{ error: passwordCheckEmpty }" placeholder="비밀번호를 다시 한번 확인해주세요">
        <div class="error-message" v-if="passwordCheckEmpty">비밀번호를 다시 한번 확인해주세요</div>
      </div>

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


let isValidEmail = ref(true);
let username = ref("");
let password = ref("");
let passwordCheck = ref("");
let email = ref("");
let phoneNum = ref("");
let verificationCode = ref("");
let isVerificationCodeSent = ref(false);

let usernameEmpty = ref(false);
let passwordEmpty = ref(false);
let passwordCheckEmpty = ref(false);
let emailEmpty = ref(false);
let phoneNumEmpty = ref(false);
let validCodeEmpty = ref(false);
const write = function () {
  usernameEmpty.value = username.value === "";
  passwordEmpty.value = password.value === "";
  passwordCheckEmpty.value = passwordCheck.value === "";
  emailEmpty.value = email.value === "";
  phoneNumEmpty.value = phoneNum.value === "";
  validCodeEmpty.value = verificationCode.value === "";
  if (
      username.value !== "" &&
      password.value !== "" &&
      passwordCheck.value !== "" &&
      email.value !== "" &&
      phoneNum.value !== "" &&
      password.value === passwordCheck.value
  ) {
    axios.post("http://localhost:8080/auth/mailVerify", {
      email: email.value,
      verificationCode: verificationCode.value
    }, {
      headers: {}
    }).then((response) => {
    }).catch(error => {
      alert("인증번호가 다릅니다, 다시 확인해주세요")
    });

    axios.post("http://localhost:8080/auth/signup", {
      username: username.value,
      password: password.value,
      email: email.value,
    }, {
      headers: {}
    }).then((response) => {
      if (response.status === 200) {
        router.replace("/login");
      } else {
      }
    });
  } else {
  }
}

let verificationCodeInput = ref(""); // 인증 코드 입력 필드
let isTimerActive = ref(false); // 타이머 활성화 여부
let timerCount = ref(300); // 5분 타이머 (초 단위)

// Send Verification Code 버튼 클릭 시 호출되는 함수


// 타이머를 시작하는 함수
let formattedTime = ref(timerCount.value); // 타이머 시간을 00:00 형태로 표시

// 타이머를 시작하는 함수
const startTimer = () => {
  isTimerActive.value = true;

  const timerInterval = setInterval(() => {
    timerCount.value--;

    if (timerCount.value <= 0) {
      // 타이머가 만료되면 타이머를 중지하고 입력 필드를 비활성화
      clearInterval(timerInterval);
      isTimerActive.value = false;
    } else {
      formattedTime.value = formatTime(timerCount.value);
    }
  }, 1000); // 1초마다 타이머 업데이트
};

// 시간을 00:00 형태로 포맷팅하는 함수
const formatTime = (seconds) => {
  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = seconds % 60;
  return `${String(minutes).padStart(2, '0')}:${String(remainingSeconds).padStart(2, '0')}`;
};
// 인증 코드 입력 필드가 업데이트될 때 호출되는 함수
onUpdated(() => {
  if (isTimerActive.value && timerCount.value <= 0) {
    // 타이머가 활성화되어 있고 만료되었을 때 입력 필드를 비활성화
    verificationCodeInput.value = ""; // 입력 필드 초기화
    verificationCodeInput.disabled = true;
  } else {
    // 타이머가 활성화되어 있지 않거나 아직 유효한 경우 입력 필드 활성화
    verificationCodeInput.disabled = false;
  }
});


const sendVerificationCode = function () {
  // 이메일 유효성 검사
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  isValidEmail.value = emailPattern.test(email.value);

  if (isValidEmail.value) {
    // 이메일 유효성이 확인되면 코드를 보냅니다.
    axios.post("http://localhost:8080/auth/sendMail", {
      email: email.value,
    }, {
      headers: {}
    }).then((response) => {

    }).catch(error => {
      alert("이미 등록된 이메일 입니다.")
      isVerificationCodeSent.value = false;
    });
    startTimer();
    isVerificationCodeSent.value = true;
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
  margin-bottom: 1.0rem;
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

.form .error-message {
  color: red;
  font-size: 14px;
  margin-bottom: 15px;
}

.form button {
  background: #009579;
  color: #fff;
  font-size: 17px;

  font-weight: 500;
  letter-spacing: 1px;
  height: 60px;
  width: 100%;
  border: none;
  border-radius: 6px;

  cursor: pointer;
  transition: 0.4s;
}

.form button:hover {
  background: #006653;
}

.form input[type="text"][v-model="verificationCode"] {
  height: 60px;
  width: 100%;
  padding: 0 15px;
  font-size: 12px;

  margin-bottom: 1.0rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  outline: none;
}

.sendButton {
  margin-bottom: 20px;

}
</style>
