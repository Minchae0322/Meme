import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router/index.js'
import mitt from 'mitt'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

/* import the fontawesome core */
import { library } from '@fortawesome/fontawesome-svg-core'

/* import font awesome icon component */
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

/* import specific icons */
import { faUserSecret } from '@fortawesome/free-solid-svg-icons'
import { faEye } from '@fortawesome/free-solid-svg-icons'
/* add icons to the library */
library.add(faUserSecret)
library.add(faEye)
const app = createApp(App)
const emitter = mitt()

app.config.globalProperties.emitter = emitter
app.use(createPinia())
app.use(router)
app.use(ElementPlus)


app.mount('#app')
