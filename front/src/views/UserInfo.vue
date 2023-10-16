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
import { reactive, ref } from 'vue'
import type { FormProps } from 'element-plus'

const labelPosition = ref<FormProps['labelPosition']>('left')

const formLabelAlign = reactive({
  name: '',
  region: '',
  type: '',
})
</script>

<style scoped>
.userInfoContainer {
  display: flex;
  justify-content: start;
  align-items: center;
}




</style>