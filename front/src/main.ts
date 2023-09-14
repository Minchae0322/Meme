import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router/index.js'
import mitt from 'mitt'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'


const app = createApp(App)
const emitter = mitt()

app.config.globalProperties.emitter = emitter
app.use(createPinia())
app.use(router)
app.use(ElementPlus)


app.mount('#app')
